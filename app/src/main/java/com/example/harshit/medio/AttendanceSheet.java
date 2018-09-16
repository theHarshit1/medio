package com.example.harshit.medio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AttendanceSheet extends AppCompatActivity {

    private String classflag,hourFlag;
    public int r,classStrength;
    private TableLayout yourRootLayout;
    private Button Commit;
    private Toolbar head;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.attendance_sheet);
        classflag = Tab2TimeTable.classFlag;
        Commit = (Button) findViewById(R.id.commitAttendance);
        yourRootLayout = (TableLayout) findViewById(R.id.attendanceTable);
        head=(Toolbar) findViewById(R.id.classhead);
        hourFlag=Tab2TimeTable.hourflag;
        head.setTitle(classflag+" - "+hourFlag);

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("users").child(classflag);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int r = 0;
                classStrength=(int)dataSnapshot.getChildrenCount();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    View v1 = yourRootLayout.getChildAt(r);
                    TableRow row = (TableRow) v1;

                    View v3 = row.getChildAt(0);
                    TextView roll = (TextView) v3;
                    roll.setText(children.getKey());
                    View v2 = row.getChildAt(1);
                    TextView name = (TextView) v2;
                    name.setText(children.child("name").getValue().toString());
                    r++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //date
        Date cal = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
        final String Datestr = df.format(cal);

        Commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AttendanceSheet.this,"Updating Attendance..",Toast.LENGTH_SHORT).show();
                DatabaseReference attRef = FirebaseDatabase.getInstance().getReference("Attendance").child(classflag).child(Datestr).child(hourFlag);
                for (int r = 0; r < classStrength; r++) {
                    View v1 = yourRootLayout.getChildAt(r);
                    TableRow row = (TableRow) v1;
                    View v2=row.getChildAt(0);
                    TextView roll=(TextView) v2;
                    View v3=row.getChildAt(2);
                    CheckBox checkBox=(CheckBox) v3;
                    if (!checkBox.isChecked()) {
                        attRef.child(roll.getText().toString()).setValue("A");
                    } else {
                        attRef.child(roll.getText().toString()).setValue("P");
                    }
                }
                Toast.makeText(AttendanceSheet.this,"Attendance Updated",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
