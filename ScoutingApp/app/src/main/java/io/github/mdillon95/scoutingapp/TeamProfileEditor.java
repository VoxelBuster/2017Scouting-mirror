package io.github.mdillon95.scoutingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeamProfileEditor extends AppCompatActivity {

    private static final String TAG = "TeamProfileActivity";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    TextView team_edit_num;
    TextView urlview_edit;
    EditText team_name_input_edit;
    EditText team_site_url_edit;
    EditText rookie_year_input_edit;
    EditText hometown_input_edit;
    EditText robot_name_input_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_profile_editor);

        team_edit_num = (TextView) findViewById(R.id.team_edit_num);
        urlview_edit = (TextView) findViewById(R.id.urlview_edit);
        team_name_input_edit = (EditText) findViewById(R.id.team_name_input_edit);
        team_site_url_edit = (EditText) findViewById(R.id.team_site_url_edit);
        rookie_year_input_edit = (EditText) findViewById(R.id.rookie_year_input_edit);
        hometown_input_edit = (EditText) findViewById(R.id.hometown_input_edit);
        robot_name_input_edit = (EditText) findViewById(R.id.robot_name_input_edit);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                String teamnumstr = TeamProfileEditor.this.getIntent().getStringExtra("teamNum");
                DataSnapshot child = ds.child("teams").child(teamnumstr);
                team_edit_num.setText("Team FRC " + teamnumstr);
                urlview_edit.setText(R.string.tba_base_url + new String(teamnumstr));
                if (child.child("name").getValue() != null) team_name_input_edit.setText(child.child("name").getValue().toString());
                if (child.child("site").getValue() != null) team_site_url_edit.setText(child.child("site").getValue().toString());
                if (child.child("rookie_year").getValue() != null) rookie_year_input_edit.setText(child.child("rookie_year").getValue().toString());
                if (child.child("hometown").getValue() != null) hometown_input_edit.setText(child.child("hometown").getValue().toString());
                if (child.child("robot_name").getValue() != null) robot_name_input_edit.setText(child.child("robot_name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void saveClick(View v) {
        String teamnumstr = TeamProfileEditor.this.getIntent().getStringExtra("teamNum");
        DatabaseReference child = myRef.getRoot().child("teams").child(teamnumstr);
        boolean doSave = true;
        if (team_name_input_edit.getText().toString() != "") child.child("name").setValue(team_name_input_edit.getText().toString());
        else {
            doSave = false;
        }
        if (team_site_url_edit.getText().toString() != "") child.child("site").setValue(team_site_url_edit.getText().toString());
        else {
            doSave = false;
        }
        if (rookie_year_input_edit.getText().toString() != "") child.child("rookie_year").setValue(rookie_year_input_edit.getText().toString());
        else {
            doSave = false;
        }
        if (hometown_input_edit.getText().toString() != "") child.child("hometown").setValue(hometown_input_edit.getText().toString());
        else {
            doSave = false;
        }
        if (robot_name_input_edit.getText().toString() != "") child.child("robot_name").setValue(robot_name_input_edit.getText().toString());
        else {
            doSave = false;
        }
        if (doSave) {
            Toast.makeText(this.getApplicationContext(), "Team " + teamnumstr + " saved.", Toast.LENGTH_SHORT).show();
            child.push();
            myRef.push();
            finish();
        } else {
            Toast.makeText(this.getApplicationContext(), "Please enter data for all fields.", Toast.LENGTH_LONG).show();
        }
    }
}
