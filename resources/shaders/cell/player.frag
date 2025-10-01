#version {version}
precision mediump float;

in struct per_vertex {
	vec2 uv;
	vec3 normal;
	vec2 frag;
	vec3 pos;
} vertex;

out vec4 fragColor;

uniform sampler2D color;
uniform sampler2D overlay;

uniform int speed;
uniform int damage;
uniform int photo;
uniform int health;

void main() {
	fragColor = texture(color, vertex.uv);
	
	if(damage > 0) {
		vec4 over = texture(overlay, mix(vec2(1.0)/vec2(3, 4)*vec2(damage-1, 3), vec2(1.0)/vec2(3, 4)*vec2(damage, 4), vertex.uv));
		fragColor = mix(fragColor, over, over.a);
	}
	
	if(health > 0) {
		vec4 over = texture(overlay, mix(vec2(1.0)/vec2(3, 4)*vec2(health-1, 2), vec2(1.0)/vec2(3, 4)*vec2(health, 3), vertex.uv));
		fragColor = mix(fragColor, over, over.a);
	}
	
	if(photo > 0) {
		vec4 over = texture(overlay, mix(vec2(1.0)/vec2(3, 4)*vec2(photo-1, 1), vec2(1.0)/vec2(3, 4)*vec2(photo, 2), vertex.uv));
		fragColor = mix(fragColor, over, over.a);
	}
	
	if(speed > 0) {
		vec4 over = texture(overlay, mix(vec2(1.0)/vec2(3, 4)*vec2(speed-1, 0), vec2(1.0)/vec2(3, 4)*vec2(speed, 1), vertex.uv));
		fragColor = mix(fragColor, over, over.a);
	}
}
