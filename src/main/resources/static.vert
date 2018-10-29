#version 330

layout(location = 0) in vec3 position;

out vec3 p_position;

uniform mat4 transformationMatrix;

void main(void){
    gl_Position = transformationMatrix * vec4(position.xyz, 1.0);
    p_position = position;
}