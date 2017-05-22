package com.example.android.opengl;

import javax.microedition.khronos.opengles.GL10;
// GL10 is only needed for Android GLSurfaceView.Renderer callbacks, e.g. onSurfaceCreated() etc.

import javax.microedition.khronos.egl.EGLConfig;

import android.opengl.GLSurfaceView;
import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.util.Log;

/**
 * MyGLRenderer provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private float[] gVertexData = {
        // vertex data structured thus:
        //  positionX, positionY, etc.

        // we're not really using these starting values in the assignment 02 template,
        //  since we're setting all vertex data from touch events.
            10.0f,  10.0f,
            100.0f, 200.0f,
            50.0f,  30.0f,
            300.0f, 200.0f,
            10.0f,  10.0f,
            100.0f, 200.0f,
            50.0f,  30.0f,
            300.0f, 200.0f,
            10.0f,  10.0f,
            100.0f, 200.0f,
            50.0f,  30.0f,
            300.0f, 200.0f,
            10.0f,  10.0f,
            100.0f, 200.0f,
            50.0f,  30.0f,
            300.0f, 200.0f,
            500.0f, 600.0f

    };
    /*private float[] tVertexData= {

            0.0f,0.0f,
            0.02f,0.0f,
                0.04f,0.0f,
                0.06f,0.0f,
                0.08f,0.0f,
                0.10f,0.0f,
                0.12f,0.0f,
                0.14f,0.0f,
                0.16f,0.0f,
                0.18f,0.0f,
                0.20f,0.0f,
                0.22f,0.0f,
                0.24f,0.0f,
                0.26f,0.0f,
                0.28f,0.0f,
                0.30f,0.0f,
                0.32f,0.0f,
                0.34f,0.0f,
                0.36f,0.0f,
                0.38f,0.0f,
                0.40f,0.0f,
                0.42f,0.0f,
                0.44f,0.0f,
                0.46f,0.0f,
            0.48f,0.0f,
            0.50f,0.0f,
            0.52f,0.0f,
            0.54f,0.0f,
            0.56f,0.0f,
            0.58f,0.0f,
            0.60f,0.0f,
            0.62f,0.0f,
            0.64f,0.0f,
            0.66f,0.0f,
            0.68f,0.0f,
            0.70f,0.0f,
            0.72f,0.0f,
            0.74f,0.0f,
            0.76f,0.0f,
            0.78f,0.0f,
            0.80f,0.0f,
            0.82f,0.0f,
            0.84f,0.0f,
            0.86f,0.0f,
            0.88f,0.0f,
            0.90f,0.0f,
            0.92f,0.0f,
            0.94f,0.0f,
            0.96f,0.0f,
            0.98f,0.0f,
            1.0f,0.0f



    };*/


    private float [] splineData=new float [1000];
    private int splineCount=0;
    private int VerNo =0;
