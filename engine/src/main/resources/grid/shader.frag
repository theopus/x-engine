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
    const float scale = 10;
    bvec2 toDiscard = greaterThan( fract(pass_uv * scale), vec2(0.05,0.05) );
    if( all(toDiscard) )
        discard;
    color = texture(sampler, pass_uv);

}