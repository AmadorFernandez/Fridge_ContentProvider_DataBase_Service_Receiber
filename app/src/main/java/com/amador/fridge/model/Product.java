package com.amador.fridge.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amador on 3/03/17.
 */

public class Product implements Parcelable {

    private long id;
    private String name;
    private String dateCaducity;
    private long categotyId;
    private int stock;

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getWarningInsert() {
        return warningInsert;
    }

    private int timeOfWarning;
    private int warningInsert;

    public Product(){

        name = "";
        id = -1;
        dateCaducity = "";
        timeOfWarning = -1;
        warningInsert = 0;
        stock = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (getId() != product.getId()) return false;
        if (getCategotyId() != product.getCategotyId()) return false;
        return getName() != null ? getName().equals(product.getName()) : product.getName() == null;

    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCaducity() {
        return dateCaducity;
    }

    public void setDateCaducity(String dateCaducity) {
        this.dateCaducity = dateCaducity;
    }

    public long getCategotyId() {
        return categotyId;
    }

    public void setCategotyId(long categotyId) {
        this.categotyId = categotyId;
    }

    public int getTimeOfWarning() {
        return timeOfWarning;
    }

    public void setTimeOfWarning(int timeOfWarning) {
        this.timeOfWarning = timeOfWarning;
    }

    public int isWarningInsert() {
        return warningInsert;
    }

    public void setWarningInsert(int warningInsert) {
        this.warningInsert = warningInsert;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.dateCaducity);
        dest.writeLong(this.categotyId);
        dest.writeInt(this.stock);
        dest.writeInt(this.timeOfWarning);
        dest.writeInt(this.warningInsert);
    }

    protected Product(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.dateCaducity = in.readString();
        this.categotyId = in.readLong();
        this.stock = in.readInt();
        this.timeOfWarning = in.readInt();
        this.warningInsert = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
