package com.example.openglsample;

import java.lang.ref.WeakReference;

import android.content.Context;

public class MatrixManager {
	protected WeakReference<Context> contextReference;
	private int width;
	private int height;
	
	public MatrixManager(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public float[] getMatrix() {
		float[] projectionMatrix = new float[16];
		
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
				
		return projectionMatrix;
	}
}
