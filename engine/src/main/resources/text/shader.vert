#version 330

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 uv;

uniform vec2 transformation;

out vec2 pass_uv;

void main(void){
    gl_Position = vec4(position.xy + transformation * vec2(2.0, -2.0), 0.0, 1.0);
    pass_uv = uv;
}
