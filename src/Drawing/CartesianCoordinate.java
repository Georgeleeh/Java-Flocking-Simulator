package Drawing;

public class CartesianCoordinate {
	protected double xPosition;
	protected double yPosition;

	public CartesianCoordinate(double x, double y) {
		xPosition = x;
		yPosition = y;
	}
	
	public String toString() {
		return "[" + xPosition + ", " + yPosition + "]";
	}

	public void setX(double xPosition) {
		this.xPosition = xPosition;
	}

	public void setY(double yPosition) {
		this.yPosition = yPosition;
	}

	public double getX() {
		return xPosition;
	}

	public double getY() {
		return yPosition;
	}

}