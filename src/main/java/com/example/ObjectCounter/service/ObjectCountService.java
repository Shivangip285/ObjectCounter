package com.example.ObjectCounter.service;

import com.example.ObjectCounter.dto.ObjectCountDto;
import com.example.ObjectCounter.exception.ObjectFoundException;
import com.example.ObjectCounter.model.ObjectCount;
import com.example.ObjectCounter.model.ObjectCountWithMongo;
import com.example.ObjectCounter.repository.ObjectRepository;
import com.example.ObjectCounter.repository.ObjectRepositoryWithMongo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

import static java.util.Objects.isNull;

@Service
public class ObjectCountService {

    private final ObjectRepository objectRepository;
    private final ObjectRepositoryWithMongo objectRepositoryWithMongo;

    private ModelMapper modelMapper;

    public ObjectCountService(ObjectRepository objectRepository, ObjectRepositoryWithMongo objectRepositoryWithMongo, ModelMapper modelMapper) {
        this.objectRepository = objectRepository;
        this.objectRepositoryWithMongo = objectRepositoryWithMongo;
        this.modelMapper = modelMapper;
    }

    public Optional<ObjectCountDto> getObjectById(int id) {
        Optional<ObjectCountWithMongo> byId = objectRepositoryWithMongo.findById(id);
        Optional<ObjectCountDto> objectCountDto= Optional.ofNullable(modelMapper.map(byId.get(), ObjectCountDto.class));
        return  Optional.ofNullable(objectCountDto.orElseThrow(() -> new NoSuchElementException("Counter not found for this id")));
    }

    public List<ObjectCountDto> getAllObjectCounter() {
        //List<ObjectCount> byId = objectRepository.findAllByOrderByCountAsc();
        List<ObjectCountDto> objectCountDto = new ArrayList<>();
        objectRepositoryWithMongo.findAll().stream().forEach(x->objectCountDto.add(modelMapper.map(x, ObjectCountDto.class)));
        if(objectCountDto.isEmpty()) {
            throw new EmptyStackException();
        }
        return objectCountDto;
    }
//no x,y,z
    //mapper config for different keys/fields like count-count1
    public ObjectCountDto createObjectCount(ObjectCount objectCount1) throws InstantiationException {
        if(objectRepositoryWithMongo.getObjectName(objectCount1.getObjectName()).size()!=0) {
            throw new InstantiationException("Object already present");
        }
        objectRepository.save(objectCount1);
        ObjectCountDto objectCountDto= modelMapper.map(objectCount1, ObjectCountDto.class);
        objectRepositoryWithMongo.insert(new ObjectCountWithMongo(objectCount1.getId(),objectCount1.getObjectName(),objectCount1.getCount()));
        ObjectCountWithMongo objectCountWithMongo=modelMapper.map(objectCount1, ObjectCountWithMongo.class);
        System.out.println(objectCountWithMongo);
        return objectCountDto;
    }

    public ObjectCountDto incrementObjectCount(@PathVariable(value="id") Integer id ) throws ObjectFoundException {
        Optional<ObjectCountWithMongo> byId = objectRepositoryWithMongo.findById(id);
        if(byId.isEmpty()){
            throw new ObjectFoundException("Object not found");
        }
        objectRepositoryWithMongo.findAndIncrementCountOrderById(id);
        ObjectCountDto objectCountDto= modelMapper.map(byId.get(), ObjectCountDto.class);
        return objectCountDto;
    }

    public ObjectCountDto decrementObjectCount(@PathVariable(value="id") Integer id ) throws ObjectFoundException {
        Optional<ObjectCountWithMongo> byId = objectRepositoryWithMongo.findById(id);
        if(byId.isEmpty()){
            throw new ObjectFoundException("Object not found");
        }
        byId.get().setCount(byId.get().getCount()-1);
        objectRepositoryWithMongo.findAndDecrementCountOrderById(id);
        ObjectCountDto objectCountDto= modelMapper.map(byId.get(), ObjectCountDto.class);
        return objectCountDto;
    }

    public ObjectCountDto deleteObjectCounter(@PathVariable(value="id") Integer id ) throws ObjectFoundException {
        Optional<ObjectCountWithMongo> byId = objectRepositoryWithMongo.findById(id);
        if(byId.isEmpty()){
            throw new ObjectFoundException("Object not found");
        }
        ObjectCountDto objectCountDto= modelMapper.map(byId, ObjectCountDto.class);
        objectRepositoryWithMongo.delete(byId.get());
        return objectCountDto;
    }
}
