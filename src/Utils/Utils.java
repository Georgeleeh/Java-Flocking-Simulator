// code for this class was taken from the module web-page, with some additions by me
package Utils;

import java.util.Random;

public class Utils {
	private static Random randomGenerator = new Random();

	public static void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// We are happy with interruptions, so do not report exception
		}
	}

	public static double randomDouble() {
		return randomGenerator.nextFloat();
	}

	public static int randomInt(int paramInt) {
		return randomGenerator.nextInt(paramInt);
	}

	// my own additions to the code

	// constants used for frame size
	public static final int X_SCREEN = 1100;
	public static final int Y_SCREEN = 700;

	/**
	 * Takes an angle and returns the corresponding angle between -179 and 180
	 * 
	 * @param angle
	 *            the angle to be placed within the range
	 * @return the input angle, shifted to be within the specified range
	 */
	public static double angleWithinBounds(double angle) {
		// make sure that angle is always between 0 and 359 inclusive (360 = 0)
		while (angle > 180) {
			angle -= 360;
		}
		while (angle < -179) {
			angle += 360;
		}
		return angle;
	}

	/**
	 * Generates an integer within a range of +/- maxInt
	 * 
	 * @param maxInt
	 *            maximum integer value +/-
	 * @return generated random integer
	 */
	public static int randomIntRange(int maxInt) {
		return randomGenerator.nextInt(maxInt * 2) - maxInt;
	}
}
