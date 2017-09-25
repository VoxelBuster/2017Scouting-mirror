package io.github.mdillon95.scoutingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MatchHistoryActivity extends AppCompatActivity {

    private static final String TAG = "MatchHistoryActivity";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    ListView mh_lv;
    ArrayAdapter adapter;
    ArrayList<String> items;
    String teamNumString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_match_history);
        items = new ArrayList<String>();
        items.clear();
        items.add("No data");
        mh_lv = (ListView) findViewById(R.id.match_history_lv);
        adapter = new ArrayAdapter(this.getApplicationContext(), R.layout.list_view_layout, R.id.lv_text, items);
        mh_lv.setAdapter(adapter);
        teamNumString = this.getIntent().getStringExtra("teamNum");
        items.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child("teams").child(teamNumString).child("matches").child("2017njbri"); // MAR Bridgewater
                if (ds != null) for (DataSnapshot child : ds.getChildren()) {
                    String s = "Bridgewater 2017 - ";
                    if (child.getKey().contains("q")) {
                        s = s.concat("Qualification " + child.getKey().replace("q",""));
                    } else if (child.getKey().contains("e_quarters")) {
                        s = s.concat("" +
                                "Quarterfinals " + child.getKey().replace("e_quarters",""));
                    } else if (child.getKey().contains("e_semis")) {
                        s = s.concat("" +
                                "Semifinals " + child.getKey().replace("e_semis",""));
                    } else if (child.getKey().contains("e_finals")) {
                        s = s.concat("" +
                                "Finals " + child.getKey().replace("e_finals",""));
                    }
                    items.add(s);
                }
                ds = dataSnapshot.child("teams").child(teamNumString).child("matches").child("2017njski"); // MAR Montgomery
                if (ds != null) for (DataSnapshot child : ds.getChildren()) {
                    String s = "Montgomery 2017 - ";
                    if (child.getKey().contains("q")) {
                        s = s.concat("Qualification " + child.getKey().replace("q",""));
                    } else if (child.getKey().contains("e_quarters")) {
                        s = s.concat("" +
                                "Quarterfinals " + child.getKey().replace("e_quarters",""));
                    } else if (child.getKey().contains("e_semis")) {
                        s = s.concat("" +
                                "Semifinals " + child.getKey().replace("e_semis",""));
                    } else if (child.getKey().contains("e_finals")) {
                        s = s.concat("" +
                                "Finals " + child.getKey().replace("e_finals",""));
                    }
                    items.add(s);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        adapter = new ArrayAdapter(this.getApplicationContext(), R.layout.list_view_layout, R.id.lv_text, items);
        adapter.notifyDataSetChanged();
        mh_lv.setAdapter(adapter);

        mh_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MatchHistoryActivity.this.getApplicationContext(), MatchActivity.class);
                intent.putExtra("teamNum", MatchHistoryActivity.this.getIntent().getStringExtra("teamNum"));
                if (parent.getItemAtPosition(position).toString().contains("Bridgewater 2017")) intent.putExtra("compKey", "2017njbri");
                else if (parent.getItemAtPosition(position).toString().contains("Montgomery 2017")) intent.putExtra("compKey", "2017njski");
                String matchtype = parent.getItemAtPosition(position).toString().split(" ")[3];
                String matchnum = parent.getItemAtPosition(position).toString().split(" ")[4];
                matchtype = matchtype.replace("Qualification", "q");
                matchtype = matchtype.replace("Quarterfinals", "e_quarters");
                matchtype = matchtype.replace("Semifinals", "e_semis");
                matchtype = matchtype.replace("Finals", "e_finals");
                intent.putExtra("matchKey", matchtype + matchnum);
                startActivity(intent);
            }
        });
        mh_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                PopupMenu popup = new PopupMenu(MatchHistoryActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.match_popup, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle() != "") {
                            DatabaseReference tempref = myRef.child("teams").child(MatchHistoryActivity.this.getIntent().getStringExtra("teamNum")).child("matches");
                            if (parent.getItemAtPosition(position).toString().contains("Bridgewater 2017")) tempref = tempref.child("2017njbri");
                            else if (parent.getItemAtPosition(position).toString().contains("Montgomery 2017")) tempref = tempref.child("2017njski");
                            String matchtype = parent.getItemAtPosition(position).toString().split(" ")[3];
                            String matchnum = parent.getItemAtPosition(position).toString().split(" ")[4];
                            matchtype = matchtype.replace("Qualification", "q");
                            matchtype = matchtype.replace("Quarterfinals", "e_quarters");
                            matchtype = matchtype.replace("Semifinals", "e_semis");
                            matchtype = matchtype.replace("Finals", "e_finals");
                            tempref.child(matchtype + matchnum).removeValue();
                            return true;
                        } else return false;
                    }
                });
                return true;
            }
        });
    }
}
