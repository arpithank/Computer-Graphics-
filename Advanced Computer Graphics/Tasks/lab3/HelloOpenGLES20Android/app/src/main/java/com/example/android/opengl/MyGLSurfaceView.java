/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.Date;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        //fix for error No Config chosen, but I don't know what this does.
        super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;


    private void zoom(boolean zOut) {
        double magnifier = 1.03;
        if(zOut) magnifier = 0.97;

    }

    private void doubleTap() {

    }

    private void swipeRight() {
        mRenderer.mTriangle.color[0] = 0.13671875f;
        mRenderer.mTriangle.color[1] = 0.509803922f;
        mRenderer.mTriangle.color[2] = 0.709803922f;
        requestRender();
    }

    private void swipeLeft() {
        mRenderer.mTriangle.color[0] = 0.63671875f;
        mRenderer.mTriangle.color[1] = 0.76953125f;
        mRenderer.mTriangle.color[2] = 0.22265625f;
        requestRender();
    }

    private void swipeUp() {
        mRenderer.mSquare.color[0] = 0.6f;
        mRenderer.mSquare.color[1] = 0.1f;
        mRenderer.mSquare.color[2] = 0.5f;
        requestRender();
    }

    private void swipeDown() {
        mRenderer.mSquare.color[0] = 0.2f;
        mRenderer.mSquare.color[1] = 0.709803922f;
        mRenderer.mSquare.color[2] = 0.898039216f;
        requestRender();
    }

    private void move(float x, float y, float dX, float dY) {
        // reverse direction of rotation above the mid-line
        if (y > getHeight() / 2) {
            dX = dX * -1 ;
        }
        // reverse direction of rotation to left of the mid-line
        if (x < getWidth() / 2) {
            dY = dY * -1 ;
        }
        mRenderer.setAngle(
                mRenderer.getAngle() +
                        ((dX + dY) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
        requestRender();
    }

    private Date downTime = new Date();
    float downX = 0, downY = 0;
    double lastDiff = 0;
    boolean wantsToMovePic = false, neverMovePic = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        if(event.getPointerCount() > 1) {
            double diffX = Math.abs(event.getX() - event.getX(event.getPointerId(1)));
            double diffY = Math.abs(event.getY() - event.getY(event.getPointerId(1)));
            double totalDiff = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
            boolean zOut = false;
            if(totalDiff < lastDiff) zOut = true;
            if(lastDiff != 0) zoom(zOut);
            lastDiff = totalDiff;
        }
        else if(event.getAction() == MotionEvent.ACTION_DOWN) {
            Date tempDown = new Date();
            if((tempDown.getTime() - downTime.getTime()) / 100 < 2) {
                doubleTap();
            }
            downX = event.getX();
            downY = event.getY();
            downTime = tempDown;
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            Date temp = new Date();
            if(wantsToMovePic && lastDiff == 0) {
                float deltaX = event.getX() - downX;
                float deltaY = event.getY() - downY;

                move(event.getX(), event.getY(), deltaX, deltaY);

                downX = event.getX();
                downY = event.getY();
            }
            else if(!neverMovePic && (temp.getTime() - downTime.getTime()) / 100 > 2) {
                if(Math.abs(downX - event.getX()) < 15) wantsToMovePic = true;
                else neverMovePic = true;
            }
        }
        else if(event.getAction() == MotionEvent.ACTION_UP) {
            Date upTime = new Date();
            long timeDiff = (upTime.getTime() - downTime.getTime())/100;
            downTime = new Date();
            if(timeDiff < 8 && !wantsToMovePic) {
                float upX = event.getX();
                float upY = event.getY();
                if(Math.abs(upX - downX) > 100) {
                    if(upX > downX && lastDiff == 0) {
                        swipeRight();
                    }
                    else if(lastDiff == 0) {
                        swipeLeft();
                    }
                }
                else if(Math.abs(upY - downY) > 100) {
                    if(upY > downY && lastDiff == 0) {
                        swipeUp();
                    }
                    else if(lastDiff == 0) {
                        swipeDown();
                    }
                }
            }
            lastDiff = 0;
            wantsToMovePic = false;
            neverMovePic = false;
        }
        return true;

    }

}
