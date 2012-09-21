//¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯ FIELDS
uniform vec4 rgbc;			// the normal rgb and chorma values for the toneModel
uniform vec4 rgbcOver;		// the overshoots from the toneModel

uniform sampler2D u_texture;

varying vec4 v_color;
varying vec2 v_texCoords;

//¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯ METHODS
float range(float val, float add) {
	return max(min(val + add, 1.0), 0.0);
}
float lerp(float current, float target, float ratio) {
	return (current - target) * ratio + target;
}

//¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯ MAIN
void main() {
	// the current fragment with it's color values in rgba
	vec4 fragment = texture2D(u_texture, v_texCoords);
	
	// range the rgb values with the overshoots and multiply the fragment with rgbc values
	// the range method makes sure we keep the values in [0, 1]
	fragment.r = range(fragment.r * rgbc.r, rgbcOver.r);
	fragment.g = range(fragment.g * rgbc.g, rgbcOver.g);
	fragment.b = range(fragment.b * rgbc.b, rgbcOver.b);
	
	// add chroma by using the gray scale caluclaion and lerp them over every color channel
	// the alpha channel of the rgbc uniform holds the chroma value
	float gray = fragment.r * 0.30 + fragment.g * 0.59 + fragment. b * 0.11;
	
	fragment.r = lerp(fragment.r, gray, rgbc.a);
	fragment.g = lerp(fragment.g, gray, rgbc.a);
	fragment.b = lerp(fragment.b, gray, rgbc.a);
	
	// add the chroma overshoot while keeping the range of [0, 1]
	fragment.r = min(1.0, fragment.r * (rgbcOver.a + 1.0));
	fragment.g = min(1.0, fragment.g * (rgbcOver.a + 1.0));
	fragment.b = min(1.0, fragment.b * (rgbcOver.a + 1.0));

	gl_FragColor =  v_color * fragment;
}
