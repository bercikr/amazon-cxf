package com.googlecode.amazoncxf.util;

import com.amazon.webservices.awsecommerceservice.Item;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by rob on 5/10/2015.
 */
public class XmlMarshaller
{
    public static void marshall(Object x, String id){
        try {
            JAXBContext ctx = JAXBContext.newInstance(x.getClass());
            Marshaller m = ctx.createMarshaller();
            m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            m.marshal(x, new FileOutputStream(new File(id+".xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
