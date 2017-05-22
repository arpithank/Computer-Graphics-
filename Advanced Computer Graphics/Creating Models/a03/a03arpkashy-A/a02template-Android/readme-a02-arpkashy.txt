B581-Advanced Computer Graphics
arpkashy
Arpitha N Kashyap

I have completed all three Tasks. 
1)Adding vertices
2)Adding Edges
3)Highlighting Vertices
4)Highlighting Edges
5)Editing vertex position
6)Editing edge position

I have implemented the Proximity function:
**public void proximity(double p, double q) {

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
}**

Added lines of code to the touchesBegin function:
**proximity(this.myTouchXcurrent,this.myTouchYcurrent);
        if(VerNo<16 && vflag==0 && lflag==0)
        {
            gVertexData[VerNo*2] = x;
            gVertexData[VerNo*2+1] = this.myViewPortHeight - (float)(y) - 1.0f;
            VerNo++;
        }**

touchesMoved function:
**if(vflag==1)
        {
            gVertexData[v]=myTouchXcurrent;
            gVertexData[v+1]=myTouchYcurrent;

        }
        if(lflag==1)
        {
            float dx=myTouchXcurrent-myTouchXold;
            float dy=myTouchYcurrent-myTouchYold;
            gVertexData[e]=gVertexData[e]+dx;
            gVertexData[e+1]=gVertexData[e+1]+dy;
            gVertexData[e+2]=gVertexData[e+2]+dx;
            gVertexData[e+3]=gVertexData[e+3]+dy;
        }**

and touchedEnded function:
**vflag=0;
lflag=0;**

Also added code to the draw function:
** if(vflag==1) {
            GLES20.glUniform4f(this.myColorUniform, 1.0f, 1.0f
                    , 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_POINTS,v/2 ,1 );

        }
        if(lflag==1){
            GLES20.glUniform4f(this.myColorUniform, 1.0f, 1.0f
                    , 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, e/2, 2);
        }**

The sequence of commands used to compile and execute my program:
click on the run option on the task bar.
click on Run
A window opens to select my deployment target
i choose Moto G(my android phone) and click on ok.
