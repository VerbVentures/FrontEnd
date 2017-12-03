package verbventures.frontend.ModelClasses;

import java.io.Serializable;

/**
 * Created by Jacob on 12/1/2017.
 */

public class SelectedVerb implements Serializable {
    private String verbId;
    private String verb;
    private String definition;

    public String getVerbId() { return verbId; }

    public String getVerb() { return verb; }

    public String getDefinition() { return definition; }
}
