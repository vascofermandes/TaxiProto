package com.pocketworks.taxi.taxiproto;

/**
 * Created by Vasco on 29/06/2016.
 */
public class Favourite {

    public int id;
    public String city_name;
    public String address;
    public String post_code;

    public Favourite() {

    }

    public Favourite(int id, String city_name, String address, String post_code) {
        this.id = id;
        this.city_name = city_name;
        this.address = address;
        this.post_code = post_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }
}