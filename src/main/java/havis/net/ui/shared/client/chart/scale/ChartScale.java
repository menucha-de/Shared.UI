package havis.net.ui.shared.client.chart.scale;

import com.github.gwtd3.api.D3;
import com.github.gwtd3.api.scales.LinearScale;
import com.github.gwtd3.api.svg.Axis;
import com.github.gwtd3.api.svg.Axis.Orientation;

public class ChartScale {

	/**
	 * Specifies if scale text shall be visible.
	 */
	private boolean visible = true;

	/**
	 * The scale begin value
	 */
	private double beginValue;

	/**
	 * The scale end value
	 */
	private double endvalue;

	/**
	 * If true, the tick lines shall be visible.
	 */
	private boolean tickLinesVisible = true;

	/**
	 * Specifies the size of tick description text
	 */
	private double scaleSize = 50;

	private int ticks;

	public ChartScale() {

	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public double getBeginValue() {
		return beginValue;
	}

	public void setBeginValue(double beginValue) {
		this.beginValue = beginValue;
	}

	public double getEndvalue() {
		return endvalue;
	}

	public void setEndvalue(double endvalue) {
		this.endvalue = endvalue;
	}

	public boolean isTickLinesVisible() {
		return tickLinesVisible;
	}

	public void setTickLinesVisible(boolean tickLinesVisible) {
		this.tickLinesVisible = tickLinesVisible;
	}

	public double getScaleSize() {
		return scaleSize;
	}

	public void setScaleSize(double scaleSize) {
		this.scaleSize = scaleSize;
	}

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}	

	public LinearScale getXLinearScale(double size, double offsetLeft, double offsetRight) {
		LinearScale linearScale = D3.scale.linear();
		linearScale = linearScale.domain(beginValue, endvalue);
		linearScale = linearScale.range(offsetLeft, size - offsetRight);
		return linearScale;
	}
	
	public LinearScale getYLinearScale(double size, double offset) {
		LinearScale linearScale = D3.scale.linear();
		linearScale = linearScale.domain(beginValue, endvalue);
		linearScale = linearScale.range(size, offset);
		return linearScale;
	}
	
	public Axis buildAxis(LinearScale scale, Orientation orientation) {
		Axis axis = D3.svg().axis().scale(scale);
		axis = axis.orient(orientation);
		axis = axis.tickPadding(10);
		axis = axis.tickSize(0);

		axis = axis.ticks(ticks);
		return axis;
	}
}
