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

uniform float progress;
uniform vec2 meshSize;
uniform vec2 barSize; // fraction
uniform vec2 sliderSize;  // fraction
uniform vec4 firstColor;
uniform vec4 lastColor;
uniform vec4 fgColor;
uniform vec4 bgColor;
uniform float borderThickness;
uniform float scaleFactor;

#define cornerRadius 0.05

float box(vec2 position, vec2 halfSize, float corner) {
   position = abs(position) - halfSize + corner;
   return length(max(position, 0.0)) + min(max(position.x, position.y), 0.0) - corner;
}

void main() {
	vec2 uv = (vertex.uv-vec2(0.5, 0.5))*meshSize*scaleFactor;
	
	float barSDF = box(uv, meshSize*barSize/2, cornerRadius);
	
	if(abs(barSDF) < borderThickness) {
		fragColor = vec4(0, 0, 0, 1);
	}else if(barSDF > 0) {
		fragColor = bgColor;
	}else {
		if(((vertex.uv-vec2(0.5, 0.5))*scaleFactor+vec2(0.5, 0.5)).x > progress) {
			fragColor = lastColor;
		}else {
			fragColor = firstColor;
		}
	}
	
	float sliderSDF = box(uv-vec2(mix(-meshSize.x/2, meshSize.x/2, progress), 0), meshSize*sliderSize/2, cornerRadius/2);
	
	if(abs(sliderSDF) < borderThickness) {
		fragColor = vec4(0, 0, 0, 1);
	}else if(sliderSDF > 0) {
		// pass
	}else {
		fragColor = fgColor;
	}
	
	
	// fragColor = vec4(box(vertex.uv, barSize/2, 0.0), 0, 1, 1);
}
