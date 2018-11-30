#version 330

layout(location = 0) in vec3 position;

out vec3 p_position;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main(void){
//    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position.xyz, 1.0);
    gl_Position = transformationMatrix * vec4(position.xyz, 1.0);
    p_position = position;
}
