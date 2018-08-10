package com.fhir.facade.fhir_facade.conversion;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuestionnaireResponseConversion {
    private PatientConversion patientConversion = new PatientConversion();
    private ProcedureConversion procedureConversion = new ProcedureConversion();
    private QuestionnaireConversion questionnaireConversion= new QuestionnaireConversion();
    public QuestionnaireResponseConversion(){}

    private String defaultPath = "http://localhost:8080/api/";
    private String serverBaseUrl = "http://hapi.fhir.org/baseDstu3";

    private RestTemplate restTemplate = new RestTemplate();

    private FhirContext ctx = FhirContext.forDstu3();
    private IParser p =ctx.newJsonParser().setPrettyPrint(true);
    private IGenericClient client = ctx.newRestfulGenericClient(serverBaseUrl);

    public String conversionSingle(String rawData){
        try{
            JSONObject jsonObject = new JSONObject(rawData);
            QuestionnaireResponse questionnaireResponse = questionnaireResponseConversion(jsonObject);
            String encode = p.encodeResourceToString(questionnaireResponse);
            return encode;
        } catch (JSONException e){
            e.printStackTrace();
            return "conversion error";
        }
    }

    public String conversionArray(String rawData){
        try {
            JSONArray jsonArray = new JSONArray(rawData);
            JSONArray FHIRarray = new JSONArray();

            for(int i = 0; i < jsonArray.length(); i++){
                FHIRarray.put(new JSONObject(p.encodeResourceToString
                        (questionnaireResponseConversion(jsonArray.getJSONObject(i)))));
            }
            return FHIRarray.toString(4);
        }catch (JSONException e){
            e.printStackTrace();
            return "conversion error";
        }
    }

    private QuestionnaireResponse questionnaireResponseConversion(JSONObject jsonObject){
        QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
        try {
            //add id
            questionnaireResponse.setId(jsonObject.get("id").toString());

            //add status
            if (jsonObject.get("status").equals("STARTED")){
                questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.INPROGRESS);
            }
            if (jsonObject.get("status").equals("UNINITIALISED")){
                questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.STOPPED);
            }
            if (jsonObject.get("status").equals("COMPLETED")){
                questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
            }
            if (jsonObject.get("status").equals("UNKNOWN")){
                questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.STOPPED);
            }
            if (jsonObject.get("status").equals("PENDING")){
                questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.INPROGRESS);
            }

            //add patient
            JSONObject jsonPatient = new JSONObject(jsonObject.get("patient").toString());
            Patient patient = patientConversion.patientConversion(jsonPatient);

            Reference refePa = new Reference(patient);
            questionnaireResponse.setSubject(refePa);

            //add procedure
            JSONObject jsonProcedureBooking = jsonObject.getJSONObject("careEvent").
                    getJSONObject("followupPlan").getJSONObject("procedureBooking");
            ResponseEntity<String> response = restTemplate.getForEntity(defaultPath + "_search/procedures?query="
                    +jsonProcedureBooking.get("primaryProcedure"), String.class);

            JSONArray jsonProcedure = new JSONArray(response.getBody());

            Procedure procedure = new Procedure();
            JSONObject jPro = jsonProcedure.getJSONObject(0);
            procedure.setStatus(Procedure.ProcedureStatus.UNKNOWN);
            CodeableConcept codeableConcept = new CodeableConcept();
            codeableConcept.addCoding().setCode(jPro.get("localCode").toString()).
                        setDisplay(jPro.get("name").toString().substring(1, jPro.get("name").toString().length()));
            procedure.setCode(codeableConcept);
            Reference refePr = new Reference(procedure);
            questionnaireResponse.addParent(refePr);


            //add completed date
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            if(!jsonObject.get("completedDate").toString().equals("null")) {
                try {
                    date = format.parse(jsonObject.get("completedDate").toString());
                    questionnaireResponse.setAuthored(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // add patient's questionnaire need to accomplish, in the format of fhir standard, json format.
            JSONObject jsonQuestionnaire = new JSONObject(jsonObject.get("questionnaire").toString());

            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setStatus(Enumerations.PublicationStatus.ACTIVE).
                    setName(jsonQuestionnaire.get("name").toString()).
                    setCopyright(jsonQuestionnaire.get("copyright").toString());
            Reference refeQu = new Reference(questionnaire);
            questionnaireResponse.setQuestionnaire(refeQu);

            // display each question and its corresponding answer for the questionnaire.
            if(!jsonObject.get("responseItems").toString().equals("null")){
                JSONArray jsonResponseItems = new JSONArray(jsonObject.get("responseItems").toString());
                for(int i=0; i<jsonResponseItems.length(); i++){
                    IntegerType j = new IntegerType();
                    j.setValue((Integer) jsonResponseItems.getJSONObject(i).get("value"));
                    questionnaireResponse.addItem().setLinkId(jsonResponseItems.getJSONObject(i).get("id").
                            toString()).setText(jsonResponseItems.getJSONObject(i).get("localId").
                            toString()).addAnswer().setValue(j);
                }
            }

            // outcome comment
            StringType s = new StringType();
            s.setValue(jsonObject.get("outcomeComment").toString());
            questionnaireResponse.addItem().addAnswer().setValue(s);


            String author = jsonObject.get("createdBy").toString();
            RelatedPerson relatedPerson = new RelatedPerson();
            relatedPerson.addName().setFamily(author);
            Reference authorRef = new Reference(relatedPerson);
            questionnaireResponse.setAuthor(authorRef);
        } catch (JSONException e){
            e.printStackTrace();
        }


//        MethodOutcome outcome = client.create().resource(questionnaireResponse).execute();
//        System.out.println("link: " + outcome.getId() + "\n" + outcome.getCreated());

        return  questionnaireResponse;
    }
}

