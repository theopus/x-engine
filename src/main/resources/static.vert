#version 330

layout(location = 0) in vec3 position;


uniform mat4 transformationMatrix;

void main(void){
    gl_Position = transformationMatrix * vec4(position.xyz, 1.0);
}