package com.chatclip.websearch.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "webData")
public class Models {

    @Id
    private String id;

    private String url, title, content;

    // getter
    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    // setter
    public void setId(String id_Got) {
        this.id = id_Got;
    }

    public void setUrl(String url_Got) {
        this.url = url_Got;
    }

    public void setTitle(String title_Got) {
        this.title = title_Got;
    }

    public void setContent(String content_Got) {
        this.content = content_Got;
    }
   
}