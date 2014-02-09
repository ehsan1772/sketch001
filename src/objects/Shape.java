package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.openglsample.programs.ColorShaderProgram;
import com.example.openglsample.programs.ShaderProgram;
import com.example.openglsample.programs.TextureShaderProgram;

import data.Color;
import data.VertexArray;
import android.content.Context;
import android.graphics.PointF;

public abstract class Shape {
	protected static final int BYTES_PER_FLOAT = 4;
	protected static final int POSITION_COMPONENT_COUNT = 2;
	protected static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	protected static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	
	protected Color color; 
	protected List<PointF> geometry;
	protected DrawingTechnique drawingTechnique;
	protected VertexArray vertexArray;
	protected HashMap<DrawingTechnique, ShaderProgram> shaderPrograms;
	protected HashMap<DrawingTechnique, VertexArray> vertexArrays;
	protected Context context;
	public enum DrawingTechnique {COLOR, TEXTURE};
	
	public Shape(Context context) {
		this.context = context;
		shaderPrograms = new HashMap<Shape.DrawingTechnique, ShaderProgram>();
		vertexArrays = new HashMap<Shape.DrawingTechnique, VertexArray>();
		geometry = new ArrayList<PointF>();
		drawingTechnique = DrawingTechnique.TEXTURE;
		color = new Color();
	}
	
	protected ColorShaderProgram generateColorShaderProgram() {
		return new ColorShaderProgram(context);
	}
	protected TextureShaderProgram generateTextureShaderProgram(){
		return new TextureShaderProgram(context);
	}
	
	abstract VertexArray generateColorVertexArray();
	abstract VertexArray generateTextureVertexArray();
	
	public abstract void draw(float x, float y);
	public abstract void draw(float x, float y, float[] matrix);
	
	public void addDrawingTechnique(DrawingTechnique technique, ShaderProgram program) {
		shaderPrograms.put(technique, program);
	}
	
	public void generateProgram() {
		switch (drawingTechnique) {
			case COLOR:
				shaderPrograms.put(DrawingTechnique.COLOR, generateColorShaderProgram());
				break;
			case TEXTURE:
				shaderPrograms.put(DrawingTechnique.TEXTURE, generateTextureShaderProgram());
				break;
		}
	}
	
	public void generateVertexArray() {
		switch (drawingTechnique) {
			case COLOR:
				vertexArrays.put(DrawingTechnique.COLOR, generateColorVertexArray());
				break;
			case TEXTURE:
				vertexArrays.put(DrawingTechnique.TEXTURE, generateTextureVertexArray());
				break;
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public DrawingTechnique getDrawingTechnique() {
		return drawingTechnique;
	}
	
	public void setDrawingTechnique(DrawingTechnique drawingTechnique) {
		this.drawingTechnique = drawingTechnique;
	}
}
