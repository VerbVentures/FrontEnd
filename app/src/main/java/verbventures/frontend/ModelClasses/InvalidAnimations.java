package verbventures.frontend.ModelClasses;

import java.io.Serializable;

/**
 * Created by Jacob on 12/1/2017.
 */

public class InvalidAnimations implements Serializable {
    private String animationId;
    private String imageAddress;

    public String getAnimationId() { return animationId; }

    public String getImageAddress() { return imageAddress; }
}
