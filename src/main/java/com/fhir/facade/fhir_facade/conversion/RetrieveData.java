package com.fhir.facade.fhir_facade.conversion;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RetrieveData {
    private String url;

    private PatientConversion patientConversion;
    private QuestionnaireResponseConversion questionnaireResponseConversion;
    private ProcedureConversion procedureConversion;
    private QuestionnaireConversion questionnaireConversion;

    private String authenticateToken = "";
    private Boolean requestMonitor = true;

    // change the following parameters based on the remote system
    private String url_auth = "http://localhost:8080/api/authenticate";
    private String username = "admin";
    private String password = "admin";
    private HttpHeaders header_authentication = new HttpHeaders();
    private HttpHeaders header_authenticated = new HttpHeaders();


    public RetrieveData(String type){
        header_authenticated.add("Accept", "application/json");
        header_authentication.add("Content-Type", "application/json");
        header_authentication.add("Accept", "application/json");

        switch (type) {
            case "patient":
                patientConversion = new PatientConversion();
                break;
            case "questionnaireResponse":
                questionnaireResponseConversion = new QuestionnaireResponseConversion();
                break;
            case "questionnaire":
                questionnaireConversion = new QuestionnaireConversion();
                break;
            case "procedure":
                procedureConversion = new ProcedureConversion();
                break;
            default:
                break;
        }

    }


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


    /**
     * GET the new authenticate token
     */
    private void GetAuthenticateToken(){
        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();
        JSONObject userPass = new JSONObject();
        try {
            userPass.put("username", username).put("password", password);

            HttpEntity<String> entity = new HttpEntity<String>(userPass.toString(), header_authentication);
            response = restTemplate.exchange(url_auth, HttpMethod.POST, entity, String.class);

            authenticateToken = response.getHeaders().get("Authorization").get(0);
            System.out.println("Authenticate Token: " + authenticateToken);
        }catch (Exception e){
            System.out.println("authenticate token generate fail");
            e.printStackTrace();
        }
    }


    /**
     *
     * @param url
     * @return get the resources from the URL
     */
    private ResponseEntity<String> getResponse(String url){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        HttpHeaders header = new HttpHeaders();
        try {
            header.add("Authorization", authenticateToken);
            HttpEntity<String> requestEntity = new HttpEntity<String>(null, header);
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            if (!requestMonitor){
                e.printStackTrace();
                System.out.println("url error, please type the correct url" );
                requestMonitor = true;
                return null;
            }

            requestMonitor = false;
            System.out.println("try to get new authenticate token ==============>" );
            GetAuthenticateToken();
            System.out.println("try second time ================================>" );

            response = getResponse(url);
            if (response == null){
                System.out.println("url error, please type the correct url" );
                return null;
            }
            return response;
        }
        requestMonitor = true;
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

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", authenticateToken);
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, header);

        String answer = questionnaireResponseConversion.conversionArray(response.getBody(), requestEntity);
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}