package io.github.mdillon95.scoutingpc;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class TeamDialog extends JDialog {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    public TeamDialog(ScoutingFrame parent) {
        setTitle("Add New Team");
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        JPanel numPane = new JPanel();
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        JFormattedTextField num = new JFormattedTextField(format);
        num.setColumns(40);
        numPane.add(new JLabel("Team Number: "));
        numPane.add(num);
        JPanel namePane = new JPanel();
        JTextField name = new JTextField(40);
        namePane.add(new JLabel("Team Name: "));
        namePane.add(name);
        JButton addButton = new JButton("Save");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (num.getText().equals("") || name.getText().equals("")) return;
                ref.child("teams").child(num.getText()).child("name").setValue(name.getText());
                ref.child("teams").child(num.getText()).child("site").setValue("");
                ref.child("teams").child(num.getText()).child("tba_url").setValue("https://www.thebluealliance.com/team/" + num.getText());
                ref.child("teams").child(num.getText()).child("robot_name").setValue("");
                ref.child("teams").child(num.getText()).child("rookie_year").setValue("");
                ref.child("teams").child(num.getText()).child("hometown").setValue("");
                ref.push();
                parent.populateTeams();
                TeamDialog.this.dispose();
            }
        });
        add(numPane);
        add(namePane);
        add(addButton);
        pack();
        setVisible(true);
    }
}
