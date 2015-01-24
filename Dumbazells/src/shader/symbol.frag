uniform sampler2D tex;
uniform sampler2D backgroundTex;
uniform float blur;
uniform vec4 mapScaleOff;
uniform int type;

void main()
{
	vec3 back = texture2DLod(backgroundTex, gl_TexCoord[0].xy * mapScaleOff.xy + mapScaleOff.zw, 0.0).xyz;
	float backSum = (back.x + back.y + back.z);
	float luminance = sqrt(backSum) * 1.2;
    gl_FragColor = texture2D(tex, gl_TexCoord[0].xy, blur * 6.0);
	if(type == 0) 	   gl_FragColor *= back.x > 0.0f ? luminance : 0.0;
	else if(type == 1) gl_FragColor *= back.y > 0.0f ? luminance : 0.0;
	else if(type == 2) gl_FragColor *= back.z > 0.0f ? luminance : 0.0;
	else gl_FragColor *= (backSum == 0.0) ? 1.0 : 0.0;
	gl_FragColor.a *= max(0.0, 0.9-blur) / 0.9;
	//gl_FragColor.a = 1.0;
}
