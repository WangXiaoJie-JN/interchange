package com.viewcent.data.interchange.utils;

public enum ResponseStatus
{
    ERROR("ERROR"), OK("OK");
    
    private String value;
    
    ResponseStatus(String value)
    {
        this.value = value;
    }
    
    private String getValue()
    {
        return value;
    }
}
