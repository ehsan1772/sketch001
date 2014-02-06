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
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import objects.Mallet;
import objects.PLine;
import objects.Table;
import util.MatrixHelper;
import util.TextureHelper;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import static android.opengl.GLES20.*;

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
	
	private int floatCounter;
	
	private static float[] VERTEX_DATA = {
	// Order of coordinates: X, Y, R, G, B
	0f, -0.4f, 0f, 0f, 0f,
	0.2f, -0.2f, 0f, 0f, 0f,
	0.3f, 0f, 0f, 0f, 0f,
	0.2f, 0.2f, 0f, 0f, 0f,
	0f, 0.4f, 0f, 0f, 0f };

	public AirHockeyRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		table = new Table();
		mallet = new Mallet();
		pline = new PLine();
		textureProgram = new TextureShaderProgram(context);
		colorProgram = new ColorShaderProgram(context);
		texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
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

		MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);

		setIdentityM(modelMatrix, 0);
		translateM(modelMatrix, 0, 0f, 0f, -3f);
		rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

		final float[] temp = new float[16];
		multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
		System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
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
		colorProgram.useProgram();
		colorProgram.setUniforms(projectionMatrix);
		mallet.bindData(colorProgram);
		mallet.draw();
		// Draw the lines
		colorProgram.useProgram();
		colorProgram.setUniforms(projectionMatrix);
		pline.bindDataAndDrawWithThickness(colorProgram, VERTEX_DATA, floatCounter);
		//pline.draw();
	}

	public void handleTouchDrag(float normalizedX, float normalizedY) {
		Log.d("Drag", String.valueOf(normalizedX) + " , " + String.valueOf(normalizedX));
		addNewPoint(normalizedX, normalizedY);
	}

	public void handleTouchPress(float normalizedX, float normalizedY) {
		Log.d("Press", String.valueOf(normalizedX) + " , " + String.valueOf(normalizedX));
		VERTEX_DATA = new float[5000];
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
