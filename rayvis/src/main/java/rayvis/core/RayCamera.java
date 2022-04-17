package rayvis.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import static java.lang.Math.*;

import org.lwjgl.opengl.GL11;

public class RayCamera {

	ShapeRenderer renderer;

	public RayCamera() {
		renderer = new ShapeRenderer();
	}

	/**
	 * Renders the map from a first person point of view and a top-down point of
	 * view
	 * 
	 * Uses a custom raycasting method to render graphics
	 * 
	 * @param map       the map containing the walls
	 * @param position  the position of the user
	 * @param direction the direction of the user, in degrees
	 */
	public void render(Map map, Point position, double direction, int FOV, int amountOfRays) {

		// Prepare the renderer
		renderer.setAutoShapeType(true);
		renderer.begin();
		renderer.set(ShapeType.Filled);

		// General variables for raycasting
		final double cameraDistanceToScreen = (double) ((Gdx.graphics.getWidth() / 4) / tan((toRadians(FOV / 2))));
		final double widthPerRay = map.getWidth() / (double) amountOfRays;

		// Goes through every ray and renders its view on screen
		for (int rayIndex = 0; rayIndex < amountOfRays; rayIndex++) {

			// Finds the offset on the screen at which the ray hits. These are meant to be
			// evenly distanced to simulate what the human eye sees
			double screenCollisionOffset = rayIndex * widthPerRay + widthPerRay * 0.5;
			
			double angleFromCamera = toDegrees(
					atan((screenCollisionOffset - Application.WIDTH / 4) / cameraDistanceToScreen));
			double rayAngle = direction + angleFromCamera;

			// Places the angle within a range 0 <= angle < 360
			while (rayAngle < 0) {
				rayAngle += 360;
			}
			rayAngle %= 360;

			// Retrieves and draws the ray's wall collision
			Point collision = getCollision(map, position, rayAngle);
			if (collision != null) {
				drawRay(position, collision, rayIndex, widthPerRay, angleFromCamera);
			}
		}

		// Draw the user in the top-down view
		renderer.setColor(Color.WHITE);
		renderer.circle((float) position.x, (float) position.y, 5);

		// Draw lines for the walls in the top-down view
		for (var wall : map.getWalls()) {
			renderer.line((float) wall.x1, (float) wall.y1, (float) wall.x2, (float) wall.y2);
		}

		renderer.end();
	}

	/**
	 * Draws a ray in the top-down view and a wall segment that it collided with
	 * 
	 * @param position        the position of the user
	 * @param collision       the point where the ray collided with the closest wall
	 * @param rayIndex        the index of the ray in a order from left to right
	 * @param widthPerRay     the amount of pixels which a ray's collision takes up
	 *                        in the first person view
	 * @param angleFromCamera the ray's direction relative to the camera's direction
	 */
	private void drawRay(Point position, Point collision, int rayIndex, double widthPerRay, double angleFromCamera) {

		// Draw the ray in the top-down view
		renderer.setColor(Color.WHITE);
		renderer.line((float) position.x, (float) position.y, (float) collision.x, (float) collision.y);

		final double leftX = Application.WIDTH / 2 + rayIndex * widthPerRay;
		final double distanceX = position.x - collision.x;
		final double distanceY = position.y - collision.y;

		// Find and adjust the distance between the camera and the collision to simulate
		// a realistic vision
		double distance = sqrt(distanceX * distanceX + distanceY * distanceY);
		distance *= cos(toRadians(abs(angleFromCamera)));
		final double height = 15_000 / distance;

		// Draw a rectangle for the wall segment that the ray collided with in the first
		// person view
		renderer.setColor(
				new Color(1 - (float) distance / 500, 1 - (float) distance / 500, 1 - (float) distance / 500, 1));
		renderer.rect((float) leftX, (float) (Application.HEIGHT / 2 - height / 2), (float) widthPerRay,
				(float) height);

	}

	/**
	 * Returns the closest point in which a ray intersects a wall
	 * 
	 * null is returned if the ray intersects no wall
	 * 
	 * @param map      the map containing the walls
	 * @param position the position of the user
	 * @param rayAngle the absolute angle of the ray's direction
	 * 
	 * @return the point of closest collision between the ray and a wall
	 */
	private Point getCollision(Map map, Point position, double rayAngle) {

		// Finding direction vectors to describe the angle
		final double dirX = cos(toRadians(-rayAngle + 90));
		final double dirY = sin(toRadians(-rayAngle + 90));

		// Checking all walls for collision and saving the point of the closest
		// collision
		Point closestPoint = null;
		double smallestDistance = -1;

		for (var wall : map.getWalls()) {

			// Using a formula to determine whether or not a ray intersects a line segment
			// (the wall) and in that case where
			final double x1 = wall.x1;
			final double y1 = wall.y1;
			final double x2 = wall.x2;
			final double y2 = wall.y2;

			final double x3 = position.x;
			final double y3 = position.y;
			final double x4 = position.x + dirX;
			final double y4 = position.y + dirY;

			final double den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

			// Checking if the denominator is 0, meaning that the ray is parallel to the
			// wall
			if (den == 0) {
				continue;
			}

			final double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / den;
			final double u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / den;

			// Checking if the ray intersects the line
			if (t > 0 && t < 1 && u > 0) {

				// Finding points of intersection
				final double intersectionX = x1 + t * (x2 - x1);
				final double intersectionY = y1 + t * (y2 - y1);

				final double distance = sqrt(
						pow((position.x - intersectionX), 2) + pow((position.y - intersectionY), 2));

				if (distance < smallestDistance || smallestDistance == -1) {
					smallestDistance = distance;
					closestPoint = new Point(intersectionX, intersectionY);
				}
			}
		}

		return closestPoint;
	}

}
