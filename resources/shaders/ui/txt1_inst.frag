#version {version}
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;
flat in int index;
in float hover;

out vec4 fragColor;

uniform sampler2D txt1;

void main() {
	fragColor = texture(txt1, texCoord)+vec4(0, 1, 0, 1)*hover;
}
