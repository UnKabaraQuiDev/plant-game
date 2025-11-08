#version {version}
precision mediump float;

layout (location = 0) in vec3 i_pos;
layout (location = 1) in vec3 i_norm;
layout (location = 2) in vec2 i_uv;
layout (location = 3) in mat4 i_transform;
layout (location = 7) in uint i_char;

flat out struct per_instance {
	int index;
	uint character;
} instance;

out struct per_Vertex {
	vec3 worldpos;
	vec2 fragpos;
	vec2 uv;
	vec3 normal;
} vertex;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 viewPos;
uniform bool transparent;

void main() {
	gl_Position = projectionMatrix * viewMatrix * (transformationMatrix * i_transform) * vec4(i_pos, 1);

	instance.index = gl_InstanceID;
	instance.character = i_char;

	vertex.worldpos = i_pos;
	vertex.fragpos = gl_Position.xy;
	vertex.uv = i_uv;
	vertex.normal = i_norm;
}
