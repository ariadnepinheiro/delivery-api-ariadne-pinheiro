package com.deliverytech.delivery.exceptions;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String mensagem){
        super(mensagem);
    }
}