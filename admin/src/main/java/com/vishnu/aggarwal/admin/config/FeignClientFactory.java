package com.vishnu.aggarwal.admin.config;

/*
Created by vishnu on 17/4/18 12:51 PM
*/

import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
public class FeignClientFactory {
    private GsonEncoder gsonEncoder;
    private GsonDecoder gsonDecoder;
    private Request.Options options;

    public FeignClientFactory() {
        this.gsonDecoder = new GsonDecoder();
        this.gsonEncoder = new GsonEncoder();
        this.options = new Request.Options(15 * 1000, 25 * 1000);
    }

    public FeignClientFactory(GsonEncoder gsonEncoder, GsonDecoder gsonDecoder, Request.Options options) {
        this.gsonEncoder = gsonEncoder;
        this.gsonDecoder = gsonDecoder;
        this.options = options;
    }

    public <T> T getInstance(Class<T> apiService, String baseUrl) {
        return Feign
                .builder()
                .options(options)
                .encoder(gsonEncoder)
                .decoder(gsonDecoder)
                .logger(new Logger.JavaLogger())
                .logLevel(Logger.Level.FULL)
                .retryer(Retryer.NEVER_RETRY)
                .target(apiService, baseUrl);
    }
}
