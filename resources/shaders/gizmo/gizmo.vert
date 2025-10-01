#version {version}
precision mediump float;

layout (location=0) in vec3 positions;
layout (location=1) in vec4 colors;

out vec3 fragPos;
out vec4 color;
out vec3 camPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 viewPos;

void main()
{
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(positions, 1.0);
	fragPos = (transformationMatrix * vec4(positions, 1.0)).xyz;
	color = colors;
	camPos = viewPos;
}
