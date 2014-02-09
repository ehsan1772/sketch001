/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.example.openglsample;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import objects.Mallet;
import objects.PLine;
import objects.Table;
import objects.TexturedSquare;
import util.TextureHelper;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.example.openglsample.programs.ColorShaderProgram;
import com.example.openglsample.programs.TextureShaderProgram;

public class AirHockeyRenderer implements Renderer {
	private final Context context;
	private final float[] projectionMatrix = new float[16];
	private final float[] modelMatrix = new float[16];
	private Table table;
	private Mallet mallet;
	private PLine pline;
	private TextureShaderProgram textureProgram;
	private ColorShaderProgram colorProgram;
	private int texture;
	
	private float mWidth;
	private float mHeight;
	private float mAspectRatio;
	
	int screenStart = 0;
	
	TexturedSquare sq;
	
	
	private int floatCounter;
	
	private static float[] VERTEX_DATA;

//	private static float[] VERTEX_DATA = {
//	// Order of coordinates: X, Y, R, G, B
//	0f, -0.4f, 0f, 0f, 0f,
//	0.2f, -0.2f, 0f, 0f, 0f,
//	0.3f, 0f, 0f, 0f, 0f,
//	0.2f, 0.2f, 0f, 0f, 0f,
//	0f, 0.4f, 0f, 0f, 0f };

	public AirHockeyRenderer(Context context) {
		this.context = context;
		
		VERTEX_DATA = new float[5000];
		java.util.Arrays.fill(VERTEX_DATA, -1f);
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		table = new Table();
		mallet = new Mallet();
		pline = new PLine();
		textureProgram = new TextureShaderProgram(context);
		colorProgram = new ColorShaderProgram(context);
		texture = TextureHelper.loadTexture(context, R.drawable.line);
		
		sq = new TexturedSquare(.2f, .2f, context,R.drawable.air_hockey_surface);
	}

	/**
	 * onSurfaceChanged is called whenever the surface has changed. This is
	 * called at least once when the surface is initialized. Keep in mind that
	 * Android normally restarts an Activity on rotation, and in that case, the
	 * renderer will be destroyed and a new one created.
	 * 
	 * @param width
	 *            The new width, in pixels.
	 * @param height
	 *            The new height, in pixels.
	 */
	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		// Set the OpenGL viewport to fill the entire surface.
		glViewport(0, 0, width, height);

		//MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
		mWidth = width;
		mHeight = height;
		
		final float aspectRatio = width > height ?
				(float) width / (float) height :
				(float) height / (float) width;
				if (width > height) {
				// Landscape
					android.opengl.Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
				} else {
				// Portrait or square
					android.opengl.Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
				}
				
		mAspectRatio = aspectRatio;
	}

	/**
	 * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
	 * this is done at the refresh rate of the screen.
	 */
	@Override
	public void onDrawFrame(GL10 glUnused) {
		// Clear the rendering surface.
		glClear(GL_COLOR_BUFFER_BIT);
		// Draw the table.
		textureProgram.useProgram();
		textureProgram.setUniforms(projectionMatrix, texture);
		table.bindData(textureProgram);
		table.draw();
		// Draw the mallets.
//		colorProgram.useProgram();
//		colorProgram.setUniforms(projectionMatrix);
//		mallet.bindData(colorProgram);
//		mallet.draw();
		// Draw the lines
		colorProgram.useProgram();
		colorProgram.setUniforms(projectionMatrix);
		pline.bindDataAndDrawWithThickness(colorProgram, VERTEX_DATA, floatCounter);
		//pline.draw();
		
		
		sq.draw(0f,0f, projectionMatrix);
	}

	public void handleTouchDrag(float normalizedX, float normalizedY) {
		if (mWidth > mHeight) {
		// Landscape
			normalizedX = normalizedX * mAspectRatio;
		} else {
		// Portrait or square
			normalizedY = normalizedY * mAspectRatio;
		}
		Log.d("Drag", String.valueOf(normalizedX) + " , " + String.valueOf(normalizedX));
		addNewPoint(normalizedX, normalizedY);
	}

	public void handleTouchPress(float normalizedX, float normalizedY) {
		if (mWidth > mHeight) {
		// Landscape
			normalizedX = normalizedX * mAspectRatio;
		} else {
		// Portrait or square
			normalizedY = normalizedY * mAspectRatio;
		}
		Log.d("Press", String.valueOf(normalizedX) + " , " + String.valueOf(normalizedX));
		VERTEX_DATA = new float[5000];
		java.util.Arrays.fill(VERTEX_DATA, -1f);
		floatCounter = 0;
		addNewPoint(normalizedX, normalizedY);
	}
	
	private void addNewPoint(float x, float y) {
		if (floatCounter < VERTEX_DATA.length ) {
			VERTEX_DATA[floatCounter++] = x;
			VERTEX_DATA[floatCounter++] = y;
			VERTEX_DATA[floatCounter++] = 0f;
			VERTEX_DATA[floatCounter++] = 0f;
			VERTEX_DATA[floatCounter++] = 0f;
		}
	}
}
