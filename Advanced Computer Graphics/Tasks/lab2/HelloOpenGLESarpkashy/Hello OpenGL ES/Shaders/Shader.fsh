//
//  Shader.fsh
//  Hello OpenGL ES
//
//  Created by Kashyap, Arpitha Nandakumar on 1/20/17.
//  Copyright © 2017 B581 Spring 2017. All rights reserved.
//

varying lowp vec4 colorVarying;

void main()
{
    gl_FragColor = colorVarying;
}
