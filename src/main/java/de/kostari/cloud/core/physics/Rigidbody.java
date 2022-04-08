package de.kostari.cloud.core.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import de.kostari.cloud.core.components.Component;

public class Rigidbody extends Component {

    private float angularDamping = 0.0f;
    private float linearDamping = 0.0f;
    private float mass = 1;
    private BodyType bodyType = BodyType.DYNAMIC;
    private float friction = 0.1f;
    private float angularVelocity = 0.0f;
    private float gravityScale = 1.0f;
    private boolean isSensor = false;
    private boolean allowSleeping = false;

    private boolean fixedRotation = false;
    private boolean continuousCollision = false;

    private transient Body physicsBody = null;

    @Override
    public void update(float dt) {
        if (physicsBody == null)
            return;
        this.gameObject.transform.position.set(
                physicsBody.getPosition().x, physicsBody.getPosition().y);
        this.gameObject.transform.rotation = (float) Math.toDegrees(physicsBody.getAngle());

    }

    public Rigidbody setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
        if (physicsBody != null) {
            this.physicsBody.setGravityScale(gravityScale);
        }
        return this;
    }

    public float getFriction() {
        return this.friction;
    }

    public boolean isSensor() {
        return this.isSensor;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public Rigidbody setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
        return this;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public Rigidbody setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
        return this;
    }

    public float getMass() {
        return mass;
    }

    public Rigidbody setMass(float mass) {
        this.mass = mass;
        return this;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public Rigidbody setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
        return this;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public Rigidbody setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
        return this;
    }

    public boolean isContinuousCollision() {
        return continuousCollision;
    }

    public boolean allowSleeping() {
        return allowSleeping;
    }

    public Rigidbody setAllowSleeping(boolean allowSleeping) {
        this.allowSleeping = allowSleeping;
        return this;
    }

    public Rigidbody setContinuousCollision(boolean continuousCollision) {
        this.continuousCollision = continuousCollision;
        return this;
    }

    public Body getPhysicsBody() {
        return physicsBody;
    }

    public Rigidbody setPhysicsBody(Body physicsBody) {
        this.physicsBody = physicsBody;
        return this;
    }

    public float getGravityScale() {
        return gravityScale;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public Rigidbody addVelocity(float x, float y) {
        if (physicsBody == null)
            return this;
        physicsBody.applyForceToCenter(new Vec2(x, y));
        return this;
    }

    public Rigidbody addImpulse(float x, float y) {
        if (physicsBody == null)
            return this;
        physicsBody.applyLinearImpulse(new Vec2(x, y), physicsBody.getWorldCenter());
        return this;
    }

    public Rigidbody setVelocity(float x, float y) {
        if (physicsBody == null)
            return this;
        physicsBody.setLinearVelocity(new Vec2(x, y));
        return this;
    }

    public Rigidbody setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
        if (physicsBody != null) {
            this.physicsBody.setAngularVelocity(angularVelocity);
        }
        return this;
    }

}
