package com.example.da106g2_main.mall;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Item implements Serializable {

    private String item_id;
    private String item_type_id;
    private String item_name;
    private Integer item_stock;
    private Double item_price;
    private byte[] item_picture;
    private String item_content;
    private Integer item_status;

    public Item(String item_id, String item_name, Integer item_stock) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_stock = item_stock;
    }

    public Item() {
    }

    //比對用
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Item)) {
            return false;
        }
        return this.item_id.equals(((Item) obj).getItem_id());
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_type_id() {
        return item_type_id;
    }

    public void setItem_type_id(String item_type_id) {
        this.item_type_id = item_type_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Integer getItem_stock() {
        return item_stock;
    }

    public void setItem_stock(Integer item_stock) {
        this.item_stock = item_stock;
    }

    public Double getItem_price() {
        return item_price;
    }

    public void setItem_price(Double item_price) {
        this.item_price = item_price;
    }

    public byte[] getItem_picture() {
        return item_picture;
    }

    public void setItem_picture(byte[] item_picture) {
        this.item_picture = item_picture;
    }

    public String getItem_content() {
        return item_content;
    }

    public void setItem_content(String item_content) {
        this.item_content = item_content;
    }

    public Integer getItem_status() {
        return item_status;
    }

    public void setItem_status(Integer item_status) {
        this.item_status = item_status;
    }

}
