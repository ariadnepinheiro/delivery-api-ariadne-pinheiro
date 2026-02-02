package com.deliverytech.delivery.exceptions;

public class BusinessException extends RuntimeException{
    public BusinessException(String mensagem){
        super(mensagem);
    }
} 