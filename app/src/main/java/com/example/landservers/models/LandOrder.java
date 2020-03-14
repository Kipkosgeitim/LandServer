package com.example.landservers.models;

public class LandOrder {

    private String LandOrderId,LandNameLocation,SizeOfLand,Price,LandTitle;


    public LandOrder() {
    }

    public LandOrder(String landOrderId , String landNameLocation , String sizeOfLand , String price , String landTitle) {
        LandOrderId = landOrderId;
        LandNameLocation = landNameLocation;
        SizeOfLand = sizeOfLand;
        Price = price;
        LandTitle = landTitle;
    }

    public String getLandOrderId() {
        return LandOrderId;
    }

    public void setLandOrderId(String landOrderId) {
        LandOrderId = landOrderId;
    }

    public String getLandNameLocation() {
        return LandNameLocation;
    }

    public void setLandNameLocation(String landNameLocation) {
        LandNameLocation = landNameLocation;
    }

    public String getSizeOfLand() {
        return SizeOfLand;
    }

    public void setSizeOfLand(String sizeOfLand) {
        SizeOfLand = sizeOfLand;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getLandTitle() {
        return LandTitle;
    }

    public void setLandTitle(String landTitle) {
        LandTitle = landTitle;
    }
}
