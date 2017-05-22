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
    private int ROTATE = 0, TXY = 1, TXZ = 2, SCALE = 3;
    private int OBJ1 = 0, OBJ2 = 1, OBJ3 = 2, CAMERA = 3;
    private int topSelection = ROTATE, bottomSelection = OBJ1;
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

    public float[] ViewMat= {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, -10.0f, 1.0f
    };



    public float[] ModTrans= {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
    };

    public float[] ScaleMat= {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
    };


    public float[] TransMat= {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, -10.0f, 1.0f
    };


    public float[] IdentityMatrix = {
            1.0f,0.0f,0.0f,0.0f,
            0.0f,1.0f,0.0f,0.0f,
            0.0f,0.0f,1.0f,0.0f,
            1.0f,1.0f,1.0f,1.0f

    };
    public float[] RotMat= {
            10.0f, 10.0f, 10.0f, 0.0f,
            10.0f, 10.0f, 10.0f, 0.0f,
            10.0f, 10.0f, 10.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
    };

    public float[] xticks ={
            1.0f, 0.0f, 0.0f,
            1.0f, 0.5f, 0.0f,

            2.0f, 0.0f, 0.0f,
            2.0f, 0.5f, 0.0f,

            3.0f, 0.0f, 0.0f,
            3.0f, 0.5f, 0.0f,

            4.0f, 0.0f, 0.0f,
            4.0f, 0.5f, 0.0f,

            5.0f, 0.0f, 0.0f,
            5.0f, 0.5f, 0.0f,

            6.0f, 0.0f, 0.0f,
            6.0f, 0.5f, 0.0f,

            7.0f, 0.0f, 0.0f,
            7.0f, 0.5f, 0.0f,

            8.0f, 0.0f, 0.0f,
            8.0f, 0.5f, 0.0f,

            9.0f, 0.0f, 0.0f,
            9.0f, 0.5f, 0.0f,

            10.0f, 0.0f, 0.0f,
            10.0f, 0.5f, 0.0f

    };
    public float[] yticks= {
            0.0f, 1.0f, 0.0f,
            0.5f, 1.0f, 0.0f,

            0.0f, 2.0f, 0.0f,
            0.5f, 2.0f, 0.0f,

            0.0f, 3.0f, 0.0f,
            0.5f, 3.0f, 0.0f,

            0.0f, 4.0f, 0.0f,
            0.5f, 4.0f, 0.0f,

            0.0f, 5.0f, 0.0f,
            0.5f, 5.0f, 0.0f,

            0.0f, 6.0f, 0.0f,
            0.5f, 6.0f, 0.0f,

            0.0f, 7.0f, 0.0f,
            0.5f, 7.0f, 0.0f,

            0.0f, 8.0f, 0.0f,
            0.5f, 8.0f, 0.0f,

            0.0f, 9.0f, 0.0f,
            0.5f, 9.0f, 0.0f,

            0.0f, 10.0f, 0.0f,
            0.5f, 10.0f, 0.0f
    };

    public float[] zticks={
            0.0f, 0.0f, 1.0f,
            0.5f, 0.0f, 1.0f,

            0.0f, 0.0f, 2.0f,
            0.5f, 0.0f, 2.0f,

            0.0f, 0.0f, 3.0f,
            0.5f, 0.0f, 3.0f,

            0.0f, 0.0f, 4.0f,
            0.5f, 0.0f, 4.0f,

            0.0f, 0.0f, 5.0f,
            0.5f, 0.0f, 5.0f,

            0.0f, 0.0f, 6.0f,
            0.5f, 0.0f, 6.0f,

            0.0f, 0.0f, 7.0f,
            0.5f, 0.0f, 7.0f,

            0.0f, 0.0f, 8.0f,
            0.5f, 0.0f, 8.0f,

            0.0f, 0.0f, 9.0f,
            0.5f, 0.0f, 9.0f,

            0.0f, 0.0f, 10.0f,
            0.5f, 0.0f, 10.0f,

            0.0f, 0.0f, 1.0f,
            0.0f, 0.5f, 1.0f,

            0.0f, 0.0f, 2.0f,
            0.0f, 0.5f, 2.0f,

            0.0f, 0.0f, 3.0f,
            0.0f, 0.5f, 3.0f,

            0.0f, 0.0f, 4.0f,
            0.0f, 0.5f, 4.0f,

            0.0f, 0.0f, 5.0f,
            0.0f, 0.5f, 5.0f,

            0.0f, 0.0f, 6.0f,
            0.0f, 0.5f, 6.0f,

            0.0f, 0.0f, 7.0f,
            0.0f, 0.5f, 7.0f,

            0.0f, 0.0f, 8.0f,
            0.0f, 0.5f, 8.0f,

            0.0f, 0.0f, 9.0f,
            0.0f, 0.5f, 9.0f,

            0.0f, 0.0f, 10.0f,
            0.0f, 0.5f, 10.0f,

    };
//  this may become useful when computing a centroid in Assignment 03:
//     public float[] centroid = {
//             -1, -1
//     }

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

