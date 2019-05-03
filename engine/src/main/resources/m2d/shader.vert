#version 330

layout (location=0) in vec2 position;
layout (location=1) in vec2 uv;

out vec2 pass_uv;

uniform mat4 transformationMatrix;

void main(void){
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	pass_uv = uv;
}