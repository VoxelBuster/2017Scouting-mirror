package io.github.mdillon95.scoutingapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamsDbActivity extends AppCompatActivity {

    private static final String TAG = "TeamsDbActivity";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    ArrayList<String> items = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String[] list = new String[] {
            "No data"
    };

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_db);
        items.addAll(Arrays.asList(list));
        ListView lv = (ListView) findViewById(R.id.list);
        EditText sv = (EditText) findViewById(R.id.db_search);
        ImageButton sb = (ImageButton) findViewById(R.id.query_button);
        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> itemsToRemove = new ArrayList<String>();
                for (String item : items) {
                    if (!item.contains(((EditText) findViewById(R.id.db_search)).getText().toString())) {
                        itemsToRemove.add(item);
                    }
                }
                for (String i : itemsToRemove){
                    adapter.remove(i);
                }
            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot child : dataSnapshot.child("teams").getChildren()) {
                    String s;
                    s = new String("Team " + child.getKey().toString() + " - " + child.child("name").getValue());
                    items.add(s);
                }
                TeamsDbActivity.this.adapter = new ArrayAdapter<String>(TeamsDbActivity.this.getApplicationContext(), R.layout.list_view_layout, R.id.lv_text, items);
                TeamsDbActivity.this.lv = (ListView) findViewById(R.id.list);
                TeamsDbActivity.this.lv.setAdapter(adapter);
                TeamsDbActivity.this.adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new ArrayAdapter<String>(this.getApplicationContext(), R.layout.list_view_layout, R.id.lv_text, items);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_teams_db);
        items.addAll(Arrays.asList(list));
        ListView lv = (ListView) findViewById(R.id.list);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot child : dataSnapshot.child("teams").getChildren()) {
                    String s;
                    s = new String("Team " + child.getKey().toString() + " - " + child.child("name").getValue());
                    items.add(s);
                }
                TeamsDbActivity.this.adapter = new ArrayAdapter<String>(TeamsDbActivity.this.getApplicationContext(), R.layout.list_view_layout, R.id.lv_text, items);
                TeamsDbActivity.this.lv = (ListView) findViewById(R.id.list);
                TeamsDbActivity.this.lv.setAdapter(adapter);
                TeamsDbActivity.this.adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ImageButton sb = (ImageButton) findViewById(R.id.query_button);
        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> itemsToRemove = new ArrayList<String>();
                for (String item : items) {
                    if (!item.toUpperCase().contains(((EditText) findViewById(R.id.db_search)).getText().toString().toUpperCase())) {
                        itemsToRemove.add(item);
                    }
                }
                for (String i : itemsToRemove){
                    adapter.remove(i);
                }
            }
        });
        ImageButton cancel_search = (ImageButton) findViewById(R.id.cancel_search);
        cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) findViewById(R.id.db_search)).setText("");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        items.clear();
                        for (DataSnapshot child : dataSnapshot.child("teams").getChildren()) {
                            String s;
                            s = new String("Team " + child.getKey().toString() + " - " + child.child("name").getValue());
                            items.add(s);
                        }
                        TeamsDbActivity.this.adapter = new ArrayAdapter<String>(TeamsDbActivity.this.getApplicationContext(), R.layout.list_view_layout, R.id.lv_text, items);
                        TeamsDbActivity.this.lv = (ListView) findViewById(R.id.list);
                        TeamsDbActivity.this.lv.setAdapter(adapter);
                        TeamsDbActivity.this.adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        adapter = new ArrayAdapter<String>(this.getApplicationContext(), R.layout.list_view_layout, R.id.lv_text, items);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("No data")) return;
                TextView tv = ((TextView) (((LinearLayout) view).getChildAt(0)));
                String viewString = tv.getText().toString();
                String regex = "[^\\d]";
                String teamNumStr = viewString.replaceAll(regex, "");
                Intent intent = new Intent(TeamsDbActivity.this, TeamProfileActivity.class);
                intent.putExtra("teamNum",teamNumStr);
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("No data")) return false;
                PopupMenu popup = new PopupMenu(TeamsDbActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.edit_team_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Edit Team")) {
                            Intent intent = new Intent(TeamsDbActivity.this, TeamProfileEditor.class);
                            String viewString = parent.getItemAtPosition(position).toString();
                            String regex = "[^\\d]";
                            String teamNumStr = viewString.replaceAll(regex, "");
                            intent.putExtra("teamNum", teamNumStr);
                            startActivity(intent);
                        } else if (item.getTitle().equals("Delete Team")) {
                            new AlertDialog.Builder(TeamsDbActivity.this)
                                    .setTitle("Delete Team")
                                    .setMessage("Are you sure you want to delete this team?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String viewString = parent.getItemAtPosition(position).toString();
                                            String regex = "[^\\d]";
                                            String teamNumStr = viewString.replaceAll(regex, "");
                                            myRef.getRoot().child("teams").child(teamNumStr).removeValue();
                                            myRef.push();
                                            TeamsDbActivity.this.onResume();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {}
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)

                                    .show();
                        } else if (item.getTitle().equals("Copy JSON Node")) {
                            String viewString = parent.getItemAtPosition(position).toString();
                            String regex = "[^\\d]";
                            final String teamNumStr = viewString.replaceAll(regex, "");
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String node = dataSnapshot.child("teams").child(teamNumStr).getValue().toString();
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData.newPlainText(teamNumStr + "_node", node);
                                    Toast.makeText(TeamsDbActivity.this.getApplicationContext(), "Copied to clipboard.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        } else if (item.getTitle().equals("View Team Stats")) {
                            String viewString = parent.getItemAtPosition(position).toString();
                            String regex = "[^\\d]";
                            String teamNumStr = viewString.replaceAll(regex, "");
                            Intent intent = new Intent(TeamsDbActivity.this.getApplicationContext(), TeamStatsActivity.class);
                            intent.putExtra("teamNum",teamNumStr);
                            startActivity(intent);
                        }
                        return false;
                    }
                });
                popup.show();
                return true;
            }
        });
    }

    public void fabOnClick(View v) {
        PopupMenu popup = new PopupMenu(TeamsDbActivity.this, v);
        popup.getMenuInflater().inflate(R.menu.teams_db_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("Create Team Entry")) {
                    Intent intent = new Intent(TeamsDbActivity.this, TeamEntryEditor.class);
                    startActivity(intent);
                } else if (item.getTitle().toString().equals("Draw Game Plan")) {
                    Intent intent = new Intent(TeamsDbActivity.this, GamePlanActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        popup.show();
    }
}
