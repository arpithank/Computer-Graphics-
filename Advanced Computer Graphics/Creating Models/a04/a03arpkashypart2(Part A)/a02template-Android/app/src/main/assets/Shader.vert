//
//  Shader.vsh
//  a02template
//
//  Created by Mitja Hmeljak on 2017-02-13.
//

uniform float u_Width;
uniform float u_Height;
uniform float u_px0;
uniform float u_px1;
uniform float u_px2;
uniform float u_px3;

uniform float u_py0;
uniform float u_py1;
uniform float u_py2;
uniform float u_py3;
uniform float t;
uniform float t2;
uniform float splineflag;
attribute vec4 a_Position;

mat4 myOrtho2D(float pLeft, float pRight, float pBottom, float pTop) {
    float lNear = -1.0;
    float lFar = 1.0;
    float rl = pRight-pLeft;
    float tb = pTop-pBottom;
    float fn = lFar-lNear;
    // the returned matrix is defined "transposed", i.e. the last row
    //   is really the last column as used in matrix multiplication:
    return mat4(2.0/rl,             0.0,                0.0,               0.0,
                0.0,                2.0/tb,             0.0,               0.0,
                0.0,                0.0,                -2.0/fn,           0.0,
                -(pRight+pLeft)/rl, -(pTop+pBottom)/tb, -(lFar+lNear)/fn,  1.0 );
}

    float linearInterpolation(float p0, float p1, float t)
    {
            float x = (1.0 - t)*p0 + t*p1;
            return x;
    }
    float mySplineCompute(float x0, float x1, float x2, float x3, float t)
    {
        float p1=linearInterpolation(x0,x1,t);
        float p2=linearInterpolation(x1,x2,t);
        float p3=linearInterpolation(x2,x3,t);
        float p4=linearInterpolation(p1,p2,t);
        float p5=linearInterpolation(p2,p3,t);
        float p6=linearInterpolation(p4,p5,t);
        return p6;
    }

// define a varying variable:
varying vec2 var_Position;

void main() {

if(splineflag>0.5)
{
                float p0=mySplineCompute(u_px0,u_px1,u_px2,u_px3,t);
                float p1=mySplineCompute(u_py0,u_py1,u_py2,u_py3,t);
                float p2=mySplineCompute(u_px0,u_px1,u_px2,u_px3,t2);
                float p3=mySplineCompute(u_py0,u_py1,u_py2,u_py3,t2);

                vec4 proj = vec4(p0,p1,0.0,1.0);

                mat4 projectionMatrix = myOrtho2D(0.0, u_Width, 0.0, u_Height);

                    gl_PointSize = 10.0;

                    gl_Position = projectionMatrix * proj;



                    // the value for var_Position is set in this vertex shader,
                    // then it goes through the interpolator before being
                    // received (interpolated!) by a fragment shader:
                    var_Position = gl_Position.xy;

}
else
{
mat4 projectionMatrix = myOrtho2D(0.0, u_Width, 0.0, u_Height);

    gl_PointSize = 10.0;

    gl_Position = projectionMatrix * a_Position;



    // the value for var_Position is set in this vertex shader,
    // then it goes through the interpolator before being
    // received (interpolated!) by a fragment shader:
    var_Position = gl_Position.xy;
}

}
