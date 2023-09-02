package com.example.ObjectCounter.controller;

import com.example.ObjectCounter.dto.ObjectCountDto;
import com.example.ObjectCounter.exception.ObjectFoundException;
import com.example.ObjectCounter.model.ObjectCount;
import com.example.ObjectCounter.service.ObjectCountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.CollectionUtils.isEmpty;

@WebMvcTest(ObjectCountController.class)
@AutoConfigureMockMvc(addFilters = false)
class ObjectCountControllerTest {

    // Try MockMVc
    // Do not call controller method
    @Autowired
    public MockMvc mockMvc;
    @MockBean
    private ObjectCountService objectCountService;

    @Test
    void shouldReturnExpectedAssertFromGetCountApi() throws Exception {
        int objectId=1;
        Optional<ObjectCountDto> objectCountDto= Optional.ofNullable(ObjectCountDto.builder().objectName("Buildings").count(5).build());
        when(objectCountService.getObjectById(objectId)).thenReturn(objectCountDto);

        mockMvc
                .perform(get("/getCount/"+objectId))
                .andExpect(status().is(200));
    }

    @Test
    void shouldReturnExpectedAssertFromGetAllCounterApi() throws Exception {
        List<ObjectCountDto> objectCountDto= List.of(ObjectCountDto.builder().objectName("Buildings").count(5).build(),ObjectCountDto.builder().objectName("Building").count(8).build());
        when(objectCountService.getAllObjectCounter()).thenReturn(objectCountDto);

        mockMvc
                .perform(get("/getAllCounter"))
                .andExpect(status().is(200));
    }

    @Test
    void shouldReturnExpectedAssertFromCreateCountApi() throws Exception {
        ObjectCount objectCount = ObjectCount.builder().objectName("Buildings").count(5).build();
        ObjectCountDto objectCountDto = ObjectCountDto.builder().objectName("Buildings").count(5).build();
        when(objectCountService.createObjectCount(objectCount)).thenReturn(objectCountDto);
       Map<String, String> a=new HashMap<>();
       a.put("a","b");
       a.put("a","c");
        System.out.println(new Locale("frCA").toLanguageTag());
        mockMvc
                .perform(post("/createCount")
                .content(new ObjectMapper().writeValueAsString(objectCount))
                .contentType(APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(objectCountDto)));
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsInvalidForCreateCountApi() throws Exception {
        Map<String,String> objectCount= Map.of("object","Buildings");
        ObjectCountDto objectCountDto = ObjectCountDto.builder().objectName("Buildings").count(5).build();

        mockMvc
                .perform(post("/createCount")
                        .content(new ObjectMapper().writeValueAsString(objectCount))
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnExpectedAssertFromIncrementCountApi() throws Exception, ObjectFoundException {
        int objectId=1;
        ObjectCount objectCount = ObjectCount.builder().id(objectId).objectName("Buildings").count(5).build();
        ObjectCountDto objectCountDto = ObjectCountDto.builder().objectName("Buildings").count(6).build();
        when(objectCountService.incrementObjectCount(objectCount.getId())).thenReturn(objectCountDto);

        mockMvc
                .perform(put("/incrementCount/"+objectId)
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(objectCountDto)));
    }

    @Test
    void shouldReturnExpectedAssertFromDecrementCountApi() throws Exception, ObjectFoundException {
        int objectId=1;
        ObjectCount objectCount = ObjectCount.builder().id(objectId).objectName("Buildings").count(5).build();
        ObjectCountDto objectCountDto = ObjectCountDto.builder().objectName("Buildings").count(4).build();
        when(objectCountService.decrementObjectCount(objectCount.getId())).thenReturn(objectCountDto);

        mockMvc
                .perform(put("/decrementCount/"+objectId)
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(objectCountDto)));
    }

    @Test
    void shouldReturnExpectedAssertFromDeleteCountApi() throws Exception, ObjectFoundException {
        int objectId=1;
        ObjectCount objectCount = ObjectCount.builder().id(objectId).objectName("Buildings").count(5).build();
        ObjectCountDto objectCountDto = ObjectCountDto.builder().objectName("Buildings").count(4).build();
        when(objectCountService.deleteObjectCounter(objectCount.getId())).thenReturn(objectCountDto);

        mockMvc
                .perform(delete("/deleteCounter/"+objectId)
                        .contentType(APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(objectCountDto)));
    }
}