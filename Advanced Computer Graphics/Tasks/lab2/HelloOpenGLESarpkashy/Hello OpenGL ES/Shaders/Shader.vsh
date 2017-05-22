//
//  Shader.vsh
//  Hello OpenGL ES
//
//  Created by Kashyap, Arpitha Nandakumar on 1/20/17.
//  Copyright © 2017 B581 Spring 2017. All rights reserved.
//

attribute vec4 position;
attribute vec3 normal;

varying lowp vec4 colorVarying;

uniform mat4 modelViewProjectionMatrix;
uniform mat3 normalMatrix;

void main()
{
    vec3 eyeNormal = normalize(normalMatrix * normal);
    vec3 lightPosition = vec3(0.3, 0.4, 1.0);
    vec4 diffuseColor = vec4(0.6, 0.5, 1.0, 1.0);
    
    float nDotVP = max(0.0, dot(eyeNormal, normalize(lightPosition)));
                 
    colorVarying = diffuseColor * nDotVP;
    
    gl_Position = modelViewProjectionMatrix * position;
}
