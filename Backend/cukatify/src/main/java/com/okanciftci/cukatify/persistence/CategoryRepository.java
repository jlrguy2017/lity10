package com.okanciftci.cukatify.persistence;

import com.okanciftci.cukatify.entities.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;


public interface CategoryRepository extends MongoRepository<Category, String> {

    @Query("{ '_id': ?0 }")
    Optional<Category> findBysId(ObjectId objectId);


}
