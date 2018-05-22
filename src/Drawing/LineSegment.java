package Drawing;

public class LineSegment {
	private CartesianCoordinate startPoint;
	private CartesianCoordinate endPoint;
	
	public LineSegment(CartesianCoordinate startPosition, CartesianCoordinate endPosition) {
		this.startPoint = startPosition;
		this.endPoint = endPosition;
	}

	public CartesianCoordinate getStartPoint() {
		return startPoint;
	}

	public CartesianCoordinate getEndPoint() {
		return endPoint;
	}
	
	public double length() {
		double x,y;
		x = (startPoint.getX() - endPoint.getX());
		y = (startPoint.getY() - endPoint.getY());
		return Math.sqrt( x*x + y*y );
	}

}
