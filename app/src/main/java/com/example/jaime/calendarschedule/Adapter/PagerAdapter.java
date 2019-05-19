package com.example.jaime.calendarschedule.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jaime.calendarschedule.Fragment.DailyFragment;
import com.example.jaime.calendarschedule.Fragment.MonthlyFragment;
import com.example.jaime.calendarschedule.Fragment.WeeklyFragment;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mData;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        // 세 가지 프래그먼트 준비
        mData = new ArrayList<>();
        mData.add(new MonthlyFragment());
        mData.add(new WeeklyFragment());
        mData.add(new DailyFragment());
    }

    @Override
    public Fragment getItem(int i) {
        return mData.get(i);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    // 탭의 제목
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "월간";
            case 1:
                return "주간";
            case 2:
                return "금일";
        }
        return "";
    }
}
