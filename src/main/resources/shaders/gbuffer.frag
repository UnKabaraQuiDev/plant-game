#version {version}

in vec3 vWorldPos;
in vec3 vWorldNormal;
in vec2 vUV;

uniform int materialId;
uniform vec3 objectId;

layout(location = 0) out vec4 gPosition;
layout(location = 1) out vec4 gNormal;
layout(location = 2) out vec2 gUV;
layout(location = 3) out vec4 gObjMat;

void main() {
    gPosition = vec4(vWorldPos, 1.0);
    gNormal   = vec4(normalize(vWorldNormal), 1.0);
    gUV       = vec2(vUV, 0.0, 1.0);
    gObjMat   = vec4(materialId, objectId.r, objectId.g, objectId.b);
}