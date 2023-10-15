package com.example.ObjectCounter.integration;

import com.example.ObjectCounter.dto.ObjectCountDto;
import com.example.ObjectCounter.exception.ObjectFoundException;
import com.example.ObjectCounter.model.ObjectCount;
import com.example.ObjectCounter.repository.ObjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.EmptyStackException;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ObjectCounterIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private ObjectRepository objectRepository;
    @BeforeEach
    void clean() {

        objectRepository.deleteAll();
    }
    @AfterEach
    void cleanDB() {
        objectRepository.deleteAll();
    }

    @Test
    void shouldReturnExpectedAssertFromGetCountApi() throws Exception {
        ObjectCount objectCount = ObjectCount.builder().objectName("Buildings").count(5).build();
        objectRepository.save(objectCount);

        mockMvc
                .perform(get("/counter/"+objectCount.getId()))
                .andExpect(status().is(200));
    }

    @Test
    void shouldThrowExceptionWhenObjectNotPresentForGetCountApi() throws Exception {
        int objectId=1;

        mockMvc
                .perform(get("/counter/" + objectId))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException));
    }

    @Test
    void shouldReturnExpectedAssertFromGetAllCounterApi() throws Exception {
        ObjectCount objectCount = ObjectCount.builder().objectName("Buildings").count(5).build();
        objectRepository.save(objectCount);

        mockMvc
                .perform(get("/counters"))
                .andExpect(status().is(200));
    }

    @Test
    void shouldThrowExceptionWhenObjectNotPresentForGetAllCounterApi() throws Exception {

       mockMvc
                .perform(get("/counters/"))
                .andExpect(status().is4xxClientError())
       .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmptyStackException));
    }

    @Test
    void shouldReturnExpectedAssertFromCreateCountApi() throws Exception {
        ObjectCount objectCount = ObjectCount.builder().objectName("Buildings").count(5).build();
        ObjectCountDto objectCountDto = ObjectCountDto.builder().objectName("Buildings").count(5).build();


        mockMvc
                .perform(post("/counter")
                        .content(new ObjectMapper().writeValueAsString(objectCount))
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(objectCountDto)));
    }

    @Test
    void   shouldThrowExceptionWhenObjectAlreadyPresentForCreateCountApi() throws Exception {
        ObjectCount objectCount = ObjectCount.builder().objectName("Buildings").count(5).build();
        objectRepository.save(objectCount);

        ResultActions resultActions = mockMvc
                .perform(post("/counter")
                        .content(new ObjectMapper().writeValueAsString(objectCount))
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().is4xxClientError());
        //resultActions--

        //.andExpect(result -> assertTrue(result.getResolvedException().getCause() instanceof InstantiationException));
    }
    @Test
    void shouldReturnBadRequestWhenRequestBodyIsInvalidForCreateCountApi() throws Exception {
        Map<String,String> objectCount= Map.of("object","Buildings");
        ObjectCountDto objectCountDto = ObjectCountDto.builder().objectName("Buildings").count(5).build();

        mockMvc
                .perform(post("/counter")
                        .content(new ObjectMapper().writeValueAsString(objectCount))
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnExpectedAssertFromIncrementCountApi() throws Exception {
        ObjectCount objectCount = ObjectCount.builder().objectName("Buildings").count(5).build();
        objectRepository.save(objectCount);
        ObjectCountDto objectCountDto = ObjectCountDto.builder().objectName("Buildings").count(6).build();

        mockMvc
                .perform(put("/counter/"+objectCount.getId()+"/increment")
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().isOk())
               .andExpect(content().string(new ObjectMapper().writeValueAsString(objectCountDto)));
    }

    @Test
    void shouldThrowExceptionWhenObjectNotPresentForIncrementCountApi() throws Exception {
        int objectId=1;

       mockMvc
                .perform(put("/counter/" + objectId+"/increment")
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().is4xxClientError())
               .andExpect(result -> assertTrue(result.getResolvedException().getCause() instanceof ObjectFoundException))
               .andExpect(result -> assertTrue(result.getResolvedException().getCause().getMessage().equals("Object not found")));
    }
    @Test
    void shouldReturnExpectedAssertFromDecrementCountApi() throws Exception {
        ObjectCount objectCount=ObjectCount.builder().objectName("Buildings").count(5).build();
        ObjectCountDto objectCountDto=ObjectCountDto.builder().objectName("Buildings").count(4).build();
        objectRepository.save(objectCount);

     mockMvc
                .perform(put("/counter/" + objectCount.getId()+"/decrement")
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().isOk())
             .andExpect(content().string(new ObjectMapper().writeValueAsString(objectCountDto)));
    }

    @Test
    void shouldThrowExceptionWhenObjectNotPresentForDecrementCountApi() throws Exception {
        int objectId=1;

        mockMvc
                .perform(put("/counter/" + objectId+"/decrement")
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().is4xxClientError())
                 .andExpect(result -> assertTrue(result.getResolvedException().getCause() instanceof ObjectFoundException))
                .andExpect(result -> assertTrue(result.getResolvedException().getCause().getMessage().equals("Object not found")));
    }
    @Test
    void shouldReturnExpectedAssertFromDeleteCountApi() throws Exception, ObjectFoundException {
        ObjectCount objectCount = ObjectCount.builder().objectName("Buildings").count(5).build();
        objectRepository.save(objectCount);

        mockMvc
                .perform(delete("/counter/"+objectCount.getId())
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().isOk());
                //.andExpect(content().string(new ObjectMapper().writeValueAsString(modelMapper.map(objectCount, ObjectCountDto.class))));
    }

    @Test
    void shouldThrowExceptionWhenObjectNotPresentForDeleteCountApi() throws Exception {
        int objectId=1;

        mockMvc
                .perform(delete("/counter/" + objectId)
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().is4xxClientError())
        .andExpect(result -> assertTrue(result.getResolvedException().getCause() instanceof ObjectFoundException))
        .andExpect(result -> assertTrue(result.getResolvedException().getCause().getMessage().equals("Object not found")));
        //getCause()
    }

}
//1-success,1-failure scn for all endpoints