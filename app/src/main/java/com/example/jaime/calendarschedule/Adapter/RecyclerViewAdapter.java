package com.example.jaime.calendarschedule.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaime.calendarschedule.DB.ScheduleDBHandler;
import com.example.jaime.calendarschedule.Data.Schedule;
import com.example.jaime.calendarschedule.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Schedule> schedules;

    public RecyclerViewAdapter(Context context, ArrayList<Schedule> schedules) {
        this.context = context;
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview, viewGroup, false));
    }

    // 삭제할 때 AlertDialog.Builder를 통해 다이얼로그를 띄우려고 했으나 알 수 없는 이유로 계속 실패하여
    // 버튼을 누르면 해당 스케줄을 바로 삭제하는 것으로 처리했습니다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.content.setText(schedules.get(i).getContent());

        final int idx = i;
        final int year = schedules.get(idx).getYear();
        final int month = schedules.get(idx).getMonth();
        final int day = schedules.get(idx).getDay();
        final String text = schedules.get(idx).getContent();

        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleDBHandler scheduleDBHandler = new ScheduleDBHandler(context);
                if(scheduleDBHandler.deleteSchedule(year, month, day, text)){
                    schedules.remove(idx);
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "스케줄 삭제 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    // 스케줄 추가
    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
        notifyDataSetChanged();
    }

    // 뷰 홀더 패턴
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView content;
        ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.rvContent);
            deleteBtn = itemView.findViewById(R.id.rvDelete);
        }
    }
}
