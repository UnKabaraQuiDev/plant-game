#version {version}
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;
flat in int index;
flat in int state;

out vec4 fragColor;

uniform sampler2D txt1;
uniform int columns; 

void main() {
	float colIndex = float(index % columns);
	
	float colWidth = (1.0/1.0);
	
	fragColor = texture(txt1, mix(vec2(colWidth*colIndex, 0.0), vec2(colWidth*(colIndex+1.0), 1.0), texCoord));
	
	if(fragColor.a == 0.0) {
		discard;
	}
}
