package lu.kbra.plant_game.engine.scene.ui.overlay;

import org.joml.Matrix3f;
import org.joml.Matrix3fc;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.standalone.gameengine.utils.geo.GeoAxis;
import lu.kbra.standalone.gameengine.utils.transform.Transform;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class Transform3DShear extends Transform3D implements ShearOwner {

	protected Matrix3f shear;

	public Transform3DShear() {
		this(new Vector3f(0), new Quaternionf().identity(), new Vector3f(1), new Matrix3f().identity());
	}

	public Transform3DShear(final Vector3fc translation, final Quaternionfc rotation, final Vector3fc scale, final Matrix3fc shear) {
		super(true);
		super.translation = new Vector3f(translation);
		super.rotation = new Quaternionf(rotation);
		super.scale = new Vector3f(scale);
		this.shear = new Matrix3f(shear);
		this.updateMatrix();
	}

	public Transform3DShear(final Vector3f translation, final Quaternionf rotation, final Vector3f scale, final Matrix3f shear) {
		super(true);
		super.translation = translation;
		super.rotation = rotation;
		super.scale = scale;
		this.shear = shear;
		this.updateMatrix();
	}

	public Transform3DShear(final Vector3fc translation) {
		this(new Vector3f(translation), new Quaternionf().identity(), new Vector3f(1), new Matrix3f().identity());
	}

	public Transform3DShear(final Vector3fc trans, final Quaternionfc rot) {
		super(trans, rot);
	}

	public Transform3DShear(final Quaternionf rot) {
		super(rot);
	}

	public Transform3DShear(final Quaternionfc rot) {
		super(rot);
	}

	public Transform3DShear(final Vector3f trans, final Quaternionf rot, final Vector3f scale) {
		super(trans, rot, scale);
	}

	public Transform3DShear(final Vector3f trans, final Quaternionf rot) {
		super(trans, rot);
	}

	public Transform3DShear(final Vector3f trans) {
		super(trans);
	}

	public Transform3DShear(final Vector3fc trans, final Quaternionfc rot, final Vector3fc scale) {
		super(trans, rot, scale);
	}

	protected Transform3DShear(final boolean _noAction) {
		super(_noAction);
	}

	/* chaining methods for shear */

	@Override
	public Transform3DShear shearSet(final GeoAxis targetAxis, final GeoAxis sourceAxis, final float factor) {
		this.shear.set(targetAxis.getIndex(), sourceAxis.getIndex(), factor);
		return this;
	}

	@Override
	public Transform3DShear shearAdd(final GeoAxis targetAxis, final GeoAxis sourceAxis, final float factor) {
		this.shear.set(targetAxis.getIndex(), sourceAxis.getIndex(), this.shear.get(targetAxis.getIndex(), sourceAxis.getIndex()) + factor);
		return this;
	}

	@Override
	public Transform3DShear setShear(final Matrix3f shear) {
		this.shear = shear;
		return this;
	}

	@Override
	public Transform3DShear shearReset() {
		this.shear.identity();
		return this;
	}

	@Override
	public Matrix3f getShear() {
		return this.shear;
	}

	@Override
	public Matrix4f updateMatrix() {
		this.matrix.identity();

		this.matrix.translate(this.translation);

		this.matrix.rotate(this.rotation);

		this.matrix.mulAffine(this.getShearMatrix());

		this.matrix.scale(this.scale);

		return this.matrix;
	}

	@Override
	public Transform3DShear update() {
		this.updateMatrix();
		return this;
	}

	@Override
	public Matrix4f getShearMatrix() {
		return new Matrix4f(
				this.shear.m00(),
				this.shear.m01(),
				this.shear.m02(),
				0f,
				this.shear.m10(),
				this.shear.m11(),
				this.shear.m12(),
				0f,
				this.shear.m20(),
				this.shear.m21(),
				this.shear.m22(),
				0f,
				0f,
				0f,
				0f,
				1f);
	}

	@Override
	public Transform clone() {
		return new Transform3DShear(
				this.translation.get(new Vector3f()),
				this.rotation.get(new Quaternionf()),
				this.scale.get(new Vector3f()),
				this.shear.get(new Matrix3f()));
	}

	@Override
	public String toString() {
		return "Transform3DShear [translation=" + this.translation + ", rotation=" + this.rotation + ", scale=" + this.scale + ", shear="
				+ this.shear + "]";
	}
}
