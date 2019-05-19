package com.example.jaime.calendarschedule.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.jaime.calendarschedule.Fragment.DailyFragment;
import com.example.jaime.calendarschedule.Fragment.MonthlyFragment;
import com.example.jaime.calendarschedule.Fragment.WeeklyFragment;
import com.example.jaime.calendarschedule.Adapter.PagerAdapter;
import com.example.jaime.calendarschedule.R;

import java.util.Calendar;

import static com.example.jaime.calendarschedule.Fragment.DailyFragment.SCHEDULE_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    public Calendar calendar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();

        viewPager = findViewById(R.id.viewPager);
        adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tab);
        // 뷰 페이저와 탭 레이아웃 연동
        tabLayout.setupWithViewPager(viewPager);
    }

    // ScheduleActivity가 업데이트 되면
    // 모든 프래그먼트도 업데이트 시켜줘야합니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SCHEDULE_REQUEST_CODE && resultCode == RESULT_OK){
            getMonthlyFragment().updateCalendar();
            getWeeklyFragment().updateCalendar();
            getDailyFragment().updateCalendar();
        }
    }

    // 핵심인 금일 달력 프래그먼트로 전환
    public void changeToDailyTab(){
        tabLayout.clearFocus();
        viewPager.setCurrentItem(2);
    }

    // 월간 달력 프래그먼트 반환
    public MonthlyFragment getMonthlyFragment(){
        Fragment fragment = adapter.getItem(0);
        if(fragment instanceof MonthlyFragment)
            return (MonthlyFragment)fragment;
        return null;
    }

    // 주간 달력 프래그먼트 반환
    public WeeklyFragment getWeeklyFragment(){
        Fragment fragment = adapter.getItem(1);
        if(fragment instanceof WeeklyFragment)
            return (WeeklyFragment) fragment;
        return null;
    }

    // 금일 달력 프래그먼트 반환
    public DailyFragment getDailyFragment(){
        Fragment fragment = adapter.getItem(2);
        if(fragment instanceof DailyFragment)
            return (DailyFragment)fragment;
        return null;
    }
}
