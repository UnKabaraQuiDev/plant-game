#version 420 core

in vec3 bet_WorldPos;
in vec3 bet_WorldNormal;
in vec2 bet_UV;
flat in uint bet_MaterialId;
flat in uvec3 bet_ObjectId;

out vec4 fragColor;

uniform vec4 tint;

void main() {
    fragColor = tint;
}
