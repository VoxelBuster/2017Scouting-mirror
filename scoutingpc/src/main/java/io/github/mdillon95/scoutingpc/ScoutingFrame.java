package io.github.mdillon95.scoutingpc;

import com.google.firebase.database.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

// TODO clean up code and fix conventions warnings
public class ScoutingFrame extends JFrame {
    private static final long serialVersionUID = 4764084028399055834L;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();

    private JTabbedPane tabpane = new JTabbedPane();
    private JMenuBar menuBar = new JMenuBar();

    private JPanel contentPane = new JPanel();

    private int selected = -1;

    private JPanel teamsListPane = new JPanel();
    private JList<String> teamsList = new JList<>();
    private JScrollPane listScrollPane = new JScrollPane(teamsList);
    private DefaultListModel<String> teamsModel = new DefaultListModel<>();
    private ArrayList<String> teamNumsList = new ArrayList<>();
    private JTextField teamsSearch = new JTextField(50);
    private JButton searchClear = new JButton("Clear/Refresh");
    private JButton search = new JButton("Search");
    private JButton addTeam = new JButton("Add Team");
    private JPanel searchView = new JPanel();
    private JPanel buttonsView = new JPanel();

    private PaintablePanel drawingPane = new PaintablePanel();
    private JPanel drawingTools = new JPanel();
    private JButton chooseColor = new JButton("Choose Color");
    private JCheckBox eraser = new JCheckBox("Eraser");
    private JCheckBox enablebg = new JCheckBox("Enable Background");
    private JButton saveDwg = new JButton("Save Image");
    private JButton clearDwg = new JButton("Clear");
    private JPanel gamePlanPane = new JPanel();

    private JPanel statsTable = new JPanel();
    private JScrollPane statsPane = new JScrollPane(statsTable);
    private HashMap<String, JLabel> statsLabels = new HashMap<>();
    private JLabel auto = new JLabel("Autonomous");
    private JLabel teleop = new JLabel("Teleop");
    private JLabel results = new JLabel("Match Results");

    private JPanel editPane = new JPanel();
    private JPanel editTeam = new JPanel();
    private JPanel editRobot = new JPanel();
    private JFormattedTextField teamNum;
    private JTextField teamName = new JTextField(40);
    private JTextField location = new JTextField(40);
    private JTextField tbaLink = new JTextField(40);
    private JTextField teamSite = new JTextField(40);
    private JTextField roboName = new JTextField(40);
    private JComboBox<String> dtType = new JComboBox<>();
    private JComboBox<String> sType = new JComboBox<>();
    private JComboBox<String> pUType = new JComboBox<>();
    private JCheckBox canClimb = new JCheckBox();
    private JComboBox<String> gearPUType = new JComboBox<>();
    private JFormattedTextField hopperCap;
    private JComboBox<String> roboSpeed = new JComboBox<>();
    private JFormattedTextField shooterSpeed;
    private JCheckBox autoMoves = new JCheckBox();
    private JCheckBox autoBl = new JCheckBox();
    private JComboBox<String> autoHopper = new JComboBox<>();
    private JCheckBox autoGear = new JCheckBox();
    private JComboBox<String> autoGoal = new JComboBox<>();
    private NumberFormat format = NumberFormat.getIntegerInstance();
    private JFormattedTextField rookieYear;
    private JPanel scoreboardPane = new JPanel();

