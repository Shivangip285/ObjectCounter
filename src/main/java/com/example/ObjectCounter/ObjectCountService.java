package com.example.ObjectCounter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ObjectCountService {
    @Autowired
    private final ObjectRepository objectRepository;
    Counter counter=new Counter();

    public ObjectCountService(ObjectRepository objectRepository) {
        this.objectRepository = objectRepository;
    }

    public Optional<ObjectCount> getObjectById(int id) {
      return  Optional.ofNullable(objectRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Counter not found for this id")));
    }

    public List<ObjectCount> getAllObjectCounter() {
        List<ObjectCount> byId = objectRepository.findAll();
        if(byId.isEmpty()) {
            throw new NoSuchElementException();
        }
        return byId;
    }

    public ObjectCount createObjectCount(ObjectCount objectCount1) throws InstantiationException {

        if(objectRepository.findByObjectName(objectCount1.getObjectName()).size()!=0) {
            throw new InstantiationException();
        }
        objectRepository.save(objectCount1);
        return objectCount1;
    }

    public ObjectCount incrementObjectCount(@PathVariable(value="id") Integer id ){
        Optional<ObjectCount> byId = objectRepository.findById(id);
        int countValue = counter.incrementCountValue(byId.get().getCount());
        byId.get().setCount(countValue);
        objectRepository.save(byId.get());
        return byId.get();
    }

    public ObjectCount decrementObjectCount(@PathVariable(value="id") Integer id ){
        Optional<ObjectCount> byId = objectRepository.findById(id);
        int countValue = counter.decrementCountValue(byId.get().getCount());
        byId.get().setCount(countValue);
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
