#version {version}
precision mediump float;

layout (location = 0) in vec3 i_pos;
layout (location = 1) in vec3 i_norm;
layout (location = 2) in vec2 i_uv;
layout (location = 3) in mat4 i_transform;
layout (location = 7) in int i_state;

out vec2 texCoord;
out vec3 normal;
out vec2 fragPos;
flat out int index;
flat out int state;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main() {
	gl_Position = projectionMatrix * viewMatrix * (transformationMatrix * i_transform) * vec4(i_pos, 1.0);

	fragPos = gl_Position.xy;
	texCoord = i_uv;
	normal = i_norm;
	index = gl_InstanceID;
	state = i_state;
}
