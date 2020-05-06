package com.example.da106g2_main.mall;

import java.io.Serializable;
import java.sql.Timestamp;

//此為訂單
public class Orders implements Serializable {
    private static final long serialVersionUID = 2L;
    private String order_id;
    private String member_id;
    private Double order_price;
    private String order_address;
    private Integer order_status;
    private Timestamp order_time;
    private Integer paywith;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public Double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(Double order_price) {
        this.order_price = order_price;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public Timestamp getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Timestamp order_time) {
        this.order_time = order_time;
    }

    public String getOrder_address() {
        return order_address;
    }

    public void setOrder_address(String order_address) {
        this.order_address = order_address;
    }

    public Integer getPaywith() {
        return paywith;
    }

    public void setPaywith(Integer paywith) {
        this.paywith = paywith;
    }
}
