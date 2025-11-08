#version 420 core

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec3 in_Normal;
layout(location = 2) in vec2 in_UV;
layout(location = 3) in uint in_MaterialId;
layout(location = 4) in uvec3 in_ObjectId;
// instances
layout(location = 5) in mat4 in_InstanceMatrix;
// text related
layout(location = 9) in uint in_CharIndex;

uniform mat4 transformationMatrix; // global
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec3 bet_WorldPos;
out vec3 bet_WorldNormal;
out vec2 bet_UV;
// instances
flat out uint bet_MaterialId;
flat out uvec3 bet_ObjectId;
// text related
flat out uint bet_Index;
flat out uint bet_CharIndex;

void main() {
    mat4 modelMatrix = transformationMatrix * in_InstanceMatrix;

    vec4 worldPos4 = modelMatrix * vec4(in_Position, 1.0);
    bet_WorldPos = worldPos4.xyz;

    mat3 normalMatrix = transpose(inverse(mat3(modelMatrix)));
    bet_WorldNormal = normalize(normalMatrix * in_Normal);

    bet_UV = in_UV;
    bet_MaterialId = in_MaterialId;
    bet_ObjectId = in_ObjectId;
    
    bet_Index = gl_InstanceID;
	bet_CharIndex = in_CharIndex;

    gl_Position = projectionMatrix * viewMatrix * worldPos4;
}
