package io.github.mdillon95.scoutingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    TextView team_tv_match;
    String teamNumStr;
    String compKey;
    String matchKey;
    Spinner match_type;
    Spinner rspeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        teamNumStr = this.getIntent().getStringExtra("teamNum");
        compKey = this.getIntent().getStringExtra("compKey");
        matchKey = this.getIntent().getStringExtra("matchKey");
        team_tv_match = (TextView) findViewById(R.id.team_tv_match);
        rspeed = (Spinner) findViewById(R.id.rspeed);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                team_tv_match.setText(dataSnapshot.child("teams").child(teamNumStr).child("name").getValue().toString());
                if (matchKey == null) return;
                DataSnapshot child = dataSnapshot.child("teams").child(teamNumStr).child("matches").child(compKey).child(matchKey);
                if (child.getValue() == null || child.getValue() == "") return;
                ((EditText) findViewById(R.id.scouter_name)).setText(child.child("scouter_name").getValue().toString());
                Spinner event_spinner = ((Spinner) findViewById(R.id.event_spinner));
                if (compKey.equals("2017njbri")) event_spinner.setSelection(0);
                else if (compKey.equals("2017njski")) event_spinner.setSelection(1);
                if (matchKey.contains("q_")) ((RadioButton) findViewById(R.id.quals)).setChecked(true);
                else if (matchKey.contains("e_")) ((RadioButton) findViewById(R.id.elim_button)).setChecked(true);
                if (child.child("alliance").getValue().toString().equals("blue")) ((RadioButton) findViewById(R.id.blue)).setChecked(true);
                else if (child.child("alliance").getValue().toString().equals("red")) ((RadioButton) findViewById(R.id.red)).setChecked(true);
                String regex = "[^\\d]";
                String matchNum = matchKey.replaceAll(regex, "");
                ((EditText) findViewById(R.id.match_number)).setText(matchNum);
                ((EditText) findViewById(R.id.ds_number)).setText(child.child("ds_number").getValue().toString());
                if (child.child("robot_speed").getValue().toString().contains("Slow"))((Spinner) findViewById(R.id.rspeed)).setSelection(1);
                else if (child.child("robot_speed").getValue().toString().contains("Medium"))((Spinner) findViewById(R.id.rspeed)).setSelection(2);
                else if (child.child("robot_speed").getValue().toString().contains("Fast"))((Spinner) findViewById(R.id.rspeed)).setSelection(3);
                if (child.child("fuel_intake").child("hopper").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.hopper_cb)).setChecked(true);
                // Auto
                if (child.child("fuel_intake").child("floor").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.floor_cb)).setChecked(true);
                if (child.child("auto").child("moved").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.moved_cb)).setChecked(true);
                if (child.child("auto").child("baseline").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.bl_cb)).setChecked(true);
                ((EditText) findViewById(R.id.auto_gears)).setText(child.child("auto").child("gears").getValue().toString());
                if (child.child("auto").child("rr_hopper").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.rr_hopper)).setChecked(true);
                if (child.child("auto").child("rk_hopper").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.rk_hopper)).setChecked(true);
                if (child.child("auto").child("center_hopper").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.center_hopper)).setChecked(true);
                if (child.child("auto").child("br_hopper").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.br_hopper)).setChecked(true);
                if (child.child("auto").child("bk_hopper").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.bk_hopper)).setChecked(true);
                ((EditText) findViewById(R.id.lg_auto)).setText(child.child("auto").child("lg_fuel").getValue().toString());
                ((EditText) findViewById(R.id.hg_auto)).setText(child.child("auto").child("hg_fuel").getValue().toString());
                if (child.child("shoot_speed").getValue().toString().equals("slow")) ((RadioButton) findViewById(R.id.auto_slow)).setChecked(true);
                else if (child.child("shoot_speed").getValue().toString().equals("medium")) ((RadioButton) findViewById(R.id.auto_med)).setChecked(true);
                else if (child.child("shoot_speed").getValue().toString().equals("fast")) ((RadioButton) findViewById(R.id.auto_fast)).setChecked(true);
                switch (child.child("auto").child("acc").getValue().toString()) {
                    case "0":
                        ((RadioButton) findViewById(R.id.auto_acc_0)).setChecked(true);
                    case "1":
                        ((RadioButton) findViewById(R.id.auto_acc_1)).setChecked(true);
                    case "2":
                        ((RadioButton) findViewById(R.id.auto_acc_2)).setChecked(true);
                }
                // Telop
                ((EditText) findViewById(R.id.teleop_lg_fuel)).setText(child.child("teleop").child("lg_fuel").getValue().toString());
                ((EditText) findViewById(R.id.teleop_lg_cycles)).setText(child.child("teleop").child("lg_cycles").getValue().toString());
                ((EditText) findViewById(R.id.teleop_hg_fuel)).setText(child.child("teleop").child("hg_fuel").getValue().toString());
                switch (child.child("teleop").child("acc").getValue().toString()) {
                    case "0":
                        ((RadioButton) findViewById(R.id.acc_0)).setChecked(true);
                    case "1":
                        ((RadioButton) findViewById(R.id.acc_1)).setChecked(true);
                    case "2":
                        ((RadioButton) findViewById(R.id.acc_2)).setChecked(true);
                }
                ((EditText) findViewById(R.id.teleop_gears)).setText(child.child("teleop").child("gears").getValue().toString());
                if (child.child("teleop").child("hang").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.teleop_hang)).setChecked(true);
                // Fouls
                ((EditText) findViewById(R.id.fouls)).setText(child.child("fouls").getValue().toString());
                ((EditText) findViewById(R.id.tech_fouls)).setText(child.child("fouls").getValue().toString());
                if (child.child("disabled").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.disabled)).setChecked(true);
                if (child.child("re_enabled").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.re_enabled)).setChecked(true);
                if (child.child("yellow_card").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.yellow_card)).setChecked(true);
                if (child.child("red_card").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.red_card)).setChecked(true);
                if (child.child("won").getValue().toString().equals("true")) ((CheckBox) findViewById(R.id.win_cb)).setChecked(true);
                ((EditText) findViewById(R.id.final_score)).setText(child.child("final_score").getValue().toString());
                ((EditText) findViewById(R.id.ally_1)).setText(child.child("ally_1").getValue().toString());
                ((EditText) findViewById(R.id.ally_2)).setText(child.child("ally_2").getValue().toString());
                ((EditText) findViewById(R.id.comments)).setText(child.child("comments").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        RadioGroup match_type_rgroup = (RadioGroup) findViewById(R.id.match_type_rgroup);
        match_type = (Spinner) findViewById(R.id.match_type);
        match_type.setEnabled(false);
        match_type_rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.elim_button) {
                    match_type.setEnabled(true);
                } else {
                    match_type.setEnabled(false);
                }
            }
        });
    }

    public void saveMatchClick(View v) {
        Spinner event_spinner = (Spinner) findViewById(R.id.event_spinner);
        DatabaseReference teamRef = myRef.child("teams");
        RadioGroup match_type_rgroup = (RadioGroup) findViewById(R.id.match_type_rgroup);
        EditText matchnum = (EditText) findViewById(R.id.match_number);
        boolean save = true;
        if (matchnum.getText().toString() == "") {
            save = false;
        }
        if (event_spinner.getSelectedItem().equals(this.getString(R.string.e2017njbri))) {
            teamRef = teamRef.child(teamNumStr).child("matches").child("2017njbri");
            if (match_type_rgroup.getCheckedRadioButtonId() == R.id.quals) {
                teamRef = teamRef.child("q" + matchnum.getText().toString());
            } else if (match_type_rgroup.getCheckedRadioButtonId() == R.id.elim_button) {
                if (match_type.getSelectedItem().equals("Quarter-Finals"))
                    teamRef = teamRef.child("e_quarters" + matchnum.getText().toString());
                else if (match_type.getSelectedItem().equals("Semi-Finals"))
                    teamRef = teamRef.child("e_semis" + matchnum.getText().toString());
                else if (match_type.getSelectedItem().equals("Finals"))
                    teamRef = teamRef.child("e_finals" + matchnum.getText().toString());
            }
        } else if (event_spinner.getSelectedItem().equals(this.getString(R.string.e2017njski))) {
            teamRef = teamRef.child(teamNumStr).child("matches").child("2017njski");
            if (match_type_rgroup.getCheckedRadioButtonId() == R.id.quals) {
                teamRef = teamRef.child("q" + matchnum.getText().toString());
            } else if (match_type_rgroup.getCheckedRadioButtonId() == R.id.elim_button) {
                if (match_type.getSelectedItem().equals("Quarter-Finals"))
                    teamRef = teamRef.child("e_quarters" + matchnum.getText().toString());
                else if (match_type.getSelectedItem().equals("Semi-Finals"))
                    teamRef = teamRef.child("e_semis" + matchnum.getText().toString());
                else if (match_type.getSelectedItem().equals("Finals"))
                    teamRef = teamRef.child("e_finals" + matchnum.getText().toString());
            }
        } else {
            save = false;
        }
        if (((EditText) findViewById(R.id.scouter_name)).getText().toString() != "")
            teamRef.child("scouter_name").setValue(((EditText) findViewById(R.id.scouter_name)).getText().toString());
        else {
            save = false;
        }
        RadioGroup alliance_rgroup = (RadioGroup) findViewById(R.id.alliance_rgroup);
        if (alliance_rgroup.getCheckedRadioButtonId() == R.id.blue)
            teamRef.child("alliance").setValue("blue");
        else if (alliance_rgroup.getCheckedRadioButtonId() == R.id.red)
            teamRef.child("alliance").setValue("red");
        else {
            save = false;
        }
        teamRef.child("ds_number").setValue(((EditText) findViewById(R.id.ds_number)).getText().toString());
        if (!((Spinner) findViewById(R.id.rspeed)).getSelectedItem().toString().contains("*"))
            teamRef.child("robot_speed").setValue(((Spinner) findViewById(R.id.rspeed)).getSelectedItem().toString());
        else {
            save = false;
        }
        teamRef.child("fuel_intake").child("hopper").setValue(((CheckBox) findViewById(R.id.hopper_cb)).isChecked());
        teamRef.child("fuel_intake").child("floor").setValue(((CheckBox) findViewById(R.id.floor_cb)).isChecked());
        teamRef.child("auto").child("moved").setValue(((CheckBox) findViewById(R.id.moved_cb)).isChecked());
        teamRef.child("auto").child("baseline").setValue(((CheckBox) findViewById(R.id.bl_cb)).isChecked());
        if (((EditText) findViewById(R.id.auto_gears)).getText().toString() != "")
            teamRef.child("auto").child("gears").setValue(((EditText) findViewById(R.id.auto_gears)).getText().toString());
        else {
            save = false;;
        }
        teamRef.child("auto").child("rr_hopper").setValue(((CheckBox) findViewById(R.id.rr_hopper)).isChecked());
        teamRef.child("auto").child("rk_hopper").setValue(((CheckBox) findViewById(R.id.rk_hopper)).isChecked());
        teamRef.child("auto").child("center_hopper").setValue(((CheckBox) findViewById(R.id.center_hopper)).isChecked());
        teamRef.child("auto").child("br_hopper").setValue(((CheckBox) findViewById(R.id.br_hopper)).isChecked());
        teamRef.child("auto").child("bk_hopper").setValue(((CheckBox) findViewById(R.id.bk_hopper)).isChecked());
        if (((EditText) findViewById(R.id.lg_auto)).getText().toString() != "")
            teamRef.child("auto").child("lg_fuel").setValue(((EditText) findViewById(R.id.lg_auto)).getText().toString());
        else {
            save = false;
        }
        if (((EditText) findViewById(R.id.hg_auto)).getText().toString() != "")
            teamRef.child("auto").child("hg_fuel").setValue(((EditText) findViewById(R.id.hg_auto)).getText().toString());
        else {
            save = false;
        }
        RadioGroup auto_shooter_speed_rgroup = (RadioGroup) findViewById(R.id.auto_shooter_rgroup);
        if (auto_shooter_speed_rgroup.getCheckedRadioButtonId() == R.id.auto_slow)
            teamRef.child("shoot_speed").setValue("slow");
        else if (auto_shooter_speed_rgroup.getCheckedRadioButtonId() == R.id.auto_med)
            teamRef.child("shoot_speed").setValue("medium");
        else if (auto_shooter_speed_rgroup.getCheckedRadioButtonId() == R.id.auto_fast)
            teamRef.child("shoot_speed").setValue("fast");
        RadioGroup auto_acc_rgroup = (RadioGroup) findViewById(R.id.auto_acc_rgroup);
        if (auto_acc_rgroup.getCheckedRadioButtonId() == R.id.auto_acc_0)
            teamRef.child("auto").child("acc").setValue(0);
        else if (auto_acc_rgroup.getCheckedRadioButtonId() == R.id.auto_acc_1)
            teamRef.child("auto").child("acc").setValue(1);
        else if (auto_acc_rgroup.getCheckedRadioButtonId() == R.id.auto_acc_2)
            teamRef.child("auto").child("acc").setValue(2);
        if (((EditText) findViewById(R.id.teleop_lg_fuel)).getText().toString() != "")
            teamRef.child("teleop").child("lg_fuel").setValue(((EditText) findViewById(R.id.teleop_lg_fuel)).getText().toString());
        else {
            save = false;
        }
        if (((EditText) findViewById(R.id.teleop_lg_cycles)).getText().toString() != "")
            teamRef.child("teleop").child("lg_cycles").setValue(((EditText) findViewById(R.id.teleop_lg_cycles)).getText().toString());
        else {
            save = false;
        }
        if (((EditText) findViewById(R.id.teleop_hg_fuel)).getText().toString() != "")
            teamRef.child("teleop").child("hg_fuel").setValue(((EditText) findViewById(R.id.teleop_hg_fuel)).getText().toString());
        else {
            save = false;
        }
        RadioGroup teleop_acc_rgroup = (RadioGroup) findViewById(R.id.teleop_acc_rgroup);
        if (teleop_acc_rgroup.getCheckedRadioButtonId() == R.id.acc_0)
            teamRef.child("teleop").child("acc").setValue(0);
        else if (teleop_acc_rgroup.getCheckedRadioButtonId() == R.id.acc_1)
            teamRef.child("teleop").child("acc").setValue(1);
        else if (teleop_acc_rgroup.getCheckedRadioButtonId() == R.id.acc_2)
            teamRef.child("teleop").child("acc").setValue(2);
        teamRef.child("teleop").child("gears").setValue(((EditText) findViewById(R.id.teleop_gears)).getText().toString());
        teamRef.child("teleop").child("hang").setValue(((CheckBox) findViewById(R.id.teleop_hang)).isChecked());
        if (((EditText) findViewById(R.id.fouls)).getText().toString() != "")
            teamRef.child("fouls").setValue(((EditText) findViewById(R.id.fouls)).getText().toString());
        else {
            save = false;
        }
        if (((EditText) findViewById(R.id.tech_fouls)).getText().toString() != "")
            teamRef.child("tech_fouls").setValue(((EditText) findViewById(R.id.tech_fouls)).getText().toString());
        else {
            save = false;
        }
        teamRef.child("disabled").setValue(((CheckBox) findViewById(R.id.disabled)).isChecked());
        teamRef.child("re_enabled").setValue(((CheckBox) findViewById(R.id.re_enabled)).isChecked());
        teamRef.child("yellow_card").setValue(((CheckBox) findViewById(R.id.yellow_card)).isChecked());
        teamRef.child("red_card").setValue(((CheckBox) findViewById(R.id.red_card)).isChecked());
        teamRef.child("won").setValue(((CheckBox) findViewById(R.id.win_cb)).isChecked());
        if (((EditText) findViewById(R.id.comments)).getText().toString() != "")
            teamRef.child("comments").setValue(((EditText) findViewById(R.id.comments)).getText().toString());
        else {
            teamRef.child("comments").setValue("");
        }
        if (((EditText) findViewById(R.id.final_score)).getText().toString() != "")
            teamRef.child("final_score").setValue(((EditText) findViewById(R.id.final_score)).getText().toString());
        else {
            save = false;
        }
        if (((EditText) findViewById(R.id.ally_1)).getText().toString() != "")
            teamRef.child("ally_1").setValue(((EditText) findViewById(R.id.ally_1)).getText().toString());
        else {
            save = false;
        }
        if (((EditText) findViewById(R.id.ally_2)).getText().toString() != "")
            teamRef.child("ally_2").setValue(((EditText) findViewById(R.id.ally_2)).getText().toString());
        else {
            save = false;
        }

        if (!save) {
            Toast.makeText(this.getApplicationContext(), "A text field was not filled out!", Toast.LENGTH_SHORT).show();
            teamRef.removeValue();
            return;
        } else {
            teamRef.push();
            finish();
        }
    }
}
