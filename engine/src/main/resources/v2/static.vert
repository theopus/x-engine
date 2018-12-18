#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec3 normal;

out vec2 pass_uv;
out vec3 lightIntensity;

uniform mat4 transformationMatrix;
//uniform vec3 lightPosition;
//uniform vec3 Kd; // Diffuse reflectivity
//uniform vec3 Ld; //Light density


layout (std140) uniform Matrices
{
      mat4 view;
      mat4 projection;
};

void main(void){
    vec4 worldCoords = transformationMatrix * vec4(position, 1.0);
    //position after view apply
    vec4 cameraCoords = view * worldCoords;
    gl_Position = projection * cameraCoords;

    vec3 Kd = vec3(1,1,1); // Diffuse reflectivity
    vec3 Ld = vec3(1,1,1);
    vec3 lightPosition = vec3(-20,1000,0);

    vec3 surfaceNormal = normalize((transformationMatrix * vec4(normal, 0.0)).xyz);
    vec3 toLightVector = normalize(lightPosition - worldCoords.xyz);
    vec3 toCameraVector = normalize((inverse(view) * vec4(0.0,0.0,0.0,1.0)).xyz - worldCoords.xyz);

    lightIntensity = Ld * Kd * max( dot( toLightVector, surfaceNormal), 0.0);
    pass_uv = uv;
}
