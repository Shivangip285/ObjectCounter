package com.example.ObjectCounter.service;

import com.example.ObjectCounter.model.ObjectCount;
import com.example.ObjectCounter.exception.ObjectFoundException;
import com.example.ObjectCounter.repository.ObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObjectCountService {
    @Autowired
    private final ObjectRepository objectRepository;

    public ObjectCountService(ObjectRepository objectRepository) {
        this.objectRepository = objectRepository;
    }

    public Optional<ObjectCount> getObjectById(int id) {
        Optional<ObjectCount> byId = objectRepository.findById(id);
        return  Optional.ofNullable(byId.orElseThrow(() -> new NoSuchElementException("Counter not found for this id")));
    }

    public List<ObjectCount> getAllObjectCounter() {
        List<ObjectCount> byId = objectRepository.findAllByOrderByCountAsc();
        if(byId.isEmpty()) {
            throw new NoSuchElementException();
        }
        return byId;
    }

    public ObjectCount createObjectCount(ObjectCount objectCount1) throws InstantiationException {

        if(objectRepository.findByObjectName(objectCount1.getObjectName()).size()!=0) {
            throw new InstantiationException("Object already present");
        }
        objectRepository.save(objectCount1);
        return objectCount1;
    }

    public ObjectCount incrementObjectCount(@PathVariable(value="id") Integer id ) throws ObjectFoundException {
        Optional<ObjectCount> byId = objectRepository.findById(id);
        if(byId.isEmpty()){
            throw new ObjectFoundException("Object not found");
        }
        byId.get().setCount(byId.get().getCount()+1);
        objectRepository.save(byId.get());
        return byId.get();
    }

    public ObjectCount decrementObjectCount(@PathVariable(value="id") Integer id ) throws ObjectFoundException {
        Optional<ObjectCount> byId = objectRepository.findById(id);
        if(byId.isEmpty()){
            throw new ObjectFoundException("Object not found");
        }
        byId.get().setCount(byId.get().getCount());
        objectRepository.save(byId.get());
        return byId.get();
    }

    public ObjectCount deleteObjectCounter(@PathVariable(value="id") Integer id ) throws ObjectFoundException {
        Optional<ObjectCount> byId = objectRepository.findById(id);
        if(byId.isEmpty()){
            throw new ObjectFoundException("Object not found");
        }
        objectRepository.delete(byId.get());
        return byId.get();
    }
}
