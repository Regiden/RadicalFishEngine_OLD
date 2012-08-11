// simple texture fragment shader

uniform sampler2D sampler0;

void main() {
	vec4 color = texture2D(sampler0, gl_TexCoord[0].st);
	gl_FragColor = color;
}
