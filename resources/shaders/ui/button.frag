#version {version}
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;

out vec4 fragColor;

uniform sampler2D txt1;

void main() {
	fragColor = texture(txt1, texCoord);
}
