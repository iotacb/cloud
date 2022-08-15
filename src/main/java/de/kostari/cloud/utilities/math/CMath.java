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

	public static int fromRange(int min, int max) {
		return (int) random() * (max - min) + min;
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

	public static float MatrixDeterminant(Matrix mat) {
		// Cache the matrix values (speed optimization)
		float a00 = mat.m0, a01 = mat.m1, a02 = mat.m2, a03 = mat.m3;
		float a10 = mat.m4, a11 = mat.m5, a12 = mat.m6, a13 = mat.m7;
		float a20 = mat.m8, a21 = mat.m9, a22 = mat.m10, a23 = mat.m11;
		float a30 = mat.m12, a31 = mat.m13, a32 = mat.m14, a33 = mat.m15;

		return a30 * a21 * a12 * a03 - a20 * a31 * a12 * a03 - a30 * a11 * a22 * a03 + a10 * a31 * a22 * a03 +
				a20 * a11 * a32 * a03 - a10 * a21 * a32 * a03 - a30 * a21 * a02 * a13 + a20 * a31 * a02 * a13 +
				a30 * a01 * a22 * a13 - a00 * a31 * a22 * a13 - a20 * a01 * a32 * a13 + a00 * a21 * a32 * a13 +
				a30 * a11 * a02 * a23 - a10 * a31 * a02 * a23 - a30 * a01 * a12 * a23 + a00 * a31 * a12 * a23 +
				a10 * a01 * a32 * a23 - a00 * a11 * a32 * a23 - a20 * a11 * a02 * a33 + a10 * a21 * a02 * a33 +
				a20 * a01 * a12 * a33 - a00 * a21 * a12 * a33 - a10 * a01 * a22 * a33 + a00 * a11 * a22 * a33;
	}

	public static float MatrixTrace(Matrix m) {
		return m.m0 + m.m5 + m.m10 + m.m15;
	}

	public static Matrix MatrixTranspose(Matrix mat) {
		Matrix result = new Matrix();

		result.m0 = mat.m0;
		result.m1 = mat.m4;
		result.m2 = mat.m8;
		result.m3 = mat.m12;
		result.m4 = mat.m1;
		result.m5 = mat.m5;
		result.m6 = mat.m9;
		result.m7 = mat.m13;
		result.m8 = mat.m2;
		result.m9 = mat.m6;
		result.m10 = mat.m10;
		result.m11 = mat.m14;
		result.m12 = mat.m3;
		result.m13 = mat.m7;
		result.m14 = mat.m11;
		result.m15 = mat.m15;

		return result;
	}

	public static Matrix MatrixInvert(Matrix mat) {
		Matrix result = new Matrix();

		// Cache the matrix values (speed optimization)
		float a00 = mat.m0, a01 = mat.m1, a02 = mat.m2, a03 = mat.m3;
		float a10 = mat.m4, a11 = mat.m5, a12 = mat.m6, a13 = mat.m7;
		float a20 = mat.m8, a21 = mat.m9, a22 = mat.m10, a23 = mat.m11;
		float a30 = mat.m12, a31 = mat.m13, a32 = mat.m14, a33 = mat.m15;

		float b00 = a00 * a11 - a01 * a10;
		float b01 = a00 * a12 - a02 * a10;
		float b02 = a00 * a13 - a03 * a10;
		float b03 = a01 * a12 - a02 * a11;
		float b04 = a01 * a13 - a03 * a11;
		float b05 = a02 * a13 - a03 * a12;
		float b06 = a20 * a31 - a21 * a30;
		float b07 = a20 * a32 - a22 * a30;
		float b08 = a20 * a33 - a23 * a30;
		float b09 = a21 * a32 - a22 * a31;
		float b10 = a21 * a33 - a23 * a31;
		float b11 = a22 * a33 - a23 * a32;

		// Calculate the invert determinant (inlined to avoid double-caching)
		float invDet = 1.0f / (b00 * b11 - b01 * b10 + b02 * b09 + b03 * b08 - b04 * b07 + b05 * b06);

		result.m0 = (a11 * b11 - a12 * b10 + a13 * b09) * invDet;
		result.m1 = (-a01 * b11 + a02 * b10 - a03 * b09) * invDet;
		result.m2 = (a31 * b05 - a32 * b04 + a33 * b03) * invDet;
		result.m3 = (-a21 * b05 + a22 * b04 - a23 * b03) * invDet;
		result.m4 = (-a10 * b11 + a12 * b08 - a13 * b07) * invDet;
		result.m5 = (a00 * b11 - a02 * b08 + a03 * b07) * invDet;
		result.m6 = (-a30 * b05 + a32 * b02 - a33 * b01) * invDet;
		result.m7 = (a20 * b05 - a22 * b02 + a23 * b01) * invDet;
		result.m8 = (a10 * b10 - a11 * b08 + a13 * b06) * invDet;
		result.m9 = (-a00 * b10 + a01 * b08 - a03 * b06) * invDet;
		result.m10 = (a30 * b04 - a31 * b02 + a33 * b00) * invDet;
		result.m11 = (-a20 * b04 + a21 * b02 - a23 * b00) * invDet;
		result.m12 = (-a10 * b09 + a11 * b07 - a12 * b06) * invDet;
		result.m13 = (a00 * b09 - a01 * b07 + a02 * b06) * invDet;
		result.m14 = (-a30 * b03 + a31 * b01 - a32 * b00) * invDet;
		result.m15 = (a20 * b03 - a21 * b01 + a22 * b00) * invDet;

		return result;
	}

	public static Matrix MatrixNormalize(Matrix mat) {
		Matrix result = new Matrix();

		float det = MatrixDeterminant(mat);

		result.m0 = mat.m0 / det;
		result.m1 = mat.m1 / det;
		result.m2 = mat.m2 / det;
		result.m3 = mat.m3 / det;
		result.m4 = mat.m4 / det;
		result.m5 = mat.m5 / det;
		result.m6 = mat.m6 / det;
		result.m7 = mat.m7 / det;
		result.m8 = mat.m8 / det;
		result.m9 = mat.m9 / det;
		result.m10 = mat.m10 / det;
		result.m11 = mat.m11 / det;
		result.m12 = mat.m12 / det;
		result.m13 = mat.m13 / det;
		result.m14 = mat.m14 / det;
		result.m15 = mat.m15 / det;

		return result;
	}

	public static Matrix MatrixIdentity() {
		return new Matrix(
				1.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f);
	}

	public static Matrix MatrixAdd(Matrix left, Matrix right) {
		Matrix result = MatrixIdentity();

		result.m0 = left.m0 + right.m0;
		result.m1 = left.m1 + right.m1;
		result.m2 = left.m2 + right.m2;
		result.m3 = left.m3 + right.m3;
		result.m4 = left.m4 + right.m4;
		result.m5 = left.m5 + right.m5;
		result.m6 = left.m6 + right.m6;
		result.m7 = left.m7 + right.m7;
		result.m8 = left.m8 + right.m8;
		result.m9 = left.m9 + right.m9;
		result.m10 = left.m10 + right.m10;
		result.m11 = left.m11 + right.m11;
		result.m12 = left.m12 + right.m12;
		result.m13 = left.m13 + right.m13;
		result.m14 = left.m14 + right.m14;
		result.m15 = left.m15 + right.m15;

		return result;
	}

	public static Matrix MatrixSubtract(Matrix left, Matrix right) {
		Matrix result = MatrixIdentity();

		result.m0 = left.m0 - right.m0;
		result.m1 = left.m1 - right.m1;
		result.m2 = left.m2 - right.m2;
		result.m3 = left.m3 - right.m3;
		result.m4 = left.m4 - right.m4;
		result.m5 = left.m5 - right.m5;
		result.m6 = left.m6 - right.m6;
		result.m7 = left.m7 - right.m7;
		result.m8 = left.m8 - right.m8;
		result.m9 = left.m9 - right.m9;
		result.m10 = left.m10 - right.m10;
		result.m11 = left.m11 - right.m11;
		result.m12 = left.m12 - right.m12;
		result.m13 = left.m13 - right.m13;
		result.m14 = left.m14 - right.m14;
		result.m15 = left.m15 - right.m15;

		return result;
	}

	public static float[] MatrixToFloat(Matrix mat) {
		return MatrixToFloatV(mat).v;
	}

	public static Float16 MatrixToFloatV(Matrix mat) {
		Float16 buffer = new Float16();

		buffer.v[0] = mat.m0;
		buffer.v[1] = mat.m1;
		buffer.v[2] = mat.m2;
		buffer.v[3] = mat.m3;
		buffer.v[4] = mat.m4;
		buffer.v[5] = mat.m5;
		buffer.v[6] = mat.m6;
		buffer.v[7] = mat.m7;
		buffer.v[8] = mat.m8;
		buffer.v[9] = mat.m9;
		buffer.v[10] = mat.m10;
		buffer.v[11] = mat.m11;
		buffer.v[12] = mat.m12;
		buffer.v[13] = mat.m13;
		buffer.v[14] = mat.m14;
		buffer.v[15] = mat.m15;

		return buffer;
	}

	public static Matrix MatrixMultiply(Matrix left, Matrix right) {
		Matrix result = new Matrix();

		result.m0 = left.m0 * right.m0 + left.m1 * right.m4 + left.m2 * right.m8 + left.m3 * right.m12;
		result.m1 = left.m0 * right.m1 + left.m1 * right.m5 + left.m2 * right.m9 + left.m3 * right.m13;
		result.m2 = left.m0 * right.m2 + left.m1 * right.m6 + left.m2 * right.m10 + left.m3 * right.m14;
		result.m3 = left.m0 * right.m3 + left.m1 * right.m7 + left.m2 * right.m11 + left.m3 * right.m15;
		result.m4 = left.m4 * right.m0 + left.m5 * right.m4 + left.m6 * right.m8 + left.m7 * right.m12;
		result.m5 = left.m4 * right.m1 + left.m5 * right.m5 + left.m6 * right.m9 + left.m7 * right.m13;
		result.m6 = left.m4 * right.m2 + left.m5 * right.m6 + left.m6 * right.m10 + left.m7 * right.m14;
		result.m7 = left.m4 * right.m3 + left.m5 * right.m7 + left.m6 * right.m11 + left.m7 * right.m15;
		result.m8 = left.m8 * right.m0 + left.m9 * right.m4 + left.m10 * right.m8 + left.m11 * right.m12;
		result.m9 = left.m8 * right.m1 + left.m9 * right.m5 + left.m10 * right.m9 + left.m11 * right.m13;
		result.m10 = left.m8 * right.m2 + left.m9 * right.m6 + left.m10 * right.m10 + left.m11 * right.m14;
		result.m11 = left.m8 * right.m3 + left.m9 * right.m7 + left.m10 * right.m11 + left.m11 * right.m15;
		result.m12 = left.m12 * right.m0 + left.m13 * right.m4 + left.m14 * right.m8 + left.m15 * right.m12;
		result.m13 = left.m12 * right.m1 + left.m13 * right.m5 + left.m14 * right.m9 + left.m15 * right.m13;
		result.m14 = left.m12 * right.m2 + left.m13 * right.m6 + left.m14 * right.m10 + left.m15 * right.m14;
		result.m15 = left.m12 * right.m3 + left.m13 * right.m7 + left.m14 * right.m11 + left.m15 * right.m15;

		return result;
	}

	public static Matrix MatrixTranslate(float x, float y, float z) {
		return new Matrix(
				1.0f, 0.0f, 0.0f, x,
				0.0f, 1.0f, 0.0f, y,
				0.0f, 0.0f, 1.0f, z,
				0.0f, 0.0f, 0.0f, 1.0f);
	}

	public static Matrix MatrixRotate(float x, float y, float z, float angle) {
		Matrix result = new Matrix();

		float lengthSquared = x * x + y * y + z * z;

		if ((lengthSquared != 1.0f) && (lengthSquared != 0.0f)) {
			float ilength = (float) (1.0f / Math.sqrt(lengthSquared));
			x *= ilength;
			y *= ilength;
			z *= ilength;
		}

		float sinres = (float) Math.sin(angle);
		float cosres = (float) Math.cos(angle);
		float t = 1.0f - cosres;

		result.m0 = x * x * t + cosres;
		result.m1 = y * x * t + z * sinres;
		result.m2 = z * x * t - y * sinres;
		result.m3 = 0.0f;

		result.m4 = x * y * t - z * sinres;
		result.m5 = y * y * t + cosres;
		result.m6 = z * y * t + x * sinres;
		result.m7 = 0.0f;

		result.m8 = x * z * t + y * sinres;
		result.m9 = y * z * t - x * sinres;
		result.m10 = z * z * t + cosres;
		result.m11 = 0.0f;

		result.m12 = 0.0f;
		result.m13 = 0.0f;
		result.m14 = 0.0f;
		result.m15 = 1.0f;

		return result;
	}

	public static Matrix MatrixScale(float x, float y, float z) {
		return new Matrix(
				x, 0.0f, 0.0f, 0.0f,
				0.0f, y, 0.0f, 0.0f,
				0.0f, 0.0f, z, 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f);
	}

}
