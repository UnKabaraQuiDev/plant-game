#version {version}
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;
flat in int applyIcon;

out vec4 fragColor;

uniform sampler2D txt1;

uniform float progress;
uniform float icon;
uniform vec4 tint;

#define QUAD_START vec2(0.015, 0.146)
#define QUAD_END vec2(0.085, 0.854)

vec2 map(vec2 value, vec2 min1, vec2 max1, vec2 min2, vec2 max2) {
	return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

void main() {
	vec4 bgColor = texture(txt1, texCoord);

	if(applyIcon == 0) {
		fragColor = bgColor * tint;
		return;
	}
	
	vec4 fgColor = texture(txt1, mix(1.0/vec2(10.0, 1.0)*icon, 1.0/vec2(10.0, 1.0)*(icon+1.0), map(texCoord, QUAD_START, QUAD_END, vec2(0.0), vec2(1.0))));
	
	fragColor = vec4(mix(bgColor, fgColor, fgColor.a).rgb, max(fgColor.a, bgColor.a));
}