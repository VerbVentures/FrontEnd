package verbventures.frontend.ModelClasses;

import java.io.Serializable;

/**
 * Created by thetraff on 11/9/17.
 */

public class VerbPack implements Serializable {
    private String verbPackId;
    private String title;
    private String admin;
    private String[] verbPackVerbs;
    private String[] userVerbPacks;

    public String getVerbPackId() {
        return verbPackId;
    }

    public void setVerbPackId(String verbPackId) {
        this.verbPackId = verbPackId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String[] getVerbPackVerbs() {
        return verbPackVerbs;
    }

    public void setVerbPackVerbs(String[] verbPackVerbs) {
        this.verbPackVerbs = verbPackVerbs;
    }

    public String[] getUserVerbPacks() {
        return userVerbPacks;
    }

    public void setUserVerbPacks(String[] userVerbPacks) {
        this.userVerbPacks = userVerbPacks;
    }



}
