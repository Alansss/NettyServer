package com.alan.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wanglijie
 * 消息扫描器
 */
public class ClassScanner {

    //扫描包
    public static void main(String[] args) {
        //配置文件中配置包名
        scannerClass("com.alan.controller");
    }

    public static Map<String, Class<?>> scannerClass(String packagePath) {
        //
        String filePath = packagePath.replace(".", "/") + "/";
        System.out.println(filePath);

        Map<String, Class<?>> classes = new HashMap<>();

        //取得路径对象
        URL url = Thread.currentThread().getContextClassLoader().getResource(filePath);
        System.out.println(url.getPath());
        //判断文件是文件还是文件夹
        if ("file".equals(url.getProtocol())) {
            String path = url.getPath();
            if (path.contains("")) {

            }
            File folder = null;
            try {
                folder = new File(URLDecoder.decode(url.getPath(), "utf-8").substring(1));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //扫描所有(子文件夹)类
            scannerFile(folder, filePath, classes);

        }
        return classes;
    }

    private static void scannerFile(File folder, String root, Map<String, Class<?>> classes) {
        //先找到所在这个文件下下的文件对象
        File[] listFiles = folder.listFiles();
        for (File file : listFiles) {
            //是不是文件夹
            if (file.isDirectory()) {
                scannerFile(file, root + file.getName() + "/", classes);
            } else {
                String path = file.getAbsolutePath();
                //判断路径是不是以.class结尾
                if (path.endsWith(".class")) {
                    path = path.replace("\\", "/");
                    String className = root + path.substring(path.lastIndexOf("/") + 1, path.indexOf(".class"));
                    className = className.replace("/", ".");
                    System.out.println(className);
                    try {
                        classes.put(className, Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
