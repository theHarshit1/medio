package com.example.harshit.medio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StudentTab1Attendance extends Fragment{

    public String stuclass,stuemail;
    TableLayout attTable;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.student_tab1attendance, container, false);

        stuclass=StudentLoginActivity.sec;
        stuemail=StudentLoginActivity.email;
        attTable=view.findViewById(R.id.studentAttendance);
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("Attendance").child(stuclass);
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TableRow row=new TableRow(view.getContext());
                TextView date=new TextView(view.getContext());
                date.setText(dataSnapshot.getKey());
                date.setTextSize(20);
                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                date.setBackgroundResource(R.drawable.roundbutton4);
                date.setTextColor(Color.parseColor("#ffffff"));
                date.setPadding(2,12,2,12);
                row.setMinimumWidth(TableRow.LayoutParams.WRAP_CONTENT);
                row.setMinimumHeight(TableRow.LayoutParams.WRAP_CONTENT);
                row.addView(date, TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                for(int i=1;i<8;i++) {
                    String hour="Hour "+String.valueOf(i);
                    TextView textView = new TextView(view.getContext());
                    textView.setBackgroundResource(R.drawable.date_time_bg);
                    textView.setTextSize(20);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setPadding(8,12,8,12);
                    if(dataSnapshot.child(hour).child(stuemail).exists()) {
                        textView.setText(dataSnapshot.child(hour).child(stuemail).getValue().toString());
                    }
                        TableRow.LayoutParams params=new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
                        row.addView(textView,i,params);
                }
                attTable.addView(row, TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
