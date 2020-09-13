package com.viewcent.data.interchange.utils;

public class Response
{
    
    private ResponseStatus status;
    
    private int            code;
    
    private Object         data;
    
    private String         message;
    
    public int getCode()
    {
        return code;
    }
    
    public void setCode(int code)
    {
        this.code = code;
    }
    
    public Object getData()
    {
        return data;
    }
    
    public void setData(Object data)
    {
        this.data = data;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public ResponseStatus getStatus()
    {
        return status;
    }
    
    public void setStatus(ResponseStatus status)
    {
        this.status = status;
    }
}
