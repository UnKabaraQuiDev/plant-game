package lu.kbra.plant_game.engine.data.json;

import java.io.IOException;
import java.util.List;

import org.joml.Matrix3d;
import org.joml.Matrix3f;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Matrix4x3d;
import org.joml.Matrix4x3f;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4d;
import org.joml.Vector4f;
import org.joml.Vector4i;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class OrgJOMLModule extends SimpleModule {

	public OrgJOMLModule() {
		super("OrgJOMLModule");

		// Vectors
		this.addSerializer(Vector2i.class, new Vector2iSerializer());
		this.addSerializer(Vector3i.class, new Vector3iSerializer());
		this.addSerializer(Vector4i.class, new Vector4iSerializer());
		this.addSerializer(Vector2f.class, new Vector2fSerializer());
		this.addSerializer(Vector3f.class, new Vector3fSerializer());
		this.addSerializer(Vector4f.class, new Vector4fSerializer());
		this.addSerializer(Vector2d.class, new Vector2dSerializer());
		this.addSerializer(Vector3d.class, new Vector3dSerializer());
		this.addSerializer(Vector4d.class, new Vector4dSerializer());

		// Quaternions
		this.addSerializer(Quaternionf.class, new QuaternionfSerializer());
		this.addSerializer(Quaterniond.class, new QuaterniondSerializer());

		// Matrices
		this.addSerializer(Matrix3f.class, new Matrix3fSerializer());
		this.addSerializer(Matrix3d.class, new Matrix3dSerializer());
		this.addSerializer(Matrix4f.class, new Matrix4fSerializer());
		this.addSerializer(Matrix4d.class, new Matrix4dSerializer());
		this.addSerializer(Matrix4x3f.class, new Matrix4x3fSerializer());
		this.addSerializer(Matrix4x3d.class, new Matrix4x3dSerializer());

		// Vectors
		this.addDeserializer(Vector2i.class, new Vector2iDeserializer());
		this.addDeserializer(Vector3i.class, new Vector3iDeserializer());
		this.addDeserializer(Vector4i.class, new Vector4iDeserializer());
		this.addDeserializer(Vector2f.class, new Vector2fDeserializer());
		this.addDeserializer(Vector3f.class, new Vector3fDeserializer());
		this.addDeserializer(Vector4f.class, new Vector4fDeserializer());
		this.addDeserializer(Vector2d.class, new Vector2dDeserializer());
		this.addDeserializer(Vector3d.class, new Vector3dDeserializer());
		this.addDeserializer(Vector4d.class, new Vector4dDeserializer());

		// Quaternions
		this.addDeserializer(Quaternionf.class, new QuaternionfDeserializer());
		this.addDeserializer(Quaterniond.class, new QuaterniondDeserializer());

		// Matrices
		this.addDeserializer(Matrix3f.class, new Matrix3fDeserializer());
		this.addDeserializer(Matrix3d.class, new Matrix3dDeserializer());
		this.addDeserializer(Matrix4f.class, new Matrix4fDeserializer());
		this.addDeserializer(Matrix4d.class, new Matrix4dDeserializer());
		this.addDeserializer(Matrix4x3f.class, new Matrix4x3fDeserializer());
		this.addDeserializer(Matrix4x3d.class, new Matrix4x3dDeserializer());
	}

	/* -- SERIALIZERS -- */
	// ------------------ VECTORS ------------------

	public class Vector2iSerializer extends JsonSerializer<Vector2i> {
		@Override
		public void serialize(final Vector2i v, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(v.x());
			gen.writeNumber(v.y());
			gen.writeEndArray();
		}
	}

	public class Vector3iSerializer extends JsonSerializer<Vector3i> {
		@Override
		public void serialize(final Vector3i v, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(v.x());
			gen.writeNumber(v.y());
			gen.writeNumber(v.z());
			gen.writeEndArray();
		}
	}

	public class Vector4iSerializer extends JsonSerializer<Vector4i> {
		@Override
		public void serialize(final Vector4i v, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(v.x());
			gen.writeNumber(v.y());
			gen.writeNumber(v.z());
			gen.writeNumber(v.w());
			gen.writeEndArray();
		}
	}

	public class Vector2fSerializer extends JsonSerializer<Vector2f> {
		@Override
		public void serialize(final Vector2f v, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(v.x());
			gen.writeNumber(v.y());
			gen.writeEndArray();
		}
	}

	public class Vector3fSerializer extends JsonSerializer<Vector3f> {
		@Override
		public void serialize(final Vector3f v, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(v.x());
			gen.writeNumber(v.y());
			gen.writeNumber(v.z());
			gen.writeEndArray();
		}
	}

	public class Vector4fSerializer extends JsonSerializer<Vector4f> {
		@Override
		public void serialize(final Vector4f v, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(v.x());
			gen.writeNumber(v.y());
			gen.writeNumber(v.z());
			gen.writeNumber(v.w());
			gen.writeEndArray();
		}
	}

	public class Vector2dSerializer extends JsonSerializer<Vector2d> {
		@Override
		public void serialize(final Vector2d v, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(v.x());
			gen.writeNumber(v.y());
			gen.writeEndArray();
		}
	}

	public class Vector3dSerializer extends JsonSerializer<Vector3d> {
		@Override
		public void serialize(final Vector3d v, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(v.x());
			gen.writeNumber(v.y());
			gen.writeNumber(v.z());
			gen.writeEndArray();
		}
	}

	public class Vector4dSerializer extends JsonSerializer<Vector4d> {
		@Override
		public void serialize(final Vector4d v, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(v.x());
			gen.writeNumber(v.y());
			gen.writeNumber(v.z());
			gen.writeNumber(v.w());
			gen.writeEndArray();
		}
	}

	// ------------------ QUATERNIONS ------------------

	public class QuaternionfSerializer extends JsonSerializer<Quaternionf> {
		@Override
		public void serialize(final Quaternionf q, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(q.x());
			gen.writeNumber(q.y());
			gen.writeNumber(q.z());
			gen.writeNumber(q.w());
			gen.writeEndArray();
		}
	}

	public class QuaterniondSerializer extends JsonSerializer<Quaterniond> {
		@Override
		public void serialize(final Quaterniond q, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartArray();
			gen.writeNumber(q.x());
			gen.writeNumber(q.y());
			gen.writeNumber(q.z());
			gen.writeNumber(q.w());
			gen.writeEndArray();
		}
	}

	// ------------------ MATRICES ------------------

	public class Matrix3fSerializer extends JsonSerializer<Matrix3f> {
		@Override
		public void serialize(final Matrix3f m, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			final float[] tmp = new float[9];
			m.get(tmp);
			gen.writeStartArray();
			for (final float f : tmp) {
				gen.writeNumber(f);
			}
			gen.writeEndArray();
		}
	}

	public class Matrix3dSerializer extends JsonSerializer<Matrix3d> {
		@Override
		public void serialize(final Matrix3d m, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			final double[] tmp = new double[9];
			m.get(tmp);
			gen.writeStartArray();
			for (final double d : tmp) {
				gen.writeNumber(d);
			}
			gen.writeEndArray();
		}
	}

	public class Matrix4x3fSerializer extends JsonSerializer<Matrix4x3f> {
		@Override
		public void serialize(final Matrix4x3f m, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			final float[] tmp = new float[12];
			m.get(tmp);
			gen.writeStartArray();
			for (final float f : tmp) {
				gen.writeNumber(f);
			}
			gen.writeEndArray();
		}
	}

	public class Matrix4x3dSerializer extends JsonSerializer<Matrix4x3d> {
		@Override
		public void serialize(final Matrix4x3d m, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			final double[] tmp = new double[12];
			m.get(tmp);
			gen.writeStartArray();
			for (final double d : tmp) {
				gen.writeNumber(d);
			}
			gen.writeEndArray();
		}
	}

	public class Matrix4fSerializer extends JsonSerializer<Matrix4f> {
		@Override
		public void serialize(final Matrix4f m, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			final float[] tmp = new float[16];
			m.get(tmp);
			gen.writeStartArray();
			for (final float f : tmp) {
				gen.writeNumber(f);
			}
			gen.writeEndArray();
		}
	}

	public class Matrix4dSerializer extends JsonSerializer<Matrix4d> {
		@Override
		public void serialize(final Matrix4d m, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			final double[] tmp = new double[16];
			m.get(tmp);
			gen.writeStartArray();
			for (final double d : tmp) {
				gen.writeNumber(d);
			}
			gen.writeEndArray();
		}
	}

	/* -- DESERIALIZERS -- */
	// ----------------- INTEGER VECTORS -----------------

	public class Vector2iDeserializer extends JsonDeserializer<Vector2i> {
		@Override
		public Vector2i deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Integer> arr = p.readValueAs(List.class);
			return new Vector2i(arr.get(0), arr.get(1));
		}
	}

	public class Vector3iDeserializer extends JsonDeserializer<Vector3i> {
		@Override
		public Vector3i deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Integer> arr = p.readValueAs(List.class);
			return new Vector3i(arr.get(0), arr.get(1), arr.get(2));
		}
	}

	public class Vector4iDeserializer extends JsonDeserializer<Vector4i> {
		@Override
		public Vector4i deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Integer> arr = p.readValueAs(List.class);
			return new Vector4i(arr.get(0), arr.get(1), arr.get(2), arr.get(3));
		}
	}

	// ----------------- FLOAT VECTORS -----------------
	public class Vector2fDeserializer extends JsonDeserializer<Vector2f> {
		@Override
		public Vector2f deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			return new Vector2f(arr.get(0).floatValue(), arr.get(1).floatValue());
		}
	}

	public class Vector3fDeserializer extends JsonDeserializer<Vector3f> {
		@Override
		public Vector3f deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			return new Vector3f(arr.get(0).floatValue(), arr.get(1).floatValue(), arr.get(2).floatValue());
		}
	}

	public class Vector4fDeserializer extends JsonDeserializer<Vector4f> {
		@Override
		public Vector4f deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			return new Vector4f(arr.get(0).floatValue(), arr.get(1).floatValue(), arr.get(2).floatValue(), arr.get(3).floatValue());
		}
	}

	// ----------------- DOUBLE VECTORS -----------------
	public class Vector2dDeserializer extends JsonDeserializer<Vector2d> {
		@Override
		public Vector2d deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			return new Vector2d(arr.get(0), arr.get(1));
		}
	}

	public class Vector3dDeserializer extends JsonDeserializer<Vector3d> {
		@Override
		public Vector3d deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			return new Vector3d(arr.get(0), arr.get(1), arr.get(2));
		}
	}

	public class Vector4dDeserializer extends JsonDeserializer<Vector4d> {
		@Override
		public Vector4d deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			return new Vector4d(arr.get(0), arr.get(1), arr.get(2), arr.get(3));
		}
	}

	// ----------------- QUATERNIONS -----------------
	public class QuaternionfDeserializer extends JsonDeserializer<Quaternionf> {
		@Override
		public Quaternionf deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			return new Quaternionf(arr.get(0).floatValue(), arr.get(1).floatValue(), arr.get(2).floatValue(), arr.get(3).floatValue());
		}
	}

	public class QuaterniondDeserializer extends JsonDeserializer<Quaterniond> {
		@Override
		public Quaterniond deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			return new Quaterniond(arr.get(0), arr.get(1), arr.get(2), arr.get(3));
		}
	}

	// ----------------- MATRICES -----------------
	public class Matrix3fDeserializer extends JsonDeserializer<Matrix3f> {
		@Override
		public Matrix3f deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			final float[] tmp = new float[9];
			for (int i = 0; i < 9; i++) {
				tmp[i] = arr.get(i).floatValue();
			}
			final Matrix3f m = new Matrix3f();
			m.set(tmp);
			return m;
		}
	}

	public class Matrix3dDeserializer extends JsonDeserializer<Matrix3d> {
		@Override
		public Matrix3d deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			final double[] tmp = new double[9];
			for (int i = 0; i < 9; i++) {
				tmp[i] = arr.get(i);
			}
			final Matrix3d m = new Matrix3d();
			m.set(tmp);
			return m;
		}
	}

	public class Matrix4fDeserializer extends JsonDeserializer<Matrix4f> {
		@Override
		public Matrix4f deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			final float[] tmp = new float[16];
			for (int i = 0; i < 16; i++) {
				tmp[i] = arr.get(i).floatValue();
			}
			final Matrix4f m = new Matrix4f();
			m.set(tmp);
			return m;
		}
	}

	public class Matrix4dDeserializer extends JsonDeserializer<Matrix4d> {
		@Override
		public Matrix4d deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			final double[] tmp = new double[16];
			for (int i = 0; i < 16; i++) {
				tmp[i] = arr.get(i);
			}
			final Matrix4d m = new Matrix4d();
			m.set(tmp);
			return m;
		}
	}

	public class Matrix4x3fDeserializer extends JsonDeserializer<Matrix4x3f> {
		@Override
		public Matrix4x3f deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			final float[] tmp = new float[12];
			for (int i = 0; i < 12; i++) {
				tmp[i] = arr.get(i).floatValue();
			}
			final Matrix4x3f m = new Matrix4x3f();
			m.set(tmp);
			return m;
		}
	}

	public class Matrix4x3dDeserializer extends JsonDeserializer<Matrix4x3d> {
		@Override
		public Matrix4x3d deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<Double> arr = p.readValueAs(List.class);
			final double[] tmp = new double[12];
			for (int i = 0; i < 12; i++) {
				tmp[i] = arr.get(i);
			}
			final Matrix4x3d m = new Matrix4x3d();
			m.set(tmp);
			return m;
		}
	}

}
