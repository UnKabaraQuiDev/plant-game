#version 420 core

in vec3 bet_WorldPos;
in vec3 bet_WorldNormal;
in vec2 bet_UV;
flat in uint bet_MaterialId;
flat in uvec3 bet_ObjectId;

layout(location = 0) out vec3 out_Position;
layout(location = 1) out vec3 out_Normal;
layout(location = 2) out vec2 out_UV;
layout(location = 3) out uvec4 out_ObjMat;

void main() {
    out_Position = bet_WorldPos;
    out_Normal   = normalize(bet_WorldNormal);
    out_UV       = bet_UV;
    out_ObjMat   = uvec4(bet_MaterialId, bet_ObjectId.r, bet_ObjectId.g, bet_ObjectId.b);
}
