package com.example.eventtracker;

public class Event {
    String title;
    String desc;
    String date;
    String time;
    String dept;
    String phn;
    String email;
    String image_name;
    String adminName;

    public Event() {}

    public Event(String title, String desc, String date, String time, String dept, String phn, String email, String image_name, String adminName){
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.time = time;
        this.dept = dept;
        this.phn = phn;
        this.email = email;
        this.image_name = image_name;
        this.adminName = adminName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
