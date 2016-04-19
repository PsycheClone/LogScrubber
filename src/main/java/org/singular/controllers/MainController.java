package org.singular.controllers;

import org.singular.files.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MainController {

    @Autowired
    private FileManager fileManager;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("hosts", fileManager.getAllHosts());
        return "index";
    }

    @RequestMapping("/barchart/{host}")
    public String barchart(@PathVariable("host") String host, Model model) {
        model.addAttribute("host", host);
        return "barchart";
    }

}