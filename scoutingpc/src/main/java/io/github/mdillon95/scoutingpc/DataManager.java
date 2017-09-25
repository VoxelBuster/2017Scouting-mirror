package io.github.mdillon95.scoutingpc;

import com.google.firebase.database.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class DataManager {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference ref = database.getReference();

    //private static String tbaEventsUrl = "https://github.com/the-blue-alliance/the-blue-alliance-data/tree/master/events";

    static void pullTBAData() {
        String tbaTeamsUrl = "https://github.com/the-blue-alliance/the-blue-alliance-data/raw/master/teams/teams.csv";
        Log.i(DataManager.class, "Downloading TBA Teams data from: " + tbaTeamsUrl);
        new File("data").mkdir();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(tbaTeamsUrl);
            URLConnection urlConn = url.openConnection(); //connect

            is = urlConn.getInputStream(); //get connection inputstream
            fos = new FileOutputStream("data/teams.csv"); //open outputstream to local file

            byte[] buffer = new byte[4096]; //declare 4KB buffer
            int len;

            //while we have availble data, continue downloading and storing to local file
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            JOptionPane.showMessageDialog(null, "Download Complete.", "Download Complete", JOptionPane.PLAIN_MESSAGE);
            Log.i(DataManager.class, "TBA data download complete.");
        } catch (Exception e) {
            Log.e(DataManager.class, "TBA Teams Dump failed to download! Check your internet connection.");
            JOptionPane.showMessageDialog(null, "TBA Teams Dump failed to download! Check your internet connection.", "Download Failed", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                Log.e(DataManager.class, "An IO error occurred!");
            } finally {
                if (fos != null) {
                    try { fos.close(); }
                    catch (IOException e) {
                        Log.e(DataManager.class, "An IO error occurred!");
                    }
                }
            }
        }
    }

    private static File askForFile() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Comma Separated Values", "csv"));
        fc.showOpenDialog(null);
        return fc.getSelectedFile();
    }

    static void importTeams() {
        File f = askForFile();
        try {
            Scanner reader = new Scanner(f);
            reader.nextLine(); // Rejects first line
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                Log.d(DataManager.class, "Read: " + line);
                String[] objects = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                DatabaseReference child = ref.child("teams").child(objects[2]);
                child.child("name").setValue(objects[3]);
                child.child("robot_name").setValue(objects[4]);
                child = child.child("robot");
                child.child("dt").setValue(objects[5]);
                child.child("shooter").setValue(objects[6]);
                child.child("pickup").setValue(objects[7]);
                child.child("gear_pickup").setValue(objects[8]);
                child.child("climb").setValue(objects[9]);
                child.child("hopper_cap").setValue(objects[10]);
                child.child("robot_speed").setValue(objects[11]);
                child.child("shooter_speed").setValue(objects[12]);
                child.child("auto_moves").setValue(objects[13]);
                child.child("auto_hopper").setValue(objects[14]);
                if (!objects[15].equals("")) child.child("auto_gear").setValue("Yes");
                else child.child("auto_gear").setValue("No");
                child.child("comments").setValue(objects[17]);
                child.child("auto_bl").setValue(objects[18]);
            }
        } catch (FileNotFoundException e) {
            Log.e(DataManager.class, "Could not read file " + f.getName());
        }
    }
    static void importMatches() {
        File f = askForFile();
        try {
            Scanner reader = new Scanner(f);
            reader.nextLine(); // Rejects first line
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                Log.d(DataManager.class, "Read: " + line);
                String[] objects = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                DatabaseReference compkeys = ref.child("compkeys");
                compkeys.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DatabaseReference child = ref.child("teams").child(objects[2]).child("matches");
                        for (DataSnapshot key: dataSnapshot.getChildren()) {
                            if (key.getValue().equals(objects[4])) child = child.child(key.getKey());
                        }
                        if (objects[5].equals("Qualifications")) child = child.child("q" + objects[6]);
                        else if (objects[5].equals("Quarter-Finals")) child = child.child("e_quarters" + objects[6]);
                        else if (objects[5].equals("Semi-Finals")) child = child.child("e_semis" + objects[6]);
                        else if (objects[5].equals("Finals")) child = child.child("e_finals" + objects[6]);
                        else return;
                        child.child("scouter_name").setValue(objects[1]);
                        child.child("alliance").setValue(objects[7]);
                        child.child("ds_number").setValue(objects[8]);
                        child.child("ally_1").setValue(objects[9]);
                        child.child("ally_2").setValue(objects[10]);
                        if (objects[11].equalsIgnoreCase("Yes")) child.child("auto").child("moved").setValue(true);
                        else child.child("auto").child("moved").setValue(false);
                        if (objects[30].equalsIgnoreCase("Yes")) child.child("auto").child("baseline").setValue(true);
                        else child.child("auto").child("baseline").setValue(false);
                        child.child("auto").child("gears").setValue(objects[12]);
                        child.child("auto").child("lg_fuel").setValue(objects[14]);
                        child.child("auto").child("hg_fuel").setValue(objects[15]);
                        child.child("shoot_speed").setValue(objects[16]);
                        if (objects[17].contains("0")) child.child("auto").child("acc").setValue(0);
                        else if (objects[17].contains("34")) child.child("auto").child("acc").setValue(1);
                        else if (objects[17].contains("67")) child.child("auto").child("acc").setValue(2);
                        else child.child("auto").child("acc").setValue(1);
                        child.child("teleop").child("lg_fuel").setValue(objects[18]);
                        child.child("teleop").child("lg_cycles").setValue(objects[19]);
                        child.child("teleop").child("hg_fuel").setValue(objects[20]);
                        child.child("teleop").child("gears").setValue(objects[21]);
                        if (objects[22].contains("0")) child.child("teleop").child("acc").setValue(0);
                        else if (objects[22].contains("34")) child.child("teleop").child("acc").setValue(1);
                        else if (objects[22].contains("67")) child.child("teleop").child("acc").setValue(2);
                        else child.child("teleop").child("acc").setValue(1);
                        if (objects[23].equalsIgnoreCase("Yes")) child.child("teleop").child("hang").setValue(true);
                        else child.child("teleop").child("hang").setValue(false);
                        child.child("fouls").setValue(objects[24]);
                        child.child("tech_fouls").setValue(objects[25]);
                        child.child("robot_speed").setValue("Slow (0-9 ft/s)");
                        child.child("fuel_intake").child("floor").setValue(false);
                        child.child("fuel_intake").child("hopper").setValue(false);
                        child.child("auto").child("bk_hopper").setValue(false);
                        child.child("auto").child("rk_hopper").setValue(false);
                        child.child("auto").child("br_hopper").setValue(false);
                        child.child("auto").child("rr_hopper").setValue(false);
                        child.child("auto").child("center_hopper").setValue(false);
                        if (objects[26].contains("Disabled")) child.child("disabled").setValue(true);
                        else child.child("disabled").setValue(false);
                        if (objects[26].contains("Re-")) child.child("re_enabled").setValue(true);
                        else child.child("re_enabled").setValue(false);
                        if (objects[26].contains("Yellow")) child.child("yellow_card").setValue(true);
                        else child.child("yellow_card").setValue(false);
                        if (objects[26].contains("Red")) child.child("red_card").setValue(true);
                        else child.child("red_card").setValue(false);
                        if (objects[27].equalsIgnoreCase("Yes")) child.child("won").setValue(true);
                        else child.child("won").setValue(false);
                        child.child("final_score").setValue(objects[28]);
                        child.child("comments").setValue(objects[29]);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } catch (FileNotFoundException e) {
            Log.e(DataManager.class, "Could not read file " + f.getName());
        }
    }
}
