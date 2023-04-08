package com.getir.readingIsGood.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadingIsGoodException extends RuntimeException{

    public ReadingIsGoodException(String message) {
        super(message);
    }

}
