package com.example.harshit.medio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Tab3Assignments extends Fragment {

    View view;
    int flag,a[];
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab3assignments, container, false);

        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Assignments");
        final LinearLayout feed=(LinearLayout) view.findViewById(R.id.assignmentFeed);
        flag=0;
        a=new int[3];

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                feed.removeAllViews();
                for (DataSnapshot section : dataSnapshot.getChildren()) {
                    if (section.child(MainActivity.email).exists()) {
                        long childCount = section.child(MainActivity.email).getChildrenCount();

                        if (flag!=0){
                            flag--;
                            break;
                        }
                        switch (section.getKey()) {
                            case "CSE4A":
                                a[0] = (int) childCount;
                                break;
                            case "CSE4B":
                                a[1] = (int) childCount;
                                break;
                            case "CSE4C":
                                a[2] = (int) childCount;
                                break;
                        }

                        for (int i = 0; i <childCount; i++) {

                            RelativeLayout singleAssignment=(RelativeLayout) View.inflate(getActivity(),R.layout.single_assignment,null);
                            ((TextView)singleAssignment.findViewById(R.id.title)).setText("Assignment"+String.valueOf(i+1));

                            String department=section.getKey();
                            ((TextView)singleAssignment.findViewById(R.id.department)).setText(department);

                            String dateAdded=section.child(MainActivity.email).child(String.valueOf(i+1)).child("date added").getValue().toString();
                            ((TextView)singleAssignment.findViewById(R.id.dateAdded)).setText(dateAdded);

                            String assign=section.child(MainActivity.email).child(String.valueOf(i+1)).child("assignment").getValue().toString();
                            ((TextView)singleAssignment.findViewById(R.id.assignmentText)).setText(assign);

                            String last=section.child(MainActivity.email).child(String.valueOf(i+1)).child("last date").getValue().toString();
                            ((TextView)singleAssignment.findViewById(R.id.lastDateText)).setText("Last date of submission:"+last);

                            feed.addView(singleAssignment,i);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton addbutton=(FloatingActionButton) view.findViewById(R.id.addButton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View popupView= LayoutInflater.from(getActivity()).inflate(R.layout.assignment_popup,null);
                final PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setHeight(1000);
                popupWindow.setWidth(970);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                popupWindow.showAsDropDown(popupView);
                popupWindow.setFocusable(true);
                popupWindow.update();

                Button okButton=(Button) popupView.findViewById(R.id.okButton);
                Button cancelButton=(Button) popupView.findViewById(R.id.cancelButton);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText sec=(EditText) popupView.findViewById(R.id.classSection);
                        final EditText last=(EditText) popupView.findViewById(R.id.lastDate);
                        final EditText assignment=(EditText) popupView.findViewById(R.id.assignment);

                        String s1=sec.getText().toString();
                        final String l=last.getText().toString();
                        final String as=assignment.getText().toString();
                        int count=0;
                        switch (s1){

                            case "CSE4A":
                                count=a[0];
                                break;
                            case "CSE4B":
                                count=a[1];
                                break;
                            case "CSE4C":
                                count=a[2];
                                break;
                        }
                        popupWindow.dismiss();
                        flag=2;
                        final DatabaseReference asref= FirebaseDatabase.getInstance().getReference("Assignments").child(s1).child(MainActivity.email);

                        asref.child(String.valueOf(count+1)).child("assignment").setValue(as);
                        asref.child(String.valueOf(count+1)).child("last date").setValue(l);
                        String currentDate = DateFormat.getDateTimeInstance().format(new Date());
                        asref.child(String.valueOf(count+1)).child("date added").setValue(currentDate);
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popupWindow.dismiss();
                    }
                });
            }
        });

        return view;
    }
}