package verbventures.frontend.ModelClasses;

import java.io.Serializable;

/**
 * Created by nlglo on 11/8/2017.
 */

public class VerbVentureUser implements Serializable {
    String firstName;
    String lastName;
    String userId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
