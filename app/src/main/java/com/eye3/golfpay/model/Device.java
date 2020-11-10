package com.eye3.golfpay.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
/*
* 장비 info VO 모델
 */
public class Device  implements Serializable {

    @SerializedName("deviceId")
    @Expose
    public String deviceId = "";

    @SerializedName("deviceName")
    @Expose
    public String deviceName = "";

    @SerializedName("tollcenterName")
    @Expose
    public String tollcenterName = "";

    @SerializedName("deviceCategoryCode")
    @Expose
    public String deviceCategoryCode = "";

    @SerializedName("deviceCategoryName")
    @Expose
    public String deviceCategoryName = ""; //5G, LTE

    @SerializedName("deviceTypeCode")
    @Expose
    public String deviceTypeCode = "";

    @SerializedName("deviceTypeName")
    @Expose
    public String deviceTypeName = "";  //RU, DU

    @SerializedName("locationCode")
    @Expose
    public String locationCode = "";

    @SerializedName("zipCode")
    @Expose
    public String zipCode = "";

    @SerializedName("sido")
    @Expose
    public String sido = "";

    @SerializedName("sigungu")
    @Expose
    public String sigungu = "";

    @SerializedName("eubmyundong")
    @Expose
    public String eubmyundong = "";

    @SerializedName("bungi")
    @Expose
    public String bungi = "";

    @SerializedName("buildingName")
    @Expose
    public String buildingName = "";

    @SerializedName("addressDetail")
    @Expose
    public String addressDetail = "";

    @SerializedName("deviceLocationFloor")
    @Expose
    public String deviceLocationFloo = "";

    @SerializedName("buildingFloor")
    @Expose
    public String buildingFloor = "";

//    @SerializedName("stationShape")
//    @Expose
//    public String stationShape ;

    @SerializedName("latitude")
    @Expose
    public String latitude = "";

    @SerializedName("longitude")
    @Expose
    public String longitude = "";

    @SerializedName("latitudePositioning")
    @Expose
    public String latitudePositioning = "";

    @SerializedName("longitudePositioning")
    @Expose
    public String longitudePositioning = "";

    @SerializedName("deviceAcceptCheckStatusCode")
    @Expose
    public String deviceAcceptCheckStatusCode = "";

    @SerializedName("deviceAcceptCheckStatusName")
    @Expose
    public String deviceAcceptCheckStatusName = "";

    @SerializedName("uniqueNo")
    @Expose
    public String uniqueNo = "";

    @SerializedName("mainMaintenanceCompany")
    @Expose
    public String mainMaintenanceCompany = "";

    @SerializedName("manufacturer")
    @Expose
    public String manufacturer = "";

    @SerializedName("deviceSerialNo")
    @Expose
    public String deviceSerialNo = "";

    @SerializedName("deviceBarcodeNo")
    @Expose
    public String deviceBarcodeNo = "";

    @SerializedName("center")
    @Expose
    public String center = "";

    @SerializedName("constructionCompany")
    @Expose
    public String constructionCompany = "";

//    public Device(){
//        this.deviceId = "";
//        this.deviceName = "";
//        this.addressDetail = "";
//        this.deviceCategoryCode = "";
//        this.deviceTypeCode = "";
//        this.deviceSerialNo = "";
//        this.manufacturer = "";
//        this.mainMaintenanceCompany ="";
//        this.uniqueNo = "";
//        this.center = "";
//    }
//
//    public Device(String id, String mGuestName, String  addressDetail, String category, String type, String serial, String manufacturer, String mainMaintenanceCompany, String uniqueNo) {
//        this.deviceId = id;
//        this.deviceName = mGuestName;
//        this.addressDetail = addressDetail;
//        this.deviceCategoryCode = category;
//        this.deviceTypeCode = type;
//        this.deviceSerialNo = serial;
//        this.manufacturer = manufacturer;
//        this.mainMaintenanceCompany = mainMaintenanceCompany;
//        this.uniqueNo = uniqueNo;
//    }

}
