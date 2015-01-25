#extension GL_ARB_shader_texture_lod : enable

uniform float time;

float rand(vec2 co)
{
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main()
{
   	// output fine noise
	float noise = rand(gl_FragCoord.xy * 0.1353 + vec2(time));
   	//gl_FragColor.xyz = vec3(rand(gl_FragCoord.xy * 0.1353 + vec2(time)) > 0.5 ? 1.0/255.0 : 2.0/255.0);
	gl_FragColor.xyz = vec3((1.1 + noise)/255.0);
	gl_FragColor.a = 0.0;
}
