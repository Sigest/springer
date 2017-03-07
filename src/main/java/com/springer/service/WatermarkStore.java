package com.springer.service;

import com.springer.model.Document;

public interface WatermarkStore {
    String processDocument(Document document);
    Document getStatus(String id);
}
