# 🔍 Web Search Engine

A full-stack web crawler and search engine built with Java, Spring Boot, and MongoDB Atlas.

![Project Architecture](image.png)

---

## What It Does

- Crawls the web starting from a seed URL
- Extracts page title and content (meta description or first 500 characters of body)
- Stores crawled pages in MongoDB Atlas
- Full-text search with relevance scoring via Atlas Search
- Deduplicates URLs to avoid crawling the same page twice
- Skips non-HTML files (PDFs, images, etc.)

---

## Tech Stack

- **Java 26** + **Spring Boot 4**
- **MongoDB Atlas** — cloud database + Atlas Search
- **jsoup** — web crawling and HTML parsing
- **Maven** — dependency management

---

## Project Structure

```
src/main/java/com/chatclip/websearch/
├── controller/
│   └── HomeController.java       # HTTP endpoints
├── models/
│   └── Models.java               # Page data model
├── repository/
│   └── urlRepository.java        # MongoDB repository
└── service/
    ├── webCrawler.java           # Crawler logic
    └── webService.java           # Database service
```

---

## API Endpoints

| Method | Endpoint                              | Description                      |
| ------ | ------------------------------------- | -------------------------------- |
| GET    | `/crawl?uri=https://example.com`      | Crawl a single page              |
| GET    | `/startCrawl?url=https://example.com` | Start full crawler from seed URL |
| POST   | `/seedUrl?uri=https://example.com`    | Manually save a URL              |
| GET    | `/search?q=query`                     | Search crawled pages             |

---

## Setup

1. Clone the repo
2. Copy `application.properties.example` to `application.properties`
3. Add your MongoDB Atlas connection string and password
4. Run with Maven:

```bash
mvn spring-boot:run
```

---

## MongoDB Atlas Search

This project uses Atlas Search for full-text search with relevance scoring.

After connecting your database, create a Search Index on the `webData` collection with dynamic mapping enabled.

---

## Built In

Less than 24 hours. Zero prior Java experience.
