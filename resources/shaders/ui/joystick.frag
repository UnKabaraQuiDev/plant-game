#version {version}
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;

out vec4 fragColor;

uniform vec4 color;
uniform vec2 position;
uniform float radius;
uniform float threshold;
uniform float button;

void main() {
	vec2 correctUv = (texCoord-vec2(0.5, 0.5))*2.0;
	float leng = length(correctUv);

	if(distance(correctUv, position) < radius+(button*2.0*radius)) {
		fragColor = color;
		return;
	}
	if(leng < threshold) {
		fragColor = vec4(vec3(0.3), 1.0);
	}else if(leng < 1.0+radius/2.0 && leng > 1.0-radius/2.0) {
		fragColor = vec4(vec3(0.8), 1.0);
	}else {
		discard;
	}
}
