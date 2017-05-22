//
//  Shader.vsh
//  a02template
//
//  Created by Mitja Hmeljak on 2017-02-13.
//


//
//  Shader.vsh
//  ...
uniform float u_Tx;
uniform float u_Ty;
uniform float u_FoV;
uniform float u_Aspect;
uniform float u_Near;
uniform float u_Far;
uniform float Axisflag;

attribute vec4 a_Position;

//  ...



// function that computes a 3D translation matrix:
mat4 myTranslate(float pTX, float pTY, float pTZ) {
    // the returned matrix is defined "transposed", i.e. the last row
    //   is really the last column as used in matrix multiplication:
    return mat4(  1.0,         0.0,         0.0,      0.0,
                0.0,         1.0,         0.0,      0.0,
                0.0,         0.0,         1.0,      0.0,
                pTX,         pTY,         pTZ,      1.0   );
}

// function that computes a 3D perspective transformation matrix:
mat4 myGLUPerspective(in float pFoV, in float pAspect, in float pNear, in float pFar)  {

    mat4 lPerspectiveMatrix = mat4(0.0);

    float lAngle = (pFoV / 180.0) * 3.14159;
    float lFocus = 1.0 / tan ( lAngle * 0.5 );


    lPerspectiveMatrix[0][0] = lFocus / pAspect;
    lPerspectiveMatrix[1][1] = lFocus;
    lPerspectiveMatrix[2][2] = (pFar + pNear) / (pNear - pFar);
    lPerspectiveMatrix[2][3] = -1.0;
    lPerspectiveMatrix[3][2] = (2.0 * pFar * pNear) / (pNear - pFar);

    return lPerspectiveMatrix;
}



//  ...

varying vec2 var_Position;
void main() {

if(Axisflag>0.5)
{
// mat4 projectionMatrix = myOrtho2D(0.0, u_Width, 0.0, u_Height);
    //  define a projectionMatrix using myGLUPerspective()
    //  with perspective parameters as from provided new uniform variables

    //  define a modelViewMatrix using myTranslate()
    //  with translation parameters Tx = 0, Ty = 0, Tz = -500

    mat4 projectionMatrix = myGLUPerspective(u_FoV, u_Aspect, u_Near, u_Far);
    mat4 modelViewMatrix = myTranslate(u_Tx,u_Ty,-15.0);
    gl_PointSize = 10.0;


    gl_Position = projectionMatrix * modelViewMatrix* a_Position;
    var_Position = gl_Position.xy;

    //  ...
    }


else
{

// mat4 projectionMatrix = myOrtho2D(0.0, u_Width, 0.0, u_Height);
    //  define a projectionMatrix using myGLUPerspective()
    //  with perspective parameters as from provided new uniform variables

    //  define a modelViewMatrix using myTranslate()
    //  with translation parameters Tx = 0, Ty = 0, Tz = -500

    mat4 projectionMatrix = myGLUPerspective(u_FoV, u_Aspect, u_Near, u_Far);
    mat4 modelViewMatrix = myTranslate(0.0,0.0,-15.0);
    gl_PointSize = 10.0;


    gl_Position = projectionMatrix *modelViewMatrix* a_Position;
    var_Position = gl_Position.xy;

    //  ...
}
}