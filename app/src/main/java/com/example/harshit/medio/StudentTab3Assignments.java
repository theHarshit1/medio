package com.example.harshit.medio;

import android.os.Bundle;
import android.print.PrintAttributes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentTab3Assignments extends Fragment{

    String name;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.student_tab3assignments, container, false);

        final LinearLayout feed=view.findViewById(R.id.studentAssignmentFeed);
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("Assignments").child(StudentLoginActivity.sec);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot teacher:dataSnapshot.getChildren()){

                    final long count=teacher.getChildrenCount();
                    DatabaseReference mref=FirebaseDatabase.getInstance().getReference("users").child("teachers").child(teacher.getKey());
                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            name=dataSnapshot.child("name").getValue().toString();
                            for(int i=0;i<count;i++){

                                RelativeLayout singleassign=(RelativeLayout) View.inflate(getActivity(),R.layout.student_single_assignment,null);

                                ((TextView)singleassign.findViewById(R.id.teacherName)).setText(name);

                                String date=teacher.child(String.valueOf(i+1)).child("date added").getValue().toString();
                                ((TextView)singleassign.findViewById(R.id.dateAdded)).setText(date);

                                ((TextView)singleassign.findViewById(R.id.title)).setText("Assignment"+String.valueOf(i+1));

                                String assign=teacher.child(String.valueOf(i+1)).child("assignment").getValue().toString();
                                ((TextView)singleassign.findViewById(R.id.assignmentText)).setText(assign);

                                String ldate=teacher.child(String.valueOf(i+1)).child("last date").getValue().toString();
                                ((TextView)singleassign.findViewById(R.id.lastDateText)).setText("Last date for submission:"+ldate);

                                feed.addView(singleassign);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
}