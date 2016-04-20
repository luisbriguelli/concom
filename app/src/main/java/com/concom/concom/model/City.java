package com.concom.concom.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Brian on 30/08/2015.
 */
@JsonIgnoreProperties({"POI_NAME","COUNTRY_CODE","LATITUDE","LONGITUDE","STATE_CODE"})
@Table(name = "City")
public class City extends Model {
    @Column(name = "name", notNull = true, unique = true)
    private String name;
    @Column(name = "code", notNull = true)
    private String code;

    public City(){
        super();
    }

    @JsonCreator
    public City(@JsonProperty("CITY_NAME") String name, @JsonProperty("VENDOR_CODE") String code) {
        super();
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
