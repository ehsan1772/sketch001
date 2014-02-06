package com.example.openglsample;

import static android.opengl.GLES20.*;
import android.util.Log;

public class ShaderHelper {
	private static final String TAG = "ShaderHelper";
	
	public static int compileVertexShader(String shaderCode) { 
		return compileShader(GL_VERTEX_SHADER, shaderCode);
	}
	public static int compileFragmentShader(String shaderCode) { 
		return compileShader(GL_FRAGMENT_SHADER, shaderCode);
	}
	private static int compileShader(int type, String shaderCode) {
		final int shaderObjectId = glCreateShader(type);
		if (shaderObjectId == 0) {
			Log.w(TAG, "Could not create new shader."); 
			return 0; 
		}
		
		//passing the code to the Shader instance
		glShaderSource(shaderObjectId, shaderCode);
		
		//compiling the shader
		glCompileShader(shaderObjectId);
		
		final int[] compileStatus = new int[1];
		glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

		String shaderInfo = glGetShaderInfoLog(shaderObjectId);
		// Print the shader info log to the Android log output.
		Log.v(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:"
		        + shaderInfo);
		
		//checking to see if the compile was successful, the results gets written on the 0 index of the passed array

		
		if (compileStatus[0] == 0) {
			glDeleteShader(shaderObjectId);
			// If it failed, delete the shader object. glDeleteShader(shaderObjectId);
			Log.w(TAG, "Compilation of shader failed.");
			return 0; 
		}
		
		return shaderObjectId;
	}
	
	public static int linkProgram(int vertexShaderId, int fragmentShaderId) { 
		final int programObjectId = glCreateProgram();
		if (programObjectId == 0) {
			Log.w(TAG, "Could not create new program");
			return 0; 
		}
		
		//attaching our shaders to the program
		glAttachShader(programObjectId, vertexShaderId);
		glAttachShader(programObjectId, fragmentShaderId);
		
	       // Link the two shaders together into a program.
        glLinkProgram(programObjectId);
		
		//checking to see if the attach was successful, the results gets written on the 0 index of the passed array
		final int[] linkStatus = new int[1]; 
		glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
		
		Log.v(TAG, "Results of linking program:\n"
		        + glGetProgramInfoLog(programObjectId));
		
		if (linkStatus[0] == 0) {
			// If it failed, delete the program object. glDeleteProgram(programObjectId);
			Log.w(TAG, "Linking of program failed."); 
			return 0; 
		}
		
		return programObjectId;
	}
	
	public static boolean validateProgram(int programObjectId) { 
		glValidateProgram(programObjectId);
		final int[] validateStatus = new int[1]; 
		glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0); 
		Log.v(TAG, "Results of validating program: " 
				+ validateStatus[0]
				+ "\nLog:" 
				+ glGetProgramInfoLog(programObjectId)); 
	
	return validateStatus[0] != 0;
	}
	
	public static int buildProgram(String vertexShaderSource,
			String fragmentShaderSource) {
			int program;
			// Compile the shaders.
			int vertexShader = compileVertexShader(vertexShaderSource);
			int fragmentShader = compileFragmentShader(fragmentShaderSource);
			// Link them into a shader program.
			program = linkProgram(vertexShader, fragmentShader);

			validateProgram(program);

			return program;
	}
			
}