    public ScoutingFrame() {
        this.setTitle("Drome Scouting 2017");
        this.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                Log.i(ScoutingFrame.class, "Main window closing, quitting.");
                Main.quit(0);
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }

        });
        this.setSize(1280, 720);
        constructFrame();
        this.setVisible(true);
    }

    private void constructFrame() {
        dtType.addItem("");
        dtType.addItem("Tank Drive");
        dtType.addItem("West Coast");
        dtType.addItem("Mecanum");
        dtType.addItem("Swerve");
        dtType.addItem("Slide");
        dtType.addItem("Holonomic");
        dtType.addItem("Kiwi");

        sType.addItem("");
        sType.addItem("Low Goal Dump");
        sType.addItem("High Goal Underhand");
        sType.addItem("High Goal Overhand");
        sType.addItem("Both High and Low Goal");

        pUType.addItem("");
        pUType.addItem("Floor Pickup");
        pUType.addItem("Hopper Pickup");
        pUType.addItem("Both Floor and Hopper");

        autoHopper.addItem("");
        autoHopper.addItem("Red Key");
        autoHopper.addItem("Red Retrieval");
        autoHopper.addItem("Blue Key");
        autoHopper.addItem("Blue Retrieval");
        autoHopper.addItem("None");

        gearPUType.addItem("");
        gearPUType.addItem("Floor Pickup");
        gearPUType.addItem("Passive Pickup");
        gearPUType.addItem("Both Floor and Passive Pickup");

        roboSpeed.addItem("");
        roboSpeed.addItem("Slow (0-9 ft/s)");
        roboSpeed.addItem("Medium (9-18 ft/s)");
        roboSpeed.addItem("Fast (18+ ft/s)");

        autoGoal.addItem("");
        autoGoal.addItem("Low Goal");
        autoGoal.addItem("High Goal");
        autoGoal.addItem("None");

        this.setLayout(new FlowLayout());
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        this.setContentPane(contentPane);
        this.setJMenuBar(menuBar);
        populateTeams();
        teamsList.setModel(teamsModel);
        teamsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamsList.setLayoutOrientation(JList.VERTICAL);
        statsLabels.put("teamStr", new JLabel("Team Name"));
        statsLabels.put("meanPts", new JLabel("Mean Final Score"));
        statsLabels.put("meanLgA", new JLabel("Mean Low Goal Fuel"));
        statsLabels.put("meanHgA", new JLabel("Mean High Goal Fuel"));
        statsLabels.put("meanGearsA", new JLabel("Mean Gears"));
        statsLabels.put("meanAutoPts", new JLabel("Mean Approximate Auto Score")); // auto score = (sum auto gears * 24 + sum auto high goal + sum auto low goal / 3) / number of matches
        statsLabels.put("meanLgT", new JLabel("Mean Low Goal Fuel"));
        statsLabels.put("meanHgT", new JLabel("Mean High Goal Fuel"));
        statsLabels.put("meanGearsT", new JLabel("Mean Gears"));
        statsLabels.put("meanCyclesT", new JLabel("Mean Low Goal Cycles"));
        statsLabels.put("hangProb", new JLabel("Probability of Successful Hang"));
        statsLabels.put("meanFouls", new JLabel("Mean Fouls"));
        statsLabels.put("meanTech", new JLabel("Mean Tech Fouls"));
        statsLabels.put("ylwCardProb", new JLabel("Probability of Yellow Card"));
        statsLabels.put("winrate", new JLabel("Winrate"));
        statsLabels.put("meanIsoPts", new JLabel("Mean Isolated Score"));
        teamsList.addListSelectionListener(e -> {
            if (teamsList.getSelectedIndex() > -1) selected = teamsList.getSelectedIndex();
            if (selected > -1) {
                statsLabels.get("teamStr").setText(teamsModel.get(selected));
                ref.addValueEventListener(new ValueEventListener() {

                    public void onDataChange(DataSnapshot ds) {
                        ArrayList<DataSnapshot> allmatches = new ArrayList<>();
                        DataSnapshot child = ds.child("teams").child(teamNumsList.get(selected));
                        if (child.getValue() == null) return;
                        for (DataSnapshot key : child.getChildren()) {
                            for (DataSnapshot match : key.getChildren()) {
                                allmatches.add(match);
                            }
                        }
                        float wr;
                        int matchesWon = 0, hangs = 0, yellowcards = 0, autohgsum = 0, autolgsum = 0, autogearssum = 0, autoptssum, telehgsum = 0, telelgsum = 0, telecyclessum = 0, telegearssum = 0, foulssum = 0, techsum = 0, isosum, fssum = 0;
                        int numMatches = allmatches.size();
                        for (DataSnapshot match : allmatches) {
                            if (match.child("won").getValue().toString().equals("true")) matchesWon += 1;
                            if (match.child("teleop").child("hang").getValue().toString().equals("true")) hangs += 1;
                            if (match.child("yellow_card").getValue().toString().equals("true") || match.child("red_card").getValue().toString().equals("true"))
                                yellowcards += 1;
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
                        float fsm = ((float) fssum / (float) numMatches);

                        statsLabels.get("meanPts").setText("Mean Final Score: " + fsm);
                        statsLabels.get("meanLgA").setText("Mean Low Goal Fuel: " + algm);
                        statsLabels.get("meanHgA").setText("Mean High Goal Fuel: " + ahgm);
                        statsLabels.get("meanGearsA").setText("Mean Gears: " + agm);
                        statsLabels.get("meanAutoPts").setText("Mean Approximate Auto Score: " + apm);
                        statsLabels.get("meanLgT").setText("Mean Low Goal Fuel: " + tlgm);
                        statsLabels.get("meanHgT").setText("Mean High Goal Fuel: " + thgm);
                        statsLabels.get("meanGearsT").setText("Mean Gears: " + tgm);
                        statsLabels.get("meanCyclesT").setText("Mean Low Goal Cycles: " + tlgcm);
                        statsLabels.get("hangProb").setText("Probability of Successful Hang: " + hangprob);
                        statsLabels.get("meanFouls").setText("Mean Gears: " + fm);
                        statsLabels.get("meanTech").setText("Mean Gears: " + tfm);
                        statsLabels.get("meanIsoPts").setText("Mean Isolated Score: " + isom);
                        statsLabels.get("winrate").setText("Winrate: " + wr);
                        statsLabels.get("ylwCardProb").setText("Probability of Yellow Card: " + cardprob);
                    }

                    public void onCancelled(DatabaseError arg0) {

                    }
                });
                populateTeamRobotData(selected);
            }
        });
        teamsListPane.setLayout(new BoxLayout(teamsListPane, BoxLayout.Y_AXIS));
        searchView.setLayout(new FlowLayout());
        searchView.add(teamsSearch);
        searchClear.addActionListener(e -> {
            teamsSearch.setText("");
            teamsModel.removeAllElements();
            populateTeams();
            teamsList.setModel(teamsModel);
            Log.i(this.getClass(), "Teams List Refreshed");
        });
        saveDwg.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
            chooser.showSaveDialog(null);
            File f = chooser.getSelectedFile();
            if (f != null) drawingPane.save(f);
        });
        search.addActionListener(e -> {
            String query = teamsSearch.getText();
            if (!query.equalsIgnoreCase("")) {
                DefaultListModel<String> tempModel = new DefaultListModel<>();
                for (int i = 0; i < teamsModel.size(); i++) {
                    tempModel.addElement(teamsModel.getElementAt(i));
                }
                teamsModel.removeAllElements();
                for (int i = 0; i < tempModel.size(); i++) {
                    if (tempModel.getElementAt(i).toLowerCase().contains(query.toLowerCase())) {
                        teamsModel.addElement(tempModel.getElementAt(i));
                    }
                }
            } else {
                teamsModel.removeAllElements();
                populateTeams();
            }
            teamsList.setModel(teamsModel);
        });
        eraser.addChangeListener(arg0 -> drawingPane.enableEraser(eraser.isSelected()));
        enablebg.addChangeListener(e -> drawingPane.enableBg(enablebg.isSelected()));
        chooseColor.addActionListener(arg0 -> {
            JColorChooser jcc = new JColorChooser();
            JDialog colorFrame = new JDialog();
            colorFrame.setLayout(new FlowLayout());
            jcc.getSelectionModel().addChangeListener(e -> drawingPane.setColor(jcc.getColor()));
            colorFrame.add(jcc);
            colorFrame.pack();
            colorFrame.setVisible(true);
        });
        clearDwg.addActionListener(e -> drawingPane.clear());
        addTeam.addActionListener(e -> new TeamDialog(ScoutingFrame.this));

        // Team and Robot Details pane
        editPane.setLayout(new BoxLayout(editPane, BoxLayout.Y_AXIS));
        JPanel boxedPane = new JPanel();
        boxedPane.setLayout(new BoxLayout(boxedPane, BoxLayout.X_AXIS));
        editTeam.setBorder(new TitledBorder("Team"));
        editRobot.setBorder(new TitledBorder("Robot"));
        format.setGroupingUsed(false);
        rookieYear = new JFormattedTextField(format);
        rookieYear.setColumns(40);
        teamNum = new JFormattedTextField(format);
        teamNum.setColumns(40);
        hopperCap = new JFormattedTextField(format);
        hopperCap.setColumns(40);
        shooterSpeed = new JFormattedTextField(format);
        shooterSpeed.setColumns(40);

        JLabel rl1 = new JLabel("Robot Name:");
        rl1.setLabelFor(roboName);
        JLabel rl2 = new JLabel("Drive Train Type:");
        rl2.setLabelFor(dtType);
        JLabel rl3 = new JLabel("Shooter Type:");
        rl3.setLabelFor(sType);
        JLabel rl4 = new JLabel("Pickup Type(s):");
        rl4.setLabelFor(pUType);
        JLabel rl5 = new JLabel("Gear Pickup:");
        rl5.setLabelFor(gearPUType);
        JLabel rl6 = new JLabel("Can Climb:");
        rl6.setLabelFor(canClimb);
        JLabel rl7 = new JLabel("Hopper Approximate Capacity:");
        rl7.setLabelFor(hopperCap);
        JLabel rl8 = new JLabel("Robot Speed:");
        rl8.setLabelFor(roboSpeed);
        JLabel rl9 = new JLabel("Shooter Speed:");
        rl9.setLabelFor(shooterSpeed);
        JLabel rl10 = new JLabel("Auto");
        JLabel rl11 = new JLabel("Actually Moves:");
        rl11.setLabelFor(autoMoves);
        JLabel rl12 = new JLabel("Crosses Base Line:");
        rl12.setLabelFor(autoBl);
        JLabel rl13 = new JLabel("Attempts Hopper:");
        rl13.setLabelFor(autoHopper);
        JLabel rl14 = new JLabel("Attempts Gear:");
        rl14.setLabelFor(autoGear);
        JLabel rl15 = new JLabel("Shoots Goal:");
        rl15.setLabelFor(autoGoal);

        JLabel tl1 = new JLabel("Team Number:");
        tl1.setLabelFor(teamNum);
        teamNum.setEnabled(false);
        JLabel tl2 = new JLabel("Team Name:");
        tl2.setLabelFor(teamName);
        JLabel tl3 = new JLabel("Location:");
        JLabel tl6 = new JLabel("Rookie Year:");
        tl6.setLabelFor(rookieYear);
        tl3.setLabelFor(location);
        JLabel tl4 = new JLabel("TBA URL:");
        tl4.setLabelFor(tbaLink);
        tbaLink.setEnabled(false);
        JLabel tl5 = new JLabel("Team Website:");
        tl5.setLabelFor(teamSite);

        editTeam.setLayout(new BoxLayout(editTeam, BoxLayout.Y_AXIS));
        editTeam.add(tl1);
        editTeam.add(teamNum);
        editTeam.add(tl2);
        editTeam.add(teamName);
        editTeam.add(tl3);
        editTeam.add(location);
        editTeam.add(tl6);
        editTeam.add(rookieYear);
        editTeam.add(tl4);
        editTeam.add(tbaLink);
        editTeam.add(tl5);
        editTeam.add(teamSite);

        editRobot.setLayout(new BoxLayout(editRobot, BoxLayout.Y_AXIS));
        editRobot.add(rl1);
        editRobot.add(roboName);
        editRobot.add(rl2);
        editRobot.add(dtType);
        editRobot.add(rl3);
        editRobot.add(sType);
        editRobot.add(rl4);
        editRobot.add(pUType);
        editRobot.add(rl5);
        editRobot.add(gearPUType);
        editRobot.add(rl6);
        editRobot.add(canClimb);
        editRobot.add(rl7);
        editRobot.add(hopperCap);
        editRobot.add(rl8);
        editRobot.add(roboSpeed);
        editRobot.add(rl9);
        editRobot.add(shooterSpeed);
        editRobot.add(rl10);
        editRobot.add(rl11);
        editRobot.add(autoMoves);
        editRobot.add(rl12);
        editRobot.add(autoBl);
        editRobot.add(rl13);
        editRobot.add(autoHopper);
        editRobot.add(rl14);
        editRobot.add(autoGear);
        editRobot.add(rl15);
        editRobot.add(autoGoal);

        boxedPane.add(editTeam);
        boxedPane.add(editRobot);
        JPanel editButtons = new JPanel();
        editButtons.setLayout(new BoxLayout(editButtons, BoxLayout.Y_AXIS));
        JButton revert = new JButton("Revert Changes");
        JButton push = new JButton("Save Changes");
        JButton grabTbaData = new JButton("Grab Data from TBA");
        grabTbaData.addActionListener(e -> {
            if (selected < 0 || teamNum.getText().equals("")) return;
            File f = new File("data/teams.csv");
            if (f.exists()) {
                try {
                    Scanner reader = new Scanner(f);
                    while (reader.hasNextLine()) {
                        String line = reader.nextLine();
                        if (line.startsWith("frc" + teamNum.getText() + ",")) {
                            String[] objects = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                            teamName.setText(objects[1]);
                            location.setText(objects[3] + ", " + objects[4] + ", " + objects[5]);
                            teamSite.setText(objects[6]);
                            rookieYear.setText(objects[7]);
                        }
                    }
                } catch (FileNotFoundException e1) {
                    Log.e(ScoutingFrame.class, "Could not read teams.csv");
                }
            } else {
                Log.i(ScoutingFrame.class, "teams.csv missing. Try redownloading the teams dump.");
                JOptionPane.showMessageDialog(ScoutingFrame.this, "teams.csv is missing. Click \"Options\" then \"Download TBA Data\" to autofill data.");
            }
        });
        revert.addActionListener(e -> populateTeamRobotData(selected));
        push.addActionListener(e -> {
            if (!(selected > -1)) return;
            DatabaseReference child = ref.getRoot().child("teams").child(teamNumsList.get(selected));
            child.child("tba_url").setValue(tbaLink.getText());
            boolean save = true;
            child.child("name").setValue(teamName.getText());
            child.child("site").setValue(teamSite.getText());
            child.child("rookie_year").setValue(rookieYear.getText());
            child.child("hometown").setValue(location.getText());
            child.child("robot_name").setValue(roboName.getText());
            if(Objects.equals(hopperCap.getText(), "")) {
                save = false;
            }
            if(Objects.equals(shooterSpeed.getText(), "")) {
                save = false;
            }
            if(!dtType.getSelectedItem().toString().equals("")) {
                save = false;
            }
            if(!sType.getSelectedItem().toString().equals("")) {
                save = false;
            }
            if(!pUType.getSelectedItem().toString().equals("")) {
                save = false;
            }
            if(!gearPUType.getSelectedItem().toString().equals("")) {
                save = false;
            }
            if(!roboSpeed.getSelectedItem().toString().equals("")) {
                save = false;
            }
            if(!autoGoal.getSelectedItem().toString().equals("")) {
                save = false;
            }
            if(!autoHopper.getSelectedItem().toString().equals("")) {
                save = false;
            }

            if (save) {
                DatabaseReference ref = ScoutingFrame.this.ref.child("teams").child(teamNumsList.get(selected)).child("robot");
                ref.child("hopper_cap").setValue(hopperCap.getText());
                ref.child("shooter_speed").setValue(shooterSpeed.getText());
                ref.child("dt").setValue(dtType.getSelectedItem().toString());
                ref.child("shooter").setValue(sType.getSelectedItem().toString());
                ref.child("pickup").setValue(pUType.getSelectedItem().toString());
                ref.child("gear_pickup").setValue(gearPUType.getSelectedItem().toString());
                ref.child("can_climb").setValue(canClimb.isSelected());
                ref.child("robot_speed").setValue(roboSpeed.getSelectedItem().toString());
                ref.child("auto_moves").setValue(autoMoves.isSelected());
                ref.child("auto_shoot").setValue(autoGoal.getSelectedItem().toString());
                ref.child("auto_hopper").setValue(autoHopper.getSelectedItem().toString());
                ref.child("auto_gear").setValue(autoGear.isSelected());
                ref.child("auto_bl").setValue(autoBl.isSelected());

                child.push();
                ref.push();
            }
        });
        editButtons.add(revert);
        editButtons.add(push);
        editPane.add(boxedPane);
        editPane.add(editButtons);
        editPane.add(grabTbaData);

        // Main Frame Configuration
        enablebg.setSelected(true);
        searchView.add(searchClear);
        searchView.add(search);
        buttonsView.add(addTeam);
        teamsListPane.add(searchView);
        teamsListPane.add(listScrollPane);
        teamsListPane.add(buttonsView);
        gamePlanPane.setLayout(new BoxLayout(gamePlanPane, BoxLayout.Y_AXIS));
        gamePlanPane.add(drawingPane);
        drawingPane.setPreferredSize(drawingPane.getMaximumSize());
        drawingTools.setLayout(new FlowLayout());
        drawingTools.add(chooseColor);
        drawingTools.add(eraser);
        drawingTools.add(clearDwg);
        drawingTools.add(enablebg);
        drawingTools.add(saveDwg);
        drawingTools.setPreferredSize(drawingTools.getPreferredSize());
        gamePlanPane.add(drawingTools);
        auto.setForeground(Color.BLUE);
        teleop.setForeground(Color.BLUE);
        results.setForeground(Color.BLUE);
        statsLabels.get("teamStr").setFont(new Font("teamname", Font.BOLD, 24));
        statsTable.add(statsLabels.get("teamStr"));
        statsTable.add(auto);
        statsTable.add(statsLabels.get("meanLgA"));
        statsTable.add(statsLabels.get("meanHgA"));
        statsTable.add(statsLabels.get("meanGearsA"));
        statsTable.add(statsLabels.get("meanAutoPts"));
        statsTable.add(teleop);
        statsTable.add(statsLabels.get("meanLgT"));
        statsTable.add(statsLabels.get("meanHgT"));
        statsTable.add(statsLabels.get("meanGearsT"));
        statsTable.add(statsLabels.get("meanCyclesT"));
        statsTable.add(statsLabels.get("hangProb"));
        statsTable.add(results);
        statsTable.add(statsLabels.get("meanFouls"));
        statsTable.add(statsLabels.get("meanTech"));
        statsTable.add(statsLabels.get("ylwCardProb"));
        statsTable.add(statsLabels.get("winrate"));
        statsTable.add(statsLabels.get("meanIsoPts"));
        statsTable.add(statsLabels.get("meanPts"));
        scoreboardPane.setLayout(new BoxLayout(scoreboardPane, BoxLayout.Y_AXIS));
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());
        filterPanel.add(new JLabel("Filter By:"));
        JComboBox<String> filterSpinner = new JComboBox<>();
        JTable scoreboardTable = new JTable();
        filterSpinner.addItem("Winrate");
        filterSpinner.addItem("Number of Wins");
        filterSpinner.addItem("Average Alliance Score");
        filterSpinner.addItem("Average Isolated Score");
        filterSpinner.addItem("Average High Goal Fuel");
        filterSpinner.addItem("Average Low Goal Fuel");
        filterSpinner.addItem("Average Gears");
        filterSpinner.addItem("Hang Probability");
        filterPanel.add(filterSpinner);
        scoreboardPane.setLayout(new BoxLayout(scoreboardPane, BoxLayout.Y_AXIS));
        scoreboardPane.add(filterPanel);
        scoreboardPane.add(scoreboardTable);
        statsPane.setVerticalScrollBar(statsPane.createVerticalScrollBar());
        tabpane.addTab("Teams", teamsListPane);
        tabpane.addTab("Statistics", statsPane);
        tabpane.addTab("Leaderboard", scoreboardPane);
        tabpane.addTab("Edit Team/Robot", editPane);
        tabpane.addTab("Game Plan", gamePlanPane);
        contentPane.add(tabpane);

        // JMenuBar
        JMenu options = new JMenu("Options");
        JMenuItem pushCsvTeam = new JMenuItem("Import CSV Teams/Robots");
        pushCsvTeam.addActionListener(e -> DataManager.importTeams());
        JMenuItem pushCsvMatch = new JMenuItem("Import CSV Matches");
        pushCsvMatch.addActionListener(e -> DataManager.importMatches());
        JMenuItem dlTbaData = new JMenuItem("Download TBA Data");
        dlTbaData.addActionListener(e -> DataManager.pullTBAData());
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(e -> Main.quit(0));
        options.add(pushCsvTeam);
        options.add(pushCsvMatch);
        options.addSeparator();
        options.add(dlTbaData);
        options.addSeparator();
        options.add(quit);
        menuBar.add(options);
    }

    private void populateTeamRobotData(int selected) {
        if (selected > -1) ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot child = dataSnapshot.child("teams").child(teamNumsList.get(selected));
                teamNum.setText(teamNumsList.get(selected));
                tbaLink.setText("https://www.thebluealliance.com/team/" + teamNumsList.get(selected));
                if (child.child("name").getValue() != null) teamName.setText(child.child("name").getValue().toString());
                if (child.child("site").getValue() != null) teamSite.setText(child.child("site").getValue().toString());
                if (child.child("rookie_year").getValue() != null) rookieYear.setText(child.child("rookie_year").getValue().toString());
                if (child.child("hometown").getValue() != null) location.setText(child.child("hometown").getValue().toString());
                if (child.child("robot_name").getValue() != null) roboName.setText(child.child("robot_name").getValue().toString());
                child = dataSnapshot.child("teams").child(teamNumsList.get(selected)).child("robot");
                if (child.child("hopper_cap").getValue() != null) hopperCap.setText(child.child("hopper_cap").getValue().toString());
                if (child.child("shooter_speed").getValue() != null) shooterSpeed.setText(child.child("shooter_speed").getValue().toString());
                for (int i=0;i<dtType.getItemCount();i++) {
                    if (child.child("dt").getValue() != null && child.child("dt").getValue().toString().equals(dtType.getItemAt(i))) {
                        dtType.setSelectedIndex(i);
                    }
                }
                for (int i=0;i<sType.getItemCount();i++) {
                    if (child.child("shooter").getValue() != null && child.child("shooter").getValue().toString().equals(sType.getItemAt(i))) {
                        sType.setSelectedIndex(i);
                    }
                }
                for (int i=0;i<pUType.getItemCount();i++) {
                    if (child.child("pickup").getValue() != null && child.child("pickup").getValue().toString().equals(pUType.getItemAt(i))) {
                        pUType.setSelectedIndex(i);
                    }
                }
                for (int i=0;i<gearPUType.getItemCount();i++) {
                    if (child.child("gear_pickup").getValue() != null && child.child("gear_pickup").getValue().toString().equals(gearPUType.getItemAt(i))) {
                        gearPUType.getItemAt(i);
                    }
                }
                if (child.child("can_climb").getValue() != null && child.child("can_climb").getValue().toString().equals("Yes")) {
                    canClimb.setSelected(true);
                } else {
                    canClimb.setSelected(false);
                }
                for (int i=0;i<roboSpeed.getItemCount();i++) {
                    if (child.child("robot_speed").getValue() != null && child.child("robot_speed").getValue().toString().equals(roboSpeed.getItemAt(i))) {
                        roboSpeed.setSelectedIndex(i);
                    }
                }
                if (child.child("auto_moves").getValue() != null && child.child("auto_moves").getValue().toString().equals("Yes")) {
                    autoMoves.setSelected(true);
                } else {
                    autoMoves.setSelected(false);
                }
                for (int i=0;i<autoGoal.getItemCount();i++) {
                    if (child.child("auto_shoot").getValue() != null && child.child("auto_shoot").getValue().toString().equals(autoGoal.getItemAt(i))) {
                        autoGoal.setSelectedIndex(i);
                    }
                }
                if (child.child("auto_gear").getValue() != null && child.child("auto_gear").getValue().toString().equals("Yes")) {
                    autoGear.setSelected(true);
                } else {
                    autoGear.setSelected(false);
                }
                for (int i=0;i<autoHopper.getItemCount();i++) {
                    if (child.child("auto_hopper").getValue() != null && child.child("auto_hopper").getValue().toString().equals(autoHopper.getItemAt(i))) {
                        autoHopper.setSelectedIndex(i);
                    }
                }
                if (child.child("auto_bl").getValue() != null && child.child("auto_bl").getValue().toString().equals("Yes")) {
                    autoBl.setSelected(true);
                } else {
                    autoBl.setSelected(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void populateTeams() {
        ref.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot ds) {
                teamsModel.clear();
                teamNumsList.clear();
                for (DataSnapshot child : ds.child("teams").getChildren()) {
                    teamsModel.addElement("Team " + child.getKey() + " - " + child.child("name").getValue().toString());
                    teamNumsList.add(child.getKey());
                }
            }

            public void onCancelled(DatabaseError arg0) {
            }
        });
    }

}