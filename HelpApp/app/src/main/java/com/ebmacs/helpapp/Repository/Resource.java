package com.ebmacs.helpapp.Repository;


import android.util.Log;

import androidx.annotation.Nullable;

/*
* Classe genérica usada em conjunto com LiveData e Retrofit para garantir que
* os dados sejam repassados com seus devidos status durante a chamada
*
* */



public class Resource<T> {

    private String status;
    private T data;
    private String message;

    public Resource() {

    }

    public Resource(String status, @Nullable T data, @Nullable  String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public void update(String status, @Nullable T data, @Nullable  String message){
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
