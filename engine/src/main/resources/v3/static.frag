#version 330

in vec2 pass_uv;
in vec3 lightIntensity;
out vec4 color;
uniform sampler2D sampler;

void main(void){
    color = vec4(lightIntensity, 1.0) * texture(sampler, pass_uv);
}