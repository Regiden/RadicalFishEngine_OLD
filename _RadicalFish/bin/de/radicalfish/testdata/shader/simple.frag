// simple texture fragment shader

uniform sampler2D sampler0;

void main() {
	gl_FragColor = texture2D(sampler0, gl_TexCoord[0].st);
}
