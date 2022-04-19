package rayvis.core;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * Class to represent a generic user. Can be evolved into a player or light
 * source class.
 */
public class User {

	private static final int SPEED = 200;
	private static final double ROTATION_SPEED = 1.5;

	private Point position;
	private double direction;

	public User() {
		position = new Point(300, 200);
		direction = 0;
	}

	/**
	 * Updates the user's position and direction based on current keyboard input
	 * 
	 * Uses the libGDX framework's built in keyboard activity check functions to
	 * check if a certain key is pressed
	 * 
	 * @param deltaTime the time passed since the last frame, in seconds. Used to
	 *                  ensure a constant speed not dependent on the monitor's frame
	 *                  rate
	 * @param map       the map that the user moves in. Used to ensure that the user
	 *                  does not move outside of the map
	 */
	public void update(double deltaTime, Map map) {
		if (Gdx.input.isKeyPressed(Keys.W)) {
			move(direction * -1 + 90, deltaTime, map);
		}

		if (Gdx.input.isKeyPressed(Keys.S)) {
			move(direction * -1 - 90, deltaTime, map);
		}

		if (Gdx.input.isKeyPressed(Keys.A)) {
			move(direction * -1 + 180, deltaTime, map);
		}

		if (Gdx.input.isKeyPressed(Keys.D)) {
			move(direction * -1, deltaTime, map);
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			direction -= ROTATION_SPEED;
		}

		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			direction += ROTATION_SPEED;
		}
	}

	/**
	 * Returns the position of the user
	 * 
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Returns the direction of the user
	 * 
	 * @return the direction
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * Attempts to move the user in a given direction
	 * 
	 * Uses delta time to achieve a constant speed across different frame rates
	 * 
	 * @param direction the direction to move in
	 * @param deltaTime the time passed since last frame
	 * @param map       the map that the user moves in, used to ensure that the user
	 *                  doesn't move outside of the map
	 */
	private void move(double direction, double deltaTime, Map map) {
		double movementX = cos(toRadians(direction)) * SPEED * deltaTime;
		double movementY = sin(toRadians(direction)) * SPEED * deltaTime;

		final double targetX = position.x + movementX;
		final double targetY = position.y + movementY;

		if (!(targetX <= 1 || targetX >= map.getWidth() - 1)) {
			position.x = targetX;
		}

		if (!(targetY <= 1 || targetY >= map.getHeight() - 1)) {
			position.y = targetY;
		}
	}

}
