package verbventures.frontend.ModelClasses;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Jacob on 12/1/2017.
 */

public class RandomVerb implements Serializable {
    private SelectedVerb selectedVerb;
    private CorrectAnimation correctAnimation;
    private List<InvalidAnimations> invalidAnimations;


    public SelectedVerb getSelectedVerb() { return selectedVerb; }

    public CorrectAnimation getCorrectAnimation() { return correctAnimation; }

    public List<InvalidAnimations> getInvalidAnimations() { return invalidAnimations; }
}

