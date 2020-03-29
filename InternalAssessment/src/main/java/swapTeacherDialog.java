import java.awt.BorderLayout;
import java.sql.*;
import java.time.LocalDate;

import javax.swing.*;
import java.awt.*;
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



public class swapTeacherDialog extends JDialog {
    private static JComboBox dropdown = new JComboBox();
    private static JComboBox dropdown2 = new JComboBox();
    private static JComboBox dropdownID = new JComboBox();
    private static JComboBox dropdownID2 = new JComboBox();
    private JLabel eventLabel = new JLabel("Swap Duty Dates");
    private EventDateModel model;
    private LocalDate date;

    public swapTeacherDialog(JFrame owner) {
        super(owner, "Swap", true);
        this.add(eventLabel, BorderLayout.NORTH);
        JPanel panel = new JPanel();
        panel.add(dropdown);
        panel.add(dropdown2);

        updateDrop();

        JButton save = new JButton("Swap");
        save.addActionListener(event -> {
            Integer selectedteacherindex = dropdown.getSelectedIndex();
            Integer selectedteacherindex2 = dropdown2.getSelectedIndex();
            String selectedteacher1 = dropdown.getSelectedItem().toString();
            String selectedteacher2 = dropdown2.getSelectedItem().toString();
            String selectedteacherID = dropdownID.getItemAt(selectedteacherindex).toString();
            String selectedteacherID2 = dropdownID2.getItemAt(selectedteacherindex2).toString();
            System.out.println(selectedteacher1);
            System.out.println(selectedteacher2);
            swapTeacher(selectedteacherID, selectedteacherID2);
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
        dropdown2.removeAllItems();
        dropdownID.removeAllItems();
        dropdownID2.removeAllItems();
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
                dropdown2.addItem(name);
                dropdownID2.addItem(ID);

            }
        } catch (Exception ex) {
            System.out.println("Error" + ex);
        }

    }

    public Event.Time extractTime(String line){
        int colon = line.indexOf(":");
        int hour = Integer.parseInt(line.substring(0, colon));
        int min = Integer.parseInt(line.substring(colon+1,colon+3));
        boolean am = line.substring(colon+3).equalsIgnoreCase("am");

        return new Event.Time(hour,min,am);
    }

    private void swapTeacher(String selectedteacherID, String selectedteacherID2){

        Connection conn = null;
        String url = "jdbc:mysql://127.0.0.1:3306/Boarding"; //Database -> db_java
        String user = "root";
        String pass = "pavithra15";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection Successful");
            String get_dd1 = "SELECT * FROM current_allocations WHERE TeacherID="+"'"+selectedteacherID+"';";
            String get_dd2 = "SELECT * FROM current_allocations WHERE TeacherID="+"'"+selectedteacherID2+"';";
            PreparedStatement ps3 = conn.prepareStatement(get_dd1);
            PreparedStatement ps4 = conn.prepareStatement(get_dd2);
            ResultSet result3 = ps3.executeQuery();
            ResultSet result4 = ps4.executeQuery();
            while(result3.next() && result4.next()){
                String dd1 = result3.getString("DutyTimeID");
                String dd2 = result4.getString("DutyTimeID");
                String update1 = "UPDATE current_allocations SET TeacherID="+"'"+selectedteacherID2+"'"+"WHERE DutyTimeID="+"'"+dd1+"';";
                String update2 = "UPDATE current_allocations SET TeacherID="+"'"+selectedteacherID+"'"+"WHERE DutyTimeID="+"'"+dd2+"';";
                Statement stmt = conn.createStatement();
                Statement stmt2 = conn.createStatement();
                stmt.executeUpdate(update1);
                stmt2.executeUpdate(update2);
            }

        } catch (Exception ex) {
            System.out.println("Error" + ex);
        }



    }


}
