package verbventures.frontend.ModelClasses;

import java.io.Serializable;

/**
 * Created by thetraff on 11/9/17.
 */

public class Verb implements Serializable {

    private String verb;
    private String definition;
    private String verbId;

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition() {
        this.definition = definition;
    }

    public String getVerbId() {
        return verbId;
    }
}