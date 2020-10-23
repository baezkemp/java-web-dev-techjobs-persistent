package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.dom4j.rule.Mode;
import org.launchcode.javawebdevtechjobspersistent.models.Employer;
import org.launchcode.javawebdevtechjobspersistent.models.Skill;
import org.launchcode.javawebdevtechjobspersistent.models.data.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("employers")
public class EmployerController {

    @Autowired
    private EmployerRepository employerRepository;

    @GetMapping("add")
    public String displayAddEmployerForm(Model model) {
        model.addAttribute(new Employer());
        return "employers/add";
    }

    @PostMapping("add")
    public String processAddEmployerForm(@ModelAttribute @Valid Employer newEmployer,
                                    Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Employer");
            return "employers/add";
        }
        employerRepository.save(newEmployer);
        return "employers/view";
    }

    @GetMapping("view/{employerId}")
    public String displayViewEmployer(Model model, @PathVariable int employerId) {
        Optional optEmployer = null;
        if (optEmployer.isPresent()) {
            Optional<Employer> employer = employerRepository.findById(employerId);
            model.addAttribute("employer", employer);
            return "employers/view";
        } else {
            return "redirect:../";
        }
    }

//    @GetMapping("view")
//    public String displayAllEmployers(Model model){
//        Iterable<Employer> employer = employerRepository.findAll();
//        model.addAttribute("employer", employer);
//        return "skills/view";
//    }
}
