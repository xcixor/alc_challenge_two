package com.peter.travelmantics;

import java.io.Serializable;

public class TravelDeal implements Serializable {
    private String mId;
    private String mTitle;
    private String mDescription;
    private String mPrice;
    private String imgUrl;

    public TravelDeal(){}

    public TravelDeal(String mTitle, String mDescription, String mPrice, String imgUrl) {
        this.setmId(mId);
        this.mTitle = mTitle;
        this.setmTitle(mTitle);
        this.setmDescription(mDescription);
        this.setmPrice(mPrice);
        this.setImgUrl(imgUrl);
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }
}
