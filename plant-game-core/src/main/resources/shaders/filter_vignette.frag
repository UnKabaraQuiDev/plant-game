#version {version}

in vec2 texCoord;
in vec2 fragPos;

out vec4 fragColor;

uniform sampler2D txt0;
uniform ivec2 inputSize;
uniform ivec2 outputSize;

uniform vec3 vignetteColor = vec3(0.0, 0.0, 0.0);
uniform float vignetteRadius = 0.5;
uniform float vignetteSoftness = 0.3;
uniform float vignetteStrength = 0.7;
uniform vec2 vignetteCenter = vec2(0.0, 0.0);
uniform bool followAspectRatio = true;

uniform bool edgeStyle = true;
uniform bool posterize = false;
uniform int posterizeLevels = 10;

layout(location = 0) out vec4 out_FragColor;

void main() {
	if(edgeStyle) {
		vec2 uv = texCoord * 2.0 - 1.0;

	    float aspect = float(outputSize.x) / float(outputSize.y);
		
		float left, right, bottom, top;
		
		if (!followAspectRatio) {
		    float xOffset = (1.0 - aspect);
		    left   = 1.0 - uv.x *aspect + xOffset;
		    right  = 1.0 + uv.x *aspect + xOffset;
		    bottom = 1.0 - uv.y;
		    top    = 1.0 + uv.y;
		} else {
		    left   = 1.0 - uv.x;
		    right  = 1.0 + uv.x;
		    bottom = 1.0 - uv.y;
		    top    = 1.0 + uv.y;
		}
		
		float edgeDist = max(max(left, right), max(top, bottom));
		
		float factor = smoothstep(vignetteRadius, vignetteRadius + vignetteSoftness, edgeDist);
		factor *= vignetteStrength;
		
		if (posterize) {
			float stepSize = 1.0 / float(posterizeLevels);
			
			out_FragColor = vec4(vignetteColor, floor(factor / stepSize) * stepSize);
		}	else {
			out_FragColor = vec4(vignetteColor, factor);
		}
	}else {
	    vec2 uv = texCoord * 2.0 - 1.0;
	
		if(!followAspectRatio) {
		    float aspect = float(outputSize.x) / float(outputSize.y);
		    uv.x *= aspect;
		}
	
		uv -= vignetteCenter;
	
	    float dist = length(uv);
	
	    float factor = smoothstep(vignetteRadius, vignetteRadius + vignetteSoftness, dist);
		factor *= vignetteStrength;
		
	    if (posterize) {
			float stepSize = 1.0 / float(posterizeLevels);
			
			out_FragColor = vec4(vignetteColor, floor(factor / stepSize) * stepSize);
		} else {
			out_FragColor = vec4(vignetteColor, factor);
		}
    }
}