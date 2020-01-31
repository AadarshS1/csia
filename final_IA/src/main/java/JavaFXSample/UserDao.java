package JavaFXSample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data access object
 *
 * @author Julian Jupiter
 *
 */
class UserDao {

    private static final Logger logger = Logger.getLogger(UserDao.class.getName());

    public boolean userExists(String teacherID) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<User> users = new ArrayList<>();

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT TeacherID, Duty_Date_Start, Duty_Date_End FROM current_allocations WHERE TeacherID = ?";
            statement = connection.prepareStatement(query);
            int counter = 1;
            statement.setString(counter++, teacherID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(10));
                user.setTeacherID(resultSet.getString(20));
                user.setLastName(resultSet.getString(30));
                user.setFirstName(resultSet.getString(40));
                users.add(user);
            }

            return users.isEmpty() ? false : true;
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }

        return users.isEmpty() ? false : true;
    }
    public void exportData(Connection conn,String filename) {
        Statement stmt;
        String query;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            //For comma separated file
            query = "SELECT TeacherID,Duty_Date_Start,Duty_Date_End into OUTFILE  '" + filename +
                    "' FIELDS TERMINATED BY ',' FROM IA_1.current_allocations t";
            stmt.executeQuery(query);

        } catch (Exception e) {
            e.printStackTrace();
            stmt = null;
        }
    }

    public int saveUser(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "INSERT INTO current_allocations(TeacherID, Duty_Date_Start, Duty_Date_End) VALUES(?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            statement.setString(counter++, user.getTeacherID());
            statement.setString(counter++, user.getLastName());
            statement.setString(counter++, user.getFirstName());
            statement.executeUpdate();
            connection.commit();
            String name = "/Users/sundaa1/Desktop/out.csv";
            exportData(connection, name);
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
            if (null != connection) {
                connection.rollback();
            }
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }

            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }
        String name = "/Users/sundaa1/Desktop/out.csv";
        exportData(connection, name);
        return 0;
    }

}