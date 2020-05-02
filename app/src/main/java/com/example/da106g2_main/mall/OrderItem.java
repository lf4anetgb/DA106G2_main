package com.example.da106g2_main.mall;

import java.io.Serializable;

public class OrderItem extends Item implements Serializable {
    private Integer item_quantity;
    private Double item_unit_price;

    public OrderItem(Item item, Integer item_quantity) {
//        Item(String item_id, String item_name, Integer item_stock)
        super(item.getItem_id(), item.getItem_name(), item.getItem_stock());
        this.item_quantity = item_quantity;
        this.item_unit_price = item.getItem_price();
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
}