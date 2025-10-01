#version {version}
precision mediump float;

layout (location = 0) in vec3 i_pos;
layout (location = 1) in vec3 i_norm;
layout (location = 2) in vec2 i_uv;

out vec2 texCoord;
out vec3 normal;
out vec2 fragPos;
flat out int applyIcon;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

uniform float progress;

#define START_1 vec2(0.068, 0.252)
#define END_1 vec2(0.078, 0.308)

#define SWIPE_END 0.026

void main() {
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(i_pos, 1.0);
	fragPos = i_pos.xy;
	applyIcon = 0;
	if(i_uv.x > START_1.x && i_uv.y > START_1.y && i_uv.x < END_1.x && i_uv.y < END_1.y) {
		texCoord = i_uv - vec2(SWIPE_END, 0.0) * progress;
	}else {
		texCoord = i_uv;
		applyIcon = 1;
	}
	normal = i_norm;
}