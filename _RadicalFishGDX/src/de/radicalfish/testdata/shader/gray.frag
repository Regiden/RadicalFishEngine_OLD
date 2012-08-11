// simple texture fragment shader

uniform sampler2D sampler0;
uniform float scale;

void main() {

	vec4 frag = texture2D(sampler0, gl_TexCoord[0].st);
	
	vec4 color = vec4(1.0);
	float gray = frag.r * 0.30 + frag.g * 0.59 + frag.b * 0.11;
    
	color.r = (frag.r - gray) * scale + gray;
	color.g = (frag.g - gray) * scale + gray;
	color.b = (frag.b - gray) * scale + gray;
	color.a = frag.a;

	gl_FragColor = color; 
}
