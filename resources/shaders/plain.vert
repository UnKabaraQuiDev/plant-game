#version {version}
precision mediump float;

layout (location = 0) in vec3 i_pos;
layout (location = 1) in vec3 i_norm;
layout (location = 2) in vec2 i_uv;

out struct per_vertex {
	vec2 uv;
	vec3 normal;
	vec2 frag;
	vec3 pos;
} vertex;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 viewPos;

void main() {
	vec4 worldPosition = transformationMatrix * vec4(i_pos, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;

	vertex.frag = i_pos.xy;
	vertex.uv = i_uv;
	vertex.normal = i_norm;
	vertex.pos = worldPosition.xyz;
}
