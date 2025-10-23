#version 420 core

in vec3 bet_WorldPos;
in vec3 bet_WorldNormal;
in vec2 bet_UV;
flat in uint bet_MaterialId;
flat in uvec3 bet_ObjectId;

out vec4 fragColor;

uniform sampler2D txt0;

void main() {
    fragColor = vec4(1, 0, 0, 1);
}
