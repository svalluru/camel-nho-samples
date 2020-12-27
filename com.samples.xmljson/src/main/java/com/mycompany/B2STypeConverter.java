package com.mycompany;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.support.TypeConverterSupport;

public class B2STypeConverter extends TypeConverterSupport{

    @Override
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {

        String f =  (String) value;

        To t =  new To();
        t.setName("Converted " + f);

        return (T) t;


    }



    
}
