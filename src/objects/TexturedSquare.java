package objects;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import data.VertexArray;
import util.TextureHelper;
import android.content.Context;
import android.graphics.PointF;

public class TexturedSquare extends TexturedShape {
	
	public TexturedSquare(float width, float height, Context context, int textureId) {
		super(context);
		geometry.add(new PointF(-width/2f, height/2f));
		geometry.add(new PointF(width/2f, height/2f));
		geometry.add(new PointF(-width/2f, -height/2f));
		geometry.add(new PointF(width/2f, -height/2f));
		
		texture = TextureHelper.loadTexture(context, textureId);
		
	}

	@Override
	protected float[] getVertexFloatArray() {
		float[] vertexArray = new float[geometry.size() * 4];
		int counter = 0;
		int geometryCounter = 0;

			vertexArray[counter++] = geometry.get(geometryCounter).x; 
			vertexArray[counter++] = geometry.get(geometryCounter).y; 
			vertexArray[counter++] = 0; 
			vertexArray[counter++] = 0; 
			
			geometryCounter++;
			
			vertexArray[counter++] = geometry.get(geometryCounter).x; 
			vertexArray[counter++] = geometry.get(geometryCounter).y; 
			vertexArray[counter++] = 1; 
			vertexArray[counter++] = 0; 
			
			geometryCounter++;
			
			vertexArray[counter++] = geometry.get(geometryCounter).x; 
			vertexArray[counter++] = geometry.get(geometryCounter).y; 
			vertexArray[counter++] = 0; 
			vertexArray[counter++] = 1; 
			
			geometryCounter++;
			
			vertexArray[counter++] = geometry.get(geometryCounter).x; 
			vertexArray[counter++] = geometry.get(geometryCounter).y; 
			vertexArray[counter++] = 1; 
			vertexArray[counter++] = 1; 
			
			return vertexArray;
	}

	@Override
	public void draw(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void draw(float x, float y, float[] matrix) {
		super.draw(x, y, matrix);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	}

}
