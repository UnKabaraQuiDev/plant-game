#version {version}
precision mediump float;

in struct per_vertex {
	vec2 uv;
	vec3 normal;
	vec2 frag;
	vec3 pos;
} vertex;

out vec4 fragColor;

uniform sampler2D color;

void main() {
	fragColor = texture(color, vertex.uv);
}
