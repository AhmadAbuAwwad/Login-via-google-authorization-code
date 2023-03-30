package com.spring.oauth2.social.demo.exception;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails {

    private String error;
    private Integer status;
    private String pointer;
    private String details;
}
