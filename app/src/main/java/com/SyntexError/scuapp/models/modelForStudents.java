package com.SyntexError.scuapp.models;

public class modelForStudents {
    String name , email ,id , pass ,ph, clubName,clubPosition , uid , state ;

    public modelForStudents() {
    }

    public modelForStudents(String name, String email, String id, String pass, String ph, String clubName, String clubPosition, String uid, String state) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.pass = pass;
        this.ph = ph;
        this.clubName = clubName;
        this.clubPosition = clubPosition;
        this.uid = uid;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubPosition() {
        return clubPosition;
    }

    public void setClubPosition(String clubPosition) {
        this.clubPosition = clubPosition;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
