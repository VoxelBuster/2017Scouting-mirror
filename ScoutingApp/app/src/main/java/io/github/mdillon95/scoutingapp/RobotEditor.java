package io.github.mdillon95.scoutingapp;

import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RobotEditor extends AppCompatActivity {

    private static final String TAG = "RobotEditor";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    TextView robo_edit_name;
    EditText hopper_cap_edit, shooter_speed_edit, robot_comments;
    Spinner dt_spinner, shooter_spinner, pickup_spinner, gear_pickup_spinner, can_climb_spinner, robo_speed_spinner, auto_moves_spinner,
    auto_bl_spinner, auto_hopper_spinner, auto_gear_spinner, auto_shoot_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_editor);
        robo_edit_name = (TextView) findViewById(R.id.robo_edit_name);
        robot_comments = (EditText) findViewById(R.id.robot_comments);
        hopper_cap_edit = (EditText) findViewById(R.id.hopper_cap_edit);
        shooter_speed_edit = (EditText) findViewById(R.id.shooter_speed_edit);
        dt_spinner = (Spinner) findViewById(R.id.dt_spinner);
        shooter_spinner = (Spinner) findViewById(R.id.shooter_spinner);
        pickup_spinner = (Spinner) findViewById(R.id.pickup_spinner);
        gear_pickup_spinner = (Spinner) findViewById(R.id.gear_pickup_spinner);
        can_climb_spinner = (Spinner) findViewById(R.id.can_climb_spinner);
        robo_speed_spinner = (Spinner) findViewById(R.id.robo_speed_spinner);
        auto_moves_spinner = (Spinner) findViewById(R.id.auto_moves_spinner);
        auto_bl_spinner = (Spinner) findViewById(R.id.auto_bl_spinner);
        auto_hopper_spinner = (Spinner) findViewById(R.id.auto_hopper_spinner);
        auto_gear_spinner = (Spinner) findViewById(R.id.auto_gear_spinner);
        auto_shoot_spinner = (Spinner) findViewById(R.id.auto_shoot_spinner);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                String teamnumstr = RobotEditor.this.getIntent().getStringExtra("teamNum");
                DataSnapshot child = ds.child("teams").child(teamnumstr);
                String s = new String("Team " + teamnumstr + " - " + child.child("name").getValue());
                robo_edit_name.setText(s);
                child = ds.child("teams").child(teamnumstr).child("robot");
                if (child.child("hopper_cap").getValue() != null) hopper_cap_edit.setText(child.child("hopper_cap").getValue().toString());
                if (child.child("shooter_speed").getValue() != null) shooter_speed_edit.setText(child.child("shooter_speed").getValue().toString());
                if (child.child("comments").getValue() != null) robot_comments.setText(child.child("comments").getValue().toString());
                for (int i=0;i<dt_spinner.getAdapter().getCount();i++) {
                    if (child.child("dt").getValue() != null && child.child("dt").getValue().toString().equals(dt_spinner.getAdapter().getItem(i).toString())) {
                        dt_spinner.setSelection(i);
                    }
                }
                for (int i=0;i<shooter_spinner.getAdapter().getCount();i++) {
                    if (child.child("shooter").getValue() != null && child.child("shooter").getValue().toString().equals(shooter_spinner.getAdapter().getItem(i).toString())) {
                        shooter_spinner.setSelection(i);
                    }
                }
                for (int i=0;i<pickup_spinner.getAdapter().getCount();i++) {
                    if (child.child("pickup").getValue() != null && child.child("pickup").getValue().toString().equals(pickup_spinner.getAdapter().getItem(i).toString())) {
                        pickup_spinner.setSelection(i);
                    }
                }
                for (int i=0;i<gear_pickup_spinner.getAdapter().getCount();i++) {
                    if (child.child("gear_pickup").getValue() != null && child.child("gear_pickup").getValue().toString().equals(gear_pickup_spinner.getAdapter().getItem(i).toString())) {
                        gear_pickup_spinner.setSelection(i);
                    }
                }
                for (int i=0;i<can_climb_spinner.getAdapter().getCount();i++) {
                    if (child.child("can_climb").getValue() != null && child.child("can_climb").getValue().toString().equals(can_climb_spinner.getAdapter().getItem(i).toString())) {
                        can_climb_spinner.setSelection(i);
                    }
                }
                for (int i=0;i<robo_speed_spinner.getAdapter().getCount();i++) {
                    if (child.child("robot_speed").getValue() != null && child.child("robot_speed").getValue().toString().equals(robo_speed_spinner.getAdapter().getItem(i).toString())) {
                        robo_speed_spinner.setSelection(i);
                    }
                }
                for (int i=0;i<auto_moves_spinner.getAdapter().getCount();i++) {
                    if (child.child("auto_moves").getValue() != null && child.child("auto_moves").getValue().toString().equals(auto_moves_spinner.getAdapter().getItem(i).toString())) {
                        auto_moves_spinner.setSelection(i);
                    }
                }
                for (int i=0;i<auto_shoot_spinner.getAdapter().getCount();i++) {
                    if (child.child("auto_shoot").getValue() != null && child.child("auto_shoot").getValue().toString().equals(auto_shoot_spinner.getAdapter().getItem(i).toString())) {
                        auto_shoot_spinner.setSelection(i);
                    }
                }
                for (int i=0;i<auto_gear_spinner.getAdapter().getCount();i++) {
                    if (child.child("auto_gear").getValue() != null && child.child("auto_gear").getValue().toString().equals(auto_gear_spinner.getAdapter().getItem(i).toString())) {
                        auto_gear_spinner.setSelection(i);
                    }
                }
                for (int i=0;i<auto_hopper_spinner.getAdapter().getCount();i++) {
                    if (child.child("auto_hopper").getValue() != null && child.child("auto_hopper").getValue().toString().equals(auto_hopper_spinner.getAdapter().getItem(i).toString())) {
                        auto_hopper_spinner.setSelection(i);
                    }
                }
                for (int i=0;i<auto_bl_spinner.getAdapter().getCount();i++) {
                    if (child.child("auto_bl").getValue() != null && child.child("auto_bl").getValue().toString().equals(auto_bl_spinner.getAdapter().getItem(i).toString())) {
                        auto_bl_spinner.setSelection(i);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void submitClick(View v) {
        boolean save = true;
        if(hopper_cap_edit.getText().toString() == "") {
            save = false;
        }
        if(shooter_speed_edit.getText().toString() == "") {
            save = false;
        }
        if(dt_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if(shooter_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if(pickup_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if(gear_pickup_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if(can_climb_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if(robo_speed_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if(auto_moves_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if(auto_gear_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if(auto_shoot_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if(auto_hopper_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if(auto_bl_spinner.getSelectedItem().toString().contains("*")) {
            save = false;
        }
        if (save) {
            String teamnumstr = this.getIntent().getStringExtra("teamNum");
            DatabaseReference ref = myRef.child("teams").child(teamnumstr).child("robot");
            ref.child("hopper_cap").setValue(hopper_cap_edit.getText().toString());
            ref.child("shooter_speed").setValue(shooter_speed_edit.getText().toString());
            ref.child("dt").setValue(dt_spinner.getSelectedItem().toString());
            ref.child("shooter").setValue(shooter_spinner.getSelectedItem().toString());
            ref.child("pickup").setValue(pickup_spinner.getSelectedItem().toString());
            ref.child("gear_pickup").setValue(gear_pickup_spinner.getSelectedItem().toString());
            ref.child("can_climb").setValue(can_climb_spinner.getSelectedItem().toString());
            ref.child("robot_speed").setValue(robo_speed_spinner.getSelectedItem().toString());
            ref.child("auto_moves").setValue(auto_moves_spinner.getSelectedItem().toString());
            ref.child("auto_shoot").setValue(auto_shoot_spinner.getSelectedItem().toString());
            ref.child("auto_hopper").setValue(auto_hopper_spinner.getSelectedItem().toString());
            ref.child("auto_gear").setValue(auto_gear_spinner.getSelectedItem().toString());
            ref.child("auto_bl").setValue(auto_bl_spinner.getSelectedItem().toString());
            ref.child("comments").setValue(robot_comments.getText().toString());
            ref.push();
            Toast.makeText(this.getApplicationContext(), "Robot saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this.getApplicationContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
        }
    }
}
