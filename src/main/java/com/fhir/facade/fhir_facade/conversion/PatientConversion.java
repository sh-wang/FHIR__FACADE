package com.fhir.facade.fhir_facade.conversion;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Patient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PatientConversion {

    public PatientConversion(){ }

    private FhirContext ctx = FhirContext.forDstu3();
    private IParser p =ctx.newJsonParser().setPrettyPrint(true);

    private String defaultPath = "http://localhost:8080/api/";
    private String serverBaseUrl = "http://hapi.fhir.org/baseDstu3";

    private IGenericClient client = ctx.newRestfulGenericClient(serverBaseUrl);

    private RestTemplate restTemplate = new RestTemplate();


    public String conversionSingle(String rawData){
        try {
            JSONObject jsonObject = new JSONObject(rawData);
            Patient patient = patientConversion(jsonObject);
            String encode = p.encodeResourceToString(patient);
            return encode;
        } catch (JSONException e){
            e.printStackTrace();
            return "conversion error";
        }
    }

    public String conversionArray(String rawData) {
        try {
            JSONArray jsonArray = new JSONArray(rawData);
            JSONArray FHIRarray = new JSONArray();

            for(int i = 0; i < jsonArray.length(); i++){
                FHIRarray.put(new JSONObject(p.encodeResourceToString
                        (patientConversion(jsonArray.getJSONObject(i)))));
            }
            return FHIRarray.toString(4);
        }catch (JSONException e){
            e.printStackTrace();
            return "conversion error";
        }
    }

    public Patient patientConversion(JSONObject jsonObject){
        Patient patient = new Patient();
        try {
//            patient.setId(jsonObject.get("id").toString().replaceAll(".0+?$", ""));
            // add name
            patient.addName().setFamily(jsonObject.get("familyName").toString()).addGiven(jsonObject.get("givenName").toString());
            // add dob
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                patient.setBirthDate(format.parse(jsonObject.get("birthDate").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //add gender
            if (jsonObject.get("gender").equals("MALE")) {
                patient.setGender(Enumerations.AdministrativeGender.MALE);
            } else if (jsonObject.get("gender").equals("FEMALE")) {
                patient.setGender(Enumerations.AdministrativeGender.FEMALE);
            } else if (jsonObject.get("gender").equals("OTHER")) {
                patient.setGender(Enumerations.AdministrativeGender.OTHER);
            } else if (jsonObject.get("gender").equals("UNKNOWN")) {
                patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            }

            // add NHS number
            if (jsonObject.get("nhsNumber") == null) {
                patient.addIdentifier().setSystem("nhsNumber").setValue("0000000000");
            } else {
                patient.addIdentifier().setSystem("nhsNumber").setValue(jsonObject.get("nhsNumber").toString());
            }

            // add Email
            if (jsonObject.get("email") == null) {
                patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue("null");
            }else{
                patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue(jsonObject.get("email").toString());
            }

            // add address
            String addressUrl = defaultPath + "addresses/" + jsonObject.get("id").toString();
            ResponseEntity<String> response;
            try {
                response = restTemplate.getForEntity(addressUrl, String.class);
            } catch (Exception e) {
                return  patient;
            }

            JSONObject addressJson = new JSONObject(response.getBody());
            if (addressJson!=null) {
                org.hl7.fhir.dstu3.model.Address addressFHIR = new org.hl7.fhir.dstu3.model.Address();
                addressFHIR.setPostalCode(addressJson.get("postalCode").toString());
                addressFHIR.setCity(addressJson.get("city").toString());
                addressFHIR.setCountry(addressJson.get("country").toString());

                for (int i = 0; i < addressJson.getJSONArray("lines").length(); i++){
                    addressFHIR.addLine(addressJson.getJSONArray("lines").get(i).toString());
                }

                patient.addAddress(addressFHIR);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        MethodOutcome outcome = client.create().resource(patient).execute();
//
//        System.out.println(outcome.getId());
//        System.out.println(outcome.getCreated());

        return  patient;
    }
}

