package com.example.ffmpegav;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

public abstract class BaseUIActivity extends FragmentActivity {

	private static final String TAG = "BaseUIActivity";

	protected int surfaceWidth = 0;
	protected int surfaceHeight = 0;

	protected boolean Isrender = false;
	protected boolean bOnSurfaceCreated = false;
	private static Bitmap mBackground;

	public GLsurfaceViewActivity glView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// //////////////////////////////////////////////////////////////////////////
	public class GLsurfaceViewActivity extends BaseGLSurfaceView {
		private String playurl = "";
		private final boolean DEBUG = false;
		public EGL10 mEgl = null;
		public ArrayList<Runnable> mMsEventQueue = new ArrayList<Runnable>();

		public GLsurfaceViewActivity(Context context, AttributeSet Attrs) {

			super(context, Attrs);
			init(true, 0, 0);
		}

		public GLsurfaceViewActivity(Context context, String playurl,
				boolean translucent) {
			super(context);
			this.playurl = playurl;
			init(translucent, 0, 0);

		}

		public GLsurfaceViewActivity(Context context, boolean translucent,
				int depth, int stencil) {
			super(context);
			init(translucent, depth, stencil);
		}

		public void setOgImage(Bitmap image, String appName, String sceneName,
				String objName, int subEntityID, int techID, int passID,
				int stateID, boolean formatFlag) {
		}

		public void setContext(Context context) {
			init(false, 0, 0);
		}

		private void init(boolean translucent, int depth, int stencil) {

			/*
			 * By default, GLSurfaceView() creates a RGB_565 opaque surface. If
			 * we want a translucent one, we should change the surface's format
			 * here, using PixelFormat.TRANSLUCENT for GL Surfaces is
			 * interpreted as any 32-bit surface with alpha by SurfaceFlinger.
			 */
			if (translucent) {
				this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
			}

			setPreserveEGLContextOnPause(true);

			/*
			 * Setup the context factory for 2.0 rendering. See ContextFactory
			 * class definition below
			 */
			setEGLContextFactory(new ContextFactory());

			/*
			 * We need to choose an EGLConfig that matches the format of our
			 * surface exactly. This is going to be done in our custom config
			 * chooser. See ConfigChooser class definition below.
			 */
			setEGLConfigChooser(translucent ? new ConfigChooser(8, 8, 8, 8,
					depth, stencil) : new ConfigChooser(5, 6, 5, 0, depth,
					stencil));

			/* Set the renderer responsible for frame rendering */
			setRenderer(new Renderer());

		}

		private class ContextFactory implements
				EGLContextFactory {
			private int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

			public EGLContext createContext(EGL10 egl, EGLDisplay display,
					EGLConfig eglConfig) {
				checkEglError("Before eglCreateContext", egl);
				int[] attrib_list = { EGL_CONTEXT_CLIENT_VERSION, 2,
						EGL10.EGL_NONE };
				EGLContext context = egl.eglCreateContext(display, eglConfig,
						EGL10.EGL_NO_CONTEXT, attrib_list);
				checkEglError("After eglCreateContext", egl);
				mEgl = egl;
				return context;
			}

			public void destroyContext(EGL10 egl, EGLDisplay display,
					EGLContext context) {
				egl.eglDestroyContext(display, context);
			}
		}

		private void checkEglError(String prompt, EGL10 egl) {
			int error;
			while ((error = egl.eglGetError()) != EGL10.EGL_SUCCESS) {
				Log.e(TAG, String.format("%s: EGL error: 0x%x", prompt, error));
			}
		}

