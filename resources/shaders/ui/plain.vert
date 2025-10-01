#version {version}
precision mediump float;

layout (location = 0) in vec3 i_pos;
layout (location = 1) in vec3 i_norm;
layout (location = 2) in vec2 i_uv;

out vec2 texCoord;
out vec3 normal;
out vec2 fragPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main() {
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(i_pos, 1.0);
	fragPos = i_pos.xy;
	texCoord = i_uv;
	normal = i_norm;
}
