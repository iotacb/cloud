package de.kostari.cloud.core.physics;

import de.kostari.cloud.core.physics.colliders.AABBCollider;
import de.kostari.cloud.core.physics.colliders.BoxCollider;
import de.kostari.cloud.core.physics.colliders.CircleCollider;
import de.kostari.cloud.core.physics.raycast.Ray;
import de.kostari.cloud.core.physics.raycast.RaycastResult;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;

public class Collisions {

    public static boolean pointOnLine(Vec point, Line line) {
        float dy = line.getEnd().y - line.getStart().y;
        float dx = line.getEnd().x - line.getStart().x;
        if (dx == 0f) {
            return CMath.compare(point.x, line.getStart().x);
        }
        float m = dy / dx;

        float b = line.getEnd().y - (m * line.getEnd().x);

        return point.y == m * point.x + b;
    }

    public static boolean pointInCircle(Vec point, CircleCollider circle) {
        Vec circleCenter = circle.getCenter();
        Vec centerToPoint = new Vec(point).sub(circleCenter);

        return centerToPoint.getMagnitudeSq() <= circle.getSize() * circle.getSize();
    }

    public static boolean pointInAABB(Vec point, AABBCollider box) {
        Vec min = box.getLocalMin();
        Vec max = box.getLocalMax();

        return point.x <= max.x && min.x <= point.x &&
                point.y <= max.y && min.y <= point.y;
    }

    public static boolean pointInBox2D(Vec point, BoxCollider box) {
        Vec pointLocalBoxSpace = new Vec(point);
        CMath.rotate(pointLocalBoxSpace, box.getRigidbody().gameObject.transform.position,
                box.getRigidbody().gameObject.transform.rotation);

        Vec min = box.getLocalMin();
        Vec max = box.getLocalMax();

        return pointLocalBoxSpace.x <= max.x && min.x <= pointLocalBoxSpace.x &&
                pointLocalBoxSpace.y <= max.y && min.y <= pointLocalBoxSpace.y;
    }

    public static boolean lineAndCircle(Line line, CircleCollider circle) {
        if (pointInCircle(line.getStart(), circle) || pointInCircle(line.getEnd(), circle)) {
            return true;
        }

        Vec ab = new Vec(line.getEnd()).sub(line.getStart());

        Vec circleCenter = circle.getCenter();
        Vec centerToLineStart = new Vec(circleCenter).sub(line.getStart());
        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        if (t < 0.0f || t > 1.0f) {
            return false;
        }

        Vec closestPoint = new Vec(line.getStart()).add(ab.mul(t));

        return pointInCircle(closestPoint, circle);
    }

    public static boolean lineAndAABB(Line line, AABBCollider box) {
        if (pointInAABB(line.getStart(), box) || pointInAABB(line.getEnd(), box)) {
            return true;
        }

        Vec unitVector = new Vec(line.getEnd()).sub(line.getStart());
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;

        Vec min = box.getLocalMin();
        min.sub(line.getStart()).mul(unitVector);
        Vec max = box.getLocalMax();
        max.sub(line.getStart()).mul(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tmax < 0 || tmin > tmax) {
            return false;
        }

        float t = (tmin < 0f) ? tmax : tmin;
        return t > 0f && t * t < line.getMagnitudeSq();
    }

    public static boolean lineAndBoxCollider(Line line, AABBCollider box) {
        float theta = -box.getRigidbody().gameObject.transform.rotation;
        Vec center = box.getRigidbody().gameObject.transform.position;
        Vec localStart = new Vec(line.getStart());
        Vec localEnd = new Vec(line.getEnd());
        CMath.rotate(localStart, center, theta);
        CMath.rotate(localEnd, center, theta);

        Line localLine = new Line(localStart, localEnd);
        AABBCollider aabb = new AABBCollider(box.getLocalMin(), box.getLocalMax());

        return lineAndAABB(localLine, aabb);
    }

    public static boolean raycast(CircleCollider circle, Ray ray, RaycastResult result) {
        RaycastResult.reset(result);

        Vec originToCircle = new Vec(circle.getCenter()).sub(ray.getOrigin());
        float radiusSquared = circle.getSize() * circle.getSize();
        float originToCircleLengthSquared = originToCircle.getMagnitudeSq();

        float a = originToCircle.dot(ray.getDirection());
        float bSq = originToCircleLengthSquared - (a * a);
        if (radiusSquared - bSq < 0.0f) {
            return false;
        }

        float f = (float) Math.sqrt(radiusSquared - bSq);
        float t = 0;
        if (originToCircleLengthSquared < radiusSquared) {
            t = a + f;
        } else {
            t = a - f;
        }

        if (result != null) {
            Vec point = new Vec(ray.getOrigin()).add(
                    new Vec(ray.getDirection()).mul(t));
            Vec normal = new Vec(point).sub(circle.getCenter());
            normal.normalize();

            result.init(point, normal, t, true);
        }

        return true;
    }

