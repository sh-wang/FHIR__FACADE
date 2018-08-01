package com.fhir.facade.fhir_facade.controllers;

import com.fhir.facade.fhir_facade.conversion.RetrieveData;
import com.fhir.facade.fhir_facade.domain.Questionnaire;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QuestionnaireController {
    //    private QuestionnaireConversion questionnaireConversion;
    private RetrieveData temp = new RetrieveData();
    private String getUrl="http://localhost:8080/api/questionnaires/";

//    public void setQuestionnaireConversion(QuestionnaireConversion questionnaireConversion){
//        this.questionnaireConversion = questionnaireConversion;
//    }

    @RequestMapping("/questionnaire")
    public String searchQuestionnaires(Model model, Integer id){
        model.addAttribute("questionnaire", new Questionnaire());

        if (id == null){
            model.addAttribute("questionnaireresource","no information");
            return "questionnaireform";
        }
        String url = getUrl+id;
        System.out.println(url);
        temp.setUrl(url);
        model.addAttribute("questionnaireresource",temp.convertQuestionnnaire());

        return "questionnaireform";
    }


}