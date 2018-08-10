package com.fhir.facade.fhir_facade.conversion;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Procedure;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProcedureConversion {
    public ProcedureConversion(){ }

    private FhirContext ctx = FhirContext.forDstu3();
    private IParser p =ctx.newJsonParser().setPrettyPrint(true);

    public String conversionSingle(String rawData){
        try {
            JSONObject jsonObject = new JSONObject(rawData);

            Procedure procedure = procedureConversion(jsonObject);
            String encode = p.encodeResourceToString(procedure);

            return encode;
        } catch (JSONException e){
            e.printStackTrace();
            return "conversion error";
        }
    }

    public String conversionArray(String rawData){
        try {
            JSONArray jsonArray = new JSONArray(rawData);
            JSONArray FHIRarray = new JSONArray();;


            for(int i = 0; i < jsonArray.length(); i++){
                FHIRarray.put(new JSONObject(p.encodeResourceToString
                        (procedureConversion(jsonArray.getJSONObject(i)))));
            }

            return FHIRarray.toString(4);
        }catch (JSONException e){
            e.printStackTrace();
            return "conversion error";
        }
    }

    public Procedure procedureConversion(JSONObject jsonObject){
        Procedure procedure = new Procedure();
        try{
            //add id
            procedure.setId(jsonObject.get("id").toString());

            //currently no status data
            procedure.setStatus(Procedure.ProcedureStatus.UNKNOWN);

            CodeableConcept codeableConcept = new CodeableConcept();

            //add localcode as code and add name
            codeableConcept.addCoding().setCode(jsonObject.get("localCode").toString()).
                    setDisplay(jsonObject.get("name").toString().substring(1, jsonObject.get("name").toString().length()));
            procedure.setCode(codeableConcept);
        }catch (JSONException e){
            e.printStackTrace();
        }

        FhirContext ctx = FhirContext.forDstu3();

        String serverBaseUrl = "http://hapi.fhir.org/baseDstu3";
        IGenericClient client = ctx.newRestfulGenericClient(serverBaseUrl);
        MethodOutcome outcome = client.create().resource(procedure).execute();

        System.out.println(outcome.getId());
        System.out.println(outcome.getCreated());

        return  procedure;
    }
}

