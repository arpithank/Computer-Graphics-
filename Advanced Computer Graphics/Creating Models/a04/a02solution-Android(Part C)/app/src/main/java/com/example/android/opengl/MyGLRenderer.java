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

    //Solution
    public int num_verts = 0, high_vert = 0, num_high_verts = 0;
    public int mode = 1;    // mode 0 for proximity, mode 1 for insert
    //End

    public float[] vertices = {
        // vertex data structured thus:
        //  positionX, positionY, etc.

        // we're not really using these starting values in the assignment 02 template,
        //  since we're setting all vertex data from touch events.
            0.0f,0.0f,0.0f,
            0.0f,10.0f,0.0f,
            0.0f,0.0f,0.0f,
            10.0f,0.0f,0.0f,
            0.0f,0.0f,0.0f,
            0.0f,0.0f,10.0f

    };




   private float [] gCubeVertexData =
           // Data layout for each line below is:
            // positionX, positionY, positionZ,     normalX, normalY, normalZ,
           {3f, -3f, -3f,
                   3f, 3f, -3f,
                   3f, -3f, 3f,
                   3f, -3f, 3f,
                   3f, 3f, -3f,
                   3f, 3f, 3f,

                   3f, 3f, -3f,
                   -3f, 3f, -3f,
                   3f, 3f, 3f,
                   3f, 3f, 3f,
                   -3f, 3f, -3f,
                   -3f, 3f, 3f,

                   -3f, 3f, -3f,
                   -3f, -3f, -3f,
                   -3f, 3f, 3f,
                   -3f, 3f, 3f,
                   -3f, -3f, -3f,
                   -3f, -3f, 3f,

                   -3f, -3f, -3f,
                   3f, -3f, -3f,
                   -3f, -3f, 3f,
                   -3f, -3f, 3f,
                   3f, -3f, -3f,
                   3f, -3f, 3f,

                   3f, 3f, 3f,
                   -3f, 3f, 3f,
                   3f, -3f, 3f,
                   3f, -3f, 3f,
                   -3f, 3f, 3f,
                   -3f, -3f, 3f,

                   3f, -3f, -3f,
                   -3f, -3f, -3f,
                   3f, 3f, -3f,
                   3f, 3f, -3f,
                   -3f, -3f, -3f,
                   -3f, 3f, -3f
           };

    private float[][] gColorData = {
            // color data in RGBA float format
            {0.0f, 1.0f, 0.0f, 1.0f},
            {0.0f, 0.0f, 1.0f, 1.0f},
            {1.0f, 0.0f, 0.0f, 1.0f},
            {1.0f, 1.0f, 1.0f, 1.0f},
            {1.0f, 1.0f, 0.0f, 1.0f},
            {1.0f, 0.0f, 1.0f, 1.0f}
    };


    private FloatBuffer myVertexBuffer;
    private FloatBuffer myCubeBuffer;
    private float dx=0;
    private float dy=0;

    private double dR=0;
    private double nx=0,ny=0;
    private double theta=0;



    private static final String TAG = "MyGLRenderer";



    private int myGLESProgram = -1;

    private int myViewPortWidth = -1;
    private int myViewPortHeight = -1;

    private int myTxUniform = -1;
    private int myFoVUniform = -1;
    private int myAspectUniform = -1;
    private int myNearUniform = -1;
    private int myFarUniform = -1;
    private int myTyUniform = -1;
    private int myColorUniform = -1;
    private int myVertexAttribute = -1;
    private int myAxisflag=-1;
    private int myNormX=-1;
    private int myNormY=-1;
    private int myNormZ=-1;
    private int mytheta=-1;

    public float myTouchXbegin = -1.0f;
    public float myTouchYbegin = -1.0f;
    public float myTouchXcurrent = -1.0f;
    public float myTouchYcurrent = -1.0f;
    public float myTouchXold = -1.0f;
    public float myTouchYold = -1.0f;
    public int myTouchPhase = -1;


    // public Line mLine;
    public String vertexShaderCode = "", fragmentShaderCode = "";


    // ------------------------------------------------------------------------
    // initialize OpenGL ES:
    // -------------------------------------store the Vertex Buffer:
    ByteBuffer lByteBuffer;
    ByteBuffer CByteBuffer;


    private void setupGL() {

        // a Java byte buffer where to
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
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // allocate a byte buffer, for as many floting point numbers
        //   as there are individual coordinates (one for each x, y, etc.),
        //   multiplied times 4, since there are 4 bytes for each float:
        lByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);

        // set the Java byte buffer for all vertices:
        lByteBuffer.order(ByteOrder.nativeOrder());
        this.myVertexBuffer = lByteBuffer.asFloatBuffer();
        this.myVertexBuffer.put(vertices);
        this.myVertexBuffer.position(0);

        this.myVertexAttribute = GLES20.glGetAttribLocation(this.myGLESProgram, "a_Position");

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);


