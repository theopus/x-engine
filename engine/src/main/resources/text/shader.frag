#version 330

in vec2 pass_uv;

out vec4 color;

uniform sampler2D sampler;
uniform vec3 text_color;

void main(void){
    color = vec4(text_color.xyz ,texture(sampler, pass_uv).a);
}