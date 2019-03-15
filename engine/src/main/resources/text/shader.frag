#version 330

in vec2 pass_uv;

out vec4 color;

uniform sampler2D sampler;

void main(void){
    color = vec4(1,0,0,texture(sampler, pass_uv).a);
}