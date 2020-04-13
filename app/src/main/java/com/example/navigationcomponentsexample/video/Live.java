package com.example.navigationcomponentsexample.video;

import java.sql.Timestamp;
import java.util.Date;

public class Live {
    private String live_id, // 直播ID
            member_id, // 會員ID
            videoAddress, // 影片位子
            teaser_content, // 直播開始時間
            title; // 直播標頭
    private byte[] pictuer, // 預覽圖片
            video; // 影片本體
    private Timestamp live_time; // 開始時間
    private Integer status, // 直播狀態
            watcher_num; // 觀看人數

    public Live() {
        super();
    }

    // 全塞
    public Live(String live_id, String member_id, String videoAddress, String teaser_content, String title,
                byte[] pictuer, byte[] video, Date live_time, Integer status, Integer watcher_num) {
        super();
        this.live_id = live_id;
        this.member_id = member_id;
        this.videoAddress = videoAddress;
        this.teaser_content = teaser_content;
        this.title = title;
        this.pictuer = pictuer;
        this.video = video;
        this.live_time = new Timestamp(live_time.getTime());
        this.status = status;
        this.watcher_num = watcher_num;
    }

    public Live(String live_id, String member_id, String videoAddress, String teaser_content, String title,
                Date live_time, Integer status, Integer watcher_num) {
        super();
        this.live_id = live_id;
        this.member_id = member_id;
        this.videoAddress = videoAddress;
        this.teaser_content = teaser_content;
        this.title = title;
        this.live_time = new Timestamp(live_time.getTime());
        this.status = status;
        this.watcher_num = watcher_num;
    }

    @Override
    public String toString() {
        StringBuffer re = new StringBuffer();
        re.append("[live_id=").append(live_id).append(", member_id=").append(member_id).append(", videoAddress=")
                .append(videoAddress).append(", teaser_content=").append(teaser_content).append(", title=")
                .append(title).append(", live_time=").append(live_time).append(", status=").append(status)
                .append(", watcher_num=").append(watcher_num).append("]\r\n");
        return re.toString();
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

    public byte[] getPictuer() {
        return pictuer;
    }

    public void setPictuer(byte[] pictuer) {
        this.pictuer = pictuer;
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

    public Integer getWatcher_num() {
        return watcher_num;
    }

    public void setWatcher_num(Integer watcher_num) {
        this.watcher_num = watcher_num;
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
