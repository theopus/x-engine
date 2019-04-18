#version 330 core
in vec3 pass_uv;

out vec4 color;

layout (std140) uniform Matrices
{
      mat4 view;
      mat4 projection;
};

uniform samplerCube cubemap;

void main()
{
    color = texture(cubemap, pass_uv);
}


