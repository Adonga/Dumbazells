uniform sampler2D tex;

void main()
{
    gl_FragColor = texture2D(tex, gl_TexCoord[0].xy);
	if(gl_FragColor.x + gl_FragColor.y + gl_FragColor.z != 0.0)
	{
		gl_FragColor.xyz += gl_FragColor.x > 0.0f ? 0.3 : 0.6;
		gl_FragColor.xyz /= 5.0;
	}
}
