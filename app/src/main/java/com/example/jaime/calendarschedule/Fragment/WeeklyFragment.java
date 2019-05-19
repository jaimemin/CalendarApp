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
import android.widget.Toast;

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
public class WeeklyFragment extends Fragment {

    private final int MAX = 7;

    private TextView weekTV;
    private TextView[] cellTV;
    private TextView[] titleOfSchedule;
    private Schedule[] scheduleDates;
    private ArrayList<Schedule>[] schedules;

    private ImageButton prevBtn, nextBtn;
    private GridLayout gridLayout;

    private Calendar calendar;

    public WeeklyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);
        if (getActivity() instanceof MainActivity)
            calendar = ((MainActivity) getActivity()).calendar;

        weekTV = view.findViewById(R.id.week);
        prevBtn = view.findViewById(R.id.prevWeekButton);
        nextBtn = view.findViewById(R.id.nextWeekButton);
        gridLayout = view.findViewById(R.id.weeklyCalendar);

        cellTV = new TextView[MAX];
        titleOfSchedule = new TextView[MAX];
        scheduleDates = new Schedule[MAX];
        schedules = new ArrayList[MAX];

        gridLayout.setRowCount(MAX);
        gridLayout.setColumnCount(1);
        for (int i = 0; i < MAX; i++) {
            View cell = inflater.inflate(R.layout.layout_week_cell, container, false);
            cellTV[i] = cell.findViewById(R.id.dayInWeekTV);
            titleOfSchedule[i] = cell.findViewById(R.id.weekScheduleTitleTV);

            final int idx = i;
            titleOfSchedule[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar.set(scheduleDates[idx].getYear(), scheduleDates[idx].getMonth() - 1, scheduleDates[idx].getDay());

                    Activity mainActivity = getActivity();
                    if(mainActivity instanceof MainActivity){
                        ((MainActivity)mainActivity).getDailyFragment().updateCalendar();
                        ((MainActivity)mainActivity).changeToDailyTab();
                    }
                }
            });
            gridLayout.addView(cell);
        }

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                updateCalendar();

                Activity mainActivity = getActivity();
                if (mainActivity instanceof MainActivity) {
                    ((MainActivity) mainActivity).getMonthlyFragment().updateCalendar();
                    ((MainActivity) mainActivity).getDailyFragment().updateCalendar();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                updateCalendar();

                Activity mainActivity = getActivity();
                if (mainActivity instanceof MainActivity) {
                    ((MainActivity) mainActivity).getMonthlyFragment().updateCalendar();
                    ((MainActivity) mainActivity).getDailyFragment().updateCalendar();
                }
            }
        });

        updateCalendar();
        return view;
    }

    public void updateCalendar() {
        // 날짜 원상복구를 위해 미리 선언
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        int curWeek = calendar.get(Calendar.WEEK_OF_MONTH);

        weekTV.setText((curMonth + 1) + "월 " + curWeek + " 번째 주");
        // 일요일 파악
        int temp = 1 - (calendar.get(Calendar.DAY_OF_WEEK));
        calendar.add(Calendar.DAY_OF_MONTH, temp);

        ScheduleDBHandler scheduleDBHandler = new ScheduleDBHandler(getContext());
        for (int i = 0; i < MAX; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            cellTV[i].setText(month + "/" + day);
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
            if (schedules[i].size() == 0) {
                titleOfSchedule[i].setText("스케줄이 비어있습니다.");
            } else {
                String title = schedules[i].get(0).getContent();
                // 스케줄의 길이가 10을 넘을 경우
                if(title.length() > 10) {
                    title.substring(0, 10);
                    title += "...";
                }
                // 해당 날짜의 스케줄이 두개 이상일 경우에
                if(schedules[i].size() > 1)
                    title += (" 외 +" + (schedules[i].size() - 1));
                titleOfSchedule[i].setText(title);
            }
            // 다음 날
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        // 다시 날짜를 원상복구 시켜야합니다.
        calendar.set(Calendar.YEAR, curYear);
        calendar.set(Calendar.MONTH, curMonth);
        calendar.set(Calendar.DAY_OF_MONTH, curDay);
    }
}
