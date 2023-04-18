package com.example.eventtracker;

public class EventList {
    private final String eventTitle;
    private final String eventDesc;

    private final String adminName;

    public EventList(String eventTitle, String eventDesc, String adminName){
        this.eventTitle = eventTitle;
        this.eventDesc = eventDesc;
        this.adminName = adminName;
    }

    public String getEventTitle() {
        return eventTitle;
    }
    public String getEventDesc() {
        return eventDesc;
    }
    public  String getAdminName(){ return adminName; }
}
