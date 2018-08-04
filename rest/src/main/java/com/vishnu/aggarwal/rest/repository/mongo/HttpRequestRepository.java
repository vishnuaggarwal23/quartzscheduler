package com.vishnu.aggarwal.rest.repository.mongo;

/*
Created by vishnu on 23/5/18 3:50 PM
*/

import com.vishnu.aggarwal.rest.document.HttpRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpRequestRepository extends MongoRepository<HttpRequest, String> {
}
