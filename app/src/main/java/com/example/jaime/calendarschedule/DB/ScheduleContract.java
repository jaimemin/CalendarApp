package com.example.jaime.calendarschedule.DB;

import android.provider.BaseColumns;

public class ScheduleContract {

    // 인스턴스화 금지
    private ScheduleContract(){

    }

    // BaseColumn 인터페이스는 _ID 제공
    public static class ScheduleEntry implements BaseColumns{
        public static final String TABLE_NAME = "schedule";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_CONTENT = "content";
    }
}
