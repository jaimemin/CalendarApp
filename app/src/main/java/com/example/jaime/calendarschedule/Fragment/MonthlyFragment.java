package com.example.jaime.calendarschedule.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jaime.calendarschedule.Activity.MainActivity;
import com.example.jaime.calendarschedule.Activity.ScheduleActivity;
import com.example.jaime.calendarschedule.DB.ScheduleDBHandler;
import com.example.jaime.calendarschedule.Data.Schedule;
import com.example.jaime.calendarschedule.R;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyFragment extends Fragment {

    private final int MAX = 7;
    private static final String dayName[] = {"일", "월", "화", "수", "목", "금", "토"};

    private TextView monthTV;
    private TextView[] cellTV;
    private TextView[] numOfSchedule;
    private Schedule[] scheduleDates;
    private ArrayList<Schedule>[] schedules;

    private ImageButton prevBtn, nextBtn;
    private GridLayout gridLayout;

    private Calendar calendar;

    public MonthlyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);
        // 싱글턴 패턴
        if (getActivity() instanceof MainActivity)
            calendar = ((MainActivity) getActivity()).calendar;

        monthTV = view.findViewById(R.id.month);
        prevBtn = view.findViewById(R.id.prevMonthButton);
        nextBtn = view.findViewById(R.id.nextMonthButton);
        gridLayout = view.findViewById(R.id.monthlyCalendar);
        // 가끔 6주에 걸친 달이 있으므로 6 * MAX
        cellTV = new TextView[6 * MAX];
        numOfSchedule = new TextView[6 * MAX];
        scheduleDates = new Schedule[6 * MAX];
        schedules = new ArrayList[6 * MAX];

        gridLayout.setRowCount(MAX);
        gridLayout.setColumnCount(MAX);
        // 요일 표신
        for (int i = 0; i < MAX; i++) {
            View cell = inflater.inflate(R.layout.layout_dayname_cell, container, false);
            TextView dayNameTV = cell.findViewById(R.id.dayNameTV);

            dayNameTV.setText(dayName[i]);
            if (i == 0)
                dayNameTV.setTextColor(Color.RED);
            else if (i == 6)
                dayNameTV.setTextColor(Color.BLUE);
            else
                dayNameTV.setTextColor(Color.BLACK);
            gridLayout.addView(cell);
        }

        for (int i = 0; i < 6 * MAX; i++) {
            View cell = inflater.inflate(R.layout.layout_month_cell, container, false);
            cellTV[i] = cell.findViewById(R.id.dayInMonthTV);
            numOfSchedule[i] = cell.findViewById(R.id.scheduleInDayTV);

            final int idx = i;
            // 해당 날짜를 클릭하면 그 날짜의 dailyFragment로 전환
            cellTV[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeToDailyTab(scheduleDates[idx].getYear(), scheduleDates[idx].getMonth() - 1, scheduleDates[idx].getDay());
                }
            });
            numOfSchedule[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeToDailyTab(scheduleDates[idx].getYear(), scheduleDates[idx].getMonth() - 1, scheduleDates[idx].getDay());
                }
            });
            gridLayout.addView(cell);
        }

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                updateCalendar();

                Activity mainActivity = getActivity();
                if (mainActivity instanceof MainActivity) {
                    ((MainActivity) mainActivity).getWeeklyFragment().updateCalendar();
                    // 알 수 없는 에러 발생(앱은 이 라인을 주석처리해도 정상 작동)
//                    ((MainActivity) mainActivity).getDailyFragment().updateCalendar();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                updateCalendar();

                Activity mainActivity = getActivity();
                if (mainActivity instanceof MainActivity) {
                    ((MainActivity) mainActivity).getWeeklyFragment().updateCalendar();
//                    ((MainActivity) mainActivity).getDailyFragment().updateCalendar();
                }
            }
        });

        updateCalendar();
        return view;
    }

    // DailyFragment로 전환
    private void changeToDailyTab(int year, int month, int day) {
        calendar.set(year, month, day);

        Activity mainActivity = getActivity();
        if (mainActivity instanceof MainActivity) {
            ((MainActivity) mainActivity).changeToDailyTab();
        }
    }

    public void updateCalendar() {
        // 날짜 원상복구를 위해 선언
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);

        monthTV.setText(curYear + "년 " + (curMonth + 1) + "월");
        // 1일이 무슨 요일인지 파악
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, 1 - (calendar.get(Calendar.DAY_OF_WEEK)));

        ScheduleDBHandler scheduleDBHandler = new ScheduleDBHandler(getContext());
        for (int i = 0; i < 6 * MAX; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // 6주차에 걸친 날짜는 해당 날짜만 출력
            if (i >= 5 * MAX && day < 10) {
                cellTV[i].setText("");
                numOfSchedule[i].setText("");
                continue;
            }

            cellTV[i].setText(day + "");
            if (month - 1 != curMonth)
                cellTV[i].setTextColor(Color.LTGRAY);
            else if (i % 7 == 6)
                cellTV[i].setTextColor(Color.BLUE);
            else if (i % 7 == 0)
                cellTV[i].setTextColor(Color.RED);
            else
                cellTV[i].setTextColor(Color.BLACK);

            scheduleDates[i] = new Schedule(year, month, day, null);
            schedules[i] = scheduleDBHandler.searchSchedule(year, month, day);
            // 달간 달력 프래그먼트에는 스케줄의 개수만 표시
            if (schedules[i].size() == 0)
                numOfSchedule[i].setVisibility(View.INVISIBLE);
            else {
                numOfSchedule[i].setText("+" + schedules[i].size());
                numOfSchedule[i].setVisibility(View.VISIBLE);
                numOfSchedule[i].setTextColor(Color.GREEN);
            }

            // 다음 날
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 날짜 원상복구
        calendar.set(Calendar.YEAR, curYear);
        calendar.set(Calendar.MONTH, curMonth);
        calendar.set(Calendar.DAY_OF_MONTH, curDay);
    }
}
