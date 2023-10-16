package mz.co.keomasoftware.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;

import org.hibernate.mapping.Set;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
    
    public static String[] getNullPropNames(Object source){
        final BeanWrapper src = new BeanWrapperImpl(source);

        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyValues = new HashSet<>();

        for(PropertyDescriptor pd: pds){
            //Code Goes Here ...
        }
    }

}
