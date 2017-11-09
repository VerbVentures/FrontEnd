package verbventures.frontend.ModelClasses;

import java.io.Serializable;

/**
 * Created by nlglo on 11/8/2017.
 */

public class Admin implements Serializable {

    private VerbVentureUser user;
    private String accountKitId;
    private String email;

    public VerbVentureUser getUser() {
        return user;
    }

    public void setUser(VerbVentureUser user) {
        this.user = user;
    }

    public String getAccountKitId() {
        return accountKitId;
    }

    public void setAccountKitId() {
        this.accountKitId = accountKitId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
