package rayvis.core;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

public class Map {
	
	private static final int WIDTH = Application.WIDTH / 2, HEIGHT = Application.HEIGHT; 
	
	private List<Wall> walls;
	
	public Map() {
		walls = new ArrayList<>();
		
		// Creating surrounding walls that make up a square
		walls.add(new Wall(0, 0, 0, HEIGHT - 1));
		walls.add(new Wall(0, HEIGHT - 1, WIDTH - 1, HEIGHT - 1));
		walls.add(new Wall(WIDTH - 1, HEIGHT - 1, WIDTH - 1, 0));
		walls.add(new Wall(WIDTH - 1, 0, 0, 0));
	
		// Creating walls placed in the middle to show off features 
		walls.add(new Wall(280, 240, 335, 220));
		walls.add(new Wall(50, 150, 290, 280));
		walls.add(new Wall(350, 70, 470, 130));
	}
	
	/**Returns the map's walls
	 * 
	 * @return the walls*/
	public List<Wall> getWalls() {
		return walls;
	}
	
	/**Returns the map's width
	 * 
	 * @return the width, in pixels*/
	public int getWidth() {
		return Application.WIDTH / 2;
	}
	
	/**Returns the map's height
	 * 
	 * @return the height, in pixels*/
	public int getHeight() {
		return Application.HEIGHT;
	}
}
