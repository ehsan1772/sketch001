package objects;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.PointF;
import com.example.openglsample.programs.ShaderProgram;

import data.Color;
import data.VertexArray;

public abstract class Shape {
	protected static final int BYTES_PER_FLOAT = 4;
	
	protected Color color; 
	protected List<PointF> geometry;
	protected ShaderProgram program;
	protected VertexArray vertexArray;
	protected Context context;
	
	public Shape(Context context) {
		this.context = context;
		geometry = new ArrayList<PointF>();
		setProgram();
		
	}

	protected abstract void setProgram();
	protected abstract float[] getVertexFloatArray();	

	
	public abstract void draw(float x, float y);
//	public abstract void draw(float x, float y, float[] matrix);
}
