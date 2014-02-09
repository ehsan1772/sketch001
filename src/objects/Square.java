package objects;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import objects.Shape.DrawingTechnique;
import util.TextureHelper;

import com.example.openglsample.R;
import com.example.openglsample.programs.ColorShaderProgram;
import com.example.openglsample.programs.TextureShaderProgram;

import data.VertexArray;
import android.content.Context;
import android.graphics.PointF;

public class Square extends Shape {
	int texture;
	public Square(float width, float height, Context context, int textureId) {
		super(context);
		geometry.add(new PointF(-width/2f, height/2f));
		geometry.add(new PointF(width/2f, height/2f));
		geometry.add(new PointF(-width/2f, -height/2f));
		geometry.add(new PointF(width/2f, -height/2f));
		
		drawingTechnique = DrawingTechnique.TEXTURE;
		
		generateVertexArray();
		generateProgram();
		
		texture = TextureHelper.loadTexture(context, textureId);
	}


	@Override
	public void draw(float x, float y) {
		// TODO Auto-generated method stub
		
	}


	@Override
	VertexArray generateColorVertexArray() {
		float[] vertexArray = new float[geometry.size() * 6];
		int counter = 0;
		for (PointF point: geometry) {
			vertexArray[counter++] = point.x; 
			vertexArray[counter++] = point.y; 
			vertexArray[counter++] = color.r; 
			vertexArray[counter++] = color.b; 
			vertexArray[counter++] = color.g; 
			vertexArray[counter++] = color.alpha; 
		}
		return new VertexArray(vertexArray);
	}


	@Override
	VertexArray generateTextureVertexArray() {
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

		return new VertexArray(vertexArray);
	}


	@Override
	public void draw(float x, float y, float[] matrix) {
		
		

		
		
		TextureShaderProgram textureProgram = (TextureShaderProgram) shaderPrograms.get(DrawingTechnique.TEXTURE);
		vertexArray = vertexArrays.get(DrawingTechnique.TEXTURE);
		
		textureProgram.useProgram();
		textureProgram.setUniforms(matrix, texture);
		
		vertexArray.setVertexAttribPointer(0,  textureProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
		vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, textureProgram.getTextureCoordinatesAttributeLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
		
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		
	}

}
