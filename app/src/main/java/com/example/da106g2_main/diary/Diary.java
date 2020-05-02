package com.example.da106g2_main.diary;

import java.io.Serializable;
import java.sql.Timestamp;

public class Diary implements Serializable {
    private String diary_id;//日誌ID
    private String member_id;//成員(作者)ID
    private String diary_write;//內容
    private String diary_title;//標題
    private String diary_addr;//地址
    private Timestamp diary_time;//時間
    private byte[] diary_img;//圖片
    private int diary_status;//狀態

    public String getDiary_id() { //get取得資料
        return diary_id;
    }

    public void setDiary_id(String diary_id) { //set存入資料
        this.diary_id = diary_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getDiary_write() {
        return diary_write;
    }

    public void setDiary_write(String diary_write) {
        this.diary_write = diary_write;
    }

    public String getDiary_title() {
        return diary_title;
    }

    public void setDiary_title(String diary_title) {
        this.diary_title = diary_title;
    }

    public String getDiary_addr() {
        return diary_addr;
    }

    public void setDiary_addr(String diary_addr) {
        this.diary_addr = diary_addr;
    }

    public Timestamp getDiary_time() {
        return diary_time;
    }

    public void setDiary_time(Timestamp diary_time) {
        this.diary_time = diary_time;
    }

    public int getDiary_status() {
        return diary_status;
    }

    public void setDiary_status(int diary_status) {
        this.diary_status = diary_status;
    }

    public byte[] getDiary_img() {
        return diary_img;
    }

    public void setDiary_img(byte[] diary_img) {
        this.diary_img = diary_img;
    }
}
