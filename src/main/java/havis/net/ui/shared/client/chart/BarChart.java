package havis.net.ui.shared.client.chart;

import havis.net.ui.shared.client.chart.group.ChartValueGroup;
import havis.net.ui.shared.client.chart.scale.ChartScale;
import havis.net.ui.shared.client.chart.scale.OrdinalChartScale;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

import com.github.gwtd3.api.core.Selection;
import com.github.gwtd3.api.scales.LinearScale;
import com.github.gwtd3.api.scales.OrdinalScale;
import com.github.gwtd3.api.svg.Axis;
import com.github.gwtd3.api.svg.Axis.Orientation;

public class BarChart extends Chart {

	private OrdinalScale xOrdinalScale;

	private LinearScale yLinearLeftScale;

	private LinearScale yLinearRightScale;

	private Integer maxBarWidth;

	private double strokeWidth = 1.5;

	public BarChart(OrdinalChartScale xScale, ChartScale yLeftScale, ChartScale yRightScale) {
		super();
		this.xScale = xScale;
		this.yLeftScale = yLeftScale;
		this.yRightScale = yRightScale;
	}

	@Override
	public void drawChart() {
		super.drawChart();

		buildXAxis();
		if (yLeftScale.isVisible()) {
			buildYAxisLeft();
		}
		if (yRightScale.isVisible()) {
			buildYAxisRight();
		}

		drawLimits();

		refresh();
	}

	@Override
	protected void drawLimits() {
		if (getLimits().size() > 0) {
			throw new RuntimeException(BarChart.class.getName() + ".drawLimits() is not implemented!");
		}
	}

	public int getMaxBarWidth() {
		int maxBarWidth = (int) (chartWidth - yLeftScale.getScaleSize() * 2 - yRightScale.getScaleSize());
		int bars = 0;
		for (ChartValueGroup<?, ?> group : chartValueGroups) {
			bars += group.getPoints().size();
		}
		if (bars > 0) {
			maxBarWidth = (int) (maxBarWidth / bars - 2 * strokeWidth - 1);
		}
		if (this.maxBarWidth != null && maxBarWidth > this.maxBarWidth) {
			maxBarWidth = this.maxBarWidth;
		}
		return maxBarWidth;
	}

	public void setMaxBarWidth(Integer maxBarWidth) {
		this.maxBarWidth = maxBarWidth;
	}

	public double getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(double strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	/**
	 * Building x axis
	 */
	private void buildXAxis() {
		xOrdinalScale = ((OrdinalChartScale) xScale).getOrdinalScale(chartWidth, yLeftScale.getScaleSize(), yRightScale.getScaleSize());
		Axis xAxis = ((OrdinalChartScale) xScale).buildAxis(xOrdinalScale, Orientation.BOTTOM);

		// Draw x-axis
		Selection s = chart.append("g");
		s = s.attr("id", "xaxis");
		s = s.attr("class", ResourceBundle.INSTANCE.css().axis());
		s = s.attr("transform", "translate(" + yLeftScale.getScaleSize() + "," + chartHeight + ")");
		s = s.call(xAxis);

		if (xScale.isTickLinesVisible()) {
			chart.select("#xaxis").selectAll(".tick").select("line").style("opacity", "0.3")
					.attr("y2", "-" + (chartHeight - Y_SCALE_OFFSET_TOP)).attr("x2", 0);
		}
	}

	/**
	 * Building left y axis
	 */
	private void buildYAxisLeft() {
		yLinearLeftScale = yLeftScale.getYLinearScale(chartHeight, Y_SCALE_OFFSET_TOP);
		Axis yAxis = yLeftScale.buildAxis(yLinearLeftScale, Orientation.LEFT);

		// Draw y-axis
		Selection s = chart.append("g");
		s = s.attr("id", "yaxisl");
		s = s.attr("class", ResourceBundle.INSTANCE.css().axis());
		s = s.attr("transform", "translate(" + (yLeftScale.getScaleSize() * 2) + "," + 0 + ")");
		s = s.call(yAxis);

		if (yLeftScale.isTickLinesVisible()) {
			// Draw orientation lines on yAxis
			chart.select("#yaxisl").selectAll(".tick").select("line").style("opacity", "0.3")
					.attr("x2", chartWidth - yLeftScale.getScaleSize() - yRightScale.getScaleSize());
		}
	}

	/**
	 * Building right y axis
	 */
	private void buildYAxisRight() {
		yLinearRightScale = yRightScale.getYLinearScale(chartHeight, Y_SCALE_OFFSET_TOP);
		Axis yAxis = yRightScale.buildAxis(yLinearRightScale, Orientation.RIGHT);

		// Draw y-axis
		Selection s = chart.append("g");
		s = s.attr("id", "yaxisr");
		s = s.attr("class", ResourceBundle.INSTANCE.css().axis());
		s = s.attr("transform", "translate(" + (chartWidth + yLeftScale.getScaleSize() - yRightScale.getScaleSize()) + "," + 0 + ")");
		s = s.call(yAxis);

		if (yRightScale.isTickLinesVisible()) {
			// Draw orientation lines on yAxis
			chart.select("#yaxisr").selectAll(".tick").select("line").style("opacity", "0.3")
					.attr("x2", "-" + (chartWidth - yLeftScale.getScaleSize() - yRightScale.getScaleSize()));
		}

	}
}
