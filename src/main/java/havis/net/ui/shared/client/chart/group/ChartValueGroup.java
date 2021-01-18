package havis.net.ui.shared.client.chart.group;

import havis.net.ui.shared.client.chart.ChartPoint;
import havis.net.ui.shared.client.chart.LabelPosition;

import java.util.ArrayList;
import java.util.List;

import com.github.gwtd3.api.core.Selection;

/**
 * Defines a group of points.
 * 
 * @param <X>
 *            Type of x value
 * @param <Y>
 *            Type of y value
 */
public abstract class ChartValueGroup<X, Y> {

	private boolean appended;
	/**
	 * The line/group color.
	 */
	protected String color = "#000000";

	/**
	 * Contains the group values.
	 */
	protected List<ChartPoint<X, Y>> points;
	
	protected String label;
	
	protected LabelPosition labelPosition = LabelPosition.RIGHT_OUTER_MIDDLE;

	protected ChartValueGroup() {

	}

	/**
	 * @return The CSS line/group color.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the CSS group color. Default is black.
	 * 
	 * @param color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return The value list.
	 */
	public List<ChartPoint<X, Y>> getPoints() {
		if (points == null) {
			points = new ArrayList<ChartPoint<X, Y>>();
		}
		return points;
	}

	/**
	 * Sets the value list.
	 * 
	 * @param points
	 */
	public void setPoints(List<ChartPoint<X, Y>> points) {
		this.points = points;
	}

	/**
	 * @return True if groups is appended.
	 */
	public boolean isAppended() {
		return appended;
	}

	/**
	 * Sets whether the group is appended. Should be set to true in the
	 * {@link ChartValueGroup#append(Selection, double)} method
	 * 
	 * @param appended
	 */
	public void setAppended(boolean appended) {
		this.appended = appended;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public LabelPosition getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(LabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}

	/**
	 * Refreshing the view
	 */
	public abstract void refresh();

	/**
	 * Adding group to parent.
	 * 
	 * @param parent
	 * @param marginLeft
	 *            Should be the y-axis width.
	 */
	public abstract void append(Selection parent, double marginLeft);
}