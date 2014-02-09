package data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import static android.opengl.GLES20.*;

public class VertexArray {
	private final FloatBuffer floatBuffer;
	private final static int BYTES_PER_FLOAT = 4;

	public VertexArray(float[] vertexData) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT);
		buffer.order(ByteOrder.nativeOrder());
		floatBuffer = buffer.asFloatBuffer();
		floatBuffer.put(vertexData);
	}

	public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
		floatBuffer.position(dataOffset);
		glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
		glEnableVertexAttribArray(attributeLocation);
		floatBuffer.position(0);
	}
}
