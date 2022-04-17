package rayvis.core;

public class Wall {

	/**The x-coordinate of the first point*/
	public double x1;
	
	/**The y-coordinate of the first point*/
	public double y1;
	
	/**The x-coordinate of the second point*/
	public double x2;
	
	/**The y-coordinate of the second point*/
	public double y2;
	
	public Wall(double x1, double y1,double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

}
