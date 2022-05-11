package de.kostari.cloud.utilities.math;

public class CMath {

	public static float PI = (float) Math.PI, TAU = (float) Math.PI * 2;
	public static float RAD2DEG = 360 / TAU, DEG2RAD = TAU / 360;

	public static int sign(float value) {
		return (value > 0 ? 1 : 0);
	}

	public static float max(float firstValue, float secondValue) {
		return (firstValue > secondValue ? firstValue : secondValue);
	}

	public static int max(int firstValue, int secondValue) {
		return (firstValue > secondValue ? firstValue : secondValue);
	}

	public static float min(float firstValue, float secondValue) {
		return (firstValue < secondValue ? firstValue : secondValue);
	}

	public static int min(int firstValue, int secondValue) {
		return (firstValue < secondValue ? firstValue : secondValue);
	}

	public static float clamp(float value, float min, float max) {
		return (value > max ? max : value < min ? min : value);
	}

	public static int clamp(int value, int min, int max) {
		return (value > max ? max : value < min ? min : value);
	}

	public static float clamp01(float value) {
		return clamp(value, 0, 1);
	}

	public static int clamp01(int value) {
		return clamp(value, 0, 1);
	}

	public static float direction(float firstX, float firstY, float secondX, float secondY) {
		float angle = (float) Math.toDegrees(Math.atan2(secondY - firstY, secondX - firstX)) + 90;
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	public static float direction(Vec firstLocation, Vec secondLocation) {
		float angle = (float) Math
				.toDegrees(Math.atan2(secondLocation.y - firstLocation.y, secondLocation.x - firstLocation.x)) + 90;
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	public static float lengthDirX(float value, float direction) {
		return (float) Math.cos(Math.toRadians(direction)) * value;
	}

	public static float lengthDirY(float value, float direction) {
		return (float) Math.sin(Math.toRadians(direction)) * value;
	}

	public static float dist(float firstX, float firstY, float secondX, float secondY) {
		float diffX = secondX - firstX;
		float diffY = secondY - firstY;
		return (float) Math.sqrt(diffX * diffX + diffY * diffY);
	}

	public static float dist(Vec firstLocation, Vec secondLocation) {
		float diffX = secondLocation.x - firstLocation.x;
		float diffY = secondLocation.y - firstLocation.y;
		return (float) Math.sqrt(diffX * diffX + diffY * diffY);
	}

	public static float length(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}

	public static float length(Vec location) {
		return length(location.x, location.y);
	}

	public static float approach(float currentValue, float finalValue, float stepValue) {
		if (currentValue < finalValue) {
			currentValue += stepValue;
			if (currentValue > finalValue) {
				return finalValue;
			}
		} else {
			currentValue -= stepValue;
			if (currentValue < finalValue) {
				return finalValue;
			}
		}
		return currentValue;
	}

	public static float average(float... values) {
		float average = 0;
		for (float value : values) {
			average += value;
		}
		return (values.length > 0 ? average / values.length : average);
	}

	public static float wrap(float value, float min, float max) {
		if (value % 1 == 0) {
			while (value > max || value < min) {
				if (value > max) {
					value += min - max - 1;
				} else if (value < min) {
					value += max - min + 1;
				}
			}
			return value;
		} else {
			float oldValue = value + 1;
			while (value != oldValue) {
				oldValue = value;
				if (value < min) {
					value = max - (min - value);
				} else if (value > max) {
					value = min + (value - max);
				}
			}
			return value;
		}
	}

	public static float signedDistanceToCircle(int firstX, int firstY, int secondX, int secondY, int circleRadius) {
		float diffX = secondX - firstX;
		float diffY = secondY - firstY;
		return (length(diffX, diffY) - circleRadius / 2) * 2;
	}

	public static float signedDistanceToCircle(Vec firstLocation, Vec secondLocation, int circleRadius) {
		return signedDistanceToCircle((int) firstLocation.x, (int) firstLocation.y, (int) secondLocation.x,
				(int) secondLocation.y, circleRadius);
	}

	public static float signedDistanceToRect(int firstX, int firstY, int secondX, int secondY, int width, int height) {
		float offsetX = Math.abs(firstX - secondX) - width / 2;
		float offsetY = Math.abs(firstY - secondY) - height / 2;

		float unsignedDistance = length(Math.max(offsetX, 0), Math.max(offsetY, 0)) * 2;
		float distanceInRect = Math.max(Math.min(offsetX, 0), Math.min(offsetY, 0)) * 2;

		return (unsignedDistance + distanceInRect);
	}

	public static float signedDistanceToRect(Vec firstLocation, Vec secondLocation, int width, int height) {
		return signedDistanceToRect((int) firstLocation.x, (int) firstLocation.y, (int) secondLocation.x,
				(int) secondLocation.y, width, height);
	}

	public static float signedDistanceToRect(Vec firstLocation, Vec secondLocation, Vec size) {
		return signedDistanceToRect(firstLocation, secondLocation, (int) size.x, (int) size.y);
	}

	public static float map(float value, float istart, float istop, float ostart, float ostop) {
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}

	public static float map(float value, float min, float max) {
		return map(value, 0, value, min, max);
	}

	public static float lerp(float current, float target, float time) {
		return (1 - time) * current + time * target;
	}

	public static float quad(float progress, float start, float end, float duration) {
		float tmpEnd = end;
		if (start > tmpEnd) {
			tmpEnd = start;
			start = tmpEnd;
		}

		if ((progress /= duration / 2) < 1)
			return end / 2 * progress * progress + start;
		return -end / 2 * ((--progress) * (progress - 2) - 1) + start;
	}

	public static void rotate(Vec vec, Vec origin, float angleDeg) {
		float x = vec.x - origin.x;
		float y = vec.y - origin.y;

		float cos = (float) Math.cos(Math.toRadians(angleDeg));
		float sin = (float) Math.sin(Math.toRadians(angleDeg));

		float xPrime = (x * cos) - (y * sin);
		float yPrime = (x * sin) + (y * cos);

		xPrime += origin.x;
		yPrime += origin.y;

		vec.x = xPrime;
		vec.y = yPrime;
	}

	public static boolean isZero(float value, float epsilon) {
		return Math.abs(value) <= epsilon;
	}

	public static boolean isZero(float value) {
		return Math.abs(value) <= Float.MIN_VALUE;
	}

	public static boolean compare(float x, float y, float epsilon) {
		return Math.abs(x - y) <= epsilon * Math.max(1.0F, Math.max(Math.abs(x), Math.abs(y)));
	}

	public static boolean compare(Vec vec1, Vec vec2, float epsilon) {
		return compare(vec1.x, vec2.x, epsilon) && compare(vec1.y, vec2.y, epsilon);
	}

	public static boolean compare(float x, float y) {
		return compare(x, y, Float.MIN_VALUE);
	}

	public static boolean compare(Vec vec1, Vec vec2) {
		return compare(vec1, vec2, Float.MIN_VALUE);
	}

	public static float smoothMin(float a, float b, float smoothing) {
		float h = clamp01((b - a + smoothing) / (2 * smoothing));
		return a * h + b * (1 - h) - smoothing * h * (1 - h);
	}

	public static float fromRange(float min, float max) {
		return random() * (max - min) + min;
	}

	public static float random() {
		return (float) Math.random();
	}

	public static float magnitude(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}

	public static float magnitudeSq(float x, float y) {
		return x * x + y * y;
	}

	public static float magnitude(float x) {
		return (float) Math.sqrt(x * x);
	}

	public static float magnitudeSq(float x) {
		return x * x;
	}

	public static float normalize(float x) {
		float magnitude = magnitude(x);
		if (magnitude != 0 && magnitude != 1) {
			x /= magnitude;
		}
		return x;
	}

}
