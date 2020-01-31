package simple;

import java.io.Serializable;

/**
 * Domain model
 *
 * @author Julian Jupiter
 *
 */
public class User implements Serializable {

    private static final long serialVersionUID = 3789909326487155148L;
    private int id;
    private String teacherID;
    private String lastName;
    private String firstName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


}