package Drawing;

public class CoordinateWithAngle extends CartesianCoordinate {
	private double angle;
	
	public CoordinateWithAngle(double x, double y, double a) {
		super(x, y);
		angle = a;
	}
	
	public CoordinateWithAngle(CartesianCoordinate position, double a) {
		super(position.getX(), position.getY());
		angle = a;
	}
	
	public String toString() {
		return "[" + xPosition + ", " + yPosition + "] + A = " + angle;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double d) {
		this.angle = d;
	}

}
