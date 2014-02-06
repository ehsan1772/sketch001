package util;

import static android.opengl.GLES20.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureHelper {
	private static final String TAG = "TextureHelper";
	
	public static int loadTexture(Context context, int resourceId) {
		//generate a texture and acquire the id
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);
		
		// load the bitmap from a resource
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
		
		// catch the possible errors
		if (bitmap == null) {
			Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
			glDeleteTextures(1, textureObjectIds, 0);
			return 0;
			}
		if (textureObjectIds[0] == 0) {
			Log.w(TAG, "Could not generate a new OpenGL texture object.");
			return 0;
		}
		
		// bind the texture and OpenGL
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
	
		// using trilinear mipmaping for 2d minification, check page 125 for other allowable values
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		// using bilinear filtering for magnification
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		
		//load the bitmap data into OpenGL
		GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		
		//clearing the bitmap from the memory since we don't need it
		bitmap.recycle();
		
		//generating all the mipmaps
		glGenerateMipmap(GL_TEXTURE_2D);
		
		//unbinding the texture because we don't want to change it accidently
		glBindTexture(GL_TEXTURE_2D, 0);
		
	return textureObjectIds[0];
}
}
