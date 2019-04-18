#version 330
layout (location = 0) in vec3 position;

out vec3 pass_uv;

layout (std140) uniform Matrices
{
      mat4 view;
      mat4 projection;
};

void main()
{
    pass_uv = position;
    //TODO Move translation removal to java

    vec4 pos = projection * mat4(mat3(view)) * vec4(position.xyz, 1.0);
    gl_Position = pos.xyww;
}
