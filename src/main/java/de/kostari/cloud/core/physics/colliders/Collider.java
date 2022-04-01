package de.kostari.cloud.core.physics.colliders;

import de.kostari.cloud.core.components.Component;
import de.kostari.cloud.core.physics.Rigidbody;
import de.kostari.cloud.utilities.math.Vec;

public class Collider extends Component {

    private Rigidbody rigidbody;
    private Vec offset = new Vec();

    public Rigidbody getRigidbody() {
        return rigidbody;
    }

    public Vec getOffset() {
        return offset;
    }

    public Collider setRigidbody(Rigidbody rigidbody) {
        this.rigidbody = rigidbody;
        return this;
    }

    public Collider setOffset(Vec offset) {
        this.offset = offset;
        return this;
    }

}
