package com.example.landservers.models;

public class Land {

    private String Name,LandImage,LandDesription,LandPrice,SizeOfLand,LandMenuId;

    public Land() {
    }

    public Land(String name , String landImage , String landDesription , String landPrice , String sizeOfLand , String landMenuId) {
        Name = name;
        LandImage = landImage;
        LandDesription = landDesription;
        LandPrice = landPrice;
        SizeOfLand = sizeOfLand;
        LandMenuId = landMenuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLandImage() {
        return LandImage;
    }

    public void setLandImage(String landImage) {
        LandImage = landImage;
    }

    public String getLandDesription() {
        return LandDesription;
    }

    public void setLandDesription(String landDesription) {
        LandDesription = landDesription;
    }

    public String getLandPrice() {
        return LandPrice;
    }

    public void setLandPrice(String landPrice) {
        LandPrice = landPrice;
    }

    public String getSizeOfLand() {
        return SizeOfLand;
    }

    public void setSizeOfLand(String sizeOfLand) {
        SizeOfLand = sizeOfLand;
    }

    public String getLandMenuId() {
        return LandMenuId;
    }

    public void setLandMenuId(String landMenuId) {
        LandMenuId = landMenuId;
    }
}
