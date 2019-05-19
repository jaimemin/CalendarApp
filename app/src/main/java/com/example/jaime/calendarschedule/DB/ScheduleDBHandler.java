package com.example.jaime.calendarschedule.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.jaime.calendarschedule.DB.ScheduleContract;
import com.example.jaime.calendarschedule.Data.Schedule;

import java.util.ArrayList;

public class ScheduleDBHandler extends SQLiteOpenHelper {
    // DB의 버전으로 부터 시작하고 스키마가 변경될 때 숫자를 올린다
    private static final int DB_VERSION = 1;
    // DB 파일명
    private static final String DB_NAME = "Schedule.db";
    // 테이블 생성 SQL 문
    private static final String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s INTEGER, %s INTEGER, %s INTEGER, %s TEXT)",
                    ScheduleContract.ScheduleEntry.TABLE_NAME,
                    ScheduleContract.ScheduleEntry._ID,
                    ScheduleContract.ScheduleEntry.COLUMN_YEAR,
                    ScheduleContract.ScheduleEntry.COLUMN_MONTH,
                    ScheduleContract.ScheduleEntry.COLUMN_DAY,
                    ScheduleContract.ScheduleEntry.COLUMN_CONTENT);

    public ScheduleDBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // 해당 날짜에 동일한 내용이 있는지 확인
    public boolean searchForIdenticalContent(int year, int month, int day, String content){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + ScheduleContract.ScheduleEntry.TABLE_NAME +
                    " WHERE " + ScheduleContract.ScheduleEntry.COLUMN_YEAR + " = " + year + " AND " +
                    ScheduleContract.ScheduleEntry.COLUMN_MONTH + " = " + month + " AND " +
                    ScheduleContract.ScheduleEntry.COLUMN_DAY + " = " + day + " AND " +
                    ScheduleContract.ScheduleEntry.COLUMN_CONTENT + " = '" + content + "'", null);

            int cnt = 0;
            while(cursor.moveToNext())
                cnt++;

            db.close();
            // 중복된 내용이 있을 경우
            if(cnt > 0)
                return true;
            // 중복된 내용이 없을 경우
            else
                return false;
        }catch(Exception e){
            Log.d("SearchError", "동작");
            return true;
        }
    }

    // 스케줄 추가
    public boolean addSchedule(int year, int month, int day, String content){
        try{
            // 중복된 내용이 있거나 에러 발생 시
            if(searchForIdenticalContent(year, month, day, content))
                return false;

            ContentValues contentValues = new ContentValues();
            contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_YEAR, year);
            contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_MONTH, month);
            contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_DAY, day);
            contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_CONTENT, content);

            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(ScheduleContract.ScheduleEntry.TABLE_NAME, null, contentValues);
            db.close();
        }catch(Exception e){
            return false;
        }
        return true;
    }

    // 스케줄 삭제
    public boolean deleteSchedule(int year, int month, int day, String content){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(ScheduleContract.ScheduleEntry.TABLE_NAME,
                    ScheduleContract.ScheduleEntry.COLUMN_YEAR + " = " + year + " AND " +
                            ScheduleContract.ScheduleEntry.COLUMN_MONTH + " = " + month + " AND " +
                            ScheduleContract.ScheduleEntry.COLUMN_DAY + " = " + day + " AND " +
                            ScheduleContract.ScheduleEntry.COLUMN_CONTENT + " = '" + content + "'"
                    , null);
            db.close();
        }catch(Exception e){
            return false;
        }
        return true;
    }

    // 스케줄 탐색(모든 스케줄을 ArrayList에 저장해서 반환합니다)
    public ArrayList<Schedule> searchSchedule(int year, int month, int day){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + ScheduleContract.ScheduleEntry.TABLE_NAME +
                    " WHERE " + ScheduleContract.ScheduleEntry.COLUMN_YEAR + " = " + year + " AND " +
                    ScheduleContract.ScheduleEntry.COLUMN_MONTH + " = " + month + " AND " +
                    ScheduleContract.ScheduleEntry.COLUMN_DAY + " = " + day, null);

            ArrayList<Schedule> result = new ArrayList<>();
            while(cursor.moveToNext())
                result.add(new Schedule(cursor.getInt(1)
                        , cursor.getInt(2)
                        , cursor.getInt(3)
                        , cursor.getString(4)));

            db.close();
            return result;
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
