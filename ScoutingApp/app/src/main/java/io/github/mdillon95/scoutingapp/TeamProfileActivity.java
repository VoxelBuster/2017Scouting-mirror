package io.github.mdillon95.scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeamProfileActivity extends AppCompatActivity {

    /* Note: no image picking interface has been implemented yet, so all image elements will remain
    invisible */

    TextView team_name_str;
    TextView team_tba_url_2;
    TextView team_site;
    TextView team_robo_name;
    TextView team_robo_dt;
    TextView team_robo_shooter;
    TextView team_robo_pickup;
    TextView team_robo_gear_pickup;
    TextView team_robo_climb;
    TextView team_robo_hopper;
    TextView team_robo_speed;
    TextView team_robo_shooter_speed;
    TextView autolabel;
    TextView robo_auto_move;
    TextView robo_auto_baseline;
    TextView robo_auto_hopper;
    TextView robo_auto_gear;
    TextView robo_auto_goal, robo_comments_view;

    private static final String TAG = "TeamProfileActivity";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_profile);
        team_name_str = (TextView) findViewById(R.id.team_name_str);
        team_tba_url_2 = (TextView) findViewById(R.id.team_tba_url_2);
        team_site = (TextView) findViewById(R.id.team_site);
        team_robo_name = (TextView) findViewById(R.id.team_robo_name);
        team_robo_dt = (TextView) findViewById(R.id.team_robo_dt);
        team_robo_shooter = (TextView) findViewById(R.id.team_robo_shooter);
        team_robo_pickup = (TextView) findViewById(R.id.team_robo_pickup);
        team_robo_gear_pickup = (TextView) findViewById(R.id.team_robo_gear_pickup);
        team_robo_climb = (TextView) findViewById(R.id.team_robo_climb);
        team_robo_hopper = (TextView) findViewById(R.id.team_robo_hopper);
        team_robo_speed = (TextView) findViewById(R.id.team_robo_speed);
        team_robo_shooter_speed = (TextView) findViewById(R.id.team_robo_shooter_speed);
        autolabel = (TextView) findViewById(R.id.textView21);
        robo_auto_move = (TextView) findViewById(R.id.robo_auto_move);
        robo_auto_baseline = (TextView) findViewById(R.id.robo_auto_baseline);
        robo_auto_hopper = (TextView) findViewById(R.id.robo_auto_hopper);
        robo_auto_gear = (TextView) findViewById(R.id.robo_auto_gear);
        robo_auto_goal = (TextView) findViewById(R.id.robo_auto_goal);
        robo_comments_view = (TextView) findViewById(R.id.robo_comments_view);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                String teamnumstr = TeamProfileActivity.this.getIntent().getStringExtra("teamNum");
                DataSnapshot child = ds.child("teams").child(teamnumstr);
                String s = new String("Team " + teamnumstr + " - " + child.child("name").getValue());
                team_name_str.setText(s);
                team_tba_url_2.setText(child.child("tba_url").getValue().toString());
                team_site.setText(child.child("site").getValue().toString());
                team_robo_name.setText(child.child("robot_name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_team_profile);
        team_name_str = (TextView) findViewById(R.id.team_name_str);
        team_tba_url_2 = (TextView) findViewById(R.id.team_tba_url_2);
        team_site = (TextView) findViewById(R.id.team_site);
        team_robo_name = (TextView) findViewById(R.id.team_robo_name);
        team_robo_dt = (TextView) findViewById(R.id.team_robo_dt);
        team_robo_shooter = (TextView) findViewById(R.id.team_robo_shooter);
        team_robo_pickup = (TextView) findViewById(R.id.team_robo_pickup);
        team_robo_gear_pickup = (TextView) findViewById(R.id.team_robo_gear_pickup);
        team_robo_climb = (TextView) findViewById(R.id.team_robo_climb);
        team_robo_hopper = (TextView) findViewById(R.id.team_robo_hopper);
        team_robo_speed = (TextView) findViewById(R.id.team_robo_speed);
        team_robo_shooter_speed = (TextView) findViewById(R.id.team_robo_shooter_speed);
        autolabel = (TextView) findViewById(R.id.textView21);
        robo_auto_move = (TextView) findViewById(R.id.robo_auto_move);
        robo_auto_baseline = (TextView) findViewById(R.id.robo_auto_baseline);
        robo_auto_hopper = (TextView) findViewById(R.id.robo_auto_hopper);
        robo_auto_gear = (TextView) findViewById(R.id.robo_auto_gear);
        robo_auto_goal = (TextView) findViewById(R.id.robo_auto_goal);
        robo_comments_view = (TextView) findViewById(R.id.robo_comments_view);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                String teamnumstr = TeamProfileActivity.this.getIntent().getStringExtra("teamNum");
                DataSnapshot child = ds.child("teams").child(teamnumstr);
                String s = new String("Team " + teamnumstr + " - " + child.child("name").getValue());
                team_name_str.setText(s);
                if (child.child("tba_url").getValue() != null) team_tba_url_2.setText(child.child("tba_url").getValue().toString());
                if (child.child("site").getValue() != null) team_site.setText(child.child("site").getValue().toString());
                if (child.child("robot_name").getValue() != null) team_robo_name.setText(child.child("robot_name").getValue().toString());
                child = ds.child("teams").child(teamnumstr).child("robot");
                if (child.child("dt").getValue() != null) team_robo_dt.setText("Drive Train Type: " + child.child("dt").getValue().toString());
                if (child.child("shooter").getValue() != null) team_robo_shooter.setText("Shooter Type: " + child.child("shooter").getValue().toString());
                if (child.child("pickup").getValue() != null) team_robo_pickup.setText("Pickup Type: " + child.child("pickup").getValue().toString());
                if (child.child("gear_pickup").getValue() != null) team_robo_gear_pickup.setText("Gear Pickup Type: " + child.child("gear_pickup").getValue().toString());
                if (child.child("can_climb").getValue() != null) team_robo_climb.setText("Can Climb: " + child.child("can_climb").getValue().toString());
                if (child.child("robot_speed").getValue() != null) team_robo_speed.setText("Robot Speed: " + child.child("robot_speed").getValue().toString());
                if (child.child("hopper_cap").getValue() != null) team_robo_hopper.setText("Hopper Approx. Capacity: " + child.child("hopper_cap").getValue().toString());
                if (child.child("shooter_speed").getValue() != null) team_robo_shooter_speed.setText("Shooter Speed: " + child.child("shooter_speed").getValue().toString());
                if (child.child("auto_moves").getValue() != null) robo_auto_move.setText("Actually Moves: " + child.child("auto_moves").getValue().toString());
                if (child.child("auto_bl").getValue() != null) robo_auto_baseline.setText("Shoots Goal: " + child.child("auto_bl").getValue().toString());
                if (child.child("auto_gear").getValue() != null) robo_auto_gear.setText("Attempts Hopper: " + child.child("auto_gear").getValue().toString());
                if (child.child("auto_shoot").getValue() != null) robo_auto_goal.setText("Attempts Gear: " + child.child("auto_shoot").getValue().toString());
                if (child.child("auto_hopper").getValue() != null) robo_auto_hopper.setText("Crosses Base Line: " + child.child("auto_hopper").getValue().toString());
                if (child.child("comments").getValue() != null) robo_comments_view.setText(child.child("comments").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void editRobotClick(View v) {
        Intent intent = new Intent(this.getApplicationContext(), RobotEditor.class);
        intent.putExtra("teamNum", this.getIntent().getStringExtra("teamNum"));
        startActivity(intent);
    }

    public void editTeamClick(View v) {
        Intent intent = new Intent(this.getApplicationContext(), TeamProfileEditor.class);
        intent.putExtra("teamNum", this.getIntent().getStringExtra("teamNum"));
        startActivity(intent);
    }
    public void addMatchClick(View v) {
        Intent intent = new Intent(this.getApplicationContext(), MatchActivity.class);
        intent.putExtra("teamNum", this.getIntent().getStringExtra("teamNum"));
        startActivity(intent);
    }

    public void matchHistoryClick(View v) {
        Intent intent = new Intent(this.getApplicationContext(), MatchHistoryActivity.class);
        intent.putExtra("teamNum", this.getIntent().getStringExtra("teamNum"));
        startActivity(intent);
    }
}
