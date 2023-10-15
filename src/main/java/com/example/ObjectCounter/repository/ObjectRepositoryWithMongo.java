package com.example.ObjectCounter.repository;

import com.example.ObjectCounter.model.ObjectCount;
import com.example.ObjectCounter.model.ObjectCountWithMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectRepositoryWithMongo extends MongoRepository<ObjectCountWithMongo, Integer> {


  //@Query("{}")
  //@Update{}
  @Query("")
  ObjectCountWithMongo findAllByCountGreaterThan(int countCondition);
  @Query("{'objectName': ?0}")
  List<ObjectCount> getObjectName(String objectName);

//  @Modifying
//  @Query(value="update ObjectCountWithMongo o set o.objectName =abc where where o.id=?0")
//  ObjectCountWithMongo getObjectNames(@Param("id")int id);
  @Query("{'id' : 5}")
  @Update("{'$set': {'objectName': abc}}")
  void updateObjectName(int id);


  @Update("{ '$inc' : { 'count' : 1 } }")
  long findAndIncrementCountOrderById(int id);

   @Update("{ '$inc' : { 'count' : 1 } }")
   long findAndDecrementCountOrderById(int id);
}
//MongoTemplate
// Partial find and update  in one go
//external database
//mocking database in test