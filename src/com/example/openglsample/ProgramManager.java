package com.example.openglsample;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import com.example.openglsample.programs.ShaderProgram;

import data.Color;

import android.content.Context;

public abstract class ProgramManager {
	protected WeakReference<Context> contextReference;
	protected ShaderProgram currentProgram;
	protected MatrixManager matrixManager;
	protected HashMap<Program, ShaderProgram> programs;
	
	ProgramManager(Context context, MatrixManager matrixManager) {
		this.matrixManager = matrixManager;
		contextReference = new WeakReference<Context>(context); 
		programs = new HashMap<ProgramManager.Program, ShaderProgram>();
	}
	public enum Program {SIMPLE_TEXTURE_PROGRAM, SIMPLE_COLOR_PROGRAM};
	public enum Attribute {POSITION, TEXTURE};
	abstract boolean addProgram(Program program);
	public abstract ShaderProgram getProgram(Program program);
	public abstract boolean setTexture(int texture);
	public abstract boolean setColor(Color color);
	abstract int getTextureId();
	abstract int getAttributeLocation();
	abstract int getStride();
}
