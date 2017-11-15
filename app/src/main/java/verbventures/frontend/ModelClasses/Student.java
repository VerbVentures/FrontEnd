package verbventures.frontend.ModelClasses;

import java.io.Serializable;

/**
 * Created by thetraff on 11/9/17.
 */

public class Student implements Serializable {

    private VerbVentureUser user;
    private String studentId;
    private Admin adminObj;
    private String admin;

    public VerbVentureUser getUser() {
        return user;
    }

    public void setUser(VerbVentureUser user) {
        this.user = user;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId() {
        this.studentId = studentId;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdminObj(Admin adminObj) {
        this.adminObj = adminObj;
    }

    public Admin getAdminObj() {
        return adminObj;
    }

    public String toString() {
        return user.getFirstName() + " " + user.getLastName();
    }

}