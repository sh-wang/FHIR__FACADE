package com.fhir.facade.fhir_facade.controllers;

import com.fhir.facade.fhir_facade.conversion.RetrieveData;
import com.fhir.facade.fhir_facade.domain.Procedure;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProcedureController {
    //    private ProcedureConversion procedureConversion;
    private RetrieveData temp = new RetrieveData("procedure");
    private String getUrl="http://localhost:8080/api/procedures/";

//    public void setProcedureConversion(ProcedureConversion procedureConversion){
//        this.procedureConversion = procedureConversion;
//    }

    @RequestMapping("/procedure")
    public String searchQuestionnaires(Model model, Integer id){
        model.addAttribute("procedure", new Procedure());

        if (id == null){
            model.addAttribute("procedureresource","no information");
            return "procedureform";
        }
        String url = getUrl+id;
        System.out.println(url);
        temp.setUrl(url);
        model.addAttribute("procedureresource",temp.convertProcedure());

        return "procedureform";
    }
}
