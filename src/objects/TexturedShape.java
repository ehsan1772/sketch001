package objects;

import android.content.Context;

import com.example.openglsample.programs.TextureShaderProgram;

import data.VertexArray;

public abstract class TexturedShape extends Shape {
	protected static final int POSITION_COMPONENT_COUNT = 2;
	protected static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	protected static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	protected int texture;
	
	public TexturedShape(Context context) {
		super(context);
	}

	protected void setProgram() {
		program = new TextureShaderProgram(context);
	}

	public void draw(float x, float y, float[] matrix) {
		if (vertexArray == null) {
			vertexArray = new VertexArray(getVertexFloatArray());
		}
		
		program.useProgram();
		((TextureShaderProgram) program).setUniforms(matrix, texture);
		vertexArray.setVertexAttribPointer(0, ((TextureShaderProgram) program).getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
		vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, ((TextureShaderProgram) program).getTextureCoordinatesAttributeLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
	}

}
