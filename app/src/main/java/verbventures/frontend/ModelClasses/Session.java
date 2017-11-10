package verbventures.frontend.ModelClasses;

import java.io.Serializable;

/**
 * Created by thetraff on 11/9/17.
 */

public class Session implements Serializable {

    private String sessionId;
    private String SessionDt;
    private String admin;
    private String[] sessionStudents;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionDt() {
        return SessionDt;
    }

    public void setSessionDt(String sessionDt) {
        SessionDt = sessionDt;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String[] getSessionStudents() {
        return sessionStudents;
    }

    public void setSessionStudents(String[] sessionStudents) {
        this.sessionStudents = sessionStudents;
    }



}
