package com.concom.concom.model;

import com.activeandroid.Model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Brian on 30/08/2015.
 */
@JsonIgnoreProperties({"rate","facilities","roomRPH"})
public class Hotel extends Model {
    private String name;
    private String code;
    private String hotelRoom;
    private String guestCount;
    private String interestedPeople;
    private String ratePerPerson;

    List<Services> servicesList;

    public Hotel(){
        super();
    }

    @JsonCreator
    public Hotel(@JsonProperty("hotelName") String name, @JsonProperty("hotelCode") String code, @JsonProperty("services") List<Services> servicesList,@JsonProperty("ratePerPerson") String rate, @JsonProperty("hotelRoom") String room, @JsonProperty("interestedPeople") int interestedPeople ) {
        super();
        this.name = name;
        this.servicesList = servicesList;
        this.code = code;
        this.ratePerPerson = rate;
        this.hotelRoom = room;
        this.interestedPeople = String.valueOf(interestedPeople);
    }

    public List<Services> getServicesList() {
        return servicesList;
    }

    public void setServicesList(List<Services> servicesList) {
        this.servicesList = servicesList;
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

    public String getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(String guestCount) {
        this.guestCount = guestCount;
    }

    public String getRatePerPerson() {
        return ratePerPerson;
    }

    public void setRatePerPerson(String ratePerPerson) {
        this.ratePerPerson = ratePerPerson;
    }

    public String getHotelRoom() {
        return hotelRoom;
    }

    public void setHotelRoom(String hotelRoom) {
        this.hotelRoom = hotelRoom;
    }

    public String getInterestedPeople() {
        return interestedPeople;
    }

    public void setInterestedPeople(String interestedPeople) {
        this.interestedPeople = interestedPeople;
    }
}
