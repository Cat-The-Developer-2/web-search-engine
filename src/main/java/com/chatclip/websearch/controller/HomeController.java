package com.chatclip.websearch.controller;

import org.springframework.web.bind.annotation.GetMapping;
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


@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private webService webService;

    @Autowired
    private webCrawler webCrawler;

    @GetMapping("/")
    public String home() {
        return "<style>.container{display:flex;background-color:#1e90ff}.container div{background-color:#f1f1f1;margin:10px;padding:20px;font-size:30px}</style><div class=container><div>Item 1</div><div>Item 2</div><div>Item 3</div></div>";
    }

    @GetMapping("/user/{name}")
    public Map<String, String>  getUserById(@PathVariable String name) {

        Map<String, String> UserDetail = new HashMap<>();

        UserDetail.put("Name", name);
        UserDetail.put("Age", "19");

        return UserDetail;
    }

    @GetMapping("/search")
    public Map<String, String> getSearch(@RequestParam String q) {
        Map<String, String> response = new HashMap<>();
        ArrayList<String> results = new ArrayList<>();

        if ("java".equals(q)) {
            results.add("Java Tutorial");
            results.add("Spring Boot Guide");
            results.add("Java vs Python");
        } else if ("python".equals(q)) {
            results.add("Python Basics");
            results.add("Flask Backend");
            results.add("AI with Python");
        } else {   
            results.add("No results found");
        }

        response.put("query", q);
        response.put("results", results.toString()); 

        return response;
    }

    @PostMapping("/seedUrl")
    public String seedUrl(@RequestParam String uri) {
        Models page = new Models();
        page.setUrl(uri);
        webService.saveUrl(page);
        return "check database dude";
    }

    @GetMapping("/startCrawl")
    public String startCrawl(@RequestParam String url) {
        webCrawler.startCrawl(url);
        return "Crawling started!";
    }

    @GetMapping("/crawl")
    public Models crawler(@RequestParam String uri) {
        return webCrawler.scrapeUrl(uri);
    }
}