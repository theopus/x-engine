#version 330

in vec3 p_position;

out vec4 color;

layout (std140) uniform Matrices
{
      mat4 view;
      mat4 projection;
};

void main(void){
    color = vec4(view * vec4(p_position, 1.0));
}