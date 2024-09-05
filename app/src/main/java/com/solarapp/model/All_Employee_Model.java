package com.solarapp.model;

public class All_Employee_Model {







    public String address,
            email
,
            name
,
            phone_number
    ,
            uid,

    status

            ;



    public All_Employee_Model() {
    }


    public All_Employee_Model(String address, String email, String name, String phone_number, String uid, String status) {
        this.address = address;
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
        this.uid = uid;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
