package verbventures.frontend.ModelClasses;

import java.io.Serializable;

/**
 * Created by thetraff on 11/9/17.
 */

public class Student implements Serializable {

    private VerbVentureUser user;
    private String studentId;
    private String admin;

    public VerbVentureUser getUser() {
        return user;
    }

    public void setUser(VerbVentureUser user) {
        this.user = user;
    }

    public String getFirstName() { return user.getFirstName(); }

    public String getLastName() { return user.getLastName(); }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId() {
        this.studentId = studentId;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String email) {
        this.admin = email;
    }

    public String toString() {
        return user.getFirstName() + " " + user.getLastName();
    }

}
