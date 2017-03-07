package com.springer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springer.exception.IncorrectDocumentTypeException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Watermark {
    private String content;
    private String title;
    private String author;
    private String topic;

    public Watermark() {
        super();
    }

    public Watermark(Document forDocument) {
        super();
        this.title = forDocument.getTitle();
        this.author = forDocument.getAuthor();

        if (forDocument instanceof Book) {
            content = ContentType.BOOK.name().toLowerCase();
            topic = ((Book) forDocument).getTopic();
        } else if (forDocument instanceof Journal) {
            content = ContentType.JOURNAL.name().toLowerCase();
        } else {
            throw new IncorrectDocumentTypeException();
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}