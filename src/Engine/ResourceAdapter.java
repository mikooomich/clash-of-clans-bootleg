package Engine;

import ChallengeDecision.ChallengeResource;
import Village.Buildings.ResourceProduction.Production;

public class ResourceAdapter {
    ChallengeResource resource;

    /**
     * Convert to resource type
     * @param entity
     */
    public ResourceAdapter(Production entity) {
        resource = new ChallengeResource<>(100.0, entity.currentHitpoints + 0.0);
     }

    /**
     * Get the resource object
     * @return
     */
    public ChallengeResource get() {
        return resource;
    }

}
