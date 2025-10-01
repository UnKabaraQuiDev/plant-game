#version {version}
precision mediump float;

in struct per_vertex {
	vec2 uv;
	vec3 normal;
	vec2 frag;
	vec3 pos;
} vertex;

out vec4 fragColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 viewPos;

uniform sampler2D txt1;

void main() {
	fragColor = texture(txt1, vertex.uv+viewPos.xy/60.0);
}
