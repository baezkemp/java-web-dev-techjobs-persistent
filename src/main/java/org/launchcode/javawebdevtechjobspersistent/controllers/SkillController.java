package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.launchcode.javawebdevtechjobspersistent.models.Skill;
import org.launchcode.javawebdevtechjobspersistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("skills")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;

    @GetMapping
    public String displaySkills(Model model) {
        model.addAttribute("title", "All Skills");
        model.addAttribute("skills", skillRepository.findAll());
        return "skills/index";
    }

    @GetMapping("add")
    public String displayAddSkillForm(Model model) {
        model.addAttribute(new Skill());
        return "skills/add";
    }

    @PostMapping("add")
    public String processAddSkillForm(@ModelAttribute @Valid Skill newSkill,
                                         Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Skill");
            return "skills/add";
        }
        skillRepository.save(newSkill);
        return "skills/view";
    }

    @GetMapping(path = {"view/{skillId}", "view"})
    public String displayViewSkill(Model model, @PathVariable (required = false) Integer skillId) {
        if (skillId == null) {
            model.addAttribute("skills", skillRepository.findAll());
            return "skills/index";
        } else {
            Optional<Skill> result = skillRepository.findById(skillId);
            if (result.isEmpty()) {
                return "redirect:../";
            } else {
                Skill skill = result.get();
                model.addAttribute("skill", skill);
            }
        }
        return "skills/view";
    }

}
