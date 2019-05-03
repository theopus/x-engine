#version 330

in vec2 pass_uv;

out vec4 color;

uniform sampler2D sampler;

void main(void){
	color = texture(sampler,pass_uv);
}