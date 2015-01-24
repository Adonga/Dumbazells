uniform sampler2D tex;
uniform float blur;

void main()
{
    gl_FragColor = texture2D(tex, gl_TexCoord[0].xy, blur * 7.0);
	gl_FragColor.a *= max(0.0, 0.9-blur) / 0.9;
}