		private class ConfigChooser implements
				EGLConfigChooser {

			public ConfigChooser(int r, int g, int b, int a, int depth,
					int stencil) {
				mRedSize = r;
				mGreenSize = g;
				mBlueSize = b;
				mAlphaSize = a;
				mDepthSize = depth;
				mStencilSize = stencil;
			}

			/*
			 * This EGL config specification is used to specify 2.0 rendering.
			 * We use a minimum size of 4 bits for red/green/blue, but will
			 * perform actual matching in chooseConfig() below.
			 */
			private int EGL_OPENGL_ES2_BIT = 4;
			private int[] s_configAttribs2 = { EGL10.EGL_RED_SIZE, 4,
					EGL10.EGL_GREEN_SIZE, 4, EGL10.EGL_BLUE_SIZE, 4,
					EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
					EGL10.EGL_SAMPLE_BUFFERS, 1, EGL10.EGL_SAMPLES, 4,
					EGL10.EGL_NONE };

			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {

				/*
				 * Get the number of minimally matching EGL configurations
				 */
				int[] num_config = new int[1];
				egl.eglChooseConfig(display, s_configAttribs2, null, 0,
						num_config);

				int numConfigs = num_config[0];

				if (numConfigs <= 0) {
					throw new IllegalArgumentException(
							"No configs match configSpec");
				}

				/*
				 * Allocate then read the array of minimally matching EGL
				 * configs
				 */
				EGLConfig[] configs = new EGLConfig[numConfigs];
				egl.eglChooseConfig(display, s_configAttribs2, configs,
						numConfigs, num_config);

				if (DEBUG) {
					printConfigs(egl, display, configs);
				}
				/*
				 * Now return the "best" one
				 */
				return chooseConfig(egl, display, configs);
			}

			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display,
					EGLConfig[] configs) {
				for (EGLConfig config : configs) {
					int d = findConfigAttrib(egl, display, config,
							EGL10.EGL_DEPTH_SIZE, 0);
					int s = findConfigAttrib(egl, display, config,
							EGL10.EGL_STENCIL_SIZE, 0);

					// We need at least mDepthSize and mStencilSize bits
					if (d < mDepthSize || s < mStencilSize)
						continue;

					// We want an *exact* match for red/green/blue/alpha
					int r = findConfigAttrib(egl, display, config,
							EGL10.EGL_RED_SIZE, 0);
					int g = findConfigAttrib(egl, display, config,
							EGL10.EGL_GREEN_SIZE, 0);
					int b = findConfigAttrib(egl, display, config,
							EGL10.EGL_BLUE_SIZE, 0);
					int a = findConfigAttrib(egl, display, config,
							EGL10.EGL_ALPHA_SIZE, 0);

					if (r == mRedSize && g == mGreenSize && b == mBlueSize
							&& a == mAlphaSize)
						return config;
				}
				return null;
			}

			private int findConfigAttrib(EGL10 egl, EGLDisplay display,
					EGLConfig config, int attribute, int defaultValue) {

				if (egl.eglGetConfigAttrib(display, config, attribute, mValue)) {
					return mValue[0];
				}
				return defaultValue;
			}

			private void printConfigs(EGL10 egl, EGLDisplay display,
					EGLConfig[] configs) {
				int numConfigs = configs.length;
				Log.w(TAG, String.format("%d configurations", numConfigs));
				for (int i = 0; i < numConfigs; i++) {
					Log.w(TAG, String.format("Configuration %d:\n", i));
					printConfig(egl, display, configs[i]);
				}
			}

			private void printConfig(EGL10 egl, EGLDisplay display,
					EGLConfig config) {
				int[] attributes = {
						EGL10.EGL_BUFFER_SIZE,
						EGL10.EGL_ALPHA_SIZE,
						EGL10.EGL_BLUE_SIZE,
						EGL10.EGL_GREEN_SIZE,
						EGL10.EGL_RED_SIZE,
						EGL10.EGL_DEPTH_SIZE,
						EGL10.EGL_STENCIL_SIZE,
						EGL10.EGL_CONFIG_CAVEAT,
						EGL10.EGL_CONFIG_ID,
						EGL10.EGL_LEVEL,
						EGL10.EGL_MAX_PBUFFER_HEIGHT,
						EGL10.EGL_MAX_PBUFFER_PIXELS,
						EGL10.EGL_MAX_PBUFFER_WIDTH,
						EGL10.EGL_NATIVE_RENDERABLE,
						EGL10.EGL_NATIVE_VISUAL_ID,
						EGL10.EGL_NATIVE_VISUAL_TYPE,
						0x3030, // EGL10.EGL_PRESERVED_RESOURCES,
						EGL10.EGL_SAMPLES,
						EGL10.EGL_SAMPLE_BUFFERS,
						EGL10.EGL_SURFACE_TYPE,
						EGL10.EGL_TRANSPARENT_TYPE,
						EGL10.EGL_TRANSPARENT_RED_VALUE,
						EGL10.EGL_TRANSPARENT_GREEN_VALUE,
						EGL10.EGL_TRANSPARENT_BLUE_VALUE,
						0x3039, // EGL10.EGL_BIND_TO_TEXTURE_RGB,
						0x303A, // EGL10.EGL_BIND_TO_TEXTURE_RGBA,
						0x303B, // EGL10.EGL_MIN_SWAP_INTERVAL,
						0x303C, // EGL10.EGL_MAX_SWAP_INTERVAL,
						EGL10.EGL_LUMINANCE_SIZE, EGL10.EGL_ALPHA_MASK_SIZE,
						EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RENDERABLE_TYPE,
						0x3042 // EGL10.EGL_CONFORMANT
				};
				String[] names = { "EGL_BUFFER_SIZE", "EGL_ALPHA_SIZE",
						"EGL_BLUE_SIZE", "EGL_GREEN_SIZE", "EGL_RED_SIZE",
						"EGL_DEPTH_SIZE", "EGL_STENCIL_SIZE",
						"EGL_CONFIG_CAVEAT", "EGL_CONFIG_ID", "EGL_LEVEL",
						"EGL_MAX_PBUFFER_HEIGHT", "EGL_MAX_PBUFFER_PIXELS",
						"EGL_MAX_PBUFFER_WIDTH", "EGL_NATIVE_RENDERABLE",
						"EGL_NATIVE_VISUAL_ID", "EGL_NATIVE_VISUAL_TYPE",
						"EGL_PRESERVED_RESOURCES", "EGL_SAMPLES",
						"EGL_SAMPLE_BUFFERS", "EGL_SURFACE_TYPE",
						"EGL_TRANSPARENT_TYPE", "EGL_TRANSPARENT_RED_VALUE",
						"EGL_TRANSPARENT_GREEN_VALUE",
						"EGL_TRANSPARENT_BLUE_VALUE",
						"EGL_BIND_TO_TEXTURE_RGB", "EGL_BIND_TO_TEXTURE_RGBA",
						"EGL_MIN_SWAP_INTERVAL", "EGL_MAX_SWAP_INTERVAL",
						"EGL_LUMINANCE_SIZE", "EGL_ALPHA_MASK_SIZE",
						"EGL_COLOR_BUFFER_TYPE", "EGL_RENDERABLE_TYPE",
						"EGL_CONFORMANT" };
				int[] value = new int[1];
				for (int i = 0; i < attributes.length; i++) {
					int attribute = attributes[i];
					String name = names[i];
					if (egl.eglGetConfigAttrib(display, config, attribute,
							value)) {
						Log.w(TAG, String.format("  %s: %d\n", name, value[0]));
					} else {
						while (egl.eglGetError() != EGL10.EGL_SUCCESS)
							;
					}
				}
			}

