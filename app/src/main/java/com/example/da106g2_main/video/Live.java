package com.example.da106g2_main.video;

import java.sql.Timestamp;
import java.util.Date;

public class Live {
    private String live_id; // 直播ID
    private String member_id; // 會員ID
    private String videoAddress; // 影片位子
    private String teaser_content; // 直播預告內容
    private String title; // 直播標頭
    private int status; // 直播狀態
    private int watched_num; // 觀看人數
    private byte[] picture; // 預覽圖片
    private byte[] video; // 影片本體
    private Timestamp live_time; // 開始時間


    public Live() {
        super();
    }

    // 全塞
    public Live(String live_id, String member_id, String videoAddress, String teaser_content, String title,
                byte[] picture, byte[] video, Date live_time, Integer status, Integer watched_num) {
        super();
        this.live_id = live_id;
        this.member_id = member_id;
        this.videoAddress = videoAddress;
        this.teaser_content = teaser_content;
        this.title = title;
        this.picture = picture;
        this.video = video;
        this.live_time = new Timestamp(live_time.getTime());
        this.status = status;
        this.watched_num = watched_num;
    }

    public Live(String live_id, String member_id, String videoAddress, String teaser_content, String title,
                Date live_time, Integer status, Integer watched_num) {
        super();
        this.live_id = live_id;
        this.member_id = member_id;
        this.videoAddress = videoAddress;
        this.teaser_content = teaser_content;
        this.title = title;
        this.live_time = new Timestamp(live_time.getTime());
        this.status = status;
        this.watched_num = watched_num;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTeaser_content() {
        return teaser_content;
    }

    public void setTeaser_content(String teaser_content) {
        this.teaser_content = teaser_content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Timestamp getLive_time() {
        return live_time;
    }

    public void setLive_time(Date live_time) {
        this.live_time.setTime(live_time.getTime());
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWatched_num() {
        return watched_num;
    }

    public void setWatched_num(Integer watched_num) {
        this.watched_num = watched_num;
    }

    public String getVideoAddress() {
        return videoAddress;
    }

    public void setVideoAddress(String videoAddress) {
        this.videoAddress = videoAddress;
    }

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }
}
