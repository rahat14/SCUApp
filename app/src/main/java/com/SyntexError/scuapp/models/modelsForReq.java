package com.SyntexError.scuapp.models;

public class modelsForReq {
    String itemID , quantity ,  fromDate,  toDate , comment , userID , status , isDamage , userName  , reqID , equipName ;

    public modelsForReq() {
    }

    public modelsForReq(String itemID, String quantity, String fromDate, String toDate, String comment, String userID, String status, String isDamage, String userName, String reqID, String equipName) {
        this.itemID = itemID;
        this.quantity = quantity;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.comment = comment;
        this.userID = userID;
        this.status = status;
        this.isDamage = isDamage;
        this.userName = userName;
        this.reqID = reqID;
        this.equipName = equipName;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public modelsForReq(String itemID, String quantity, String fromDate, String toDate, String comment, String userID, String status, String isDamage, String userName, String reqID) {
        this.itemID = itemID;
        this.quantity = quantity;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.comment = comment;
        this.userID = userID;
        this.status = status;
        this.isDamage = isDamage;
        this.userName = userName;
        this.reqID = reqID;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDamage() {
        return isDamage;
    }

    public void setIsDamage(String isDamage) {
        this.isDamage = isDamage;
    }
}