			// Subclasses can adjust these values:
			protected int mRedSize;
			protected int mGreenSize;
			protected int mBlueSize;
			protected int mAlphaSize;
			protected int mDepthSize;
			protected int mStencilSize;
			private int[] mValue = new int[1];
		}

		private class Renderer implements BaseGLSurfaceView.Renderer {

			// if render one frame use > 15ms , that is GPU work to slow ,change
			// to sw render

			final long one_render_tolerance = 15;
			final long max_detectCnt = 200;
			int rel = 0, detectCnt = 0, runCnt = 0;
			boolean bsystem_error = false;
			long pretime = 0, dec = 0;

			void RenderModeAutoDetect(int dec) {

				if (dec > one_render_tolerance) {
					detectCnt++;
				}

				if (detectCnt > (max_detectCnt / 2)) {

					// force to software render
				}
			}

			public boolean onDrawFrame(GL10 gl) {

				if (bOnSurfaceCreated == true && bsystem_error == false) {
					rel = nativeDecodeFrameFromFile();
					if (rel > 0)// decode ok ;
					{
						pretime = System.currentTimeMillis();
						nativeRenderFrame();
						if (runCnt < max_detectCnt) {
							runCnt++;
							dec = System.currentTimeMillis() - pretime;
							RenderModeAutoDetect((int) dec);
						}

					}
					if (rel == -2)// net disconnect
					{
						Log.e(TAG, "get socket error!!!");
						bsystem_error = true;
					}
				}
				return (rel == 1) ? true : false;
			}

			public void onSurfaceChanged(GL10 gl, int width, int height) {

				startPlayer(playurl);

				new FFMpegAV().startreadThread();

				nativeInitViewport(width, height, 0);

				if (BaseUIActivity.this.Isrender) {
					surfaceWidth = width;
					surfaceHeight = height;

				}
			}

			public void onSurfaceCreated(GL10 gl, EGLConfig config) {

				mBackground = Bitmap.createBitmap(640, 480,
						Bitmap.Config.RGB_565);
				bOnSurfaceCreated = nativeOpenVideo(mBackground) == 0;
			}

			public void onSurfaceDestroyed() {

				bOnSurfaceCreated = false;
				nativeCloseVideo();
				stopPlayer();

			}
		}
	}

	static {

		try {
			System.loadLibrary("avjni");
		} catch (UnsatisfiedLinkError use) {
			Log.e("Gimi", "Error: Could not load avjni.so");
		}

	}

	private native int nativeInitViewport(int w, int h, int print_msg);

	private native int nativeOpenVideo(Object bitmap);

	private native int nativeDecodeFrameFromFile(); // never touch the bitmap
													// here

	private native int nativeRenderFrame(); // never touch the bitmap here

	private native void startPlayer(String string);

	public native void stopPlayer();

	public native void nativeCloseVideo();

}