private float[] masterArray;

    private int myMat4Uniform;
    private FloatBuffer myVertexBuffer;
    private FloatBuffer myCubeBuffer;
    private FloatBuffer myMatrixBuffer;
    private float dx=0;
    private float dy=0;
    private float rdx=0;
    private float rdy=0;

    private double dR=0;
    private double nx=0,ny=0,nz=0;
    private double theta=0;

    private float ScaleX=1;
    private float ScaleY=1;
    private float ScaleZ=1;

    private float Trans;
    private float myTx;
    private float myTy;
    private float myTz;

    private float CamTx;
    private float CamTy;
    private float CamTz;

    private float CamRotx=0;
    private float CamRoty=0;
    private float CamRotz=0;

    private float CamTheta=0;

    private float CofMX;
    private float CofMY;
    private float CofMZ;

    private float TranX=0;
    private float TranY=0;
    private float TranZ=-10;

    private float RotX=0;
    private float RotY=0;
    private float RotZ=-10;



    private float cubeV=0;
    private float Rotate0;
    private float Rotate1;
    private float Rotate2;
    private float Rotate3;
    private float Rotate4;
    private float Rotate5;
    private float Rotate6;
    private float Rotate7;
    private float Rotate8;
    private float Rotate9;
    private float Rotate10;


    private static final String TAG = "MyGLRenderer";

    private int myGLESProgram = -1;

    private int myViewPortWidth = -1;
    private int myViewPortHeight = -1;

    //private int myWidthUniform = -1;
    //private int myHeightUniform = -1;
    private int myColorUniform = -1;
    private int myVertexAttribute = -1;
    private int myTxUniform = -1;
    private int myFoVUniform = -1;
    private int myAspectUniform = -1;
    private int myNearUniform = -1;
    private int myFarUniform = -1;
    private int myTyUniform = -1;
    private int myTzUniform = -1;
    private int myAxisflag=-1;
    private int myViewMatUniform=-1;


    //private int myNormX=-1;
    //private int myNormY=-1;
    //private int myNormZ=-1;
    //private int mytheta=-1;

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
    // ------------------------------------------------------------------------
    private void setupGL() {
        combineVertices();
        // a Java byte buffer where to store the Vertex Buffer:
        ByteBuffer lByteBuffer;
        ByteBuffer CByteBuffer;
        ByteBuffer MByteBuffer;

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
        lByteBuffer = ByteBuffer.allocateDirect(masterArray.length * 4);

        // set the Java byte buffer for all vertices:
        lByteBuffer.order(ByteOrder.nativeOrder());
        this.myVertexBuffer = lByteBuffer.asFloatBuffer();
        this.myVertexBuffer.put(masterArray);
        this.myVertexBuffer.position(0);

        this.myVertexAttribute = GLES20.glGetAttribLocation(this.myGLESProgram, "a_Position");

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);

        //For the cube
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

        MByteBuffer = ByteBuffer.allocateDirect(IdentityMatrix.length * 4);

        // set the Java byte buffer for all vertices:
        MByteBuffer.order(ByteOrder.nativeOrder());
        this.myMatrixBuffer = MByteBuffer.asFloatBuffer();
        this.myMatrixBuffer.put(IdentityMatrix);
        this.myMatrixBuffer.position(0);

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myMatrixBuffer);

        // get location of uniform variables in the shaders:
        //this.myWidthUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Width");
        //this.myHeightUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Height");
        this.myColorUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Color");
        this.myTxUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Tx");
        this.myTyUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Ty");
        this.myTzUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Tz");
        this.myFoVUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_FoV");
        this.myAspectUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Aspect");
        this.myNearUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Near");
        this.myFarUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Far");
        this.myAxisflag = GLES20.glGetUniformLocation(this.myGLESProgram, "Axisflag");
        //this.myNormX = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Normx");
        //this.myNormY = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Normy");
        //this.myNormZ = GLES20.glGetUniformLocation(this.myGLESProgram, "u_Normz");
        //this.mytheta = GLES20.glGetUniformLocation(this.myGLESProgram, "u_theta");
        this.myMat4Uniform=GLES20.glGetUniformLocation(this.myGLESProgram, "u_Mat4");
        this.myViewMatUniform = GLES20.glGetUniformLocation(this.myGLESProgram, "u_ViewMat");

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
        //GLES20.glUniform1f(this.mytheta, (float)0.0);
        //GLES20.glUniform1f(this.myNormX, (float)0.0);
        //GLES20.glUniform1f(this.myNormY, (float)0.0);
        //GLES20.glUniform1f(this.myNormZ, (float)0.0);
        //GLES20.glUniform1f(this.myTxUniform, dx);
        //GLES20.glUniform1f(this.myTyUniform, dy);
        GLES20.glUniform1f(this.myTxUniform, CamTx);
        GLES20.glUniform1f(this.myTyUniform, CamTy);
        GLES20.glUniform1f(this.myTzUniform, CamTz);
        this.myMatrixBuffer.put(ViewMat);
        this.myMatrixBuffer.position(0);
        GLES20.glUniformMatrix4fv(this.myViewMatUniform,1, false, myMatrixBuffer);
        TransMat[12]=TranX;
        TransMat[13]=TranY;
        TransMat[14]=TranZ;

        TransMat[0] = (float)(nx * nx * (1.0 - Math.cos(theta)) + Math.cos(theta));
        TransMat[1] = (float)(ny * nx * (1.0 - Math.cos(theta)) + nz * Math.sin(theta));
        TransMat[2] = (float)(nx * nz * (1.0 - Math.cos(theta)) - ny * Math.sin(theta));

        TransMat[4] = (float)(nx * ny * (1.0 - Math.cos(theta)) - nz * Math.sin(theta));
        TransMat[5] = (float)(ny * ny * (1.0 - Math.cos(theta)) + Math.cos(theta));
        TransMat[6] = (float)(ny * nz * (1.0 - Math.cos(theta)) + nx * Math.sin(theta));

        TransMat[8] = (float)(nx * nz * (1.0 - Math.cos(theta)) + ny * Math.sin(theta));
        TransMat[9] = (float)(ny * nz * (1.0 - Math.cos(theta)) - nx * Math.sin(theta));
        TransMat[10] = (float)(nz * nz * (1.0 - Math.cos(theta)) + Math.cos(theta));



        this.myMatrixBuffer.put(TransMat);
        this.myMatrixBuffer.position(0);
        GLES20.glUniformMatrix4fv(this.myMat4Uniform, 1, false, myMatrixBuffer);