    public static boolean raycast(AABBCollider box, Ray ray, RaycastResult result) {
        RaycastResult.reset(result);

        Vec unitVector = ray.getDirection();
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;

        Vec min = box.getLocalMin();
        min.sub(ray.getOrigin()).mul(unitVector);
        Vec max = box.getLocalMax();
        max.sub(ray.getOrigin()).mul(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tmax < 0 || tmin > tmax) {
            return false;
        }

        float t = (tmin < 0f) ? tmax : tmin;
        boolean hit = t > 0f;
        if (!hit) {
            return false;
        }

        if (result != null) {
            Vec point = new Vec(ray.getOrigin()).add(
                    new Vec(ray.getDirection()).mul(t));
            Vec normal = new Vec(ray.getOrigin()).sub(point);
            normal.normalize();

            result.init(point, normal, t, true);
        }

        return true;
    }

    public static boolean raycast(BoxCollider box, Ray ray, RaycastResult result) {
        RaycastResult.reset(result);

        Vec size = box.getHalfSize();
        Vec xAxis = new Vec(1, 0);
        Vec yAxis = new Vec(0, 1);
        CMath.rotate(xAxis, new Vec(0, 0), -box.getRigidbody().gameObject.transform.rotation);
        CMath.rotate(yAxis, new Vec(0, 0), -box.getRigidbody().gameObject.transform.rotation);

        Vec p = new Vec(box.getRigidbody().gameObject.transform.position).sub(ray.getOrigin());
        Vec f = new Vec(
                xAxis.dot(ray.getDirection()),
                yAxis.dot(ray.getDirection()));
        Vec e = new Vec(
                xAxis.dot(p),
                yAxis.dot(p));

        float[] tArr = { 0, 0, 0, 0 };
        for (int i = 0; i < 2; i++) {
            if (CMath.compare(f.getAxis(i), 0)) {
                if (-e.getAxis(i) - size.getAxis(i) > 0 || -e.getAxis(i) + size.getAxis(i) < 0) {
                    return false;
                }
                f.set(i, 0.00001F);
            }
            tArr[i * 2 + 0] = (e.getAxis(i) + size.getAxis(i)) / f.getAxis(i);
            tArr[i * 2 + 1] = (e.getAxis(i) - size.getAxis(i)) / f.getAxis(i);
        }

        float tmin = Math.max(Math.min(tArr[0], tArr[1]), Math.min(tArr[2], tArr[3]));
        float tmax = Math.min(Math.max(tArr[0], tArr[1]), Math.max(tArr[2], tArr[3]));

        float t = (tmin < 0f) ? tmax : tmin;
        boolean hit = t > 0f;
        if (!hit) {
            return false;
        }

        if (result != null) {
            Vec point = new Vec(ray.getOrigin()).add(
                    new Vec(ray.getDirection()).mul(t));
            Vec normal = new Vec(ray.getOrigin()).sub(point);
            normal.normalize();

            result.init(point, normal, t, true);
        }

        return true;
    }

    public static boolean circleAndLine(CircleCollider circle, Line line) {
        return lineAndCircle(line, circle);
    }

    public static boolean circleAndCircle(CircleCollider c1, CircleCollider c2) {
        Vec vecBetweenCenters = new Vec(c1.getCenter()).sub(c2.getCenter());
        float radiiSum = c1.getSize() + c2.getSize();
        return vecBetweenCenters.getMagnitudeSq() <= radiiSum * radiiSum;
    }

    public static boolean circleAndAABB(CircleCollider circle, AABBCollider box) {
        Vec min = box.getLocalMin();
        Vec max = box.getLocalMax();

        Vec closestPointToCircle = new Vec(circle.getCenter());
        if (closestPointToCircle.x < min.x) {
            closestPointToCircle.x = min.x;
        } else if (closestPointToCircle.x > max.x) {
            closestPointToCircle.x = max.x;
        }

        if (closestPointToCircle.y < min.y) {
            closestPointToCircle.y = min.y;
        } else if (closestPointToCircle.y > max.y) {
            closestPointToCircle.y = max.y;
        }

        Vec circleToBox = new Vec(circle.getCenter()).sub(closestPointToCircle);
        return circleToBox.getMagnitudeSq() <= circle.getSize() * circle.getSize();
    }

