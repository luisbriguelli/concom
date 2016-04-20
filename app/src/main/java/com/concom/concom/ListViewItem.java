package com.concom.concom;

/**
 * Created by Luis Fernando Briguelli da Silva on 17/11/2014.
 */
public class ListViewItem extends com.activeandroid.app.Application {
    private Object object;
    private int type;
    private String key;

    public ListViewItem() {
        super();
    }

    public ListViewItem(Object object, int type){
        this.object = object;
        this.type = type;
    }

    public ListViewItem(Object object, int type, String key){
        this.object = object;
        this.type = type;
        this.key = key;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
