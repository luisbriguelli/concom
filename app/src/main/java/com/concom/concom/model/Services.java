package com.concom.concom.model;

import com.activeandroid.Model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Brian on 30/08/2015.
 */
public class Services extends Model {
    private String name;

    public Services(){
        super();
    }

    @JsonCreator
    public Services(@JsonProperty("text") String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
