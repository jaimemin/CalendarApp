package com.example.jaime.calendarschedule.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
public class DailyFragment extends Fragment {

    public static final int SCHEDULE_REQUEST_CODE = 1000;

    private TextView dayTV;
    private Schedule scheduleDates;
    private ArrayList<Schedule> schedules;

    private ImageButton prevBtn, nextBtn;
    private GridLayout gridLayout;
    private TextView titleOfSchedule;

    private FloatingActionButton floatingActionButton;
    private Calendar calendar;

    public DailyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        if (getActivity() instanceof MainActivity)
            calendar = ((MainActivity) getActivity()).calendar;
        dayTV = view.findViewById(R.id.dayTV);
        prevBtn = view.findViewById(R.id.prevDayButton);
        nextBtn = view.findViewById(R.id.nextDayButton);
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                intent.putExtra("year", scheduleDates.getYear());
                intent.putExtra("month", scheduleDates.getMonth());
                intent.putExtra("day", scheduleDates.getDay());
                intent.putExtra("schedule", schedules);
                getActivity().startActivityForResult(intent, SCHEDULE_REQUEST_CODE);
            }
        });

        gridLayout = view.findViewById(R.id.dailyCalendar);
        gridLayout.setRowCount(1);
        gridLayout.setColumnCount(1);

        View cell = inflater.inflate(R.layout.layout_day_cell, container, false);
        titleOfSchedule = cell.findViewById(R.id.dayScheduleTitleTV);
        gridLayout.addView(cell);

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                updateCalendar();

                Activity mainActivity = getActivity();
                if (mainActivity instanceof MainActivity) {
                    ((MainActivity) mainActivity).getMonthlyFragment().updateCalendar();
                    ((MainActivity) mainActivity).getWeeklyFragment().updateCalendar();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                updateCalendar();

                Activity mainActivity = getActivity();
                if (mainActivity instanceof MainActivity) {
                    ((MainActivity) mainActivity).getMonthlyFragment().updateCalendar();
                    ((MainActivity) mainActivity).getWeeklyFragment().updateCalendar();
                }
            }
        });

        updateCalendar();
        return view;
    }

    public void updateCalendar() {
        // 현재 날짜
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                dayTV.setText(curMonth + "월 " + curDay + "일 일요일");
                dayTV.setTextColor(Color.RED);
                break;
            case 2:
                dayTV.setText(curMonth + "월 " + curDay + "일 월요일");
                dayTV.setTextColor(Color.BLACK);
                break;
            case 3:
                dayTV.setText(curMonth + "월 " + curDay + "일 화요일");
                dayTV.setTextColor(Color.BLACK);
                break;
            case 4:
                dayTV.setText(curMonth + "월 " + curDay + "일 수요일");
                dayTV.setTextColor(Color.BLACK);
                break;
            case 5:
                dayTV.setText(curMonth + "월 " + curDay + "일 목요일");
                dayTV.setTextColor(Color.BLACK);
                break;
            case 6:
                dayTV.setText(curMonth + "월 " + curDay + "일 금요일");
                dayTV.setTextColor(Color.BLACK);
                break;
            case 7:
                dayTV.setText(curMonth + "월 " + curDay + "일 토요일");
                dayTV.setTextColor(Color.BLUE);
                break;
        }

        scheduleDates = new Schedule(curYear, curMonth, curDay, null);
        ScheduleDBHandler scheduleDBHandler = new ScheduleDBHandler(getContext());
        schedules = scheduleDBHandler.searchSchedule(curYear, curMonth, curDay);
        if (schedules.size() == 0)
            titleOfSchedule.setText("스케줄이 비어있습니다.");
        else {
            String temp ="";
            for(int i=0; i<schedules.size(); i++) {
                temp += schedules.get(i).getContent();
                temp += "\n";
            }
            titleOfSchedule.setText(temp);
            titleOfSchedule.setTextSize(20);
        }
    }
}
