package com.springer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springer.model.Book;
import com.springer.model.Document;
import com.springer.model.Journal;
import com.springer.model.Watermark;
import com.springer.service.ResponseGenerator;
import com.springer.service.WatermarkStoreImpl;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(locations = {"classpath:/spring/container/spring-config.xml"})
public class ServletControllerTest  extends MainUnitTestClass {


    @Mock
    private WatermarkStoreImpl watermarkStoreMock;

    @Mock
    private ResponseGenerator responseGeneratorMock;

    @InjectMocks
    @Autowired
    private ServletController servletController;

    private MockMvc mockMvc;

    private String existBookId = "53317a2d-08da-4080-975e-8e285c5d2f35";
    private String existJournalId = "e4e5be66-3bf7-49fe-b0a6-e764cf7d7d83";
    private String nonExistId = "108e1e4d-33d0-4b10-abfc-3680887d8639";
    private String inProgressBookId = "ecf37ae5-72bd-4ff2-989a-ca207f1da91d";
    private String inProgressJournalId = "81fefc27-28a5-4426-b1ea-73e14064a25c";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(servletController).build();

        when(watermarkStoreMock.getStatus(eq(existBookId))).thenReturn(bookProcessed);
        when(watermarkStoreMock.getStatus(eq(existJournalId))).thenReturn(journalProcessed);
        when(watermarkStoreMock.getStatus(eq(nonExistId))).thenReturn(null);
        when(watermarkStoreMock.getStatus(eq(inProgressBookId))).thenReturn(bookInProgress);
        when(watermarkStoreMock.getStatus(eq(inProgressJournalId))).thenReturn(journalInProgress);
        when(watermarkStoreMock.processDocument(anyObject())).thenReturn(inProgressBookId);

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonBookToBreReturned = objectMapper.writeValueAsString(bookProcessed);
        String jsonJournalToBreReturned = objectMapper.writeValueAsString(journalProcessed);

        when(responseGeneratorMock.getResponse(eq(bookInProgress))).thenReturn(inProgressMessage);
        when(responseGeneratorMock.getResponse(eq(journalInProgress))).thenReturn(inProgressMessage);
        when(responseGeneratorMock.getResponse(eq(null))).thenReturn(notFoundMessage);
        when(responseGeneratorMock.getResponse(eq(bookProcessed))).thenReturn(jsonBookToBreReturned);
        when(responseGeneratorMock.getResponse(eq(journalProcessed))).thenReturn(jsonJournalToBreReturned);

        responseGeneratorMock.setObjectMapper(objectMapper);
        servletController.setObjectMapper(objectMapper);
    }

    @Test
    public void getWatermarkStatusBook() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.GET, "/v1/status/" + existBookId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.request().asyncStarted()).andReturn();

        Thread.sleep(50);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonMediaType))
                .andExpect(jsonPath("title", is("Book processed title")))
                .andExpect(jsonPath("author", is("Book processed author")))
                .andExpect(jsonPath("topic", is("Topic")))
                .andExpect(jsonPath("watermark.content", is("book")))
                .andExpect(jsonPath("watermark.title", is("Book processed title")))
                .andExpect(jsonPath("watermark.author", is("Book processed author")))
                .andExpect(jsonPath("watermark.topic", is("Topic")));
    }

    @Test
    public void getWatermarkStatusJournal() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.GET, "/v1/status/" + existJournalId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.request().asyncStarted()).andReturn();

        Thread.sleep(50);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonMediaType))
                .andExpect(jsonPath("title", is("Journal processed title")))
                .andExpect(jsonPath("author", is("Journal processed author")))
                .andExpect(jsonPath("topic").doesNotExist())
                .andExpect(jsonPath("watermark.content", is("journal")))
                .andExpect(jsonPath("watermark.title", is("Journal processed title")))
                .andExpect(jsonPath("watermark.author", is("Journal processed author")))
                .andExpect(jsonPath("watermark.topic").doesNotExist());
    }

    @Test
    public void getWatermarkStatusInProgressBook() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.GET, "/v1/status/" + inProgressBookId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.request().asyncStarted()).andReturn();

        Thread.sleep(50);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonMediaType))
                .andExpect(jsonPath("message", notNullValue()))
                .andExpect(jsonPath("status", is("in progress")));
    }

    @Test
    public void getWatermarkStatusInProgressJournal() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.GET, "/v1/status/" + inProgressJournalId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.request().asyncStarted()).andReturn();

        Thread.sleep(50);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonMediaType))
                .andExpect(jsonPath("message", notNullValue()))
                .andExpect(jsonPath("status", is("in progress")));
    }

    @Test
    public void getWatermarkStatusNonExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.GET, "/v1/status/" + nonExistId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.request().asyncStarted()).andReturn();

        Thread.sleep(50);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonMediaType))
                .andExpect(jsonPath("message", notNullValue()))
                .andExpect(jsonPath("status", is("not found")));
    }

    @Test
    public void getWatermarkStatusNoIdInputed() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.GET, "/v1/status/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.request().asyncNotStarted()).andReturn();
    }


    @Test
    public void processBook() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.POST, "/v1/watermark")
                .content(newBookJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.request().asyncStarted()).andReturn();

        Thread.sleep(50);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(textMediaType))
                .andExpect(content().string(is(inProgressBookId)))
                .andDo(print());
    }

    @Test
    public void processJournal() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.POST, "/v1/watermark")
                .content(newJournalJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.request().asyncStarted()).andReturn();

        Thread.sleep(50);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(textMediaType))
                .andExpect(content().string(is(inProgressBookId)))
                .andDo(print());
    }

    @Test
    public void processNoBody() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.POST, "/v1/watermark")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.request().asyncNotStarted()).andReturn();
    }

    @Test
    public void processEmptyBody() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.POST, "/v1/watermark")
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.request().asyncNotStarted()).andReturn();
    }

    @Test
    public void processEmptyJson() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.POST, "/v1/watermark")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.request().asyncStarted()).andReturn();

        Thread.sleep(50);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(textMediaType))
                .andExpect(content().string(is("Service error")))
                .andDo(print());
    }

    @Test
    public void processNotCorrectJson() throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.POST, "/v1/watermark")
                .content("{\"unknownField\":\"some text\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.request().asyncStarted()).andReturn();

        Thread.sleep(50);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(textMediaType))
                .andExpect(content().string(is("Incorrect document")))
                .andDo(print());
    }

}