private int vflag=0,lflag=0;
        private int v,e;
    private FloatBuffer mySplineBuffer;
    private float[][] gColorData = {
            // color data in RGBA float format
            {0.0f, 1.0f, 0.0f, 1.0f},
            {0.0f, 0.0f, 1.0f, 1.0f},
            {1.0f, 0.0f, 0.0f, 1.0f}
    };


    private FloatBuffer myVertexBuffer;

    private static final String TAG = "MyGLRenderer";

    private int myGLESProgram = -1;

    private int myViewPortWidth = -1;
    private int myViewPortHeight = -1;

    private int myWidthUniform = -1;

    private int mypx0=-1;
    private int mypx1=-1;
    private int mypx2=-1;
    private int mypx3=-1;
    private int mypy0=-1;
    private int mypy1=-1;
    private int mypy2=-1;
    private int mypy3=-1;
    private int myt=-1;
    private int myt2=-1;
    private int mySplineflag=-1;

    private int myHeightUniform = -1;
    private int myColorUniform = -1;
    private int myVertexAttribute = -1;
    private int mySplineAttribute = -1;

    private float myTouchXbegin = -1.0f;
    private float myTouchYbegin = -1.0f;
    private float myTouchXcurrent = -1.0f;
    private float myTouchYcurrent = -1.0f;
    private float myTouchXold = -1.0f;
    private float myTouchYold = -1.0f;
    private int myTouchPhase = -1;


    // public Line mLine;
    public String vertexShaderCode = "", fragmentShaderCode = "";


    // ------------------------------------------------------------------------
    // initialize OpenGL ES:
    // ------------------------------------------------------------------------
    private void setupGL() {

        // a Java byte buffer where to store the Vertex Buffer:
        ByteBuffer lByteBuffer;
        ByteBuffer lSplineBuffer;

        System.out.println("setupGL() ---------------------------------- ");
        String lGL_VERSION = GLES20.glGetString(GLES20.GL_VERSION);
        System.out.println("setupGL() - glGetString(GL_VERSION) returned " + lGL_VERSION);
        String lGL_SHADING_LANGUAGE_VERSION = GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION);
        System.out.println("setupGL() - glGetString(GL_SHADING_LANGUAGE_VERSION) returned " + lGL_SHADING_LANGUAGE_VERSION);
        System.out.println("setupGL() ---------------------------------- ");


        // get shaders ready -- load, compile, link GLSL code into GPU program:
        if (!this.loadShaders()) {
            System.err.println("setupGL() hasn't been successful in creating OpenGL shaders");
        }

        // in 2D, we don't need depth/Z-buffer:
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        // allocate a byte buffer, for as many floting point numbers
        //   as there are individual coordinates (one for each x, y, etc.),
        //   multiplied times 4, since there are 4 bytes for each float:
        lByteBuffer = ByteBuffer.allocateDirect(gVertexData.length * 4);

        // set the Java byte buffer for all vertices:
        lByteBuffer.order(ByteOrder.nativeOrder());
        this.myVertexBuffer = lByteBuffer.asFloatBuffer();
        this.myVertexBuffer.put(gVertexData);
        this.myVertexBuffer.position(0);

        /*lSplineBuffer = ByteBuffer.allocateDirect(splineData.length * 4);

        // set the Java byte buffer for all vertices:
        lSplineBuffer.order(ByteOrder.nativeOrder());
        this.mySplineBuffer = lSplineBuffer.asFloatBuffer();
        this.mySplineBuffer.put(splineData);
        this.mySplineBuffer.position(0);*/

        this.myVertexAttribute = GLES20.glGetAttribLocation(this.myGLESProgram, "a_Position");
        //this.mySplineAttribute = GLES20.glGetAttribLocation(this.myGLESProgram, "t");

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 2,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);

        // get location of uniform variables in the shaders:
        this.myWidthUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Width");
        this.myHeightUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Height");
        this.myColorUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Color");
        this.mypx0 = GLES20.glGetUniformLocation(this.myGLESProgram, "u_px0");
        this.mypx1 = GLES20.glGetUniformLocation(this.myGLESProgram, "u_px1");
        this.mypx2 = GLES20.glGetUniformLocation(this.myGLESProgram, "u_px2");
        this.mypx3 = GLES20.glGetUniformLocation(this.myGLESProgram, "u_px3");
        this.mypy0 = GLES20.glGetUniformLocation(this.myGLESProgram, "u_py0");
        this.mypy1 = GLES20.glGetUniformLocation(this.myGLESProgram, "u_py1");
        this.mypy2 = GLES20.glGetUniformLocation(this.myGLESProgram, "u_py2");
        this.mypy3 = GLES20.glGetUniformLocation(this.myGLESProgram, "u_py3");
        this.myt = GLES20.glGetUniformLocation(this.myGLESProgram, "t");
        this.myt2 = GLES20.glGetUniformLocation(this.myGLESProgram, "t2");
        this.mySplineflag = GLES20.glGetUniformLocation(this.myGLESProgram, "splineflag");



        // glViewport(x, y, width, height)
        GLES20.glViewport ( 0, 0, this.myViewPortWidth, this.myViewPortHeight );

        // Set the background color:
        GLES20.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );


    } // end of setupGL()


    // ------------------------------------------------------------------------
    private void draw() {

        GLES20.glUseProgram(myGLESProgram);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // pass current viewport dimensions to the vertex shader:
        GLES20.glUniform1f(this.myWidthUniform, this.myViewPortWidth);
        GLES20.glUniform1f(this.myHeightUniform, this.myViewPortHeight);

        // set vertex coordinates:
        /*GLES20.glUniform1f(this.mypx0, this.mypy0);
        GLES20.glUniform1f(this.mypx1, this.mypy1);
        GLES20.glUniform1f(this.mypx2, this.mypy2);
        GLES20.glUniform1f(this.mypx3, this.mypy3);*/


        // the "current" line :
        /*gVertexData[0] = this.myTouchXbegin;
        gVertexData[1] = this.myTouchYbegin;
        gVertexData[2] = this.myTouchXcurrent;
        gVertexData[3] = this.myTouchYcurrent;
        gVertexData[4] = this.myTouchXold;
        gVertexData[5] = this.myTouchYold;
*/
        // the "delta" line (point from old to current touch location) :
        /*gVertexData[6] = this.myTouchXold;
        gVertexData[7] = this.myTouchYold;
        gVertexData[8] = this.myTouchXcurrent;
        gVertexData[9] = this.myTouchYcurrent;
*/
        // update the byte buffer for vertices:
        this.myVertexBuffer.put(gVertexData);
        this.myVertexBuffer.position(0);
        //this.mySplineBuffer.put(splineData);
        //this.mySplineBuffer.position(0);

        // enable which vertex attributes the buffer data is going to use:
        GLES20.glEnableVertexAttribArray(myVertexAttribute);
        //GLES20.glEnableVertexAttribArray(mySplineAttribute);

        // now call glVertexAttribPointer() to specify the location and data format
        //   of the array of generic vertex attributes at index,
        //   to be used at rendering time, when glDrawArrays() is going to be called.
        //
        // public func glVertexAttribPointer(indx: GLuint, _ size: GLint,
        //   _ type: GLenum, _ normalized: GLboolean,
        //   _ stride: GLsizei, _ ptr: UnsafePointer<Void>)
        // see https://www.khronos.org/opengles/sdk/docs/man/xhtml/glVertexAttribPointer.xml

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 2,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);



        GLES20.glLineWidth(1.0f);

        // what color to use for the first line:
        /*GLES20.glUniform4f(this.myColorUniform,
                gColorData[0][0],
                gColorData[0][1],
                gColorData[0][2],
                gColorData[0][3]);

        // draw the first line in the array:
        // these are the parameters:
        //   glDrawArrays(mode, first, count)
        GLES20.glDrawArrays( GLES20.GL_LINES, 0, 2 );*/
        GLES20.glUniform1f(this.mySplineflag, 0.0f);
        // what color to use for the second line:
        GLES20.glUniform4f(this.myColorUniform,
                gColorData[1][0],
                gColorData[1][1],
                gColorData[1][2],
                gColorData[1][3]);

        // draw the second line in the array:
        GLES20.glDrawArrays( GLES20.GL_LINE_STRIP, 0, VerNo);


        // what color to use for the highlight point:
        // what color to use for the second line:
        GLES20.glUniform4f(this.myColorUniform,
                gColorData[2][0],
                gColorData[2][1],
                gColorData[2][2],
                gColorData[2][3]);
        // draw the second vertex in the array:
        GLES20.glDrawArrays( GLES20.GL_POINTS, 0, VerNo );

        if(vflag==1) {
            GLES20.glUniform4f(this.myColorUniform, 1.0f, 1.0f
                    , 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_POINTS,v/2 ,1 );
            GLES20.glDrawArrays(GLES20.GL_LINE_STRIP,v/2 ,1 );

        }

        this.myVertexBuffer.put(gVertexData);
        this.myVertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(myVertexAttribute);

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 2,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[0][0],
                gColorData[0][1],
                gColorData[0][2],
                gColorData[0][3]);

        for (int i=0;i<(VerNo*2)-6;i+=6)
        {
            for(float t=0.01f;t<=1;t+=0.01)
            {
                GLES20.glUniform1f(this.mypx0, gVertexData[i]);
                GLES20.glUniform1f(this.mypx1, gVertexData[i+2]);
                GLES20.glUniform1f(this.mypx2, gVertexData[i+4]);
                GLES20.glUniform1f(this.mypx3, gVertexData[i+6]);

                GLES20.glUniform1f(this.mypy0, gVertexData[i+1]);
                GLES20.glUniform1f(this.mypy1, gVertexData[i+3]);
                GLES20.glUniform1f(this.mypy2, gVertexData[i+5]);
                GLES20.glUniform1f(this.mypy3, gVertexData[i+7]);
        float t2=Math.min(1.0f,t+0.01f);

                GLES20.glUniform1f(this.myt,t);
                GLES20.glUniform1f(this.mySplineflag, 1.0f);

                GLES20.glUniform1f(this.myt2,t2);
                GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);

            }
        }
        /*if(lflag==1){
            GLES20.glUniform4f(this.myColorUniform, 1.0f, 1.0f
                    , 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, e/2, 2);
        }*/
        /*if(VerNo>=4) {
            GLES20.glUniform4f(this.myColorUniform,
                    gColorData[1][0],
                    gColorData[1][1],
                    gColorData[1][2],
                    gColorData[1][3]);
            this.mySplineBuffer.put(splineData);
            this.mySplineBuffer.position(0);

            // enable which vertex attributes the buffer data is going to use:
            GLES20.glEnableVertexAttribArray(myVertexAttribute);
            //GLES20.glEnableVertexAttribArray(mySplineAttribute);


            GLES20.glVertexAttribPointer(
                    myVertexAttribute, 2, //changed
                    GLES20.GL_FLOAT, false,
                    0, mySplineBuffer);

            GLES20.glDrawArrays(GLES20.GL_LINES, 0, splineCount/2);
        }*/
    } // end of draw()
    // ------------------------------------------------------------------------






    // ------------------------------------------------------------------------
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // called when the surface is created or recreated

        // this method is a convenient place to put code to create EGL resources
        // that need to be created when the rendering starts,
        // and that need to be recreated when the EGL context is lost.

        this.setupGL();
    }


    // ------------------------------------------------------------------------
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // called after the surface is created and whenever the OpenGL ES surface size changes

        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        this.myViewPortHeight = height;
        this.myViewPortWidth = width;

    }


    // ------------------------------------------------------------------------
    @Override
    public void onDrawFrame(GL10 unused) {

        // called to draw the current frame
        this.draw();
    }


    // ------------------------------------------------------------------------
    private boolean loadShaders() {
        this.myGLESProgram = GLES20.glCreateProgram();

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, this.vertexShaderCode);

        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, this.fragmentShaderCode);

        GLES20.glAttachShader(myGLESProgram, vertexShader);
        GLES20.glAttachShader(myGLESProgram, fragmentShader);
        GLES20.glLinkProgram(myGLESProgram);

        return true;

    } // end of loadShaders()


    // ------------------------------------------------------------------------
    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public void proximity(double p, double q) {

        int i;
        double dist;

        for (i = 0; i < VerNo; i++) {
            float x = gVertexData[i * 2];
            float y = gVertexData[i * 2 + 1];
            dist = Math.pow((p - x) * (p - x) + (q - y) * (q - y), 0.5);

            if (dist < 50) {
                vflag = 1;
                v = i * 2;
                return;
            }

        }
        if (VerNo >= 2) {
            for (int j = 0; j < VerNo; j++) {

                float nx1 = gVertexData[j * 2];
                float ny1 = gVertexData[j * 2 + 1];
                float nx2 = gVertexData[j * 2 + 2];
                float ny2 = gVertexData[j * 2 + 3];
                dist = Math.pow((p - nx1) * (p - nx1) + (q - ny1) * (q - ny1), 0.5);
                double slope1 = (q - ny1) / (p - nx1);
                double slope2 = (ny2 - ny1) / (nx2 - nx1);
                double theta = Math.atan((slope2 - slope1) / (1 + slope1 * slope2));
                double dist2 = dist * Math.sin(theta);
                if (Math.abs(dist2) < 50 && ((p >= nx1 && p <= nx2) || (p >= nx2 && p <= nx1))){
                    lflag = 1;
                    e = j * 2;
                    return;
                }
            }
        }
    }

    /*public void buildSplineCurve()
    {

        if(VerNo>=4)
        {
            splineCount=0;
        for(int i=0;i<VerNo-3;i=i+3)
        {
            float x0 = gVertexData[i*2];
            float x1 = gVertexData[i*2+2];
            float x2 = gVertexData[i*2+4];
            float x3 = gVertexData[i*2+6];

            float y0 = gVertexData[i*2+1];
            float y1 = gVertexData[i*2+3];
            float y2 = gVertexData[i*2+5];
            float y3 = gVertexData[i*2+7];

            for (float t=0.01f;t<=1;t+=0.01) {

                float p0=mySplineCompute(x0,x1,x2,x3,t);
                float p1=mySplineCompute(y0,y1,y2,y3,t);
                float p2=mySplineCompute(x0,x1,x2,x3,Math.min(1.0f,t+0.01f));
                float p3=mySplineCompute(y0,y1,y2,y3,Math.min(1.0f,t+0.01f));
                splineData[splineCount]=p0;
                splineData[splineCount+1]=p1;
                splineCount++;

                splineData[splineCount]=p1;
                splineData[splineCount+1]=p2;
                splineCount++;



            }
        }
    }
    }

    public float linearInterpolation(float p0, float p1, float t)
    {
            float x = (1 - t)*p0 + t*p1;
            return x;
    }
    public float mySplineCompute(float x0, float x1, float x2, float x3, float t)
    {
        float p1=linearInterpolation(x0,x1,t);
        float p2=linearInterpolation(x1,x2,t);
        float p3=linearInterpolation(x2,x3,t);
        float p4=linearInterpolation(p1,p2,t);
        float p5=linearInterpolation(p2,p3,t);
        float p6=linearInterpolation(p4,p5,t);
        return p6;
    }*/


    public void touchesBegan(float x, float y) {
        String lMessage = "Touch Began at " + x + " , " + y;

        // the position where the finger begins touching the screen:
        this.myTouchXbegin = (float)(x);
        this.myTouchYbegin = this.myViewPortHeight - (float)(y) - 1.0f;

        // the position where the finger currently touches the screen:
        this.myTouchXcurrent = this.myTouchXbegin;
        this.myTouchYcurrent = this.myTouchYbegin;

        // the last known position of the finger touching the screen:
        //   at redraw or at new (first) touch event:
        this.myTouchXold = this.myTouchXcurrent;
        this.myTouchYold = this.myTouchYcurrent;
        proximity(this.myTouchXcurrent,this.myTouchYcurrent);
        if(VerNo<16 && vflag==0 && lflag==0) {
            gVertexData[VerNo * 2] = x;
            gVertexData[VerNo * 2 + 1] = this.myViewPortHeight - (float) (y) - 1.0f;
            VerNo++;
            if (VerNo < 32) {
                //buildSplineCurve();
            }
        }
        // we are in the "we've just began" phase of the touch event sequence:
        this.myTouchPhase = 1;
        //proximity(x,this.myViewPortHeight - (float)(y) - 1.0f);


        System.out.println(lMessage);
    }

    public void touchesMoved(float x, float y) {
        String lMessage = "Touch Moved to " + x + " , " + y;

        // store "current" to "old" touch coordinates
        this.myTouchXold = this.myTouchXcurrent;
        this.myTouchYold = this.myTouchYcurrent;

        // get new "current" touch coordinates
        this.myTouchXcurrent = (float)(x);
        this.myTouchYcurrent = this.myViewPortHeight - (float)(y) - 1.0f;
        if(vflag==1)
        {
            gVertexData[v]=myTouchXcurrent;
            gVertexData[v+1]=myTouchYcurrent;

        }
        /*if(lflag==1)
        {
            float dx=myTouchXcurrent-myTouchXold;
            float dy=myTouchYcurrent-myTouchYold;
            gVertexData[e]=gVertexData[e]+dx;
            gVertexData[e+1]=gVertexData[e+1]+dy;
            gVertexData[e+2]=gVertexData[e+2]+dx;
            gVertexData[e+3]=gVertexData[e+3]+dy;
        }*/
        if(VerNo>=4) {
            gVertexData[v]=myTouchXcurrent;
            gVertexData[v+1]=myTouchYcurrent;
            //buildSplineCurve();
        }
        // we are in the "something has moved" phase of the touch event sequence:
        this.myTouchPhase = 2;

        System.out.println(lMessage);

    }

    public void touchesEnded(float x,float y) {
        String lMessage = "Touch Ended at " + x + " , " + y;

        // store "current" to "old" touch coordinates
        this.myTouchXold = this.myTouchXcurrent;
        this.myTouchYold = this.myTouchYcurrent;

        // get new "current" touch coordinates
        this.myTouchXcurrent = (float)(x);
        this.myTouchYcurrent = this.myViewPortHeight - (float)(y) - 1.0f;

        vflag=0;
        lflag=0;
        if(VerNo>32)
        {
            VerNo=0;
        }


        // we are in the "something has moved" phase of the touch event sequence:
        this.myTouchPhase = 3;

        System.out.println(lMessage);
    }

}