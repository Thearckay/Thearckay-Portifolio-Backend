package com.thearckay.portifolio.dto;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse {

    private Integer status;
    private List<Object> data = new ArrayList<>();
    private String message;
    private List<Object> meta = new ArrayList<>();


    public ApiResponse(Integer status, List<Object> data, String message, List<Object> meta) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.meta = meta;
    }
    public ApiResponse(Integer status, Object objectToReturn, String message, Object metaToReturn){
        setStatus(status);
        addData(objectToReturn);
        setMessage(message);
        addMeta(metaToReturn);
    }


    public Boolean addMeta(Object objectToAdd){
        return getMeta().add(objectToAdd);
    }
    public Boolean addData(Object objectToAdd){
         return getData().add(objectToAdd);
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public List<Object> getData() {
        return data;
    }
    public void setData(List<Object> data) {
        this.data = data;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<Object> getMeta() {
        return meta;
    }
    public void setMeta(List<Object> meta) {
        this.meta = meta;
    }
}
