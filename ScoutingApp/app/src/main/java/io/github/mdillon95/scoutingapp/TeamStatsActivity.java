package io.github.mdillon95.scoutingapp;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeamStatsActivity extends AppCompatActivity {

    private static final String TAG = "TeamStatsActivity";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    TextView auto_lg_mean, auto_hg_mean, auto_gears_mean, auto_approx_pts, teleop_hg_mean,
    teleop_lg_mean, teleop_mean_cycles, teleop_mean_gears, hang_prob, mean_fouls, mean_tech,
    ylw_card_prob, mean_iso_score, mean_final_score, winrate, team_tv_stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_stats);
        auto_lg_mean = (TextView) findViewById(R.id.auto_lg_mean);
        auto_hg_mean = (TextView) findViewById(R.id.auto_hg_mean);
        auto_gears_mean = (TextView) findViewById(R.id.auto_gears_mean);
        auto_approx_pts = (TextView) findViewById(R.id.auto_approx_pts);
        teleop_hg_mean = (TextView) findViewById(R.id.teleop_hg_mean);
        teleop_lg_mean = (TextView) findViewById(R.id.teleop_lg_mean);
        teleop_mean_cycles = (TextView) findViewById(R.id.teleop_mean_cycles);
        teleop_mean_gears = (TextView) findViewById(R.id.teleop_mean_gears);
        hang_prob = (TextView) findViewById(R.id.hang_prob);
        mean_fouls = (TextView) findViewById(R.id.mean_fouls);
        mean_tech = (TextView) findViewById(R.id.mean_tech);
        ylw_card_prob = (TextView) findViewById(R.id.ylw_card_prob);
        mean_iso_score = (TextView) findViewById(R.id.mean_iso_score);
        mean_final_score = (TextView) findViewById(R.id.mean_final_score);
        winrate = (TextView) findViewById(R.id.winrate);
        team_tv_stats = (TextView) findViewById(R.id.team_tv_stats);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String teamName = dataSnapshot.child("teams").child(TeamStatsActivity.this.getIntent().getStringExtra("teamNum")).child("name").getValue().toString();
                DataSnapshot child = dataSnapshot.child("teams").child(TeamStatsActivity.this.getIntent().getStringExtra("teamNum")).child("matches");
                ArrayList<DataSnapshot> allmatches = new ArrayList<DataSnapshot>();
                team_tv_stats.setText(teamName);
                if (child.getValue() == null) return;
                for (DataSnapshot key : child.getChildren()) {
                    for (DataSnapshot match: key.getChildren()) {
                        allmatches.add(match);
                    }
                }
                float wr = 0;
                int matchesWon = 0, hangs = 0, yellowcards = 0, autohgsum = 0, autolgsum = 0, autogearssum = 0, autoptssum = 0, telehgsum = 0, telelgsum = 0, telecyclessum = 0, telegearssum = 0, foulssum = 0, techsum = 0, isosum = 0, fssum = 0;
                int numMatches = allmatches.size();
                for (DataSnapshot match : allmatches) {
                    if (match.child("won").getValue().toString().equals("true")) matchesWon += 1;
                    if (match.child("teleop").child("hang").getValue().toString().equals("true")) hangs += 1;
                    if (match.child("yellow_card").getValue().toString().equals("true") || match.child("red_card").getValue().toString().equals("true")) yellowcards += 1;
                    autohgsum += Integer.parseInt(match.child("auto").child("hg_fuel").getValue().toString());
                    autolgsum += Integer.parseInt(match.child("auto").child("lg_fuel").getValue().toString());
                    autogearssum += Integer.parseInt(match.child("auto").child("gears").getValue().toString());
                    telehgsum += Integer.parseInt(match.child("teleop").child("hg_fuel").getValue().toString());
                    telelgsum += Integer.parseInt(match.child("teleop").child("lg_fuel").getValue().toString());
                    telecyclessum += Integer.parseInt(match.child("teleop").child("lg_cycles").getValue().toString());
                    telegearssum += Integer.parseInt(match.child("teleop").child("gears").getValue().toString());
                    foulssum += Integer.parseInt(match.child("fouls").getValue().toString());
                    techsum += Integer.parseInt(match.child("tech_fouls").getValue().toString());
                    fssum += Integer.parseInt(match.child("final_score").getValue().toString());
                }
                wr = (((float) matchesWon) / ((float) numMatches)) * 100f;
                autoptssum = autohgsum + (autolgsum / 3) + (autogearssum * 24);
                isosum = ((telehgsum / 3) + (telelgsum / 9) + (telegearssum * 16) + (hangs * 50)) + autoptssum;
                float hangprob = (((float) hangs) / ((float) numMatches)) * 100f;
                float cardprob = (((float) yellowcards) / ((float) numMatches)) * 100f;
                float algm = (((float) autolgsum) / ((float) numMatches));
                float ahgm = (((float) autohgsum) / ((float) numMatches));
                float agm = (((float) autogearssum) / ((float) numMatches));
                float apm = (((float) autoptssum) / ((float) numMatches));
                float thgm = (((float) telehgsum) / ((float) numMatches));
                float tlgm = (((float) telelgsum) / ((float) numMatches));
                float tgm = (((float) telegearssum) / ((float) numMatches));
                float tlgcm = (((float) telecyclessum) / ((float) numMatches));
                float fm = (((float) foulssum) / ((float) numMatches));
                float tfm = (((float) techsum) / ((float) numMatches));
                float isom = ((float) isosum / (float) numMatches);
                winrate.setText("Winrate: " + wr + "%");
                hang_prob.setText("Succesfully hangs from Airship " + hangprob + "% of the time");
                ylw_card_prob.setText("Gets Yellow/Red Card " + cardprob + "% of the time");
                auto_lg_mean.setText("Mean Low Goal Fuel: " + algm);
                auto_hg_mean.setText("Mean High Goal Fuel: " + ahgm);
                auto_gears_mean.setText("Mean Gears: " + agm);
                auto_approx_pts.setText("Mean Approximate Points: " + apm);
                teleop_hg_mean.setText("Mean High Goal Fuel: " + thgm);
                teleop_lg_mean.setText("Mean Low Goal Fuel: " + tlgm);
                teleop_mean_gears.setText("Mean Gears: " + tgm);
                teleop_mean_cycles.setText("Mean Low Goal Cycles: " + tlgcm);
                mean_fouls.setText("Mean Fouls: " + fm);
                mean_tech.setText("Mean Tech Fouls: " + tfm);
                mean_final_score.setText("Mean Final Score: " + ((float) fssum / (float) numMatches));
                mean_iso_score.setText("Mean Isolated Score: " + isom);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.sv_ts), "Error" + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
