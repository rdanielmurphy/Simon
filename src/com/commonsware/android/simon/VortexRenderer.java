package com.commonsware.android.simon;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11ExtensionPack;
 
import android.opengl.GLSurfaceView;
 
public class VortexRenderer implements GLSurfaceView.Renderer {
	public enum Color{
		Red,
		Pink,
		Green,
		Blue
	}
	GL11ExtensionPack ggg;
	GL10 gGL;
    private static final String LOG_TAG = VortexRenderer.class.getSimpleName();
 
    private float _red = 0.9f;
    private float _green = 0.2f;
    private float _blue = 0.2f;
    
    boolean gbBlueClicked = true;
    boolean gbRedClicked = false;
    boolean gbPinkClicked = false;
    boolean gbGreenClicked = false;
 
    //@Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Do nothing special.
    }
 
    //@Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
    }
 
    public void setColor(float r, float g, float b) {
        _red = r;
        _green = g;
        _blue = b;
    }
 // a raw buffer to hold the vertices
 private FloatBuffer _vertexBuffer;
 private FloatBuffer _vertexBufferSmall;
 private FloatBuffer _vertexBufferQuarter1;
 private FloatBuffer _vertexBufferQuarter2;
 private FloatBuffer _vertexBufferQuarter3;
 private FloatBuffer _vertexBufferQuarter4;
 
 private void initCircle() {
	 if(_vertexBuffer != null){
	 _vertexBuffer.clear();
	 _vertexBufferSmall.clear();
	 _vertexBufferQuarter1.clear();
	 _vertexBufferQuarter2.clear();
	 _vertexBufferQuarter3.clear();
	 _vertexBufferQuarter4.clear(); 
	 }
	 
     // float has 4 bytes
     ByteBuffer vbb = ByteBuffer.allocateDirect(50 * 4);
     vbb.order(ByteOrder.nativeOrder());
     _vertexBuffer = vbb.asFloatBuffer();
     
     ByteBuffer vbb2 = ByteBuffer.allocateDirect(50 * 4);
     vbb2.order(ByteOrder.nativeOrder());
     _vertexBufferSmall = vbb2.asFloatBuffer();    

     ByteBuffer vbbQ1 = ByteBuffer.allocateDirect(50 * 4);
     vbbQ1.order(ByteOrder.nativeOrder());
     _vertexBufferQuarter1 = vbbQ1.asFloatBuffer();  

     ByteBuffer vbbQ2 = ByteBuffer.allocateDirect(50 * 4);
     vbbQ2.order(ByteOrder.nativeOrder());
     _vertexBufferQuarter2 = vbbQ2.asFloatBuffer();  
     
     ByteBuffer vbbQ3 = ByteBuffer.allocateDirect(50 * 4);
     vbbQ3.order(ByteOrder.nativeOrder());
     _vertexBufferQuarter3 = vbbQ3.asFloatBuffer();      

     ByteBuffer vbbQ4 = ByteBuffer.allocateDirect(50 * 4);
     vbbQ4.order(ByteOrder.nativeOrder());
     _vertexBufferQuarter4 = vbbQ4.asFloatBuffer();  
     
     float[] coords = new float[50];
     float[] coords2 = new float[50];
     float[] coordsq1 = new float[50];
     float[] coordsq2 = new float[50];
     float[] coordsq3 = new float[50];
     float[] coordsq4 = new float[50];
     
     int count = 0;
     
     for(float i = 0; i <= 359.99; i += (360.0/25.0)){
    	 coords[count++] = (float)(Math.cos(Math.toRadians((i)))*.5);
    	 coords[count++] = (float)(Math.sin(Math.toRadians((i)))*.5);
     }
     count = 0;
     for(float i = 0; i <= 359.99; i += (360.0/25.0)){
    	 coords2[count++] = (float)(Math.cos(Math.toRadians((i)))*.15);
    	 coords2[count++] = (float)(Math.sin(Math.toRadians((i)))*.15);
     }   
     count = 0;
     for(float i = 5; i <= 85.99; i += (360.0/25.0)){
    	 coordsq1[count++] = (float)(Math.cos(Math.toRadians((i)))*.4);
    	 coordsq1[count++] = (float)(Math.sin(Math.toRadians((i)))*.4);
     }  
     count = 0;
     for(float i = 95; i <= 174.99; i += (360.0/25.0)){
    	 coordsq2[count++] = (float)(Math.cos(Math.toRadians((i)))*.4);
    	 coordsq2[count++] = (float)(Math.sin(Math.toRadians((i)))*.4);
     }  
     count = 0;
     for(float i = 185; i <= 264.99; i += (360.0/25.0)){
    	 coordsq3[count++] = (float)(Math.cos(Math.toRadians((i)))*.4);
    	 coordsq3[count++] = (float)(Math.sin(Math.toRadians((i)))*.4);
     }  
     count = 0;
     for(float i = 275; i <= 354.99; i += (360.0/25.0)){
    	 coordsq4[count++] = (float)(Math.cos(Math.toRadians((i)))*.4);
    	 coordsq4[count++] = (float)(Math.sin(Math.toRadians((i)))*.4);
     }  
     
     _vertexBuffer.put(coords);
     _vertexBufferSmall.put(coords2);
     _vertexBufferQuarter1.put(coordsq1);
     _vertexBufferQuarter2.put(coordsq2);
     _vertexBufferQuarter3.put(coordsq3);
     _vertexBufferQuarter4.put(coordsq4);
     
     _vertexBuffer.position(0);
     _vertexBufferSmall.position(0);
     _vertexBufferQuarter1.position(0);
     _vertexBufferQuarter2.position(0);
     _vertexBufferQuarter3.position(0);
     _vertexBufferQuarter4.position(0);

 }
 
 //@Override
 public void onDrawFrame(GL10 gl) {
	 
     // define the color we want to be displayed as the "clipping wall"
     gl.glClearColor(_red, _green, _blue, 1.0f);
     
     // clear the color buffer and the depth buffer to show the ClearColor
     // we called above...
     gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
     
     gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
     // set rotation
     //gl.glRotatef(_angle, 0f, 1f, 0f);
     
     initCircle();
 
     gl.glColor4f(0, 0, 0, 0);//black
     gl.glVertexPointer(2, GL10.GL_FLOAT, 0, _vertexBuffer);
     gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 25);
 
     if(gbPinkClicked)
    	 gl.glColor4x(0, 1, 0, 0);
    	 //gl.glColor4f(0.9f, 0f, 0.5f, 1.0f);//light pink
     else
    	 gl.glColor4f(0.9f, 0f, 0.9f, 0.5f);//pink
     gl.glVertexPointer(2, GL10.GL_FLOAT, 0, _vertexBufferQuarter1);
     gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 25);

     if(gbGreenClicked)
    	 gl.glColor4f(0f, 0.9f, 0f, 1.0f);//light green
     else
    	 gl.glColor4f(0f, 0.9f, 0f, 0.5f);//green
     gl.glVertexPointer(2, GL10.GL_FLOAT, 0, _vertexBufferQuarter2);
     gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 25);
     
     if(gbRedClicked)
    	 gl.glColor4f(0.9f, 0f, 0f, 1.0f);//light red
     else
    	 gl.glColor4f(0.9f, 0f, 0f, 0.5f);//red
     gl.glVertexPointer(2, GL10.GL_FLOAT, 0, _vertexBufferQuarter3);
     gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 25);
 
     if(gbBlueClicked)
    	 gl.glColor4f(0f, 0f,0.9f, 1.0f);//light blue
     else
    	 gl.glColor4f(0f, 0f, 0.9f, 0.5f);//blue
     gl.glVertexPointer(2, GL10.GL_FLOAT, 0, _vertexBufferQuarter4);
     gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 25); 
     
     gl.glColor4f(.5f, .4f, .3f, 0);
     gl.glVertexPointer(2, GL10.GL_FLOAT, 0, _vertexBufferSmall);
     gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 25);
     
     gl.glFlush();
     gl.glFinish();
     
     gGL = gl;
         
 }
 private float _angle;
 
 public void setAngle(float angle) {
     _angle = angle;
 }
 
 ByteBuffer bu;
 
 public void ScreenClick(float x, float y){
	 ByteBuffer PixelBuffer = ByteBuffer.allocateDirect(4); 
	 PixelBuffer.order(ByteOrder.nativeOrder()); 
     
	 try{
		 gGL.glReadPixels((int)x, (int)y, 5, 5, javax.microedition.khronos.opengles.GL10.GL_RGBA, javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_BYTE, PixelBuffer);
	 }catch(Exception e){
		 bu = PixelBuffer;
	 }
	 byte b[] = new byte[4];
	 PixelBuffer.get(b);

	 bu = PixelBuffer;
	 
	 if(b[0] == 0 && b[1] == -25 && b[2] == 0){
		 try {
			ColorTouched(Color.Red);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 //Green
	 }
	 else if(b[0] == 0 && b[1] == 0 && b[2] == -25){
		 try {
			ColorTouched(Color.Pink);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //Pink
	 }
	 else if(b[0] == -25 && b[1] == 0 && b[2] == 0){
		 try {
			ColorTouched(Color.Green);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //Red
	 }
	 else if(b[0] == -25 && b[1] == 0 && b[2] == -25){
		 try {
			ColorTouched(Color.Blue);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //Blue
	 }	 
 }
 
 public void ColorTouched(Color c) throws InterruptedException{
	 if (c == Color.Green){		 
		 gbGreenClicked = true;
	     //Thread.sleep(1000, 0);
	 }
	 else if(c == Color.Blue){
		 gbBlueClicked = true;
	 }
	 else if(c == Color.Pink){
		 gbPinkClicked = true;
	 }
	 else if(c == Color.Red){
		 gbRedClicked = true;
	 }     
 }
}
