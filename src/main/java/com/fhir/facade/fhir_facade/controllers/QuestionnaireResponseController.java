package com.fhir.facade.fhir_facade.controllers;

import com.fhir.facade.fhir_facade.conversion.QuestionnaireResponseConversion;
import com.fhir.facade.fhir_facade.conversion.RetrieveData;
import com.fhir.facade.fhir_facade.domain.QuestionnaireResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QuestionnaireResponseController {
    private QuestionnaireResponseConversion questionnaireResponseConversion;

    private String defaultUrl="http://localhost:8080/api/search/Questionnaire-response?";
    private String allUrl="http://localhost:8080/api/search/Questionnaire-response";
    private RetrieveData temp = new RetrieveData();

    public void setQuestionnaireResponseConversion(QuestionnaireResponseConversion questionnaireResponseConversion) {
        this.questionnaireResponseConversion = questionnaireResponseConversion;
    }

    @RequestMapping("/questionnaireresponse")
    public String searchQuestionanireResponse(Model model, Integer identifier
            , String parent,
                                              String questionnaire, String status,
                                              String patient, String subject,
                                              String authored, String author){
        model.addAttribute("qr", new QuestionnaireResponse());

        String url=defaultUrl;
        Boolean isNull = true;

        if(status!=null && !status.equals("")){
            url += "status="+status+"&";
            isNull = false;
        }
        if(parent!=null && !parent.equals("")){
            url += "parent="+parent+"&";
            isNull = false;
        }
        if(identifier!=null && !identifier.equals("")){
            url += "identifier="+identifier+"&";
            isNull = false;
        }
        if(questionnaire!=null && !questionnaire.equals("")){
            url += "questionnaire="+questionnaire+"&";
            isNull = false;
        }
        if(patient!=null && !patient.equals("")){
            url += "patient="+patient+"&";
            isNull = false;
        }
        if(subject!=null &&  !subject.equals("")){
            url += "subject="+subject+"&";
            isNull = false;
        }
        if(authored!=null && !authored.equals("")){
            url += "authored="+authored+"&";
            isNull = false;
        }
        if(author!=null && !author.equals("")){
            url += "author="+author+"&";
            isNull = false;
        }

        if (isNull){
            model.addAttribute("qrsource","no information");
            return "qrform";
        }
        url= url.substring(0,url.length()-1);
        System.out.println(url);
//
        temp.setUrl(url);
        model.addAttribute("qrsource",temp.convertQuestionnnaireResponse());
        return "qrform";
    }

    @RequestMapping("/questionnaireresponse/all")
    public String getAll(Model model){
        String url = allUrl;
        model.addAttribute("qr", new QuestionnaireResponse());
        temp.setUrl(url);
        System.out.println(temp.convertQuestionnnaireResponse());
        model.addAttribute("qrsource",temp.convertQuestionnnaireResponse());
        return "qrform";
    }
}