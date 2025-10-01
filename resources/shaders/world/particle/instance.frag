#version {version}
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;
flat in int index;

out vec4 fragColor;

uniform sampler2D txt1;
uniform int columns; 
uniform int rows;
uniform float opacity; // 0.50

void main() {
	float rcolumns = float(columns);
	float rrows = float(rows);
	float rindex = float(index);

	float rowIndex = float(index % (columns*rows)) / rcolumns;
	float colIndex = float(index % (columns*rows) % columns);
	
	float colWidth = 1.0/float(rcolumns);
	float rowHeight = 1.0/float(rrows);
	
	fragColor = texture(txt1, mix(vec2(colWidth*colIndex, rowHeight*rowIndex), vec2(colWidth*(colIndex+1.0), rowHeight*(rowIndex+1.0)), texCoord)) - vec4(vec3(0.0), distance(texCoord, vec2(0.5, 0.5)))- vec4(vec3(0.0), 1.0-opacity);
	
	if(fragColor.a == 0.0) {
		discard;
	}
}
