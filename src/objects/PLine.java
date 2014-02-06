package objects;

import com.example.openglsample.programs.ColorShaderProgram;

import data.VertexArray;
import static android.opengl.GLES20.*;

public class PLine {
	private static final int BYTES_PER_FLOAT = 4;
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int COLOR_COMPONENT_COUNT = 3;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	private static int pointCounter = 0;
	
	private static float[] testPoints = 
		{0f, -0.5f, 0f, 0f, 0f,
		0.35f, -0.35f, 0f, 0f, 0f,
		.5f, 0f, 0f, 0f, 0f,
		0.35f, 0.35f, 0f, 0f, 0f,
		0f, 0.5f, 0f, 0f, 0f};

	private VertexArray vertexArray;

	public void bindData(ColorShaderProgram colorProgram, float[] points) {
		vertexArray = new VertexArray(points);
		vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
		vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, colorProgram.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE);

	}

	public void draw() {
		glDrawArrays(GL_LINE_STRIP, 0, 5);
	}
	
	

	public void bindDataAndDraw(ColorShaderProgram colorProgram, float[] points, int counter) {
		bindData(colorProgram, points);
		glDrawArrays(GL_LINE_STRIP, 0, counter/5);
	}
	
	public void bindDataAndDrawWithThickness(ColorShaderProgram colorProgram, float[] points, int counter) {
	//	float[] pointStrip = pointStrip(points);
		float[] pointStrip = pointStrip(testPoints);
		
		bindData(colorProgram, pointStrip);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, pointStrip.length / 5);
	}
	
	private static float[] pointStrip(float[] points) {
		pointCounter = 0;
		if ((points.length / 5) < 2) {
			return null;
		}
		
		float[] result = new float[points.length * 2];
		
		int arrayCounter=0;
		
		while (pointCounter * 5 < points.length) {
			float[] point = getPoint(points, pointCounter);

			float[] nextpoint = getPoint(points, pointCounter + 1);
			float[] previouspoint = getPoint(points, pointCounter - 1);
			
			pointCounter++;
			
			float[] secondpoint = offsetPoint(point, nextpoint, previouspoint, .05f);
			addPoint(point, result, arrayCounter);
			arrayCounter=arrayCounter + 5;
			addPoint(secondpoint, result, arrayCounter);
			arrayCounter=arrayCounter + 5;
		}
		
		return result;
		
	}
	
	private static float[] getPoint(float[] points, int counter) {
		
		if ((counter*5 + 4) > points.length - 1 || counter < 0) {
			return null;
		}
		float[] result = new float[5];
		
		result[0] = points[(counter * 5) + 0];
		result[1] = points[(counter * 5) + 1];
		result[2] = points[(counter * 5) + 2];
		result[3] = points[(counter * 5) + 3];
		result[4] = points[(counter * 5) + 4];
		
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
		
		
		
		float[] result = new float[5];
		
		result[0] = resultX;
		result[1] = resultY;
		result[2] = point[2];
		result[3] = point[3];
		result[4] = point[4];
		
		return result;
		
		//this is just for test- delete it when done!
//		result[0] = first[0] + 0.05f;
//		result[1] = first[1] + 0.05f;
//		result[2] = first[2];
//		result[3] = first[3];
//		result[4] = first[4];
//		
//		return result;
		
		//test finished
		
//		float x1 = first[0];
//		float x2 = third[0];
//		
//		float y1 = first[1];
//		float y2 = third[1];
//		
//		float m = (y2 - y1) / (x2 - x1);
//		float newm = - (1 / m);
//		
//		float newx = (float) (thickness / Math.sqrt(1 + (m * m)));
//		float newy = (float) ( (newm * thickness) / Math.sqrt(1 + (m * m)));
//		
//
//		
//		result[0] = newx;
//		result[1] = newy;
//		result[2] = first[2];
//		result[3] = first[3];
//		result[4] = first[4];
//		
//		return result;
		
	}

}