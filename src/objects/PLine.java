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
		float[] pointStrip = pointStrip(testPoints);
		
		for (int i = 0 ; i < pointStrip.length ; i++) {
			float t = pointStrip[i];
			t = t;
		}
		
//		bindData(colorProgram, testPoints);
//		glDrawArrays(GL_TRIANGLE_STRIP, 0, (counter/ 5 ) - 1);
		
		bindData(colorProgram, pointStrip);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, ((counter * 2 ) / 5) - 1);
	}
	
	private static float[] pointStrip(float[] points) {
		if ((points.length / 5) < 2) {
			return null;
		}
		
		float[] result = new float[points.length * 2];
		
		while (pointCounter * 5 < points.length - 5 ) {
			float[] firstpoint = nextPoint(points);
			pointCounter++;
			float[] thirdpoint = nextPoint(points);
			
			float[] secondpoint = secondPoint(firstpoint, thirdpoint, .05f);
			addPoint(firstpoint, result, (pointCounter * 2) - 1);
			addPoint(secondpoint, result, pointCounter * 2);
		}
		
		return result;
		
	}
	
	private static float[] nextPoint(float[] points) {
		float[] result = new float[5];
		
		result[0] = points[(pointCounter * 5) + 0];
		result[1] = points[(pointCounter * 5) + 1];
		result[2] = points[(pointCounter * 5) + 2];
		result[3] = points[(pointCounter * 5) + 3];
		result[4] = points[(pointCounter * 5) + 4];
		
		return result;
	}
	
	private static void addPoint(float[] point, float[] line, int start) {
		line[start + 0] = point[0];
		line[start + 1] = point[1];
		line[start + 2] = point[2];
		line[start + 3] = point[3];
		line[start + 4] = point[4];
	}
	
	private static float[] secondPoint(float first[], float third[], float thickness) {
		float x1 = first[0];
		float x2 = third[0];
		
		float y1 = first[1];
		float y2 = third[1];
		
		float m = (y2 - y1) / (x2 - x1);
		float newm = - (1 / m);
		
		float newx = (float) (thickness / Math.sqrt(1 + (m * m)));
		float newy = (float) ( (newm * thickness) / Math.sqrt(1 + (m * m)));
		
		float[] result = new float[5];
		
		result[0] = newx;
		result[1] = newy;
		result[2] = first[2];
		result[3] = first[3];
		result[4] = first[4];
		
		return result;
		
	}

}