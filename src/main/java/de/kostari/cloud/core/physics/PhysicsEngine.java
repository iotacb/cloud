package de.kostari.cloud.core.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.physics.colliders.BoxCollider;
import de.kostari.cloud.core.physics.colliders.CircleCollider;
import de.kostari.cloud.utilities.math.Vec;

public class PhysicsEngine {

    private Vec2 gravity;
    private World world;

    private float physicsTimeDelta = 0.0F;
    private float physicsTimeStep = 0.02F;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public PhysicsEngine() {
        this.gravity = new Vec2(0, 981F);
        this.world = new World(gravity);
    }

    public void add(GameObject gameObject) {
        Rigidbody rigidbody = gameObject.getComponent(Rigidbody.class);

        if (rigidbody == null)
            return;

        Transform transform = gameObject.transform;

        BodyDef bodyDef = new BodyDef();
        bodyDef.angle = (float) Math.toRadians(transform.rotation);
        bodyDef.position.set(transform.position.x, transform.position.y);
        bodyDef.angularDamping = rigidbody.getAngularDamping();
        bodyDef.linearDamping = rigidbody.getLinearDamping();
        bodyDef.fixedRotation = rigidbody.isFixedRotation();
        bodyDef.bullet = rigidbody.isContinuousCollision();
        bodyDef.gravityScale = rigidbody.getGravityScale();
        bodyDef.angularVelocity = rigidbody.getAngularVelocity();
        bodyDef.userData = rigidbody.gameObject;

        bodyDef.type = rigidbody.getBodyType();

        Body body = world.createBody(bodyDef);
        body.m_mass = rigidbody.getMass();

        rigidbody.setPhysicsBody(body);

        CircleCollider circleCollider;
        BoxCollider boxCollider;

        if ((circleCollider = gameObject.getComponent(CircleCollider.class)) != null) {
            addCircleCollider(rigidbody, circleCollider);
        }

        if ((boxCollider = gameObject.getComponent(BoxCollider.class)) != null) {
            addBoxCollider(rigidbody, boxCollider);
        }
    }

    public void removeRigidbody(GameObject gameObject) {
        Rigidbody rb = gameObject.getComponent(Rigidbody.class);
        if (rb != null) {
            if (rb.getPhysicsBody() != null) {
                world.destroyBody(rb.getPhysicsBody());
                rb.setPhysicsBody(null);
            }
        }
    }

    public void update(float delta) {
        physicsTimeDelta += delta;

        if (physicsTimeDelta >= 0.0F) {
            physicsTimeDelta -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    public void resetBoxCollider(Rigidbody rb, BoxCollider boxCollider) {
        Body body = rb.getPhysicsBody();
        if (body == null)
            return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addBoxCollider(rb, boxCollider);
        body.resetMassData();
    }

    public void addBoxCollider(Rigidbody rb, BoxCollider boxCollider) {
        Body body = rb.getPhysicsBody();
        if (body == null)
            return;

        PolygonShape shape = new PolygonShape();
        Vec offset = boxCollider.getOffset();
        shape.setAsBox(boxCollider.getHalfSize().x, boxCollider.getHalfSize().y,
                new Vec2(offset.x + boxCollider.getHalfSize().x, offset.y + boxCollider.getHalfSize().y), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = boxCollider.gameObject;
        body.createFixture(fixtureDef);
    }

    public void resetCircleCollider(Rigidbody rb, CircleCollider circleCollider) {
        Body body = rb.getPhysicsBody();
        if (body == null)
            return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addCircleCollider(rb, circleCollider);
        body.resetMassData();
    }

    public void addCircleCollider(Rigidbody rb, CircleCollider circleCollider) {
        Body body = rb.getPhysicsBody();
        if (body == null)
            return;

        CircleShape shape = new CircleShape();
        shape.setRadius(circleCollider.getSize() / 2);
        shape.m_p.set(circleCollider.getOffset().x, circleCollider.getOffset().y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = circleCollider.gameObject;
        body.createFixture(fixtureDef);
    }

    private int fixtureListSize(Body body) {
        int size = 0;
        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            size++;
            fixture = fixture.m_next;
        }
        return size;
    }

}
