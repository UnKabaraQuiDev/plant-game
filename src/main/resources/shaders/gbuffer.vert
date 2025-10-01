#version {version}

layout(location = 0) in vec3 inPosition;
layout(location = 1) in vec3 inNormal;
layout(location = 2) in vec2 inUV;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec3 vWorldPos;
out vec3 vWorldNormal;
out vec2 vUV;

void main() {
    vec4 worldPos4 = uModel * vec4(inPosition, 1.0);
    vWorldPos = worldPos4.xyz;
    // normal matrix: transpose(inverse(mat3(uModel)))
    vWorldNormal = normalize(mat3(uModel) * inNormal);
    vUV = inUV;
    vMaterialId = uMaterialId;
    vObjectId = uObjectId;
    
    gl_Position = uProjection * uView * worldPos4;
}
