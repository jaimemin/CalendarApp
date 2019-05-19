package com.example.jaime.calendarschedule.Data;

import java.io.Serializable;

public class Schedule implements Serializable {
    // 날짜
    private int year;
    private int month;
    private int day;
    // 스케줄 내용
    private String content;

    public Schedule(int year, int month, int day, String content) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.content = content;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getContent() {
        return content;
    }
}
