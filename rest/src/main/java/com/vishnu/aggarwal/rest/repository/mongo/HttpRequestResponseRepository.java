package com.vishnu.aggarwal.rest.repository.mongo;

/*
Created by vishnu on 24/5/18 10:24 AM
*/

import com.vishnu.aggarwal.rest.document.HttpRequestResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpRequestResponseRepository extends MongoRepository<HttpRequestResponse, String> {
}
