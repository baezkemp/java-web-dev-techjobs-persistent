package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.launchcode.javawebdevtechjobspersistent.models.Employer;
import org.launchcode.javawebdevtechjobspersistent.models.Job;
import org.launchcode.javawebdevtechjobspersistent.models.Skill;
import org.launchcode.javawebdevtechjobspersistent.models.data.EmployerRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.JobRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("title", "My Jobs");
        model.addAttribute("jobs", jobRepository.findAll());
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model,
                                    @RequestParam int employerId,
                                    @RequestParam (defaultValue = "") List<Integer> skills) {

        if (errors.hasErrors() || skills.isEmpty()) {
            model.addAttribute("title", "Add Job");
            model.addAttribute("employers", employerRepository.findAll());
            model.addAttribute("skills", skillRepository.findAll());
            if(skills.isEmpty()){
                model.addAttribute("skillsError", "Select at least one skill.");
            }
            return "add";
        }

        Employer employer = employerRepository.findById(employerId).orElse(new Employer());
        newJob.setEmployer(employer);
        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);
        model.addAttribute("job", jobRepository.save(newJob));
        model.addAttribute("employer", employer);
        model.addAttribute("skills", skillObjs);
        return "view";
    }

    @GetMapping(path = {"view/{jobId}", "view"})
    public String displayViewJob(Model model, @PathVariable(required = false) Integer jobId) {
        if (jobId == null) {
            model.addAttribute("jobs", jobRepository.findAll());
            return "index";
        } else {
            Optional<Job> result = jobRepository.findById(jobId);
            if (result.isEmpty()) {
                return "redirect:../";
            } else {
                Job job = result.get();
                model.addAttribute("job", job);
                model.addAttribute("employer", job.getEmployer());
                model.addAttribute("skills", job.getSkills());
            }
        }
        return "view";
    }
}