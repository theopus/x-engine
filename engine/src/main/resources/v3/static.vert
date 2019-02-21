#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec3 normal;

out vec2 pass_uv;
out vec3 lightIntensity;

uniform mat4 transformationMatrix;

layout (std140) uniform Matrices {
    mat4 view;        //0
    mat4 projection;  //64
} mtx;

layout (std140) uniform Light {
    vec3 position;    //0
    vec3 intensity;     //16
} light;

layout (std140) uniform Material {
    float ambientReflectivity;
    float diffuseReflectivity;
    float specularReflectivity;
    float shininess;
} material;


void main(void){
    vec4 worldCoords = transformationMatrix * vec4(position, 1.0);
    //position after view apply
    vec4 cameraCoords = mtx.view * worldCoords;
    //projected position
    gl_Position = mtx.projection * cameraCoords;

    //vector surface normal
    vec3 surfaceNormal = normalize((transformationMatrix * vec4(normal, 0.0)).xyz);

    //vector to light //s
    vec3 toLightVector = normalize(light.position - worldCoords.xyz);

    //TODO: investiagate difference
    vec3 toCameraVector = normalize((inverse(mtx.view) * vec4(0.0,0.0,0.0,1.0)).xyz - worldCoords.xyz);
//    vec3 toCameraVector = normalize(-cameraCoords.xyz);
    vec3 reflectVector = reflect(-toLightVector, surfaceNormal);

    float toLightDot = max(dot(toLightVector, surfaceNormal), 0.0);

    vec3 ambient = light.intensity * material.ambientReflectivity;
    vec3 diffuse = light.intensity * material.diffuseReflectivity * toLightDot;
    vec3 specular = vec3(0.0);
    if(toLightDot > 0.0){
        specular = light.intensity * material.specularReflectivity *
                pow(max(dot(reflectVector, toCameraVector), 0.0), material.shininess);
    }

    lightIntensity = ambient + diffuse + specular;
//    vector from vertex to viewer


    //final intensity
//    lightIntensity = light.intensity.xyz * max( dot( toLightVector, surfaceNormal), 0.0);
    pass_uv = uv;
}
