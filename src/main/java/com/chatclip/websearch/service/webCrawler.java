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
import java.net.URL;


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

    public List<String> getSitemapUrls(String url) {
        List<String> siteUrl = new ArrayList<>();
        try {
             URL parsed = new URL(url);
            String baseUrl = parsed.getProtocol() + "://" + parsed.getHost();

            Document sietmap_got = Jsoup.connect(baseUrl + "/sitemap.xml").get();
            Elements locs = sietmap_got.select("loc");

            for (Element loc : locs) {
                siteUrl.add(loc.text());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return siteUrl;
    }

    public boolean isAllowed(String url) {
        
        try {
            URL parsed = new URL(url);
            String baseUrl = parsed.getProtocol() + "://" + parsed.getHost();

            Document data_got = Jsoup.connect(baseUrl + "/robots.txt").get();
    
            String robotsText = data_got.text();
            String[] lines = robotsText.split("\n");
    
            for (String line : lines) {
                if (line.startsWith("Disallow:")) {
                    String path = line.replace("Disallow:", "").trim();
                    if (!path.isEmpty() && url.contains(path)) {
                        return false; // not allowed
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }

    }

    public void startCrawl(String seedUrl) {
        HashSet<String> toVisit = new HashSet<>();
        HashSet<String> visited = new HashSet<>();

        toVisit.add(seedUrl);

        int maxPages = 50;

        List<String> sitemapUrls = getSitemapUrls(seedUrl);

        if (sitemapUrls != null) {toVisit.addAll(sitemapUrls);}


        while (!toVisit.isEmpty() && visited.size() < maxPages) {
            Iterator<String> visiting = toVisit.iterator();
            String currentUrl = visiting.next();   // actual url string
            visiting.remove();                     // remove from toVisit
            visited.add(currentUrl);              // add to visited

            if (isAllowed(currentUrl)) {
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
}