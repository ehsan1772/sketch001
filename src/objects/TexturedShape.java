package objects;

import android.content.Context;

public abstract class TexturedShape extends Shape {
	protected static final int POSITION_COMPONENT_COUNT = 2;
	protected static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	protected static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	
	public TexturedShape(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

}
