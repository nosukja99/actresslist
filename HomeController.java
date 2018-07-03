package com.example.actresslist;


import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    ActressRepository actressRepository;
    @Autowired
    CloudinaryConfig cloudinaryConfig;

    @RequestMapping("/")
    public String showlist(Model model)
    {
        model.addAttribute("actresslist", actressRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String addActress(Model model)
    {
        model.addAttribute("actress", new Actress());
        return "form";
    }

    @PostMapping("/process")
    public String processActress(@Valid @ModelAttribute Actress actress, BindingResult
                                 result, @RequestParam("file") MultipartFile file)
    {
        if(result.hasErrors())
        {
            return "redirect:/";
        }
        if(file.isEmpty())
        {
            return "redirect:/";
        }
        try{
            Map upload = cloudinaryConfig.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            actress.setImage(upload.get("url").toString());
            actressRepository.save(actress);
            return "redirect:/";
        }catch (IOException e)
        {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @RequestMapping("/update/{id}")
    public String updateActress(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("actress", actressRepository.findById(id));
        return "form";
    }

    @RequestMapping("/detail/{id}")
    public String detailActress(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("actress", actressRepository.findById(id).get());
        return "/show";
    }

    @RequestMapping("/delete/{id}")
    public String deleteActress(@PathVariable("id") long id)
    {
        actressRepository.deleteById(id);
        return "redirect:/";
    }
}
