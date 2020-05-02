package com.example.da106g2_main.mall;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 3L;
    private String order_id;
    private String item_id;
    private Integer item_quantity;
    private Double item_unit_price;
    private String item_review;
    private Double item_rate;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public Integer getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(Integer item_quantity) {
        this.item_quantity = item_quantity;
    }

    public Double getItem_unit_price() {
        return item_unit_price;
    }

    public void setItem_unit_price(Double item_unit_price) {
        this.item_unit_price = item_unit_price;
    }

    public String getItem_review() {
        return item_review;
    }

    public void setItem_review(String item_review) {
        this.item_review = item_review;
    }

    public Double getItem_rate() {
        return item_rate;
    }

    public void setItem_rate(Double item_rate) {
        this.item_rate = item_rate;
    }
}
