#version {version}

in vec2 texCoord;
in vec2 fragPos;

out vec4 fragColor;

uniform sampler2D txt0;

layout(location = 0) out vec4 out_FragColor;

void main() {
	out_FragColor = texture(txt0, texCoord);
}
