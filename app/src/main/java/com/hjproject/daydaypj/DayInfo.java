package com.hjproject.daydaypj;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Required;

public class DayInfo extends RealmObject {

    @Override
    public String toString(){
        return "Day{" +
                "title='" + title + '\'' +
                "date='" + date + '\''+
                "dday='" + dday + '\''+
                "category='" + dcategory + '\''+
                '}';
    }

    @Required
    private String title;   //제목
    private String date;  //정한 날짜
    private String dday;   //남은 날
    private int dcategory;  //카테고리
 //n번째
    @Ignore
    private int sessionid;

    public DayInfo() {
        this.title = "아무값도 없습니다.";
    }

    public DayInfo(String title,String date,String dday,int dcategory) {
        this.title = title;
        this.date = date;
        this.dday = dday;
        this.dcategory = dcategory;
    }


    public String getTitle() {
        return title;
    }
    public String getDday() {
        return dday;
    }
    public String getDate() {
        return date;
    }
    public int getDcategory() {
        return dcategory;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDday(String dday) {
        this.dday = dday;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setDcategory(int dcategory) {
        this.dcategory = dcategory;
    }
}
