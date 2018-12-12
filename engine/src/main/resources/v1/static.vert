#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;

out vec2 pass_uv;

uniform mat4 transformationMatrix;

layout (std140) uniform Matrices
{
      mat4 view;
      mat4 projection;
};

void main(void){
    gl_Position = projection * view * transformationMatrix * vec4(position.xyz, 1.0);
    pass_uv = uv;
}
