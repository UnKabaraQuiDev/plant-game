#version 420

in vec2 texCoord;
in vec2 fragPos;

uniform sampler2D txt0;
uniform ivec2 inputSize;
uniform ivec2 outputSize;

uniform bool horizontal;
uniform float threshold;

layout(location = 0) out vec4 out_FragColor;

void main() {

	vec2 texelSize = 1.0 / vec2(inputSize);

	float weights[5] = float[](
		0.227027,
		0.1945946,
		0.1216216,
		0.054054,
		0.016216
	);

	vec3 centerColor = texture(txt0, texCoord).rgb;

	float brightness = max(max(centerColor.r, centerColor.g), centerColor.b);

	if (brightness < threshold) {
		out_FragColor = vec4(0.0);
	}

	vec3 result = centerColor * weights[0];

	for (int i = 1; i < 5; ++i) {

		vec2 offset = horizontal
			? vec2(texelSize.x * i, 0.0)
			: vec2(0.0, texelSize.y * i);

		vec3 sample1 = texture(txt0, texCoord + offset).rgb;
		vec3 sample2 = texture(txt0, texCoord - offset).rgb;

		float b1 = max(max(sample1.r, sample1.g), sample1.b);
		float b2 = max(max(sample2.r, sample2.g), sample2.b);

		if (b1 >= threshold) {
			result += sample1 * weights[i];
		}

		if (b2 >= threshold) {
			result += sample2 * weights[i];
		}
	}

	out_FragColor = vec4(result, 1.0);
	
	out_FragColor = vec4(texture(txt0, texCoord).rgb, 1);
}
