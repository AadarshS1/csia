import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.time.LocalDate;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import javax.swing.JComboBox;
import static javax.swing.JOptionPane.getRootFrame;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class UserInputDialog extends JDialog {
    private static JComboBox dropdown = new JComboBox();
    private static JComboBox dropdownID = new JComboBox();
    private JLabel dayLabel = new JLabel("Day field here");
    private JTextField startField = new JTextField("Start Time");
    private JTextField endField = new JTextField("End Time");
    private JTextField eventLabel = new JTextField("Duty Date");
    private EventDateModel model;
    private LocalDate date;


    public UserInputDialog(JFrame owner) {
        super(owner, "Create Event", true);
        this.add(eventLabel, BorderLayout.NORTH);
        JPanel panel = new JPanel();
        panel.add(dayLabel);
        panel.add(startField);
        panel.add(endField);
        panel.add(dropdown);

        updateDrop();

        JButton save = new JButton("Save");
        save.addActionListener(event -> {
            Event.Time start = extractTime(startField.getText());
            Event.Time end = extractTime(endField.getText());
            Integer selectedteacherindex = dropdown.getSelectedIndex();
            String selectedteacher = dropdown.getSelectedItem().toString();
            String selectedteacherID = dropdownID.getItemAt(selectedteacherindex).toString();
            System.out.println(start);
            System.out.println(end);
            Event sampleEvent = new Event(eventLabel.getText(), start, end);
            AssignDuty(date.toString(), date.toString(), selectedteacherID);

            boolean addResult = model.addEventToDate(date, sampleEvent);
            if (addResult) {
                setVisible(false);
                model.update();
            } else {
                JOptionPane.showMessageDialog(this, "The time for this event conflicts with an "
                        + "existing event");
                setVisible(true);
            }
        });

        panel.add(save);
        this.add(panel, BorderLayout.SOUTH);
        this.pack();
    }
    public static void updateDrop(){
        Connection conn = null;
        String url = "jdbc:mysql://127.0.0.1:3306/Boarding"; //Database -> db_java
        String user = "root";
        String pass = "pavithra15";
        dropdown.removeAllItems();
        dropdownID.removeAllItems();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection Successful");
            PreparedStatement ps = conn.prepareStatement("SELECT * from teacher");
            ResultSet result = ps.executeQuery();
            while(result.next()){
                String name = result.getString("FirstName");
                String ID = result.getString("TeacherID");
                dropdown.addItem(name);
                dropdownID.addItem(ID);

            }
        } catch (Exception ex) {
            System.out.println("Error" + ex);
        }

    }


    public void AssignDuty(String dd1, String dd2, String ID){
        Connection conn = null;
        String url = "jdbc:mysql://127.0.0.1:3306/Boarding"; //Database -> db_java
        String user = "root";
        String pass = "pavithra15";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection Successful");
            System.out.println("teacherID is got");
            PreparedStatement ps1 = conn.prepareStatement("SET foreign_key_checks = 0;");
            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO Boarding.current_allocations(TeacherID, Duty_Date_Start, Duty_Date_End) VALUES('"+ID+"', '"+dd1+"', '"+dd2+"');");
            PreparedStatement ps3 = conn.prepareStatement("SET foreign_key_checks = 1;");
            Integer result1 = ps1.executeUpdate();
            Integer result2 = ps2.executeUpdate();
            Integer result3 = ps3.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error" + ex);
        }
    }


    public void attachModel(EventDateModel model, LocalDate date){
        this.model = model;
        this.date = date;
        dayLabel.setText(date.toString());
    }

    public Event.Time extractTime(String line){
        int colon = line.indexOf(":");
        int hour = Integer.parseInt(line.substring(0, colon));
        int min = Integer.parseInt(line.substring(colon+1,colon+3));
        boolean am = line.substring(colon+3).equalsIgnoreCase("am");

        return new Event.Time(hour,min,am);
    }


}