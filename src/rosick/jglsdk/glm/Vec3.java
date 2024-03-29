package rosick.jglsdk.glm;

import java.nio.FloatBuffer;

import rosick.jglsdk.BufferableData;


/**
 * Visit https://github.com/integeruser/jglsdk for project info, updates and license terms.
 * 
 * @author integeruser
 */
public class Vec3 extends BufferableData<FloatBuffer> {
	public static final int SIZE = (3 * Float.SIZE) / Byte.SIZE;
	
	
	public float x, y, z;
	
	
	public Vec3() {
	}
	
	public Vec3(float f) {
		x = f;
		y = f;
		z = f;
	}
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3(Vec3 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}
	
	public Vec3(Vec4 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;	
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	
	

	@Override
	public FloatBuffer fillBuffer(FloatBuffer buffer) {
		buffer.put(x);
		buffer.put(y);
		buffer.put(z);

		return buffer;
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	

	public Vec3 add(Vec3 rhs) {
		x += rhs.x;
		y += rhs.y;
		z += rhs.z;
		
		return this;
	}
	
	public Vec3 sub(Vec3 rhs) {
		x -= rhs.x;
		y -= rhs.y;
		z -= rhs.z;
		
		return this;
	}
	
	public Vec3 mul(Vec3 rhs) {
		x *= rhs.x;
		y *= rhs.y;
		z *= rhs.z;
		
		return this;
	}
	
	
	public Vec3 scale(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		
		return this;
	}
	

	public Vec3 negate() {
		x = -x;
		y = -y;
		z = -z;

		return this;
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	
	
	public static Vec3 add(Vec3 lhs, Vec3 rhs) {
		Vec3 res = new Vec3(lhs);
		
		return res.add(rhs);
	}
	
	public static Vec3 sub(Vec3 lhs, Vec3 rhs) {
		Vec3 res = new Vec3(lhs);

		return res.sub(rhs);
	}	
	
	public static Vec3 mul(Vec3 lhs, Vec3 rhs) {	
		Vec3 res = new Vec3(lhs);
		
		return res.mul(rhs);
	}
	
	
	public static Vec3 scale(Vec3 vec, float scalar) {
		Vec3 res = new Vec3(vec);
		
		return res.scale(scalar);
	}
	

	public static Vec3 negate(Vec3 vec) {
		Vec3 res = new Vec3(vec);
		
		return res.negate();
	}
	
	public static boolean roughEqual(Vec3 vec1, Vec3 vec2, float bounds) {		
		
		boolean x = Math.abs(vec1.x/vec2.x - 1) < bounds;
		boolean y = Math.abs(vec1.y/vec2.y - 1) < bounds;
		boolean z = Math.abs(vec1.z/vec2.z - 1) < bounds;
		
		return x && y && z;
		
	}
	
	/////////////////////////
	
	public float getMagnitude() {	
		return (float)Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2));
	}
	
	public Vec3 clone() {
		return new Vec3(x,y,z);
	}
	
	public void normalise() {
		scale(1/getMagnitude());
	}
}