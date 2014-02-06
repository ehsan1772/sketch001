package com.example.openglsample;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	private GLSurfaceView mGLView;
	private boolean rendererSet;
	private AirHockeyRenderer mAirHockeyRenderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity.
		mGLView = new GLSurfaceView(this);

		// Check if the system supports OpenGL ES 2.0.
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		// Even though the latest emulator supports OpenGL ES 2.0,
		// it has a bug where it doesn't set the reqGlEsVersion so
		// the above check doesn't work. The below will detect if the
		// app is running on an emulator, and assume that it supports
		// OpenGL ES 2.0.
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
				|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && (Build.FINGERPRINT.startsWith("generic")
						|| Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk")
						|| Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86")));

		if (supportsEs2) {
			// Request an OpenGL ES 2.0 compatible context.
			mGLView.setEGLContextClientVersion(2);

			// Assign our renderer.
			mAirHockeyRenderer = new AirHockeyRenderer(this);
			mGLView.setRenderer(mAirHockeyRenderer);
			rendererSet = true;
		} else {
			/*
			 * This is where you could create an OpenGL ES 1.x compatible
			 * renderer if you wanted to support both ES 1 and ES 2. Since we're
			 * not doing anything, the app will crash if the device doesn't
			 * support OpenGL ES 2.0. If we publish on the market, we should
			 * also add the following to AndroidManifest.xml:
			 * 
			 * <uses-feature android:glEsVersion="0x00020000"
			 * android:required="true" />
			 * 
			 * This hides our app from those devices which don't support OpenGL
			 * ES 2.0.
			 */
			Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
			return;
		}

		mGLView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event != null) {
					// Convert touch coordinates into normalized device
					// coordinates, keeping in mind that Android's Y
					// coordinates are inverted.
					final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
					final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mGLView.queueEvent(new Runnable() {
							public void run() {
								mAirHockeyRenderer.handleTouchPress(normalizedX, normalizedY);
							}
						});
						break;
					case MotionEvent.ACTION_MOVE:
						mGLView.queueEvent(new Runnable() {
							public void run() {
								mAirHockeyRenderer.handleTouchDrag(normalizedX, normalizedY);
							}
						});
						break;
					}
					return true;
				}
				return false;
			}
		});

		setContentView(mGLView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class MyGLSurfaceView extends GLSurfaceView {

		public MyGLSurfaceView(Context context) {
			super(context);
			// Create an OpenGL ES 2.0 context
			setEGLContextClientVersion(2);

			// Set the Renderer for drawing on the GLSurfaceView
			setRenderer(new AirHockeyRenderer(context));
			rendererSet = true;
			// Render the view only when there is a change in the drawing data
			// setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (rendererSet) {
			mGLView.onPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (rendererSet) {
			mGLView.onResume();
		}
	}
}
