#version 330

in vec3 p_position;

out vec4 color;

void main(void){
    color = vec4(p_position.x + 0.5, p_position.y + 0.5f, p_position.z + 0.5, 1.0);
}