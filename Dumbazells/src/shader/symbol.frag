uniform sampler2D tex;
uniform float blur;

void main()
{
    gl_FragColor = texture2D(tex, gl_TexCoord[0].xy, blur * 7.0);
	gl_FragColor.a *= 1.0 - blur;
}
