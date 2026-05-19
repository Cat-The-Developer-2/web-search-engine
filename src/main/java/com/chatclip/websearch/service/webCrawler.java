package com.chatclip.websearch.service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;  
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.chatclip.websearch.models.Models;
import org.springframework.beans.factory.annotation.Autowired;
import com.chatclip.websearch.service.webService;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import com.chatclip.websearch.repository.urlRepository;


@Service
public class webCrawler {

    @Autowired
    private webService webService;

    @Autowired
    private urlRepository urlRepository;


  public Models  scrapeUrl(String uri) {
    try{
        Document data = Jsoup.connect(uri).get();
        String title = data.title();
        String content = data.select("meta[name=description]").attr("content");
        String bodyText = data.body().text();

        Models webPageDetails = new Models();
        
        webPageDetails.setUrl(uri);
        webPageDetails.setTitle(title);

        if (content.isEmpty()) {
            webPageDetails.setContent(bodyText.substring(0, Math.min(bodyText.length(), 500)));
        } else {
            webPageDetails.setContent(content);
        }


        Optional<Models> existing = urlRepository.findByUrl(uri);

        if (existing.isPresent()) {
           return existing.get();
        } else {
            webService.saveData(webPageDetails);
            return webPageDetails;
        }


    } catch (IOException e) {   
          e.printStackTrace();
          return null;
      }
    }

    public List<String> getLinks(String uri) {
        List<String> result = new ArrayList<>();

        try {
            Document data = Jsoup.connect(uri).get();
            Elements links = data.select("a[href]"); // links

            for (Element link : links) {
                result.add(link.attr("abs:href"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public void startCrawl(String seedUrl) {
        HashSet<String> toVisit = new HashSet<>();
        HashSet<String> visited = new HashSet<>();

        toVisit.add(seedUrl);

        int maxPages = 50;

        while (!toVisit.isEmpty() && visited.size() < maxPages) {
            Iterator<String> visiting = toVisit.iterator();
            String currentUrl = visiting.next();   // actual url string
            visiting.remove();                     // remove from toVisit
            visited.add(currentUrl);              // add to visited
            Models result = scrapeUrl(currentUrl);
            List<String> links = getLinks(currentUrl);

            if(links!= null) {  
                for (String link : links) {
                    if (!visited.contains(link) && !link.isEmpty() && !link.endsWith(".pdf") && !link.endsWith(".jpg") && !link.endsWith(".png")) {
                        toVisit.add(link);
                    }
                }
            }
        }
    }
}