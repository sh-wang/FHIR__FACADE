package com.fhir.facade.fhir_facade.conversion;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RetrieveData {
    private String url;

    private PatientConversion patientConversion = new PatientConversion();
    private QuestionnaireResponseConversion questionnaireResponseConversion = new QuestionnaireResponseConversion();
    private ProcedureConversion procedureConversion = new ProcedureConversion();
    private QuestionnaireConversion questionnaireConversion = new QuestionnaireConversion();

    public RetrieveData(String url){
        this.url=url;
    }
    public RetrieveData(){};

    private boolean urlCheck(String url){
        Boolean isArray;
        String newUrl = url.substring(url.indexOf("api") + 4);
        if (newUrl.contains("/")){
            isArray = false;
            newUrl = newUrl.substring(0, newUrl.lastIndexOf("/"));
        } else {
            isArray = true;
            if (newUrl.contains("?")){
                newUrl = newUrl.substring(0, newUrl.lastIndexOf("?"));
            }
        }
        return isArray;
    }


    private ResponseEntity<String> getResponse(String url){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("url error, please type the correct url");
            return null;
        }
        return response;
    }


    public String convertPatient(){
        ResponseEntity<String> response = getResponse(url);
        if (response == null || response.getBody().equals("[ ]")){
            return "no information";
        }

        String answer = patientConversion.conversionArray(response.getBody());
        return answer;
    }


    public String convertQuestionnnaireResponse(){
        ResponseEntity<String> response = getResponse(url);
        if (response == null || response.getBody().equals("[ ]")){
            return "no information";
        }

        String answer = questionnaireResponseConversion.conversionArray(response.getBody());
        return answer;
    }


    public String convertQuestionnnaire(){
        ResponseEntity<String> response = getResponse(url);
        if (response == null){
            return "no information";
        }

        String answer = questionnaireConversion.conversionSingle(response.getBody());
        return answer;
    }


    public String convertProcedure(){
        ResponseEntity<String> response = getResponse(url);
        if (response == null){
            return "no information";
        }

        String answer = procedureConversion.conversionSingle(response.getBody());
        return answer;
    }


//    public String ConvertResponse() {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response;
//        try {
//            response = restTemplate.getForEntity(url, String.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("url error, please type the correct url");
//            return null;
//        }
//
//        String answer = "";
//        String newUrl = url.substring(url.indexOf("api") + 4);
//        Boolean isArray;
//        if (newUrl.contains("/")){
//            isArray = false;
//            newUrl = newUrl.substring(0, newUrl.lastIndexOf("/"));
//        } else {
//            isArray = true;
//            if (newUrl.contains("?")){
//                newUrl = newUrl.substring(0, newUrl.lastIndexOf("?"));
//            }
//        }
//
//        if (isArray) {
//            switch (newUrl) {
//                case "patients":
//                    answer = patientConversion.conversionArray(response.getBody());
//                    break;
////                case "procedures":
////                    answer = procedureConversion.conversionArray(response.getBody());
////                    break;
////                case "questionnaires":
////                    answer = questionnaireConversion.conversionArray(response.getBody());
////                    break;
////                case "followup-actions":
////                    answer = questionnaireResponseConversion.conversionArray(response.getBody());
////                    break;
////                case "Questionnaire-response":
////                    answer = questionnaireResponseConversion.conversionArray(response.getBody());
////                    break;
////                default:
////                    answer = "[]";
////                    break;
//            }
//        } else {
//            switch (newUrl) {
//                case "patients":
//                    answer = patientConversion.conversionSingle(response.getBody());
//                    break;
////                case "procedures":
////                    answer = procedureConversion.conversionSingle(response.getBody());
////                    break;
////                case "questionnaires":
////                    answer = questionnaireConversion.conversionSingle(response.getBody());
////                    break;
////                case "followup-actions":
////                    answer = questionnaireResponseConversion.conversionSingle(response.getBody());
////                    break;
////                case "Questionnaire-response":
////                    answer = questionnaireResponseConversion.conversionSingle(response.getBody());
////                    break;
////                default:
////                    answer = "[]";
////                    break;
//            }
//        }
//        return answer;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}