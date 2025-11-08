#version 420 core

#define CHAR_START %CHAR_START%.0
#define CHAR_COUNT %CHAR_COUNT%.0 //255

in vec3 bet_WorldPos;
in vec3 bet_WorldNormal;
in vec2 bet_UV;
flat in uint bet_MaterialId;
flat in uvec3 bet_ObjectId;

// text related
flat in uint bet_Index;
flat in uint bet_CharIndex;

out vec4 fragColor;

uniform sampler2D txt0;

uniform vec4 bgColor;
uniform vec4 fgColor;
uniform uint len;
uniform bool transparent;

void main() {
	/*if (bet_Index >= len) {
		discard;
	}*/

	vec4 mask = texture(
			txt0,
			vec2(
					bet_UV.x*(1.0/CHAR_COUNT)+((float(bet_CharIndex)-CHAR_START)/CHAR_COUNT),
					bet_UV.y
			)
	);

	if(mask.a == 0.0 && transparent) {
		discard;
	}

	if(mask.r > 0.0) {
		fragColor = fgColor * mask;
	}else{
		fragColor = bgColor * mask;
	}
}
