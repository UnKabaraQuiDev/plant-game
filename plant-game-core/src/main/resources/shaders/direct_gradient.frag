#version 420 core

in vec3 bet_ObjPos;
in vec3 bet_ObjNormal;
in vec3 bet_WorldPos;
in vec3 bet_WorldNormal;
in vec3 bet_ViewPos;
in vec3 bet_ViewNormal;
in vec2 bet_UV;
flat in uint bet_MaterialId;
flat in uvec3 bet_ObjectId;

out vec4 fragColor;

uniform vec4 tint;
uniform uint gradientDirection;
uniform vec2 gradientRange;
uniform vec4 startColor;
uniform vec4 endColor;

const uint UV_X    = 1u;
const uint UV_Y    = 2u;
const uint OBJ_X   = 3u;
const uint OBJ_Y   = 4u;
const uint OBJ_Z   = 5u;
const uint WORLD_X = 6u;
const uint WORLD_Y = 7u;
const uint WORLD_Z = 8u;
const uint VIEW_X  = 9u;
const uint VIEW_Y  = 10u;
const uint VIEW_Z  = 11u;

float remap(float value, float inMin, float inMax, float outMin, float outMax) {
    float range = inMax - inMin;
    if (abs(range) < 1e-6)
        return outMin;
    float t = (value - inMin) / range;
    return mix(outMin, outMax, clamp(t, 0.0, 1.0));
}


void main() {
    float value = 0.0;

    switch (gradientDirection) {
        case UV_X:
            value = bet_UV.x;
            break;
        case UV_Y:
            value = bet_UV.y;
            break;

        case OBJ_X:
            value = bet_ObjPos.x;
            break;
        case OBJ_Y:
            value = bet_ObjPos.y;
            break;
        case OBJ_Z:
            value = bet_ObjPos.z;
            break;

        case WORLD_X:
            value = bet_WorldPos.x;
            break;
        case WORLD_Y:
            value = bet_WorldPos.y;
            break;
        case WORLD_Z:
            value = bet_WorldPos.z;
            break;

        case VIEW_X:
            value = bet_ViewPos.x;
            break;
        case VIEW_Y:
            value = bet_ViewPos.y;
            break;
        case VIEW_Z:
            value = bet_ViewPos.z;
            break;

        default:
            value = 0.0;
            break;
    }
    
	value = remap(value, gradientRange.x, gradientRange.y, 0.0, 1.0);

    fragColor = mix(startColor, endColor, value) * tint;
}
