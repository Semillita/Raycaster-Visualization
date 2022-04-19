package rayvis.core;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

import static java.lang.Math.*;

/**
 * Main application class that extends the libGDX framework's class
 * ApplicationAdapter, that provides methods to be called when different events
 * such as creation and updates occur.
 */
public class Application extends ApplicationAdapter {

	public static int WIDTH = 960, HEIGHT = 480;

	private static final int minFOV = 20, maxFOV = 120;
	private static final int minRays = 1, maxRays = 1000;

	/**
	 * Main method that is run when the program starts
	 * 
	 * Creates a LwjglApplication, an application instance that the libGDX framework
	 * uses to run, and a configuration with size and graphics sample amount
	 * specifications.
	 * 
	 * @param args The command line arguments that the program is run with (in this
	 *             case none).
	 */
	public static void main(String[] args) {
		var config = new LwjglApplicationConfiguration();
		config.samples = 2;
		config.width = 960;
		config.height = 480;
		config.resizable = false;

		new LwjglApplication(new Application(), config);
	}

	private User user;
	private RayCamera camera;
	private Map map;
	private Slider fovSlider;
	private Slider raysSlider;
	private long lastFrameTime;

	/**
	 * Called when the application is being created
	 * 
	 * Instantiates the necessary components
	 */
	@Override
	public void create() {
		user = new User();
		camera = new RayCamera();
		map = new Map();
		fovSlider = new Slider("FOV", 20, HEIGHT - 50, minFOV, maxFOV);
		raysSlider = new Slider("Amount of rays", 20, HEIGHT - 80, minRays, maxRays);
		lastFrameTime = System.nanoTime();

		createInputListener();
	}

	/**
	 * Called every frame, parallel to the v-sync
	 * 
	 * Renders the rays and the camera's view and the slider UI elements
	 */
	@Override
	public void render() {
		// Clears the window's frame buffer to a black background
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL11.GL_LINE_SMOOTH);

		final double deltaTime = getDeltaTime();

		user.update(deltaTime, map);
		camera.render(map, user.getPosition(), user.getDirection(), fovSlider.getValue(), raysSlider.getValue());

		fovSlider.draw();
		raysSlider.draw();
	}

	/**
	 * Returns the time passed since the last frame
	 * 
	 * @return the time passed in seconds
	 */
	private double getDeltaTime() {
		final long currentTime = System.nanoTime();
		final double deltaTime = (currentTime - lastFrameTime) / 1_000_000_000d;
		lastFrameTime = currentTime;

		return deltaTime;
	}

	/**
	 * Creates a input listener to react to mouse input events, using libGDX's
	 * built-in input handling
	 */
	private void createInputListener() {
		Gdx.input.setInputProcessor(new InputAdapter() {

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				fovSlider.click(screenX, HEIGHT - screenY);
				raysSlider.click(screenX, HEIGHT - screenY);
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				fovSlider.release();
				raysSlider.release();
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				fovSlider.drag(screenX, HEIGHT - screenY);
				raysSlider.drag(screenX, HEIGHT - screenY);
				return false;
			}
		});
	}

}
