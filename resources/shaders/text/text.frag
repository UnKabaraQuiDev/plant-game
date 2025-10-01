#version {version}
precision mediump float;

flat in struct per_instance {
	int index;
	uint character;
} instance;

in struct per_Vertex {
	vec3 worldpos;
	vec2 fragpos;
	vec2 uv;
	vec3 normal;
} vertex;

#define CHAR_START 32.0
#define CHAR_COUNT 95.0 //255

out vec4 fragColor;

uniform sampler2D txt1;
uniform vec4 bgColor;
uniform vec4 fgColor;
uniform int len;
uniform bool transparent;

void main() {
	if (instance.index >= len) {
		discard;
	}

	vec4 mask = texture(
			txt1,
			vec2(
					vertex.uv.x*(1.0/95.0)+((float(instance.character)-32.0)/95.0),
					vertex.uv.y
			).xy
	);

	if(mask.a == 0.0 && transparent) {
		discard;
	}

	if(mask.r >= 0.0) {
		fragColor = fgColor;
	}else{
		fragColor = bgColor;
	}
}
