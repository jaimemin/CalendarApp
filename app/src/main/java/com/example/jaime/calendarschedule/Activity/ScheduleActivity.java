package com.example.jaime.calendarschedule.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaime.calendarschedule.Adapter.RecyclerViewAdapter;
import com.example.jaime.calendarschedule.DB.ScheduleDBHandler;
import com.example.jaime.calendarschedule.Data.Schedule;
import com.example.jaime.calendarschedule.R;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    private TextView dateTV;
    private EditText scheduleETV;
    private Button scheduleBtn;
    private RecyclerView scheduleRV;
    private RecyclerViewAdapter adapter;
    private ArrayList<Schedule> schedules;

    private int curYear;
    private int curMonth;
    private int curDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        dateTV = findViewById(R.id.curDate);
        scheduleETV = findViewById(R.id.scheduleEditTV);
        scheduleBtn = findViewById(R.id.scheduleBtn);
        // EditText에서 엔터가 눌리면 해당 스케줄을 추가하고 키보드를 숨긴다.
        scheduleETV.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    scheduleBtn.callOnClick();
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        // 금일 달력 프래그먼트로부터 받은 날짜와 해당 날짜의 스케줄을 받음
        Intent intent = getIntent();
        curYear = intent.getIntExtra("year", 2020);
        curMonth = intent.getIntExtra("month", 1);
        curDay = intent.getIntExtra("day", 1);
        schedules = (ArrayList<Schedule>) intent.getSerializableExtra("schedule");

        dateTV.setText(curYear + "년 " + curMonth + "월 " + curDay +"일 스케줄");
        // 스케줄을 추가하는 과정, 마찬가지로 키보드를 숨긴다
        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = scheduleETV.getText().toString();
                ScheduleDBHandler scheduleDBHandler = new ScheduleDBHandler(getApplicationContext());
                if(scheduleDBHandler.addSchedule(curYear, curMonth, curDay, text)){
                    Schedule schedule = new Schedule(curYear, curMonth, curDay, text);
                    adapter.addSchedule(schedule);
                }else
                    Toast.makeText(ScheduleActivity.this, "동일한 내용은 추가하실 수 없습니다", Toast.LENGTH_SHORT).show();
                scheduleETV.setText("");
                hideKeyboard();
            }
        });


        // RecyclerView
        scheduleRV = findViewById(R.id.scheduleRV);
        adapter = new RecyclerViewAdapter(getApplicationContext(), schedules);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        scheduleRV.setLayoutManager(layoutManager);
        scheduleRV.setAdapter(adapter);
    }

    // 키보드 숨기는 함수
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(scheduleETV.getWindowToken(), 0);
    }

    // 뒤로가기를 누르면 금일 달력 프래그먼트로 전환
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();

        super.onBackPressed();
    }
}
