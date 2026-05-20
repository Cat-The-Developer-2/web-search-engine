package com.chatclip.websearch.service;

import com.chatclip.websearch.models.Models;
import com.chatclip.websearch.repository.urlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.bson.Document;
import java.util.List;

@Service
public class webService {
    @Autowired
    private urlRepository urlRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public Models saveUrl(Models page) {
        return urlRepository.save(page);
    }

    public Models saveData (Models details) {
        return urlRepository.save(details);
    }

    public List<Models> search(String query) {
           String jsonQuery = """
                {
                "index": "default",
                "text": {
                    "query": "%s",
                    "path": ["title", "content", "url"]
                }
                }
            """.formatted(query);

            TypedAggregation<Models> agg = Aggregation.newAggregation(
                Models.class,
                new AggregationOperation() {
                    @Override
                    public Document toDocument(AggregationOperationContext context) {
                        return new Document ("$search", Document.parse(jsonQuery));
                    }
                }
            );

            return mongoTemplate.aggregate(agg, Models.class).getMappedResults();
    }
}