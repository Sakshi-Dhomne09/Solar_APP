package com.solarapp.model;

public class All_Users_Model {







    public String mobile_number,password,confirm_password,member_name,phone_no,aadhar_card,electricity_bill,email,address,user_id,employee_key;


    public All_Users_Model() {
    }


    public All_Users_Model(String mobile_number, String password, String confirm_password, String member_name, String phone_no, String aadhar_card, String electricity_bill, String email, String address, String user_id, String employee_key) {
        this.mobile_number = mobile_number;
        this.password = password;
        this.confirm_password = confirm_password;
        this.member_name = member_name;
        this.phone_no = phone_no;
        this.aadhar_card = aadhar_card;
        this.electricity_bill = electricity_bill;
        this.email = email;
        this.address = address;
        this.user_id = user_id;
        this.employee_key = employee_key;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getAadhar_card() {
        return aadhar_card;
    }

    public void setAadhar_card(String aadhar_card) {
        this.aadhar_card = aadhar_card;
    }

    public String getElectricity_bill() {
        return electricity_bill;
    }

    public void setElectricity_bill(String electricity_bill) {
        this.electricity_bill = electricity_bill;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmployee_key() {
        return employee_key;
    }

    public void setEmployee_key(String employee_key) {
        this.employee_key = employee_key;
    }
}
