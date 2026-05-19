package com.chatclip.websearch.service;

import com.chatclip.websearch.models.Models;
import com.chatclip.websearch.repository.urlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class webService {
    @Autowired
    private urlRepository urlRepository;

    public Models saveUrl(Models page) {
        return urlRepository.save(page);
    }

    public Models saveData (Models details) {
        return urlRepository.save(details);
    }
}