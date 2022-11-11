package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
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

    // injects EmployerRepository and allows us to access data from the table
    @Autowired
    private EmployerRepository employerRepository;

    // injects SkillRepository and allows us to access data from the table
    @Autowired
    private SkillRepository skillRepository;

    // injects JobRepository and allows us to access data from the table
    @Autowired
    private JobRepository jobRepository;

    // displays application homepage - a list of jobs
    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("title", "My Jobs");
        model.addAttribute("jobs", jobRepository.findAll());
        return "index";
    }

    // displays add jobs form
    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        return "add";
    }

    // processes the add jobs form and creates a new job
    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {
        Optional optEmployer = employerRepository.findById(employerId);
        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);
        if (errors.hasErrors() || !optEmployer.isPresent()) {
            model.addAttribute("title", "Add Job");
            return "add";
        }
        Employer employer = (Employer) optEmployer.get();
        newJob.setEmployer(employer);
        jobRepository.save(newJob);
        return "redirect:";
    }

    // displays a job by id
    // @TODO incomplete
    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {

        return "view";
    }


}