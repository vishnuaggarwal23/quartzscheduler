package com.vishnu.aggarwal.rest.repository.mongo;

/*
Created by vishnu on 24/5/18 10:24 AM
*/

import com.vishnu.aggarwal.rest.document.HttpRequestResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface HttpRequestResponseRepository extends MongoRepository<HttpRequestResponse, String> {
}
