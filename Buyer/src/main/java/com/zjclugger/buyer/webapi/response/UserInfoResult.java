package com.zjclugger.buyer.webapi.response;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.zjclugger.lib.api.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class UserInfoResult extends BaseEntity {
    @SerializedName("email")
    private String email;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("orgId")
    private String orgId;
    @SerializedName("orgName")
    private String orgName;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("sexName")
    private String sexName;
    @SerializedName("imageUrl")
    private List<String> imageUrl = new ArrayList<>();
    private String address;
    private String birthDate;
    private double balance;

    public UserInfoResult() {
    }

    protected UserInfoResult(Parcel in) {
        email = in.readString();
        id = in.readString();
        name = in.readString();
        orgId = in.readString();
        orgName = in.readString();
        phoneNumber = in.readString();
        sexName = in.readString();
        imageUrl = in.createStringArrayList();
        address = in.readString();
        birthDate = in.readString();
        balance = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(orgId);
        dest.writeString(orgName);
        dest.writeString(phoneNumber);
        dest.writeString(sexName);
        dest.writeStringList(imageUrl);
        dest.writeString(address);
        dest.writeString(birthDate);
        dest.writeDouble(balance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfoResult> CREATOR = new Creator<UserInfoResult>() {
        @Override
        public UserInfoResult createFromParcel(Parcel in) {
            return new UserInfoResult(in);
        }

        @Override
        public UserInfoResult[] newArray(int size) {
            return new UserInfoResult[size];
        }
    };

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
