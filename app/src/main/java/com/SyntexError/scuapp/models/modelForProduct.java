package com.SyntexError.scuapp.models;

public class modelForProduct {
    String imageLink , itemdes , name , count , quatity  ,  category , itemID ;

    public modelForProduct() {
    }

    public modelForProduct(String imageLink, String itemdes, String name, String count, String quatity, String category, String itemID) {
        this.imageLink = imageLink;
        this.itemdes = itemdes;
        this.name = name;
        this.count = count;
        this.quatity = quatity;
        this.category = category;
        this.itemID = itemID;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getItemdes() {
        return itemdes;
    }

    public void setItemdes(String itemdes) {
        this.itemdes = itemdes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getQuatity() {
        return quatity;
    }

    public void setQuatity(String quatity) {
        this.quatity = quatity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}
