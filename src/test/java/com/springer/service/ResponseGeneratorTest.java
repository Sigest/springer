package com.springer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springer.MainUnitTestClass;
import com.springer.ServletController;
import com.springer.model.Book;
import com.springer.model.Document;
import com.springer.model.Journal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;


@ContextConfiguration(locations = {"classpath:/spring/container/spring-config.xml"})
public class ResponseGeneratorTest extends MainUnitTestClass {
    @Mock
    private ObjectMapper objectMapperMock;

    @InjectMocks
    @Autowired
    private ResponseGenerator responseGenerator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(objectMapperMock.writeValueAsString(any(Document.class))).thenReturn(newBookJson);
    }

    @Test
    public void getResponseBookInProgress() throws Exception {
        String result = responseGenerator.getResponse(bookInProgress);

        assertEquals(inProgressMessage, result);
    }

    @Test
    public void getResponseJournalInProgress() throws Exception {
        String result = responseGenerator.getResponse(journalInProgress);

        assertEquals(inProgressMessage, result);
    }

    @Test
    public void getResponseNullObject() throws Exception {
        String result = responseGenerator.getResponse(null);

        assertEquals(notFoundMessage, result);
    }

    @Test
    public void getResponseBookProcessed() throws Exception {
        String result = responseGenerator.getResponse(bookProcessed);

        assertEquals(newBookJson, result);
    }

    @Test
    public void getResponseBookProcessedWithRealMapper() throws Exception {
        ObjectMapper objectMapperReal = new ObjectMapper();
        responseGenerator.setObjectMapper(objectMapperReal);

        String jsonToBreReturned = objectMapperReal.writeValueAsString(bookProcessed);
        String result = responseGenerator.getResponse(bookProcessed);

        assertEquals(jsonToBreReturned, result);
    }

}