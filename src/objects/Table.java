package objects;

import com.example.openglsample.programs.TextureShaderProgram;

import data.VertexArray;
import static android.opengl.GLES20.*;

public class Table {
	private static final int BYTES_PER_FLOAT = 4;
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

//	private static final float[] VERTEX_DATA = {
//		// Order of coordinates: X, Y, S, T
//		// Triangle Fan
//		0f, 0f, 0.5f, 0.5f,
//		-0.5f, -0.8f, 0f, 0.9f,
//		0.5f, -0.8f, 1f, 0.9f,
//		0.5f, 0.8f, 1f, 0.1f,
//		-0.5f, 0.8f, 0f, 0.1f,
//		-0.5f, -0.8f, 0f, 0.9f };
	
	private static final float[] VERTEX_DATA = {
		// Order of coordinates: X, Y, S, T
		// Triangle Fan
		-0.05f, 0.8f, 0f, 0f,
		0.05f, 0.8f, 1f, 0f,
		-0.05f, 0.6f, 0f, .2f,
		0.05f, 0.6f, 1f, .2f,
		-0.05f, 0.4f, 0f, .4f,
		0.05f, 0.4f, 1f, .4f,
		-0.05f, 0.2f, 0f, .6f,
		0.05f, 0.2f, 1f, .6f,
		-0.05f, 0f, 0f, 0.8f,
		0.05f, 0f, 1f, 0.8f,
		-0.05f, -0.2f, 0f, 1f,
		0.05f, -0.2f, 1f, 1f,
		-0.05f, -0.2f, 0f, 0f,
		0.05f, -0.2f, 1f, 0f,
		-0.05f, -0.4f, 0f, .2f,
		0.05f, -0.4f, 1f, .2f,
		-0.05f, -0.6f, 0f, .4f,
		0.05f, -0.6f, 1f, .4f,
		-0.05f, -0.8f, 0f, .6f,
		0.05f, -0.8f, 1f, .6f 
		};

	private final VertexArray vertexArray;

	public Table() {
		vertexArray = new VertexArray(VERTEX_DATA);
	}

	public void bindData(TextureShaderProgram textureProgram) {
		vertexArray.setVertexAttribPointer(0, textureProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
		vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, textureProgram.getTextureCoordinatesAttributeLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
	}

	public void draw() {
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 18);
	}

}
