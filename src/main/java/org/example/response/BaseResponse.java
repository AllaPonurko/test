package org.example.response;

public abstract class BaseResponse<T>{
    private final String message;
    private final T entity;
    public BaseResponse(String message, T entity)
    {
        this.message = message;
        this.entity = entity;
    }

    public String toString() {
        return message+": "+entity.toString();
    }
}
