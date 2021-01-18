package havis.net.ui.shared.client.chart;

/**
 * Defines a point in a chart.
 * 
 * @param <X>
 *            Type of x value
 * @param <Y>
 *            Type of y value
 */
public abstract class ChartPoint<X, Y> {

	/**
	 * The x value
	 */
	protected X x;
	/**
	 * The y value
	 */
	protected Y y;

	/**
	 * Default constructor
	 */
	public ChartPoint() {

	}

	public ChartPoint(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	public abstract Double getX();

	public void setX(X x) {
		this.x = x;
	}

	public abstract Double getY();

	public void setY(Y y) {
		this.y = y;
	}
}