package com.alan.entity;
/*
 * 传输请求对象
 */

import java.io.Serializable;

public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -4546652381492921069L;
 
    private String id;
    private Object data;
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public Object getData() {
        return data;
    }
 
    public void setData(Object data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "RpcRequest{" + "id='" + id + '\'' + ", data=" + data + '}';
    }
}