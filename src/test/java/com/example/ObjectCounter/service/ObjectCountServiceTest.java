package com.example.ObjectCounter.service;

import com.example.ObjectCounter.dto.ObjectCountDto;
import com.example.ObjectCounter.exception.ObjectFoundException;
import com.example.ObjectCounter.model.ObjectCount;
import com.example.ObjectCounter.repository.ObjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
//@ExtendWith(MockitoExtension.class)
class ObjectCountServiceTest {

    @Mock
    private ObjectRepository objectRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks //use after all mock
    private ObjectCountService objectCountService;
    @Test
    void shouldReturnCountObjectById() {
        ObjectCount objectCount=ObjectCount.builder().objectName("Buildings").count(5).build();
        ObjectCountDto objectCountDto=ObjectCountDto.builder().objectName("Buildings").count(5).build();
        //ModelMapper modelMapper = mock(ModelMapper.class);
        when(modelMapper.map(any(),any())).thenReturn(objectCountDto);
        when(objectRepository.findById(objectCount.getId())).thenReturn(Optional.of(objectCount));

        Optional<ObjectCountDto> objectById = objectCountService.getObjectById(objectCount.getId());

        assertThat(objectById.get()).isEqualTo(objectCountDto);
    }

    @Test
    void shouldThrowExceptionWhenCountObjectWithGivenIdNotPresent() {
        ObjectCount objectCount=ObjectCount.builder().objectName("Buildings").count(5).build();

        when(modelMapper.map(any(),any())).thenReturn(null);
        when(objectRepository.findById(objectCount.getId())).thenReturn(Optional.of(new ObjectCount()));
        assertThrows(NoSuchElementException.class, ()->{
            objectCountService.getObjectById(objectCount.getId());
        });
    }

    @Test
    void shouldReturnAllObjectCounter() {
        List<ObjectCount> objectCount=List.of(ObjectCount.builder().objectName("Buildings").count(5).build(),ObjectCount.builder().objectName("Building").count(8).build());

        when(modelMapper.map(objectCount.get(0),ObjectCountDto.class)).thenReturn(ObjectCountDto.builder().objectName("Buildings").count(5).build());
        when(modelMapper.map(objectCount.get(1),ObjectCountDto.class)).thenReturn(ObjectCountDto.builder().objectName("Building").count(8).build());
        when(objectRepository.findAllByOrderByCountAsc()).thenReturn(objectCount);

        List<ObjectCountDto> allObjectCounter = objectCountService.getAllObjectCounter();

        assertThat(allObjectCounter).isEqualTo(List.of(ObjectCountDto.builder().objectName("Buildings").count(5).build(),ObjectCountDto.builder().objectName("Building").count(8).build()));
    }

    @Test
    void shouldThrowExceptionWhenNoObjectCounterIsNotPresent() {
        when(modelMapper.map(any(),any())).thenReturn(null);
        when(objectRepository.findAllByOrderByCountAsc()).thenReturn(Collections.emptyList());

        assertThrows(EmptyStackException.class, ()->{
            objectCountService.getAllObjectCounter();
        });
    }

    @Test
    void shouldReturnAndCreateObjectCounter() throws InstantiationException {
        ObjectCount objectCount=ObjectCount.builder().objectName("Buildings").count(5).build();
        ObjectCountDto objectCountDto=ObjectCountDto.builder().objectName("Buildings").count(5).build();

        when(modelMapper.map(objectCount,ObjectCountDto.class)).thenReturn(objectCountDto);
        when(objectRepository.save(any())).thenReturn(objectCount);

        ObjectCountDto objectCounter = objectCountService.createObjectCount(objectCount);

        assertThat(objectCounter).isEqualTo(objectCountDto);
    }

    @Test
    void shouldThrowExceptionIfCounterObjectAlreadyExist() throws InstantiationException {
        ObjectCount objectCount=ObjectCount.builder().objectName("Buildings").count(5).build();

        when(objectRepository.findByObjectName(any())).thenReturn(List.of(objectCount));

        assertThrows(InstantiationException.class, ()->{
            objectCountService.createObjectCount(objectCount);
        });
    }

    @Test
    void shouldReturnAndIncrementedObjectCounter() throws ObjectFoundException {
        ObjectCount objectCount=ObjectCount.builder().objectName("Buildings").count(5).build();
        ObjectCount objectCount1=ObjectCount.builder().objectName("Buildings").count(6).build();
        ObjectCountDto objectCountDto=ObjectCountDto.builder().objectName("Buildings").count(6).build();

        when(objectRepository.findById(any())).thenReturn(Optional.ofNullable(objectCount));
        when(objectRepository.save(any())).thenReturn(objectCount1);
        when(modelMapper.map(objectCount,ObjectCountDto.class)).thenReturn(objectCountDto);

        ObjectCountDto objectCounter = objectCountService.incrementObjectCount(objectCount.getId());

        assertThat(objectCounter).isEqualTo(objectCountDto);
    }

    @Test
    void shouldThrowExceptionIfCounterObjectDoesNotExist() throws ObjectFoundException {
        ObjectCount objectCount=ObjectCount.builder().objectName("Buildings").count(5).build();

        when(objectRepository.findById(objectCount.getId())).thenReturn(Optional.empty());

        assertThrows(ObjectFoundException.class, ()->{
            objectCountService.incrementObjectCount(objectCount.getId());
        });
    }

    @Test
    void shouldReturnAndDecrementObjectCounter() throws ObjectFoundException {
        ObjectCount objectCount=ObjectCount.builder().objectName("Buildings").count(5).build();
        ObjectCount resultObjectCount=ObjectCount.builder().objectName("Buildings").count(4).build();
        ObjectCountDto objectCountDto=ObjectCountDto.builder().objectName("Buildings").count(4).build();
        when(modelMapper.map(resultObjectCount,ObjectCountDto.class)).thenReturn(objectCountDto);
        when(objectRepository.findById(any())).thenReturn(Optional.ofNullable(objectCount));
        when(objectRepository.save(any())).thenReturn(objectCount);

        ObjectCountDto objectCounter = objectCountService.decrementObjectCount(objectCount.getId());

        assertThat(objectCounter).isEqualTo(objectCountDto);
    }

    @Test
    void shouldThrowExceptionInDecrementIfCounterObjectDoesNotExist() throws ObjectFoundException {
        ObjectCount objectCount=ObjectCount.builder().objectName("Buildings").count(5).build();

        when(objectRepository.findById(objectCount.getId())).thenReturn(Optional.empty());

        assertThrows(ObjectFoundException.class, ()->{
            objectCountService.decrementObjectCount(objectCount.getId());
        });
    }
}