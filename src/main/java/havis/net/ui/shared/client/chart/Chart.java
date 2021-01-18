package havis.net.ui.shared.client.chart;

import havis.net.ui.shared.client.chart.group.ChartValueGroup;
import havis.net.ui.shared.client.chart.scale.ChartScale;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

import java.util.ArrayList;
import java.util.List;

import com.github.gwtd3.api.D3;
import com.github.gwtd3.api.core.Selection;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class Chart extends SimplePanel {

	protected final static int Y_SCALE_OFFSET_TOP = 20;

	/**
	 * Contains value groups
	 */
	protected List<ChartValueGroup<?, ?>> chartValueGroups;

	protected int chartWidth = 400;
	protected int chartHeight = 240;

	protected ChartScale xScale;
	protected ChartScale yLeftScale;
	protected ChartScale yRightScale;

	protected ChartResizer chartResizer;

	protected Selection chart;

	/**
	 * Map&lt;Value, CSS-Color&gt;
	 */
	protected List<ChartLimit> limits;

	/**
	 * Default constructor
	 */
	public Chart() {
		chartValueGroups = new ArrayList<ChartValueGroup<?, ?>>();
	}

	public int getChartWidth() {
		return chartWidth;
	}

	public void setChartWidth(int chartWidth) {
		this.chartWidth = chartWidth;
		drawChart();
	}

	public int getChartHeight() {
		return chartHeight;
	}

	public void setChartHeight(int chartHeight) {
		this.chartHeight = chartHeight;
		drawChart();
	}

	public ChartResizer getChartResizer() {
		return chartResizer;
	}

	public ChartScale getxScale() {
		return xScale;
	}

	public void setxScale(ChartScale xScale) {
		this.xScale = xScale;
	}

	public ChartScale getyLeftScale() {
		return yLeftScale;
	}

	public void setyLeftScale(ChartScale yLeftScale) {
		this.yLeftScale = yLeftScale;
	}

	public ChartScale getyRightScale() {
		return yRightScale;
	}

	public void setyRightScale(ChartScale yRightScale) {
		this.yRightScale = yRightScale;
	}

	public void setChartResizer(ChartResizer chartResizer) {
		this.chartResizer = chartResizer;
	}

	/**
	 * @return The value groups.
	 */
	public List<ChartValueGroup<?, ?>> getGroups() {
		if (chartValueGroups == null) {
			chartValueGroups = new ArrayList<ChartValueGroup<?, ?>>();
		}
		return chartValueGroups;
	}

	/**
	 * Sets the value groups
	 * 
	 * @param groups
	 */
	public void setGroups(List<ChartValueGroup<?, ?>> groups) {
		this.chartValueGroups = groups;
	}

	public void setLimits(List<ChartLimit> limits) {
		this.limits = limits;
	}

	public List<ChartLimit> getLimits() {
		if (limits == null) {
			limits = new ArrayList<ChartLimit>();
		}
		return limits;
	}

	/**
	 * Refreshing the chart
	 */
	public void refresh() {
		if (chartResizer != null) {
			if (chartResizer.resize(getGroups())) {
				drawChart();
			}
		}
		for (ChartValueGroup<?, ?> g : chartValueGroups) {
			if (g.isAppended()) {
				g.refresh();
			} else {
				g.append(chart, yLeftScale.getScaleSize());
			}
		}
	}

	/**
	 * Drawing the chart
	 */
	public void drawChart() {
		if (chart != null) {
			chart.remove();
		}
		for (ChartValueGroup<?, ?> g : chartValueGroups) {
			g.setAppended(false);
		}
		chart = D3.select(this);
		chart = chart.append("svg");
		chart = chart.attr("class", ResourceBundle.INSTANCE.css().graph());
		chart = chart.attr("width", chartWidth + yLeftScale.getScaleSize() + yRightScale.getScaleSize());
		chart = chart.attr("height", chartHeight + xScale.getScaleSize());
	}

	protected abstract void drawLimits();
}
