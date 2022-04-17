package rayvis.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Slider {

	public static final int WIDTH = 100, HEIGHT = 15;

	private ShapeRenderer renderer;
	private Batch textBatch;
	private BitmapFont font;
	private String name;
	private int x, y;
	private double percentage = 0.5;
	private double minValue, maxValue;
	private boolean selected;

	public Slider(String name, int x, int y, int minValue, int maxValue) {
		renderer = new ShapeRenderer();
		textBatch = new SpriteBatch();
		font = new BitmapFont();
		this.name = name;
		this.x = x;
		this.y = y;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	/**
	 * Called every frame, parallel to the v-sync, to draw the slider and its info
	 */
	public void draw() {

		// Preparing the renderer
		renderer.setAutoShapeType(true);
		renderer.begin();
		renderer.set(ShapeType.Filled);
		renderer.setColor(Color.GRAY);

		// Background
		renderer.rect(x, y, WIDTH, HEIGHT);

		// Marker
		renderer.setColor(Color.WHITE);
		renderer.rect((float) (x + WIDTH * percentage - 5), y - 5, 10, HEIGHT + 10);

		renderer.end();

		// Rendering the text
		textBatch.begin();
		font.setColor(Color.RED);
		font.draw(textBatch, name + ": " + getValue(), x + WIDTH + 10, y + HEIGHT - 4);
		textBatch.end();
	}

	/**
	 * Returns the value of the slider
	 * 
	 * @return the value
	 */
	public int getValue() {
		return (int) (minValue + percentage * (maxValue - minValue));
	}

	/**
	 * Called when the user clicks the mouse
	 * 
	 * Selects the slider and moves the marker if the click is inside the slider
	 * 
	 * @param x the x-coordinate of the click
	 * @param y the y-coordinate of the click
	 */
	public void click(int x, int y) {
		if (x > this.x && x < this.x + WIDTH) {
			if (y > this.y && y < this.y + HEIGHT) {
				percentage = (x - this.x) / (double) WIDTH;
				selected = true;
			}
		}
	}

	/**
	 * Called when the user releases the mouse button
	 * 
	 * De-selects the slider
	 */
	public void release() {
		selected = false;
	}

	/**
	 * Called when the user moves the mouse while the button is pressed
	 * 
	 * Moves the marker and changes the percentage
	 * 
	 * @param x the x-coordinate of the mouse
	 * @param y the y-coordinate of the mouse
	 */
	public void drag(int x, int y) {
		if (selected) {
			x = Math.max(this.x, Math.min(x, this.x + WIDTH));
			percentage = (x - this.x) / (double) WIDTH;
		}
	}

}
