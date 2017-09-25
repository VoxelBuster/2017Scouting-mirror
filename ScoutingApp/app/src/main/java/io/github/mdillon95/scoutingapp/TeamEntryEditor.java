package io.github.mdillon95.scoutingapp;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Scanner;

public class TeamEntryEditor extends AppCompatActivity {

    public final String tba_base_url = "https://www.thebluealliance.com/team/";

    private static final String TAG = "FragmentActivity";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("teams");
    private String parentNode = "teams";

    EditText team_num_input;
    TextView tba_url;
    EditText team_name_input;
    EditText team_site_input;
    EditText rookie_year_input;
    EditText hometown_input;
    EditText robot_name_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_entry_editor);
        team_num_input = (EditText) findViewById(R.id.team_num_input);
        team_num_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String teamNum = team_num_input.getText().toString();
                    tba_url.setText(tba_base_url + teamNum);
                    // Populate Fields with TBA data
                    File f = new File(TeamEntryEditor.this.getFilesDir() + "/teams.csv");
                    if (f.exists()) {
                        try {
                            Scanner s = new Scanner(f);
                            boolean lineFound = false;
                            String line = "";
                            String[] strarray = null;
                            while (s.hasNextLine() && !lineFound) {
                                line = s.nextLine();
                                if (line.startsWith("frc" + teamNum + ",")) {
                                    lineFound = true;
                                    // team num, team name, sponsors, city, state, country, site, rookie year
                                    strarray = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                                }
                                if (strarray != null) {
                                    team_name_input.setText(strarray[1]);
                                    team_site_input.setText(strarray[6]);
                                    rookie_year_input.setText(strarray[7]);
                                    hometown_input.setText(strarray[3] + ", " + strarray[4] + ", " + strarray[5]);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        tba_url = (TextView) findViewById(R.id.urlview);
        team_name_input = (EditText) findViewById(R.id.team_name_input);
        team_site_input = (EditText) findViewById(R.id.team_site_url);
        rookie_year_input = (EditText) findViewById(R.id.rookie_year_input);
        hometown_input = (EditText) findViewById(R.id.hometown_input);
        robot_name_input = (EditText) findViewById(R.id.robot_name_input);

        final Button submit = (Button) findViewById(R.id.newentry_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!generateTeamEntry()) {
                    Snackbar.make(v, "Team entry already exists", Snackbar.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(TeamEntryEditor.this, "Team entry created", Toast.LENGTH_SHORT).show();
                    TeamEntryEditor.this.finish();
                }
            }
        });
    }

    private static boolean exists = false;

    private boolean generateTeamEntry() {
        ref = database.getReference(parentNode);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(team_num_input.getText().toString())) {
                    exists = true;
                } else {
                    exists = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (!exists) {
            ref.child(team_num_input.getText().toString()).child("name").setValue(team_name_input.getText().toString());
            ref.child(team_num_input.getText().toString()).child("site").setValue(team_site_input.getText().toString());
            ref.child(team_num_input.getText().toString()).child("tba_url").setValue(tba_url.getText().toString());
            ref.child(team_num_input.getText().toString()).child("rookie_year").setValue(rookie_year_input.getText().toString());
            ref.child(team_num_input.getText().toString()).child("hometown").setValue(hometown_input.getText().toString());
            ref.child(team_num_input.getText().toString()).child("robot_name").setValue(robot_name_input.getText().toString());
            ref.push();
            return true;
        } else {
            return false;
        }

    }
}
