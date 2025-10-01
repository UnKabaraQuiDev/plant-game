#version {version}
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;

out vec4 fragColor;

uniform vec4 buttons;

#define size 0.33
#define thickness 0.03
#define dist 0.6

void main() {
	vec2 correctUv = (texCoord-vec2(0.5, 0.5))*vec2(2.0, -2.0);
	vec2 absoluteUv = abs(correctUv);

	float yRadialMask = distance(absoluteUv, vec2(0.0, dist));
	float xRadialMask = distance(absoluteUv, vec2(dist, 0.0));
	bool yCircleMask = yRadialMask < size;
	bool xCircleMask = xRadialMask < size;
	bool xMask = correctUv.x > 0.0;
	bool yMask = correctUv.y > 0.0;

	if(xRadialMask < size+thickness/2.0) {
		if((xMask && buttons.x > 0.0) || (!xMask && buttons.z > 0.0)) {
			fragColor = vec4(1.0);
			return;
		}else if(abs(xRadialMask-size) < thickness/2.0) {
			fragColor = vec4(1.0);
			return;
		}
	}else if(yRadialMask < size+thickness/2.0) {
		if((yMask && buttons.w > 0.0) || (!yMask && buttons.y > 0.0)) {
			fragColor = vec4(1.0);
			return;
		}else if(abs(yRadialMask-size) < thickness/2.0) {
			fragColor = vec4(1.0);
			return;
		}
	}

	discard;
}
