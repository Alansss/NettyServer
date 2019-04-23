package com.alan.config;

import lombok.Data;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * @Auther: Wang Lijie
 * @Date: 2019/4/19 11:35
 * @Description: TODO
 */
@Data
public class GlobalConfigManager {

    private static GlobalConfigManager instance = null;

    private static Configuration config;

    public static GlobalConfigManager getInstance() {
        if (null == instance){
            instance = new GlobalConfigManager();
        }
        return instance;
    }

    private String aaa;
    private String ffffffsdf;
    private String handlerpath;
    private Integer rrrrrrr;

    public void init(){
        try {
            config = new Configurations().properties(new File("global.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        Iterator<String> keys = config.getKeys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = config.getString(key);
            try {
                Field field = instance.getClass().getDeclaredField(key);
                field.setAccessible(true);

                if ("java.lang.Integer".equals(field.getGenericType().getTypeName())) {
                    field.set(instance, Integer.parseInt(value));
                } else if ("java.lang.Long".equals(field.getGenericType().getTypeName())) {
                    field.set(instance, Long.parseLong(value));
                } else if ("java.lang.Double".equals(field.getGenericType().getTypeName())) {
                    field.set(instance, Double.parseDouble(value));
                } else if ("java.lang.Float".equals(field.getGenericType().getTypeName())) {
                    field.set(instance, Float.parseFloat(value));
                } else {
                    field.set(instance, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) {
//        GlobalConfig instance = GlobalConfig.getInstance();
//        System.out.println(instance.getAaa());
//        System.out.println(instance.getFfffffsdf());
//        System.out.println(instance.getRrrrrrr());
    }


}