    public static boolean circleAndBoxCollider(CircleCollider circle, BoxCollider box) {
        Vec min = new Vec();
        Vec max = new Vec(box.getHalfSize()).mul(2.0f);

        Vec r = new Vec(circle.getCenter()).sub(box.getRigidbody().gameObject.transform.position);
        CMath.rotate(r, new Vec(), -box.getRigidbody().gameObject.transform.rotation);
        Vec localCirclePos = new Vec(r).add(box.getHalfSize());

        Vec closestPointToCircle = new Vec(localCirclePos);
        if (closestPointToCircle.x < min.x) {
            closestPointToCircle.x = min.x;
        } else if (closestPointToCircle.x > max.x) {
            closestPointToCircle.x = max.x;
        }

        if (closestPointToCircle.y < min.y) {
            closestPointToCircle.y = min.y;
        } else if (closestPointToCircle.y > max.y) {
            closestPointToCircle.y = max.y;
        }

        Vec circleToBox = new Vec(localCirclePos).sub(closestPointToCircle);
        return circleToBox.getMagnitudeSq() <= circle.getSize() * circle.getSize();
    }

    public static boolean AABBAndCircle(AABBCollider box, CircleCollider circle) {
        return circleAndAABB(circle, box);
    }

    public static boolean AABBAndAABBB(AABBCollider b1, AABBCollider b2) {
        Vec axesToTest[] = { new Vec(0, 1), new Vec(1, 0) };
        for (int i = 0; i < axesToTest.length; i++) {
            if (!overlapOnAxis(b1, b2, axesToTest[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean AABBAndBoxCollider(AABBCollider b1, BoxCollider b2) {
        Vec axesToTest[] = {
                new Vec(0, 1), new Vec(1, 0),
                new Vec(0, 1), new Vec(1, 0)
        };
        CMath.rotate(axesToTest[2], new Vec(), b2.getRigidbody().gameObject.transform.rotation);
        CMath.rotate(axesToTest[3], new Vec(), b2.getRigidbody().gameObject.transform.rotation);
        for (int i = 0; i < axesToTest.length; i++) {
            if (!overlapOnAxis(b1, b2, axesToTest[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean BoxColliderAndBoxCollider(BoxCollider b1, BoxCollider b2) {
        Vec axesToTest[] = {
                new Vec(0, 1), new Vec(1, 0),
                new Vec(0, 1), new Vec(1, 0)
        };
        CMath.rotate(axesToTest[2], new Vec(), b2.getRigidbody().gameObject.transform.rotation);
        CMath.rotate(axesToTest[3], new Vec(), b2.getRigidbody().gameObject.transform.rotation);
        for (int i = 0; i < axesToTest.length; i++) {
            if (!overlapOnAxis(b1, b2, axesToTest[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean overlapOnAxis(AABBCollider b1, AABBCollider b2, Vec axis) {
        Vec interval1 = getInterval(b1, axis);
        Vec interval2 = getInterval(b2, axis);
        return ((interval2.x <= interval1.y) && (interval1.x <= interval2.y));
    }

    private static boolean overlapOnAxis(AABBCollider b1, BoxCollider b2, Vec axis) {
        Vec interval1 = getInterval(b1, axis);
        Vec interval2 = getInterval(b2, axis);
        return ((interval2.x <= interval1.y) && (interval1.x <= interval2.y));
    }

    private static boolean overlapOnAxis(BoxCollider b1, BoxCollider b2, Vec axis) {
        Vec interval1 = getInterval(b1, axis);
        Vec interval2 = getInterval(b2, axis);
        return ((interval2.x <= interval1.y) && (interval1.x <= interval2.y));
    }

    private static Vec getInterval(AABBCollider rect, Vec axis) {
        Vec result = new Vec(0, 0);

        Vec min = rect.getLocalMin();
        Vec max = rect.getLocalMax();

        Vec vertices[] = {
                new Vec(min.x, min.y), new Vec(min.x, max.y),
                new Vec(max.x, min.y), new Vec(max.x, max.y)
        };

        result.x = axis.dot(vertices[0]);
        result.y = result.x;
        for (int i = 1; i < 4; i++) {
            float projection = axis.dot(vertices[i]);
            if (projection < result.x) {
                result.x = projection;
            }
            if (projection > result.y) {
                result.y = projection;
            }
        }
        return result;
    }

    private static Vec getInterval(BoxCollider rect, Vec axis) {
        Vec result = new Vec(0, 0);

        Vec vertices[] = rect.getVertices();

        result.x = axis.dot(vertices[0]);
        result.y = result.x;
        for (int i = 1; i < 4; i++) {
            float projection = axis.dot(vertices[i]);
            if (projection < result.x) {
                result.x = projection;
            }
            if (projection > result.y) {
                result.y = projection;
            }
        }
        return result;
    }
}