/*


        // update the byte buffer for vertices:
        this.myVertexBuffer.put(masterArray);
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
        GLES20.glUniform1f(this.myAxisflag, 1.0f);
        /*
        GLES20.glUniform1f(this.mytheta, (float)theta);
        GLES20.glUniform1f(this.myNormX, (float)nx);
        GLES20.glUniform1f(this.myNormY, (float)ny);
        GLES20.glUniform1f(this.myNormZ, (float)0.0);
        */
/*
        //drawing the axes
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


        //for ticks
        this.myVertexBuffer.put(masterArray);
        this.myVertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(myVertexAttribute);

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);

        GLES20.glLineWidth(1.0f);
        //GLES20.glUniform1f(this.myAxisflag, 0.0f);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[2][0],
                gColorData[2][1],
                gColorData[2][2],
                gColorData[2][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_LINES, 0,26);

        this.myVertexBuffer.put(masterArray);
        this.myVertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(myVertexAttribute);

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);

        GLES20.glLineWidth(1.0f);
        //GLES20.glUniform1f(this.myAxisflag, 0.0f);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[0][0],
                gColorData[0][1],
                gColorData[0][2],
                gColorData[0][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_LINES, 0,60);

        this.myVertexBuffer.put(masterArray);
        this.myVertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(myVertexAttribute);

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);

        GLES20.glLineWidth(1.0f);
        //GLES20.glUniform1f(this.myAxisflag, 0.0f);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[0][0],
                gColorData[0][1],
                gColorData[0][2],
                gColorData[0][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_LINES, 0,20);
        */



        float copy[]= new float[gCubeVertexData.length];
        for(int j=0;j<copy.length;j++)
        {
            copy[j]=gCubeVertexData[j]*ScaleX;
        }


        // FOR THE CUBE
        this.myCubeBuffer.put(copy);
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
        GLES20.glUniformMatrix4fv(this.myMat4Uniform, 1, false, myMatrixBuffer);


        //drawing the cube
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


        TransMat[12]=CamTx;
        TransMat[13]=CamTy;
        TransMat[14]=CamTz;

        TransMat[0] = (float)(nx * nx * (1.0 - Math.cos(CamTheta)) + Math.cos(CamTheta));
        TransMat[1] = (float)(ny * nx * (1.0 - Math.cos(CamTheta)) + nz * Math.sin(CamTheta));
        TransMat[2] = (float)(nx * nz * (1.0 - Math.cos(CamTheta)) - ny * Math.sin(CamTheta));

        TransMat[4] = (float)(nx * ny * (1.0 - Math.cos(CamTheta)) - nz * Math.sin(CamTheta));
        TransMat[5] = (float)(ny * ny * (1.0 - Math.cos(CamTheta)) + Math.cos(CamTheta));
        TransMat[6] = (float)(ny * nz * (1.0 - Math.cos(CamTheta)) + nx * Math.sin(CamTheta));

        TransMat[8] = (float)(nx * nz * (1.0 - Math.cos(CamTheta)) + ny * Math.sin(CamTheta));
        TransMat[9] = (float)(ny * nz * (1.0 - Math.cos(CamTheta)) - nx * Math.sin(CamTheta));
        TransMat[10] = (float)(nz * nz * (1.0 - Math.cos(CamTheta)) + Math.cos(CamTheta));



        this.myMatrixBuffer.put(TransMat);
        this.myMatrixBuffer.position(0);
        GLES20.glUniformMatrix4fv(this.myMat4Uniform, 1, false, myMatrixBuffer);


        // update the byte buffer for vertices:
        this.myVertexBuffer.put(masterArray);
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
        GLES20.glUniform1f(this.myAxisflag, 1.0f);
        /*
        GLES20.glUniform1f(this.mytheta, (float)theta);
        GLES20.glUniform1f(this.myNormX, (float)nx);
        GLES20.glUniform1f(this.myNormY, (float)ny);
        GLES20.glUniform1f(this.myNormZ, (float)0.0);
        */

        //drawing the axes
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


        //for ticks
        this.myVertexBuffer.put(masterArray);
        this.myVertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(myVertexAttribute);

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);

        GLES20.glLineWidth(1.0f);
        //GLES20.glUniform1f(this.myAxisflag, 0.0f);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[2][0],
                gColorData[2][1],
                gColorData[2][2],
                gColorData[2][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_LINES, 0,26);

        this.myVertexBuffer.put(masterArray);
        this.myVertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(myVertexAttribute);

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);

        GLES20.glLineWidth(1.0f);
        //GLES20.glUniform1f(this.myAxisflag, 0.0f);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[0][0],
                gColorData[0][1],
                gColorData[0][2],
                gColorData[0][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_LINES, 0,47);

        this.myVertexBuffer.put(masterArray);
        this.myVertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(myVertexAttribute);

        GLES20.glVertexAttribPointer(
                myVertexAttribute, 3,
                GLES20.GL_FLOAT, false,
                0, myVertexBuffer);

        GLES20.glLineWidth(1.0f);
        //GLES20.glUniform1f(this.myAxisflag, 0.0f);

        GLES20.glUniform4f(this.myColorUniform,
                gColorData[1][0],
                gColorData[1][1],
                gColorData[1][2],
                gColorData[1][3]);


        //Solution
        GLES20.glDrawArrays( GLES20.GL_LINES, 0,80);






        // what color to use for all edges:
        /*GLES20.glUniform4f(this.myColorUniform,
                gColorData[0][0],
                gColorData[0][1],
                gColorData[0][2],
                gColorData[0][3]);
        

        //Solution

       GLES20.glDrawArrays( GLES20.GL_LINE_STRIP, 0, num_verts );

        // what color to use for all vertices:
        GLES20.glUniform4f(this.myColorUniform,
                gColorData[2][0],
                gColorData[2][1],
                gColorData[2][2],
                gColorData[2][3]);


        GLES20.glDrawArrays( GLES20.GL_POINTS, 0, num_verts);

        if (mode == 0) {
            // what color to use for the highlighted edge:
            GLES20.glUniform4f(this.myColorUniform,
                    gColorData[3][0],
                    gColorData[3][1],
                    gColorData[3][2],
                    gColorData[3][3]);
            GLES20.glDrawArrays( GLES20.GL_LINE_STRIP, high_vert, num_high_verts );
            // what color to use for the highlighted vertices:
            GLES20.glUniform4f(this.myColorUniform,
                    gColorData[1][0],
                    gColorData[1][1],
                    gColorData[1][2],
                    gColorData[1][3]);
            GLES20.glDrawArrays( GLES20.GL_POINTS, high_vert, num_high_verts);
        }
*/
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

    public void combineVertices()
    {
        int length= vertices.length+xticks.length+yticks.length+zticks.length;
        masterArray=new float[length];
        int count=0;
        for(int i=0;i<vertices.length;i++)
        {
            masterArray[i]=vertices[i];
            count++;
        }
        int start=count;
        for(int i=0;i<xticks.length;i++)
        {
            masterArray[i+start]=xticks[i];
            count++;
        }
        start=count;
        for(int i=0;i<yticks.length;i++)
        {
            masterArray[i+start]=yticks[i];
            count++;
        }
        start=count;
        for(int i=0;i<zticks.length;i++)
        {
            masterArray[i+start]=zticks[i];
            count++;
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

        //Solution
        testForProx(x, y);

        //  this may be useful when computing a centroid in Assignment 03:
        // testForCofMProx();

        /*if (mode == 1 && num_verts<10) {
            vertices[num_verts*2] = x;
            vertices[num_verts*2 + 1] = myViewPortHeight - (float)(y) - 1.0f;
            num_verts++;
        }*/
        
        //  this may be useful when computing a centroid in Assignment 03:
        // myCofM = ....


        System.out.println(lMessage);
    }
/*
    public void fbyfCamTransMultiplication(){

        GlobCamTrans[0] = ViewMat[0] * CamTrans[0] + ViewMat[1] * CamTrans[4] + ViewMat[2] * CamTrans[8] + ViewMat[3] * CamTrans[12];
        GlobCamTrans[1] = ViewMat[0] * CamTrans[1] + ViewMat[1] * CamTrans[5] + ViewMat[2] * CamTrans[9] + ViewMat[3] * CamTrans[13];
        GlobCamTrans[2] = ViewMat[0] * CamTrans[2] + ViewMat[1] * CamTrans[6] + ViewMat[2] * CamTrans[10] + ViewMat[3] * CamTrans[14];
        GlobCamTrans[3] = ViewMat[0] * CamTrans[3] + ViewMat[1] * CamTrans[7] + ViewMat[2] * CamTrans[11] + ViewMat[3] * CamTrans[15];

        GlobCamTrans[4] = ViewMat[4] * CamTrans[0] + ViewMat[5] * CamTrans[4] + ViewMat[6] * CamTrans[8] + ViewMat[7] * CamTrans[12];
        GlobCamTrans[5] = ViewMat[4] * CamTrans[1] + ViewMat[5] * CamTrans[5] + ViewMat[6] * CamTrans[9] + ViewMat[7] * CamTrans[13];
        GlobCamTrans[6] = ViewMat[4] * CamTrans[2] + ViewMat[5] * CamTrans[6] + ViewMat[6] * CamTrans[10] + ViewMat[7] * CamTrans[14];
        GlobCamTrans[7] = ViewMat[4] * CamTrans[3] + ViewMat[5] * CamTrans[7] + ViewMat[6] * CamTrans[11] + ViewMat[7] * CamTrans[15];

        GlobCamTrans[8] = ViewMat[8] * CamTrans[0] + ViewMat[9] * CamTrans[4] + ViewMat[10] * CamTrans[8] + ViewMat[11] * CamTrans[12];
        GlobCamTrans[9] = ViewMat[8] * CamTrans[1] + ViewMat[9] * CamTrans[5] + ViewMat[10] * CamTrans[9] + ViewMat[11] * CamTrans[13];
        GlobCamTrans[10] = ViewMat[8] * CamTrans[2] + ViewMat[9] * CamTrans[6] + ViewMat[10] * CamTrans[10] + ViewMat[11] * CamTrans[14];
        GlobCamTrans[11] = ViewMat[8] * CamTrans[3] + ViewMat[9] * CamTrans[7] + ViewMat[10] * CamTrans[11] + ViewMat[11] * CamTrans[15];

        GlobCamTrans[12] = ViewMat[12] * CamTrans[0] + ViewMat[13] * CamTrans[4] + ViewMat[14] * CamTrans[8] + ViewMat[15] * CamTrans[12];
        GlobCamTrans[13] = ViewMat[12] * CamTrans[1] + ViewMat[13] * CamTrans[5] + ViewMat[14] * CamTrans[9] + ViewMat[15] * CamTrans[13];
        GlobCamTrans[14] = ViewMat[12] * CamTrans[2] + ViewMat[13] * CamTrans[6] + ViewMat[14] * CamTrans[10] + ViewMat[15] * CamTrans[14];
        GlobCamTrans[15] = ViewMat[12] * CamTrans[3] + ViewMat[13] * CamTrans[7] + ViewMat[14] * CamTrans[11] + ViewMat[15] * CamTrans[15];

    }

    public void fbyfTransMatMultiplication() {

        GlobalTransMat[0] = ModelMat[0] * TransMat[0] + ModelMat[1] * TransMat[4] + ModelMat[2] * TransMat[8] + ModelMat[3] * TransMat[12];
        GlobalTransMat[1] = ModelMat[0] * TransMat[1] + ModelMat[1] * TransMat[5] + ModelMat[2] * TransMat[9] + ModelMat[3] * TransMat[13];
        GlobalTransMat[2] = ModelMat[0] * TransMat[2] + ModelMat[1] * TransMat[6] + ModelMat[2] * TransMat[10] + ModelMat[3] * TransMat[14];
        GlobalTransMat[3] = ModelMat[0] * TransMat[3] + ModelMat[1] * TransMat[7] + ModelMat[2] * TransMat[11] + ModelMat[3] * TransMat[15];

        GlobalTransMat[4] = ModelMat[4] * TransMat[0] + ModelMat[5] * TransMat[4] + ModelMat[6] * TransMat[8] + ModelMat[7] * TransMat[12];
        GlobalTransMat[5] = ModelMat[4] * TransMat[1] + ModelMat[5] * TransMat[5] + ModelMat[6] * TransMat[9] + ModelMat[7] * TransMat[13];
        GlobalTransMat[6] = ModelMat[4] * TransMat[2] + ModelMat[5] * TransMat[6] + ModelMat[6] * TransMat[10] + ModelMat[7] * TransMat[14];
        GlobalTransMat[7] = ModelMat[4] * TransMat[3] + ModelMat[5] * TransMat[7] + ModelMat[6] * TransMat[11] + ModelMat[7] * TransMat[15];

        GlobalTransMat[8] = ModelMat[8] * TransMat[0] + ModelMat[9] * TransMat[4] + ModelMat[10] * TransMat[8] + ModelMat[11] * TransMat[12];
        GlobalTransMat[9] = ModelMat[8] * TransMat[1] + ModelMat[9] * TransMat[5] + ModelMat[10] * TransMat[9] + ModelMat[11] * TransMat[13];
        GlobalTransMat[10] = ModelMat[8] * TransMat[2] + ModelMat[9] * TransMat[6] + ModelMat[10] * TransMat[10] + ModelMat[11] * TransMat[14];
        GlobalTransMat[11] = ModelMat[8] * TransMat[3] + ModelMat[9] * TransMat[7] + ModelMat[10] * TransMat[11] + ModelMat[11] * TransMat[15];

        GlobalTransMat[12] = ModelMat[12] * TransMat[0] + ModelMat[13] * TransMat[4] + ModelMat[14] * TransMat[8] + ModelMat[15] * TransMat[12];
        GlobalTransMat[13] = ModelMat[12] * TransMat[1] + ModelMat[13] * TransMat[5] + ModelMat[14] * TransMat[9] + ModelMat[15] * TransMat[13];
        GlobalTransMat[14] = ModelMat[12] * TransMat[2] + ModelMat[13] * TransMat[6] + ModelMat[14] * TransMat[10] + ModelMat[15] * TransMat[14];
        GlobalTransMat[15] = ModelMat[12] * TransMat[3] + ModelMat[13] * TransMat[7] + ModelMat[14] * TransMat[11] + ModelMat[15] * TransMat[15];
    }


    public void fbyfRotMatMultiplication(){

        GlobalRotMat[0] = ModelMat[0] * RotMat[0] + ModelMat[1] * RotMat[4] + ModelMat[2] * RotMat[8] + ModelMat[3] * RotMat[12];
        GlobalRotMat[1] = ModelMat[0] * RotMat[1] + ModelMat[1] * RotMat[5] + ModelMat[2] * RotMat[9] + ModelMat[3] * RotMat[13];
        GlobalRotMat[2] = ModelMat[0] * RotMat[2] + ModelMat[1] * RotMat[6] + ModelMat[2] * RotMat[10] + ModelMat[3] * RotMat[14];
        GlobalRotMat[3] = ModelMat[0] * RotMat[3] + ModelMat[1] * RotMat[7] + ModelMat[2] * RotMat[11] + ModelMat[3] * RotMat[15];

        GlobalRotMat[4] = ModelMat[4] * RotMat[0] + ModelMat[5] * RotMat[4] + ModelMat[6] * RotMat[8] + ModelMat[7] * RotMat[12];
        GlobalRotMat[5] = ModelMat[4] * RotMat[1] + ModelMat[5] * RotMat[5] + ModelMat[6] * RotMat[9] + ModelMat[7] * RotMat[13];
        GlobalRotMat[6] = ModelMat[4] * RotMat[2] + ModelMat[5] * RotMat[6] + ModelMat[6] * RotMat[10] + ModelMat[7] * RotMat[14];
        GlobalRotMat[7] = ModelMat[4] * RotMat[3] + ModelMat[5] * RotMat[7] + ModelMat[6] * RotMat[11] + ModelMat[7] * RotMat[15];

        GlobalRotMat[8] = ModelMat[8] * RotMat[0] + ModelMat[9] * RotMat[4] + ModelMat[10] * RotMat[8] + ModelMat[11] * RotMat[12];
        GlobalRotMat[9] = ModelMat[8] * RotMat[1] + ModelMat[9] * RotMat[5] + ModelMat[10] * RotMat[9] + ModelMat[11] * RotMat[13];
        GlobalRotMat[10] = ModelMat[8] * RotMat[2] + ModelMat[9] * RotMat[6] + ModelMat[10] * RotMat[10] + ModelMat[11] * RotMat[14];
        GlobalRotMat[11] = ModelMat[8] * RotMat[3] + ModelMat[9] * RotMat[7] + ModelMat[10] * RotMat[11] + ModelMat[11] * RotMat[15];

        GlobalRotMat[12] = ModelMat[12] * RotMat[0] + ModelMat[13] * RotMat[4] + ModelMat[14] * RotMat[8] + ModelMat[15] * RotMat[12];
        GlobalRotMat[13] = ModelMat[12] * RotMat[1] + ModelMat[13] * RotMat[5] + ModelMat[14] * RotMat[9] + ModelMat[15] * RotMat[13];
        GlobalRotMat[14] = ModelMat[12] * RotMat[2] + ModelMat[13] * RotMat[6] + ModelMat[14] * RotMat[10] + ModelMat[15] * RotMat[14];
        GlobalRotMat[15] = ModelMat[12] * RotMat[3] + ModelMat[13] * RotMat[7] + ModelMat[14] * RotMat[11] + ModelMat[15] * RotMat[15];

    }

    public void fbyfModMatMultiplication(){

        TransRotMat[0] = GlobalRotMat[0] * TransMat[0] + GlobalRotMat[1] * TransMat[4] + GlobalRotMat[2] * TransMat[8] + GlobalRotMat[3] * TransMat[12];
        TransRotMat[1] = GlobalRotMat[0] * TransMat[1] + GlobalRotMat[1] * TransMat[5] + GlobalRotMat[2] * TransMat[9] + GlobalRotMat[3] * TransMat[13];
        TransRotMat[2] = GlobalRotMat[0] * TransMat[2] + GlobalRotMat[1] * TransMat[6] + GlobalRotMat[2] * TransMat[10] + GlobalRotMat[3] * TransMat[14];
        TransRotMat[3] = GlobalRotMat[0] * TransMat[3] + GlobalRotMat[1] * TransMat[7] + GlobalRotMat[2] * TransMat[11] + GlobalRotMat[3] * TransMat[15];

        TransRotMat[4] = GlobalRotMat[4] * TransMat[0] + GlobalRotMat[5] * TransMat[4] + GlobalRotMat[6] * TransMat[8] + GlobalRotMat[7] * TransMat[12];
        TransRotMat[5] = GlobalRotMat[4] * TransMat[1] + GlobalRotMat[5] * TransMat[5] + GlobalRotMat[6] * TransMat[9] + GlobalRotMat[7] * TransMat[13];
        TransRotMat[6] = GlobalRotMat[4] * TransMat[2] + GlobalRotMat[5] * TransMat[6] + GlobalRotMat[6] * TransMat[10] + GlobalRotMat[7] * TransMat[14];
        TransRotMat[7] = GlobalRotMat[4] * TransMat[3] + GlobalRotMat[5] * TransMat[7] + GlobalRotMat[6] * TransMat[11] + GlobalRotMat[7] * TransMat[15];

        TransRotMat[8] = GlobalRotMat[8] * TransMat[0] + GlobalRotMat[9] * TransMat[4] + GlobalRotMat[10] * TransMat[8] + GlobalRotMat[11] * TransMat[12];
        TransRotMat[9] = GlobalRotMat[8] * TransMat[1] + GlobalRotMat[9] * TransMat[5] + GlobalRotMat[10] * TransMat[9] + GlobalRotMat[11] * TransMat[13];
        TransRotMat[10] = GlobalRotMat[8] * TransMat[2] + GlobalRotMat[9] * TransMat[6] + GlobalRotMat[10] * TransMat[10] + GlobalRotMat[11] * TransMat[14];
        TransRotMat[11] = GlobalRotMat[8] * TransMat[3] + GlobalRotMat[9] * TransMat[7] + GlobalRotMat[10] * TransMat[11] + GlobalRotMat[11] * TransMat[15];

        TransRotMat[12] = GlobalRotMat[12] * TransMat[0] + GlobalRotMat[13] * TransMat[4] + GlobalRotMat[14] * TransMat[8] + GlobalRotMat[15] * TransMat[12];
        TransRotMat[13] = GlobalRotMat[12] * TransMat[1] + GlobalRotMat[13] * TransMat[5] + GlobalRotMat[14] * TransMat[9] + GlobalRotMat[15] * TransMat[13];
        TransRotMat[14] = GlobalRotMat[12] * TransMat[2] + GlobalRotMat[13] * TransMat[6] + GlobalRotMat[14] * TransMat[10] + GlobalRotMat[15] * TransMat[14];
        TransRotMat[15] = GlobalRotMat[12] * TransMat[3] + GlobalRotMat[13] * TransMat[7] + GlobalRotMat[14] * TransMat[11] + GlobalRotMat[15] * TransMat[15];

    }

    public void fbyfScaleMatMultiplication(){

        GlobalScaleMat[0] = ModelMat[0] * ScaleMat[0] + ModelMat[1] * ScaleMat[4] + ModelMat[2] * ScaleMat[8] + ModelMat[3] * ScaleMat[12];
        GlobalScaleMat[1] = ModelMat[0] * ScaleMat[1] + ModelMat[1] * ScaleMat[5] + ModelMat[2] * ScaleMat[9] + ModelMat[3] * ScaleMat[13];
        GlobalScaleMat[2] = ModelMat[0] * ScaleMat[2] + ModelMat[1] * ScaleMat[6] + ModelMat[2] * ScaleMat[10] + ModelMat[3] * ScaleMat[14];
        GlobalScaleMat[3] = ModelMat[0] * ScaleMat[3] + ModelMat[1] * ScaleMat[7] + ModelMat[2] * ScaleMat[11] + ModelMat[3] * ScaleMat[15];

        GlobalScaleMat[4] = ModelMat[4] * ScaleMat[0] + ModelMat[5] * ScaleMat[4] + ModelMat[6] * ScaleMat[8] + ModelMat[7] * ScaleMat[12];
        GlobalScaleMat[5] = ModelMat[4] * ScaleMat[1] + ModelMat[5] * ScaleMat[5] + ModelMat[6] * ScaleMat[9] + ModelMat[7] * ScaleMat[13];
        GlobalScaleMat[6] = ModelMat[4] * ScaleMat[2] + ModelMat[5] * ScaleMat[6] + ModelMat[6] * ScaleMat[10] + ModelMat[7] * ScaleMat[14];
        GlobalScaleMat[7] = ModelMat[4] * ScaleMat[3] + ModelMat[5] * ScaleMat[7] + ModelMat[6] * ScaleMat[11] + ModelMat[7] * ScaleMat[15];

        GlobalScaleMat[8] = ModelMat[8] * ScaleMat[0] + ModelMat[9] * ScaleMat[4] + ModelMat[10] * ScaleMat[8] + ModelMat[11] * ScaleMat[12];
        GlobalScaleMat[9] = ModelMat[8] * ScaleMat[1] + ModelMat[9] * ScaleMat[5] + ModelMat[10] * ScaleMat[9] + ModelMat[11] * ScaleMat[13];
        GlobalScaleMat[10] = ModelMat[8] * ScaleMat[2] + ModelMat[9] * ScaleMat[6] + ModelMat[10] * ScaleMat[10] + ModelMat[11] * ScaleMat[14];
        GlobalScaleMat[11] = ModelMat[8] * ScaleMat[3] + ModelMat[9] * ScaleMat[7] + ModelMat[10] * ScaleMat[11] + ModelMat[11] * ScaleMat[15];

        GlobalScaleMat[12] = ModelMat[12] * ScaleMat[0] + ModelMat[13] * ScaleMat[4] + ModelMat[14] * ScaleMat[8] + ModelMat[15] * ScaleMat[12];
        GlobalScaleMat[13] = ModelMat[12] * ScaleMat[1] + ModelMat[13] * ScaleMat[5] + ModelMat[14] * ScaleMat[9] + ModelMat[15] * ScaleMat[13];
        GlobalScaleMat[14] = ModelMat[12] * ScaleMat[2] + ModelMat[13] * ScaleMat[6] + ModelMat[14] * ScaleMat[10] + ModelMat[15] * ScaleMat[14];
        GlobalScaleMat[15] = ModelMat[12] * ScaleMat[3] + ModelMat[13] * ScaleMat[7] + ModelMat[14] * ScaleMat[11] + ModelMat[15] * ScaleMat[15];

    }*/

    /*public void fbyfScaleModMatMultiplication(){

        TransScaleMat[0] = GlobalScaleMat[0] * TransMat[0] + GlobalScaleMat[1] * TransMat[4] + GlobalScaleMat[2] * TransMat[8] + GlobalScaleMat[3] * TransMat[12];
        TransScaleMat[1] = GlobalScaleMat[0] * TransMat[1] + GlobalScaleMat[1] * TransMat[5] + GlobalScaleMat[2] * TransMat[9] + GlobalScaleMat[3] * TransMat[13];
        TransScaleMat[2] = GlobalScaleMat[0] * TransMat[2] + GlobalScaleMat[1] * TransMat[6] + GlobalScaleMat[2] * TransMat[10] + GlobalScaleMat[3] * TransMat[14];
        TransScaleMat[3] = GlobalScaleMat[0] * TransMat[3] + GlobalScaleMat[1] * TransMat[7] + GlobalScaleMat[2] * TransMat[11] + GlobalScaleMat[3] * TransMat[15];

        TransScaleMat[4] = GlobalScaleMat[4] * TransMat[0] + GlobalScaleMat[5] * TransMat[4] + GlobalScaleMat[6] * TransMat[8] + GlobalScaleMat[7] * TransMat[12];
        TransScaleMat[5] = GlobalScaleMat[4] * TransMat[1] + GlobalScaleMat[5] * TransMat[5] + GlobalScaleMat[6] * TransMat[9] + GlobalScaleMat[7] * TransMat[13];
        TransScaleMat[6] = GlobalScaleMat[4] * TransMat[2] + GlobalScaleMat[5] * TransMat[6] + GlobalScaleMat[6] * TransMat[10] + GlobalScaleMat[7] * TransMat[14];
        TransScaleMat[7] = GlobalScaleMat[4] * TransMat[3] + GlobalScaleMat[5] * TransMat[7] + GlobalScaleMat[6] * TransMat[11] + GlobalScaleMat[7] * TransMat[15];

        TransScaleMat[8] = GlobalScaleMat[8] * TransMat[0] + GlobalScaleMat[9] * TransMat[4] + GlobalScaleMat[10] * TransMat[8] + GlobalScaleMat[11] * TransMat[12];
        TransScaleMat[9] = GlobalScaleMat[8] * TransMat[1] + GlobalScaleMat[9] * TransMat[5] + GlobalScaleMat[10] * TransMat[9] + GlobalScaleMat[11] * TransMat[13];
        TransScaleMat[10] = GlobalScaleMat[8] * TransMat[2] + GlobalScaleMat[9] * TransMat[6] + GlobalScaleMat[10] * TransMat[10] + GlobalScaleMat[11] * TransMat[14];
        TransScaleMat[11] = GlobalScaleMat[8] * TransMat[3] + GlobalScaleMat[9] * TransMat[7] + GlobalScaleMat[10] * TransMat[11] + GlobalScaleMat[11] * TransMat[15];

        TransScaleMat[12] = GlobalScaleMat[12] * TransMat[0] + GlobalScaleMat[13] * TransMat[4] + GlobalScaleMat[14] * TransMat[8] + GlobalScaleMat[15] * TransMat[12];
        TransScaleMat[13] = GlobalScaleMat[12] * TransMat[1] + GlobalScaleMat[13] * TransMat[5] + GlobalScaleMat[14] * TransMat[9] + GlobalScaleMat[15] * TransMat[13];
        TransScaleMat[14] = GlobalScaleMat[12] * TransMat[2] + GlobalScaleMat[13] * TransMat[6] + GlobalScaleMat[14] * TransMat[10] + GlobalScaleMat[15] * TransMat[14];
        TransScaleMat[15] = GlobalScaleMat[12] * TransMat[3] + GlobalScaleMat[13] * TransMat[7] + GlobalScaleMat[14] * TransMat[11] + GlobalScaleMat[15] * TransMat[15];

    }*/

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

        //Solution
        /*if (mode == 0) {
            for(int i = 0; i<num_high_verts; i++) {
                vertices[high_vert*2 + i*2] += myTouchXcurrent - myTouchXold;
                vertices[high_vert*2 + i*2 + 1] += myTouchYcurrent - myTouchYold;
            }
        }*/
        dx =(myTouchXcurrent-myTouchXbegin)*0.003f;
        dy =(myTouchYcurrent-myTouchYbegin)*0.003f;
if(topSelection==0) {
    rdx = (myTouchXcurrent - myTouchXbegin)*0.009f;
    rdy = (myTouchYcurrent - myTouchYbegin)*0.009f;

    RotX+=rdx;
    RotY+=rdy;

    dR = (float)(Math.sqrt(RotX*RotX + RotY* RotY));
    nx = (float)(-( RotY/dR));
    ny = (float)( RotX/dR);
    theta = (float)(dR/30.0);


    if(bottomSelection==3)
    {
        CamRotx+=rdx;
        CamRoty+=rdy;

        dR = (float)(Math.sqrt(CamRotx*CamRotx + CamRoty* CamRoty));
        nx = (float)(-( CamRoty/dR));
        ny = (float)( CamRotx/dR);
        CamTheta = (float)(dR/30.0);
    }



    /*TransMat[12] = CofMX;
    TransMat[13] = CofMY;
    TransMat[14] = CofMZ;*/
}
        if(topSelection==1)
        {
            TranX+=dx;
            TranY+=dy;

            if(bottomSelection==3) {
                CamTx += dx;
                CamTy += dy;
            }
        }
        else if(topSelection==2)
        {
            TranX+=dx;
            TranZ+=dy;

            if(bottomSelection==3) {
                CamTx += dx;
                CamTz += dy;
            }
        }

        if (topSelection == 3)
        {
            ScaleX+=dy*0.01f;
        }







       /* if (topSelection == 1 && (bottomSelection == 0 || bottomSelection == 1 || bottomSelection == 2)){

            CofMX = dx;
            CofMY = dy;

            //CoMX = (GLfloat(rdeltaX) * GLfloat(0.1))//GLfloat(deltaX)
            //CoMY = (GLfloat(rdeltaX) * GLfloat(0.1))//GLfloat(deltaY)//rdeltaY) * 0.1

            TransMat[12] = CofMX;
            TransMat[13] = CofMY;

            //fourbyfourTransMatMultiply()

            //ModelMat = GlobalTransMat

            ModelMat[12] = CofMX;
            ModelMat[13] = CofMY;

            //Trans = GLfloat(1.0)
        }

        if (topSelection == 2 && (bottomSelection == 0 || bottomSelection == 1 || bottomSelection == 2)){

            CofMX = dx;//rdeltaX) * 0.1
            CofMZ = dy;//rdeltaY) * 0.1

            TransMat[12] = CofMX;
            TransMat[14] = CofMZ;

            //fourbyfourTransMatMultiply()

            //ModelMat = GlobalTransMat

            ModelMat[12] = CofMX;
            ModelMat[14] = CofMZ;

            //Trans = GLfloat(1.0)
        }

        if (topSelection == 3 && (bottomSelection == 0 || bottomSelection == 1 || bottomSelection == 2)){

            // Translate to origin
            TransMat[12] = -CofMX;
            TransMat[13] = -CofMY;
            TransMat[14] = -CofMZ;

            fbyfTransMatMultiplication();

            ModelMat = GlobalTransMat;

            ScaleX = rdx*0.1f;
            ScaleY = rdy *0.1f;
            ScaleZ = rdy*0.1f;


            if(ScaleX != 0.0 && ScaleY != 0.0 && ScaleZ != 0.0){

                ScaleMat[0] = ScaleX;
                ScaleMat[5] = ScaleY;
                ScaleMat[10] = ScaleZ;

            }

            if(ScaleX == 0.0)  {
                ScaleMat[0] = 1.0f;
            }

            if(ScaleY == 0.0) {
                ScaleMat[5] = 1.0f;
            }
            if(ScaleZ == 0.0) {
                ScaleMat[10] = 1.0f;
            }

            fbyfScaleMatMultiplication();

            ModelMat = GlobalScaleMat;

            //Translate back

            TransMat[12] = CofMX;
            TransMat[13] = CofMY;
            TransMat[14] = CofMZ;

            fbyfScaleModMatMultiplication();

            ModelMat = TransScaleMat;



        }


        if (bottomSelection == 3 && topSelection == 1){
            CamTx = rdx * 0.1f;
            CamTy = rdy * 0.1f;

            CamTrans[12] = -(CamTx);
            CamTrans[13] = -(CamTy);

            fbyfCamTransMultiplication();

            ViewMat = GlobCamTrans;
        }

        if (bottomSelection == 3 && topSelection == 2){
            CamTx = rdx * 0.1f;
            CamTz = rdy * 0.1f;

            CamTrans[12] = -(CamTx);
            CamTrans[14] = -(CamTx);

            fbyfCamTransMultiplication();

            ViewMat = GlobCamTrans;

        }



        /*dR = Math.sqrt(dx*dx + dy*dy);
        nx = -(dy/dR)/Math.sqrt(dy/dR * dy/dR);
        ny = (dx/dR)/Math.sqrt(dx/dR * dx/dR);
        theta = dR/3.0;
        */
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

    public void topSelectionChanged(int index) {
        topSelection=index;
        System.out.println("topSelectionChanged() ---------------------------------- ");
        System.out.println("topSelectionChanged() - received " + index);
        System.out.println("topSelectionChanged() ---------------------------------- ");
    }

    public void bottomSelectionChanged(int index) {
        bottomSelection=index;
        System.out.println("bottomSelectionChanged() ---------------------------------- ");
        System.out.println("bottomSelectionChanged() - received " + index);
        System.out.println("bottomSelectionChanged() ---------------------------------- ");
    }

}
