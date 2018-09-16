package com.example.harshit.medio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StudentTab2TimeTable extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.student_tab2time_table, container, false);

        TextView date = (TextView) view.findViewById(R.id.date);
        TextView day = (TextView) view.findViewById(R.id.day);
        DatabaseReference ttref= FirebaseDatabase.getInstance().getReference("time table").child(StudentLoginActivity.sec);
        ttref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TableLayout root=(TableLayout) view.findViewById(R.id.studentTimeTable);
                int count = root.getChildCount();
                for (int r = 2; r < count; r++) {
                    View v1 = root.getChildAt(r);
                    if (v1 instanceof TableRow) {
                        TableRow row = (TableRow) v1;
                        int rowCount = row.getChildCount();
                        for (int c = 1; c < rowCount; c++) {
                            View v2 = row.getChildAt(c);
                            TextView t = (TextView) v2;
                            int cellpos=(7*(r-2))+c-1;
                            if(dataSnapshot.child(String.valueOf(cellpos)).exists()) {
                                String val = dataSnapshot.child(String.valueOf(cellpos)).getValue().toString();
                                t.setText(val);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //date
        Date cal = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM");
        String Datestr = df.format(cal);
        date.setText(Datestr);

        //day
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String daystr = sdf.format(d);
        day.setText(daystr);


        return view;
    }
}
