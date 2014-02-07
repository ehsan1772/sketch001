package objects;

import static android.opengl.GLES20.GL_LINE_STRIP;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

import com.example.openglsample.programs.TextureShaderProgram;

import data.VertexArray;

public class TexturedPLine {
	private static final int BYTES_PER_FLOAT = 4;
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	private static int pointCounter = 0;
	private static final int FLOATS_PER_POINT = POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT;
	private static float[] testPoints = 
		{0f, -0.5f, 0f, 0f, 0f,
		0.35f, -0.35f, 0f, 0f, 0f,
		.5f, 0f, 0f, 0f, 0f,
		0.35f, 0.35f, 0f, 0f, 0f,
		0f, 0.5f, 0f, 0f, 0f};

	private VertexArray vertexArray;

	public void bindData(TextureShaderProgram textureProgram, float[] points) {
		vertexArray = new VertexArray(points);
		vertexArray.setVertexAttribPointer(0, textureProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
		vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, textureProgram.getTextureCoordinatesAttributeLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);

	}

	public void draw() {
		glDrawArrays(GL_LINE_STRIP, 0, 5);
	}
	
	

	public void bindDataAndDraw(TextureShaderProgram textureProgram, float[] points, int counter) {
		bindData(textureProgram, points);
		glDrawArrays(GL_LINE_STRIP, 0, pointCounter * 2 - 2 );
	}
	
	public void bindDataAndDrawWithThickness(TextureShaderProgram textureProgram, float[] points, int counter) {
		float[] pointStrip = pointStrip(points);
	//	float[] pointStrip = pointStrip(testPoints);
		
		bindData(textureProgram, pointStrip);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, pointCounter * 2);
	}
	
	private static float[] pointStrip(float[] points) {
		pointCounter = 0;
		if ((points.length / FLOATS_PER_POINT) < 2) {
			return null;
		}
		
		float[] result = new float[points.length * 2];
		
		int arrayCounter=0;
		
		while (pointCounter * FLOATS_PER_POINT < points.length) {
			float[] point = getPoint(points, pointCounter);

			float[] nextpoint = getPoint(points, pointCounter + 1);
			float[] previouspoint = getPoint(points, pointCounter - 1);
			
			if (point == null) {
				break;
			}
			
			pointCounter++;
			
			float[] secondpoint = offsetPoint(point, nextpoint, previouspoint, .05f);
			addPoint(point, result, arrayCounter);
			arrayCounter=arrayCounter + FLOATS_PER_POINT;
			addPoint(secondpoint, result, arrayCounter);
			arrayCounter=arrayCounter + FLOATS_PER_POINT;
		}
		
		return result;
		
	}
	
	private static float[] getPoint(float[] points, int counter) {
		
		if ((counter*FLOATS_PER_POINT + 4) > points.length - 1 || counter < 0) {
			return null;
		}
		float[] result = new float[FLOATS_PER_POINT];
		
		result[0] = points[(counter * FLOATS_PER_POINT) + 0];
		result[1] = points[(counter * FLOATS_PER_POINT) + 1];
		result[2] = points[(counter * FLOATS_PER_POINT) + 2];
		result[3] = points[(counter * FLOATS_PER_POINT) + 3];
		
		if (result[0] == -1f) {
			return null;
		}
		
		return result;
	}
	
	private static void addPoint(float[] point, float[] line, int start) {
		line[start + 0] = point[0];
		line[start + 1] = point[1];
		line[start + 2] = point[2];
		line[start + 3] = point[3];
		line[start + 4] = point[4];
	}
	
	private static float getX(float[] points) {
		return points[0];
	}
	
	private static float getY(float[] points) {
		return points[1];
	}
	
	private static float[] offsetPoint(float point[], float next[], float previous[], float thickness) {
		
		float m_1 = 0f;
		float m_2 = 0f;
		float m = 0f;
		
		float x = getX(point);
		float y = getY(point);
		float x2 = x * x;
		float y2 = y * y;
		
		//calculate the slopes
		if (previous != null && next != null) {
			m = (getY(next) - getY(previous)) / (getX(next) - getX(previous));
		} else if (previous != null) {
			m = (getY(point) - getY(previous)) / (getX(point) - getX(previous));
		} else if (next != null) {
			m = (getY(next) - getY(point)) / (getX(next) - getX(point));
		}
		
		m = -(1 / m);
		float m2 = m * m;
		float c = y - (m * x); 
		float c2 = c * c;
		float r2 = thickness * thickness;
		
		float A = m2 +1;
		float B = 2 * ((m*c) - (m * y)- x);
		float B2 = B * B;
		float C = y2 - r2 + x2 - (2*c*y) + c2; 
		
		float delta = (float) ( Math.sqrt(B2 - (4*A*C)));
		
		float resultX = (float) ( delta - B)/ (2*A);
		float resultY = (m * resultX) + c;
		
		
		
		float[] result = new float[FLOATS_PER_POINT];
		
		result[0] = resultX;
		result[1] = resultY;
		result[2] = point[2];
		result[3] = point[3];
		
		return result;

		
	}

}