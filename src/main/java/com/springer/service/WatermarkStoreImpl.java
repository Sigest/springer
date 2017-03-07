package com.springer.service;

import com.springer.model.Book;
import com.springer.model.ContentType;
import com.springer.model.Document;
import com.springer.model.Watermark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Service
public class WatermarkStoreImpl implements WatermarkStore {
    private Map<String, Document> docStore;

    private ExecutorService executorService;

    @Value("${work.imitation.ms}")
    private Integer delayForImitation;

    WatermarkStoreImpl() {
        docStore = new ConcurrentHashMap<>();
    }

    @Override
    public String processDocument(Document document) {
        String id = UUID.randomUUID().toString();

        docStore.put(id, document);

        executorService.submit(runWatermarkProcess(id, document));

        return id;
    }

    @Override
    public Document getStatus(String id) {
        return docStore.get(id);
    }

    private Runnable runWatermarkProcess(final String id, final Document doc) {
        return new Runnable() {
            @Override
            public void run()  {
                Watermark watermark = new Watermark();
                watermark.setAuthor(doc.getAuthor());
                watermark.setTitle(doc.getTitle());

                if (doc instanceof Book) {
                    watermark.setTopic(((Book) doc).getTopic());
                    watermark.setContent(ContentType.BOOK.name().toLowerCase());
                } else {
                    watermark.setContent(ContentType.JOURNAL.name().toLowerCase());
                    watermark.setTopic(null);
                }

                try {
                    Thread.sleep(delayForImitation); //Just for work imitation
                } catch (InterruptedException ex) {

                }

                doc.setWatermark(watermark);
                docStore.put(id, doc);
            }
        };
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
