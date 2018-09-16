package com.example.harshit.medio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Tab2TimeTable extends Fragment implements View.OnClickListener{

    private TextView date,day;
    private Button cancelButton, okButton, editTableButton;
    private EditText classSection;
    private int flag;
    private View view,popupView;
    private DatabaseReference ttRef;
    public static String classFlag,hourflag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab2time_table, container, false);
        date = (TextView) view.findViewById(R.id.date);
        day = (TextView) view.findViewById(R.id.day);
        editTableButton = (Button) view.findViewById(R.id.editTableButton);

        popupView = LayoutInflater.from(getActivity()).inflate(R.layout.edit_timetable_popup, null);
        cancelButton = (Button) popupView.findViewById(R.id.cancelButton);
        okButton = (Button) popupView.findViewById(R.id.okButton);
        classSection = (EditText) popupView.findViewById(R.id.classSection);
        flag = 0;

        ttRef=FirebaseDatabase.getInstance().getReference("time table").child("teachers");
        ttRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(MainActivity.email).exists()){
                    for(DataSnapshot children:dataSnapshot.child(MainActivity.email).getChildren()){
                        TextView t=(TextView) view.findViewById(Integer.valueOf(children.getKey()));
                        t.setText(children.getValue().toString());
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
        final String daystr = sdf.format(d);
        day.setText(daystr);

        //onclicklistner to all hours
        TableLayout yourRootLayout = (TableLayout) view.findViewById(R.id.tableLayout);
        int count = yourRootLayout.getChildCount();
        for (int r = 2; r < count; r++) {
            View v1 = yourRootLayout.getChildAt(r);
            if (v1 instanceof TableRow) {
                TableRow row = (TableRow) v1;
                row.setId(100+r-2);
                int rowCount = row.getChildCount();
                for (int c = 1; c < rowCount; c++) {
                    View v2 = row.getChildAt(c);
                    TextView t = (TextView) v2;
                    t.setClickable(true);
                    int cellpos=(7*(r-2))+c-1;
                    t.setId(cellpos);
                    t.setOnClickListener(this);
                }
            }
        }

        //highlighting current day
        String today=day.getText().toString();
        int daycount=-1;
        switch (today){
            case"Monday":
                daycount=0;
                break;
            case"Tuesday":
                daycount=7;
                break;
            case"Wednesday":
                daycount=14;
                break;
            case"Thursday":
                daycount=21;
                break;
            case"Friday":
                daycount=28;
                break;
        }
        if(daycount!=-1) {
            TableRow r=(TableRow) ((TextView) view.findViewById(daycount)).getParent();
            TextView x=(TextView)r.getChildAt(0);
            x.setBackgroundResource(R.drawable.current_day1);
            for (int i = daycount; i < daycount + 7; i++) {
                TextView hour = (TextView) view.findViewById(i);
                hour.setBackgroundResource(R.drawable.current_day);
            }
        }

        editTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0) {
                    //when edit clicked
                    editTableButton.setText("DONE");
                    editTableButton.setBackgroundResource(R.drawable.cell_shape_head1);
                    flag = 1;
                }
                else {
                    //when done clicked
                    editTableButton.setText("EDIT");
                    editTableButton.setBackgroundResource(R.drawable.roundbutton3);
                    flag=0;
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        final TextView temp = (TextView) v;
        if(flag==1) {
            final PopupWindow popupWindow = new PopupWindow(popupView);
            popupWindow.setHeight(1000);
            popupWindow.setWidth(970);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            popupWindow.showAsDropDown(popupView);
            popupWindow.setFocusable(true);
            popupWindow.update();
            final String cellId=String.valueOf(v.getId());

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String classSec = classSection.getText().toString();
                    temp.setText(classSec);
                    popupWindow.dismiss();
                    ttRef.child(MainActivity.email).child(cellId).setValue(classSec);

                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popupWindow.dismiss();

                }
            });
        }
        else{
            classFlag=temp.getText().toString();
            if(classFlag.equals("")){
                Toast.makeText(getContext(),"No Class Assigned",Toast.LENGTH_SHORT).show();
            }
            else if(classFlag.equals("CSE4A")||classFlag.equals("CSE4B")||classFlag.equals("CSE4C")) {

                int parentid = ((TableRow) v.getParent()).getId();
                String currentday = day.getText().toString();
                if (currentday.equals("Monday") && parentid == 100 || currentday.equals("Tuesday") && parentid == 101 || currentday.equals("Wednesday") && parentid == 102 || currentday.equals("Thursday") && parentid == 103 || currentday.equals("Friday") && parentid == 104) {

                    int id = v.getId();
                    switch (id) {
                        case 0:
                        case 7:
                        case 14:
                        case 21:
                        case 28:
                            hourflag = "Hour 1";
                            break;
                        case 1:
                        case 8:
                        case 15:
                        case 22:
                        case 29:
                            hourflag = "Hour 2";
                            break;
                        case 2:
                        case 9:
                        case 16:
                        case 23:
                        case 30:
                            hourflag = "Hour 3";
                            break;
                        case 3:
                        case 10:
                        case 17:
                        case 24:
                        case 31:
                            hourflag = "Hour 4";
                            break;
                        case 4:
                        case 11:
                        case 18:
                        case 25:
                        case 32:
                            hourflag = "Hour 5";
                            break;
                        case 5:
                        case 12:
                        case 19:
                        case 26:
                        case 33:
                            hourflag = "Hour 6";
                            break;
                        case 6:
                        case 13:
                        case 20:
                        case 27:
                        case 34:
                            hourflag = "Hour 7";
                            break;
                    }
                    startActivity(new Intent(view.getContext(), AttendanceSheet.class));
                }
                else{
                    Toast.makeText(getActivity(),"Wrong day",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getContext(),"Check Class Name",Toast.LENGTH_SHORT).show();
            }
        }
    }
}