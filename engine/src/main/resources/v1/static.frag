#version 330

in vec2 pass_uv;

out vec4 color;

layout (std140) uniform Matrices
{
      mat4 view;
      mat4 projection;
};

uniform sampler2D sampler;

void main(void){
    color = texture(sampler, pass_uv);
}