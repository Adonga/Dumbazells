uniform sampler2D tex;

void main()
{
    gl_FragColor = texture2D(tex, gl_TexCoord[0].xy);
	float avg = (gl_FragColor.x + gl_FragColor.y + gl_FragColor.z) / 3.0;
	gl_FragColor.xyz /= avg;	// normalize
	float lum = sqrt(avg);
	gl_FragColor.xyz += (gl_FragColor.x > 0.0f ? 0.3 : 0.6);
	gl_FragColor.xyz /= 5.0;
	gl_FragColor.xyz *= lum;
}