// for the cube
        CByteBuffer = ByteBuffer.allocateDirect(gCubeVertexData.length * 4);

        // set the Java byte buffer for all vertices:
        CByteBuffer.order(ByteOrder.nativeOrder());
        this.myCubeBuffer = CByteBuffer.asFloatBuffer();
        this.myCubeBuffer.put(gCubeVertexData);
        this.myCubeBuffer.position(0);

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myCubeBuffer);

        // get location of uniform variables in the shaders:
        this.myTxUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Tx");
        this.myTyUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Ty");
        this.myColorUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Color");
        this.myFoVUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_FoV");
        this.myAspectUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Aspect");
        this.myNearUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Near");
        this.myFarUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Far");
        this.myAxisflag = GLES20.glGetUniformLocation(this.myGLESProgram, "Axisflag");
        this.myNormX = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Normx");
        this.myNormY = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Normy");
        this.myNormZ = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Normz");
        this.mytheta = GLES20.glGetUniformLocation(this.myGLESProgram, "u_theta");


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
        //GLES20.glUniform1f(this.myWidthUniform, this.myViewPortWidth);
        //GLES20.glUniform1f(this.myHeightUniform, this.myViewPortHeight);
        GLES20.glUniform1f(this.myFoVUniform, 121.0f);
        GLES20.glUniform1f(this.myAspectUniform, 1.0f);
        GLES20.glUniform1f(this.myNearUniform, 0.3f);
        GLES20.glUniform1f(this.myFarUniform, 1000.0f);
        GLES20.glUniform1f(this.mytheta, (float)0.0);
        GLES20.glUniform1f(this.myNormX, (float)0.0);
        GLES20.glUniform1f(this.myNormY, (float)0.0);
        GLES20.glUniform1f(this.myNormZ, (float)0.0);

        GLES20.glUniform1f(this.myTxUniform, dx);
        GLES20.glUniform1f(this.myTyUniform, dy);


        // update the byte buffer for vertices:
        this.myVertexBuffer.put(vertices);
        this.myVertexBuffer.position(0);

        // enable which vertex attributes the buffer data is going to use:
        GLES20.glEnableVertexAttribArray(myVertexAttribute);

        // now call glVertexAttribPointer() to specify the location and data format
        //   of the array of generic vertex attributes at index,
        //   to be used at rendering time, when glDrawArrays() is going to be called.
        //
        // public func glVertexAttribPointer(indx: GLuint, _ size: GLint,
        //   _ type: GLenum, _ normalized: GLboolean,
        //   _ stride: GLsizei, _ ptr: UnsafePointer<Void>)
        // see https://www.khronos.org/opengles/sdk/docs/man/xhtml/glVertexAttribPointer.xml

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);

        GLES20.glLineWidth(1.0f);
        GLES20.glUniform1f(this.myAxisflag, 0.0f);

        // what color to use for the first line:
        GLES20.glUniform4f(this.myColorUniform,
                gColorData[0][0],
                gColorData[0][1],
                gColorData[0][2],
                gColorData[0][3]);
        

        //Solution
        GLES20.glDrawArrays( GLES20.GL_LINES, 0,2);
        GLES20.glUniform4f(this.myColorUniform,
                gColorData[2][0],
                gColorData[2][1],
                gColorData[2][2],
                gColorData[2][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_LINES, 2,2);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[1][0],
                gColorData[1][1],
                gColorData[1][2],
                gColorData[1][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_LINES, 4,2);
// FOR THE CUBE
        this.myCubeBuffer.put(gCubeVertexData);
        this.myCubeBuffer.position(0);

        // enable which vertex attributes the buffer data is going to use:
        GLES20.glEnableVertexAttribArray(myVertexAttribute);

        // now call glVertexAttribPointer() to specify the location and data format
        //   of the array of generic vertex attributes at index,
        //   to be used at rendering time, when glDrawArrays() is going to be called.
        //
        // public func glVertexAttribPointer(indx: GLuint, _ size: GLint,
        //   _ type: GLenum, _ normalized: GLboolean,
        //   _ stride: GLsizei, _ ptr: UnsafePointer<Void>)
        // see https://www.khronos.org/opengles/sdk/docs/man/xhtml/glVertexAttribPointer.xml

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myCubeBuffer);

        GLES20.glUniform1f(this.myAxisflag, 1.0f);
        GLES20.glUniform1f(this.mytheta, (float)theta);
        GLES20.glUniform1f(this.myNormX, (float)nx);
        GLES20.glUniform1f(this.myNormY, (float)ny);
        GLES20.glUniform1f(this.myNormZ, (float)0.0);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[0][0],
                gColorData[0][1],
                gColorData[0][2],
                gColorData[0][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_TRIANGLES, 0,6);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[1][0],
                gColorData[1][1],
                gColorData[1][2],
                gColorData[1][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_TRIANGLES, 6,6);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[2][0],
                gColorData[2][1],
                gColorData[2][2],
                gColorData[2][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_TRIANGLES, 12,6);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[3][0],
                gColorData[3][1],
                gColorData[3][2],
                gColorData[3][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_TRIANGLES, 18,6);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[4][0],
                gColorData[4][1],
                gColorData[4][2],
                gColorData[4][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_TRIANGLES, 24,6);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[5][0],
                gColorData[5][1],
                gColorData[5][2],
                gColorData[5][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_TRIANGLES, 30,6);







        /*GLES20.glDrawArrays( GLES20.GL_LINE_STRIP, 0, num_verts );
        GLES20.glDrawArrays( GLES20.GL_POINTS, 0, num_verts);
        if (mode == 0) {
            GLES20.glUniform4f(this.myColorUniform,
                    gColorData[3][0],
                    gColorData[3][1],
                    gColorData[3][2],
                    gColorData[3][3]);
            GLES20.glDrawArrays( GLES20.GL_LINE_STRIP, high_vert, num_high_verts );
            GLES20.glDrawArrays( GLES20.GL_POINTS, high_vert, num_high_verts);
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


        // we are in the "we've just began" phase of the touch event sequence:
        this.myTouchPhase = 1;

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

        // we are in the "something has moved" phase of the touch event sequence:
        this.myTouchPhase = 2;

        System.out.println(lMessage);

        dx =(myTouchXcurrent-myTouchXbegin)*0.01f;
        dy =(myTouchYcurrent-myTouchYbegin)*0.01f;

        dR = Math.sqrt(dx*dx + dy*dy);
        nx = -(dy/dR)/Math.sqrt(dy/dR * dy/dR);
        ny = (dx/dR)/Math.sqrt(dx/dR * dx/dR);
        theta = dR/3.0;

    }

    public void touchesEnded(float x, float y) {
        String lMessage = "Touch Ended at " + x + " , " + y;

        // store "current" to "old" touch coordinates
        this.myTouchXold = this.myTouchXcurrent;
        this.myTouchYold = this.myTouchYcurrent;

        // get new "current" touch coordinates
        this.myTouchXcurrent = (float)(x);
        this.myTouchYcurrent = this.myViewPortHeight - (float)(y) - 1.0f;

        // we are in the "something has moved" phase of the touch event sequence:
        this.myTouchPhase = 3;

        System.out.println(lMessage);

        //Solution
        mode = 1;
    }


    //Solutions

    public void testForProx(float x0, float y0) {
        y0 = this.myViewPortHeight - (float)(y0) - 1.0f;
        // test for point proximimity
        for (int i = 0; i < num_verts; i++) {
            double x1 = vertices[i * 2];
            double y1 = vertices[i * 2 + 1];
            double dist = Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
            // if closer than 20 pixels, mark the vertex for highlighting
            if (dist < 20) {
                high_vert = i;
                num_high_verts = 1;
                mode = 0;  //proximity
                return;
            }
        }

        // test for line proximity
        for (int i = 0; i < (num_verts - 1); i++) {
            double x1 = vertices[i * 2];
            double y1 = vertices[i * 2 + 1];
            double x2 = vertices[i * 2 + 2];
            double y2 = vertices[i * 2 + 3];
            // calculate orthogonal unit vector n
            double nx = -(y2 - y1);
            double ny = (x2 - x1);
            double n_mag = Math.sqrt(nx * nx + ny * ny);
            nx /= n_mag;
            ny /= n_mag;
            // calculate h
            double h = Math.abs(nx * (x0 - x1) + ny * (y0 - y1));
            // if (x0,y0) is within 15 pixels of the edge
            if (h < 15) {
                // calculate unit vector v
                double vx = (x2 - x1);
                double vy = (y2 - y1);
                double v_mag = Math.sqrt(vx * vx + vy * vy);
                vx /= v_mag;
                vy /= v_mag;
                // calculate l
                double l = (vx * (x0 - x1) + vy * (y0 - y1));
                // if (x0,y0) is on the line segment, mark the edge for highlighting
                if ((l >= 0) && (l < v_mag)) {
                    high_vert = i;
                    num_high_verts = 2;
                    mode = 0;   // proximity
                    return;
                }
            }
        }
    }

}
