#version 420 core

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec3 in_Normal;
layout(location = 2) in vec2 in_UV;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec3 bet_WorldPos;
out vec3 bet_WorldNormal;
out vec2 bet_UV;

void main() {
    vec4 worldPos4 = transformationMatrix * vec4(in_Position, 1.0);
    bet_WorldPos = worldPos4.xyz;

    mat3 normalMatrix = transpose(inverse(mat3(transformationMatrix)));
    bet_WorldNormal = normalize(normalMatrix * in_Normal);

    bet_UV = in_UV;

    gl_Position = projectionMatrix * viewMatrix * worldPos4;
}
