package com.springer;

import com.springer.model.Book;
import com.springer.model.Journal;
import com.springer.model.Watermark;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

public class MainUnitTestClass {
    protected final String inProgressMessage = "{\"message\":\"document is being processed by the service. please wait\",\"status\":\"in progress\"}";
    protected final String notFoundMessage = "{\"message\":\"document id not found. please check your id\",\"status\":\"not found\"}";

    protected final String newBookJson = "{\n" +
            "    \"title\": \"Simple title\",\n" +
            "    \"author\": \"German Sidorenko\",\n" +
            "    \"topic\": \"business\"\n" +
            "}";

    protected final String newJournalJson = "{\n" +
            "    \"title\": \"Simple title\",\n" +
            "    \"author\": \"German Sidorenko\"\n" +
            "}";

    protected final Book bookInProgress;
    protected final Book bookProcessed;
    protected final Journal journalInProgress;
    protected final Journal journalProcessed;

    protected final Charset charset = Charset.forName("ISO-8859-1");
    protected final MediaType jsonMediaType = new MediaType("application", "json", charset);
    protected final MediaType textMediaType = new MediaType("text", "plain", charset);

    public MainUnitTestClass() {
        bookInProgress = new Book();
        bookInProgress.setTitle("Book title");
        bookInProgress.setAuthor("Book author");
        bookInProgress.setTopic("Topic");

        journalInProgress = new Journal();
        journalInProgress.setTitle("Journal title");
        journalInProgress.setAuthor("Journal author");

        bookProcessed = new Book();
        bookProcessed.setTitle("Book processed title");
        bookProcessed.setAuthor("Book processed author");
        bookProcessed.setTopic("Topic");
        bookProcessed.setWatermark(new Watermark(bookProcessed));

        journalProcessed = new Journal();
        journalProcessed.setTitle("Journal processed title");
        journalProcessed.setAuthor("Journal processed author");
        journalProcessed.setWatermark(new Watermark(journalProcessed));
    }

}
