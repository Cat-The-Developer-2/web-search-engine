package com.chatclip.websearch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import com.chatclip.websearch.service.webService;
import com.chatclip.websearch.service.webCrawler;
import com.chatclip.websearch.models.Models;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")


@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private webService webService;

    @Autowired
    private webCrawler webCrawler;

    @GetMapping("/")
    public String home() {
        return "get out of here!";
    }

    @GetMapping("/search")
    public List<Models> getSearch(@RequestParam String q) {
        return webService.search(q);
    }

    @GetMapping("/startCrawl")
    public String startCrawl(@RequestParam String url) {
        webCrawler.startCrawl(url);
        return "Crawling started!";
    }
}