import java.sql.*;
public class DBConnect {
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    public static void main(String[] args) {
        DBConnect connect = new DBConnect();




    }

    public DBConnect() {
        Connection conn = null;
        String url = "jdbc:mysql://127.0.0.1:3306/Boarding"; //Database -> db_java
        String user = "root";
        String pass = "pavithra15";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection Successful");
            Statement stmt=conn.createStatement();
            ResultSet rs =stmt.executeQuery("select * from teacher");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
        } catch (Exception ex) {
            System.out.println("Error" + ex);
        }
    }
}