package com.jerry.serverapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 书本实体类
 * <p>
 * Created by xujierui on 2018/3/5.
 */

public class Book implements Parcelable {
    private String name;
    private String author;
    private float price;

    public Book(String name, String author, float price) {
        this.name = name;
        this.author = author;
        this.price = price;
    }

    protected Book(Parcel in) {
        this.name = in.readString();
        this.author = in.readString();
        this.price = in.readFloat();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(author);
        dest.writeFloat(price);
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }
}
