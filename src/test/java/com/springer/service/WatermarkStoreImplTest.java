package com.springer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springer.MainUnitTestClass;
import com.springer.model.Book;
import com.springer.model.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.DocumentDefaultsDefinition;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@ContextConfiguration(locations = {"classpath:/spring/container/spring-config.xml"})
public class WatermarkStoreImplTest extends MainUnitTestClass {
    private ExecutorService executorServiceReal;

    @InjectMocks
    @Autowired
    private WatermarkStoreImpl watermarkStore;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        executorServiceReal = Executors.newCachedThreadPool();
        watermarkStore.setExecutorService(executorServiceReal);
    }

    @Test
    public void processDocument() throws Exception {
        String result = watermarkStore.processDocument(bookInProgress);

        UUID resultUuid = UUID.fromString(result);

        assertNotEquals(UUID.randomUUID(), resultUuid);
    }

    @Test
    public void getStatusBookInProgress() throws Exception {
        String result = watermarkStore.processDocument(bookInProgress);
        Document book = watermarkStore.getStatus(result);

        assertEquals(book, bookInProgress);
    }

}