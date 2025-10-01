#version {version}
precision mediump float;

layout (location = 0) in vec3 i_pos;
layout (location = 1) in vec3 i_norm;
layout (location = 2) in vec2 i_uv;
layout (location = 3) in mat4 i_transform;
layout (location = 7) in float i_scale;

out vec2 texCoord;
out vec3 normal;
out vec2 fragPos;
flat out int index;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

// https://www.shadertoy.com/view/ttc3zr
uvec3 murmurHash33(uvec3 src) {
	const uint M = 0x5bd1e995u;
	uvec3 h = uvec3(1190494759u, 2147483647u, 3559788179u);
	src *= M; src ^= src>>24u; src *= M;
	h *= M; h ^= src.x; h *= M; h ^= src.y; h *= M; h ^= src.z;
	h ^= h>>13u; h *= M; h ^= h>>15u;
	return h;
}
vec3 hash33(vec3 src) {
	uvec3 h = murmurHash33(floatBitsToUint(src));
	return uintBitsToFloat(h & 0x000ffffu | 0x00800000u) - 1.0;
}


void main() {
	float angle = hash33(vec3(float(i_scale*10.0))).x;
	
	float c = cos(angle);
	float s = sin(angle);
	mat3 rotationMatrix = mat3(
		c, -s, 0.0,
		s, c, 0.0,
		0.0, 0.0, 1.0
	);

	gl_Position = projectionMatrix * viewMatrix * (transformationMatrix * i_transform) * vec4(rotationMatrix * (i_pos * i_scale), 1.0);

	fragPos = gl_Position.xy;
	texCoord = i_uv;
	normal = i_norm;
	index = gl_InstanceID;
}
