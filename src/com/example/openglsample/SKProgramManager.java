package com.example.openglsample;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glUniform1i;
import util.TextureHelper;

import com.example.openglsample.programs.ColorShaderProgram;
import com.example.openglsample.programs.ShaderProgram;
import com.example.openglsample.programs.TextureShaderProgram;

import data.Color;

import android.content.Context;

public class SKProgramManager extends ProgramManager{

	SKProgramManager(Context context, MatrixManager matrixManager) {
		super(context, matrixManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean addProgram(Program program) {
		switch (program) {
		case SIMPLE_COLOR_PROGRAM:
			programs.put(program, new ColorShaderProgram(contextReference.get()));
			break;
		case SIMPLE_TEXTURE_PROGRAM:
			programs.put(program, new TextureShaderProgram(contextReference.get()));
			break;
		}
		return true;
	}

	@Override
	int getTextureId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	int getAttributeLocation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	int getStride() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public
	ShaderProgram getProgram(Program program) {
		ShaderProgram shProgram =  programs.get(program);
		if (shProgram == null) {
			addProgram(program);
			shProgram = programs.get(program);
		}
		currentProgram = shProgram;
		//shProgram.useProgram();
		return shProgram;
	}

	@Override
	public boolean setTexture(int texture) {
		int textureId = TextureHelper.loadTexture(contextReference.get(), texture);
		
		// Set the active texture unit to texture unit 0.
		glActiveTexture(GL_TEXTURE0);
		// Bind the texture to this unit.
		glBindTexture(GL_TEXTURE_2D, textureId);
		// Tell the texture uniform sampler to use this texture in the shader by
		// telling it to read from texture unit 0.
		int uTextureUnitLocation = ((TextureShaderProgram) currentProgram).getTextureUnitLocation();
		glUniform1i(uTextureUnitLocation, 0);
		
		return false;
	}

	@Override
	public boolean setColor(Color color) {
		// TODO Auto-generated method stub
		return false;
	}

}
