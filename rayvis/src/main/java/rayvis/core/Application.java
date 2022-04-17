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

public class Application extends ApplicationAdapter {

	public static int WIDTH = 960, HEIGHT = 480;
	
	private static double speed = 200;
	
	/**Main method that is run when the program starts
	 * 
	 * @param args The command line arguments that the program is run with 
	 * (in this case none).*/
	public static void main(String[] args) {
		var config = new LwjglApplicationConfiguration();
		config.samples = 2;
		config.width = 960;
		config.height = 480;
		config.resizable = false;
		
		new LwjglApplication(new Application(), config);
	}
	
	private RayCamera camera;
	private Map map;
	private Point position;
	private Slider fovSlider;
	private Slider raysSlider;
	private double direction = 0;
	private long lastFrameTime;
	
	/**Called when the application is being created*/
	@Override
	public void create() {
		camera = new RayCamera();
		map = new Map();
		position = new Point(300, 200);
		fovSlider = new Slider("FOV", 20, HEIGHT - 50, 20, 120);
		raysSlider = new Slider("Amount of rays", 20, HEIGHT - 80, 20, 120);
		lastFrameTime = System.nanoTime();
		
		createInputListener();
	}
	
	/**Called every frame, parallel to the v-sync*/
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL11.GL_LINE_SMOOTH);
		
		final double deltaTime = getDeltaTime();
		
		final long startTime = System.nanoTime();
		
		camera.render(map, position, direction, fovSlider.getValue(), raysSlider.getValue());
	
		fovSlider.draw();
		raysSlider.draw();
		
		final long endTime = System.nanoTime();
		
		final double seconds = (endTime - startTime) / (double) 1_000_000_000;
		//System.out.println("--------------------------------------------------------");
		//System.out.println("Render time: " + seconds + " seconds");
		//System.out.println("--------------------------------------------------------");
		
		if (Gdx.input.isKeyPressed(Keys.W)) {
			move(direction * -1 + 90, deltaTime);
		}
		
		if (Gdx.input.isKeyPressed(Keys.S)) {
			move(direction * -1 - 90, deltaTime);
		}
		
		if (Gdx.input.isKeyPressed(Keys.A)) {
			move(direction * -1 + 180, deltaTime);
		}
		
		if (Gdx.input.isKeyPressed(Keys.D)) {
			move(direction * -1, deltaTime);
		}
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			direction -= 1.5;
		}
		
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			direction += 1.5;
		}
	}
	
	/**Attempts to move the camera in a given angle
	 * 
	 * Uses delta time to achieve a constant speed across different frame rates
	 * 
	 * @param angle the angle to move in
	 * @param deltaTime the time passed since last frame*/
	private void move(double angle, double deltaTime) {
		double movementX = cos(toRadians(angle)) * speed * deltaTime;
		double movementY = sin(toRadians(angle)) * speed * deltaTime;
		
		final double targetX = position.x + movementX;
		final double targetY = position.y + movementY;
		
		if (!(targetX <= 1 || targetX >= map.getWidth() - 1)) {
			position.x = targetX;
		}
		
		if (!(targetY <= 1 || targetY >= map.getHeight() - 1)) {
			position.y = targetY;
		}
	}
	
	/**Returns the time passed since the last frame
	 * 
	 * @return the time passed in seconds*/
	private double getDeltaTime() {
		final long currentTime = System.nanoTime();
		final double deltaTime = (currentTime - lastFrameTime) / 1_000_000_000d;
		lastFrameTime = currentTime;
		
		return deltaTime;
	}
	
	/**Creates a input listener to react to mouse input events*/
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

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}
			
		});
	}
	
}
