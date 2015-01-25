#extension GL_ARB_shader_texture_lod : enable

uniform sampler2D tex;

float rand(vec2 co)
{
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float noise(vec2 co)
{
	float noise = 0.0;
	for(int octave = 1; octave < 3; ++ octave) // only 2 octaves!
	{
		vec2 coord = co * vec2(16.0, 8.0) * float(octave);
		vec2 floored = floor(coord);
		vec2 interp = coord - floored;
		
		noise += mix(mix(rand(floored + vec2(0.0, 0.0)), rand(floored + vec2(1.0, 0.0)), interp.x),
			  	     mix(rand(floored + vec2(0.0, 1.0)), rand(floored + vec2(1.0, 1.0)), interp.x), interp.y);
	}
	
	return noise * 0.5;
}

void main()
{
    gl_FragColor = texture2D(tex, gl_TexCoord[0].xy);
    gl_FragColor.a = 1.0;
    
    float avg = clamp(gl_FragColor.x + gl_FragColor.y + gl_FragColor.z, 0.0, 1.0);
    
    // 0.02-1.0 avg at max
    gl_FragColor /= avg; // color maxed
    gl_FragColor *= clamp(avg * 10.0 - rand(gl_TexCoord[0].xy) * 0.25, 0.0, 1.0);
    
	// desaturate and brighten
	vec3 gray = vec3(dot(vec3(0.2126,0.7152,0.0722), gl_FragColor.xyz));
    gl_FragColor.xyz = vec3(mix(gl_FragColor.xyz, gray, 0.4)) * (gl_FragColor.g > 0.0 ? 1.2 : 1.6); // different brigthening for green
    
    // brighten
    //gl_FragColor.xyz += vec3(0.4);
    
    // add some noise
    gl_FragColor.xyz *= (noise(gl_TexCoord[0].xy) + 0.6) * 0.5;
   	// add some grain
   	gl_FragColor.xyz += rand(gl_TexCoord[0].xy) * 4.0/255.0;
}
