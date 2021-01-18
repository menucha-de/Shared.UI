package havis.net.ui.shared.client.chart;

import havis.net.ui.shared.client.chart.group.ChartValueGroup;
import havis.net.ui.shared.client.chart.group.TimeChartValueGroup;
import havis.net.ui.shared.client.chart.scale.ChartScale;
import havis.net.ui.shared.client.chart.scale.TimeChartScale;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

import java.util.ArrayList;
import java.util.List;

import com.github.gwtd3.api.D3;
import com.github.gwtd3.api.core.Selection;
import com.github.gwtd3.api.core.Value;
import com.github.gwtd3.api.functions.BooleanDatumFunction;
import com.github.gwtd3.api.functions.DatumFunction;
import com.github.gwtd3.api.scales.LinearScale;
import com.github.gwtd3.api.svg.Axis;
import com.github.gwtd3.api.svg.Axis.Orientation;
import com.github.gwtd3.api.svg.Line.InterpolationMode;
import com.github.gwtd3.api.svg.PathDataGenerator;
import com.github.gwtd3.api.time.TimeScale;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;

public class TimeChart extends Chart {

	private Selection xAxisSelection;

	private TimeScale xTimeScale;

	private LinearScale yLinearLeftScale;

	private LinearScale yLinearRightScale;

	private List<TimeChartValueGroup<Double, Double>> drawnLimits = new ArrayList<TimeChartValueGroup<Double, Double>>();

	private List<Selection> labels = new ArrayList<>();

	/**
	 * Timer for refresh process
	 */
	protected Timer refreshTimer;

	public TimeChart(TimeChartScale xScale, ChartScale yLeftScale, ChartScale yRightScale) {
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
	public void refresh() {
		super.refresh();
		for (Selection label : labels) {
			label.remove();
		}
		labels.clear();
		for (ChartValueGroup<?, ?> group : chartValueGroups) {
			setLabel(group);
		}
	}

	@Override
	protected void drawLimits() {
		TimeChartValueGroup.PathDataGeneratorContainer pathDataGeneratorContainer = new TimeChartValueGroup.PathDataGeneratorContainer() {

			@Override
			public PathDataGenerator getPathDataGenerator() {
				return getLineGenerator(InterpolationMode.LINEAR, null, null, null);
			}
		};

		getGroups().removeAll(drawnLimits);
		drawnLimits.clear();

		for (final ChartLimit l : getLimits()) {
			TimeChartValueGroup<Double, Double> limit = new TimeChartValueGroup<Double, Double>(pathDataGeneratorContainer) {

				@Override
				public void removePointsWhereX(boolean below, double belowX, boolean above, double aboveX) {

				}

				@Override
				public void removePointsWhereY(boolean below, double belowY, boolean above, double aboveY) {

				}

			};
			limit.setColor(l.getColor());
			limit.setLabel(l.getLabel());
			limit.setLabelPosition(l.getLabelPosition());
			// set first point to draw line
			limit.getPoints().add(new ChartPoint<Double, Double>() {

				@Override
				public Double getY() {
					return l.getValue();
				}

				@Override
				public Double getX() {
					return xScale.getBeginValue();
				}
			});
			// set last point to draw line
			limit.getPoints().add(new ChartPoint<Double, Double>() {

				@Override
				public Double getY() {
					return l.getValue();
				}

				@Override
				public Double getX() {
					return xScale.getEndvalue();
				}
			});
			getGroups().add(limit);
			drawnLimits.add(limit);
		}
	}

	public void refresh(boolean drawXAxis) {
		if (drawXAxis) {
			buildXAxis();
		}
		refresh();
	}

	/**
	 * 
	 * @param xDatumFunction
	 *            (optional)
	 * @param yDatumFunction
	 *            (optional)
	 * @param booleanDatumFunction
	 *            (optional)
	 * @return A PathDataGenerator which is connected to this chart.
	 */
	public PathDataGenerator getLineGenerator(InterpolationMode interpolationMode, DatumFunction<Double> xDatumFunction,
			DatumFunction<Double> yDatumFunction, BooleanDatumFunction booleanDatumFunction) {
		if (booleanDatumFunction == null) {
			booleanDatumFunction = new BooleanDatumFunction() {
				@Override
				public boolean apply(Element context, Value d, int index) {
					if (d == null || ((ChartPoint<?, ?>) d.as()).getY() == null) {
						return false;
					}
					return true;
				}
			};
		}
		if (xDatumFunction == null) {
			xDatumFunction = new DatumFunction<Double>() {
				@Override
				public Double apply(Element context, Value d, int index) {
					return xTimeScale.apply(((ChartPoint<?, ?>) d.as()).getX()).asDouble();
				}
			};
		}
		if (yDatumFunction == null) {
			yDatumFunction = new DatumFunction<Double>() {
				@Override
				public Double apply(Element context, Value d, int index) {
					if (yLinearLeftScale != null) {
						return yLinearLeftScale.apply(((ChartPoint<?, ?>) d.as()).getY()).asDouble();
					} else if (yLinearRightScale != null) {
						return yLinearRightScale.apply(((ChartPoint<?, ?>) d.as()).getY()).asDouble();
					} else {
						return 0.0;
					}
				}

			};
		}
		if (interpolationMode == null) {
			interpolationMode = InterpolationMode.BASIS;
		}
		return D3.svg().line().interpolate(interpolationMode).x(xDatumFunction).y(yDatumFunction).defined(booleanDatumFunction);
	}

	private void buildXAxis() {
		if (xAxisSelection != null) {
			xAxisSelection.remove();
		}
		xTimeScale = ((TimeChartScale) xScale).getXTimeScale(chartWidth, yLeftScale.getScaleSize(), yRightScale.getScaleSize());
		Axis xAxis = ((TimeChartScale) xScale).buildAxis(xTimeScale, Orientation.BOTTOM);

		// Draw x-axis
		xAxisSelection = chart.append("g");
		xAxisSelection = xAxisSelection.attr("id", "xaxis");
		xAxisSelection = xAxisSelection.attr("class", ResourceBundle.INSTANCE.css().axis());
		xAxisSelection = xAxisSelection.attr("transform", "translate(" + yLeftScale.getScaleSize() + "," + chartHeight + ")");
		xAxisSelection = xAxisSelection.call(xAxis);

		if (xScale.isTickLinesVisible()) {
			chart.select("#xaxis").selectAll(".tick").select("line").style("opacity", "0.3")
					.attr("y2", "-" + (chartHeight - Y_SCALE_OFFSET_TOP)).attr("x2", 0);
		}
	}

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

	private void setLabel(ChartValueGroup<?, ?> group) {
		if (group.getLabel() != null) {
			int padding = 5;
			Selection text = chart.append("text");
			LinearScale yScale = yLinearLeftScale == null ? yLinearRightScale : yLinearLeftScale;
			if (yScale != null) {
				switch (group.getLabelPosition()) {
				case LEFT_INNER_BOTTOM:
					text = text.attr("y", yScale.apply(group.getPoints().get(0).getY()).asDouble() + padding * 3);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(0).getX()).asDouble() + yLeftScale.getScaleSize()
							+ padding);
					text = text.attr("text-anchor", "begin");
					break;
				case LEFT_INNER_MIDDLE:
					text = text.attr("y", yScale.apply(group.getPoints().get(0).getY()).asDouble() + padding);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(0).getX()).asDouble() + yLeftScale.getScaleSize()
							+ padding);
					text = text.attr("text-anchor", "begin");
					break;
				case RIGHT_INNER_BOTTOM:
					text = text
							.attr("y", yScale.apply(group.getPoints().get(group.getPoints().size() - 1).getY()).asDouble() + padding * 3);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(group.getPoints().size() - 1).getX()).asDouble()
							+ yLeftScale.getScaleSize() - padding);
					text = text.attr("text-anchor", "end");
					break;
				case RIGHT_INNER_MIDDLE:
					text = text.attr("y", yScale.apply(group.getPoints().get(group.getPoints().size() - 1).getY()).asDouble() + padding);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(group.getPoints().size() - 1).getX()).asDouble()
							+ yLeftScale.getScaleSize() - padding);
					text = text.attr("text-anchor", "end");
					break;
				case RIGHT_INNER_TOP:
					text = text.attr("y", yScale.apply(group.getPoints().get(group.getPoints().size() - 1).getY()).asDouble() - padding);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(group.getPoints().size() - 1).getX()).asDouble()
							+ yLeftScale.getScaleSize() - padding);
					text = text.attr("text-anchor", "end");
					break;
				case LEFT_OUTER_TOP:
					text = text.attr("y", yScale.apply(group.getPoints().get(0).getY()).asDouble() - padding);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(0).getX()).asDouble() + yLeftScale.getScaleSize()
							- padding);
					text = text.attr("text-anchor", "end");
					break;
				case LEFT_OUTER_MIDDLE:
					text = text.attr("y", yScale.apply(group.getPoints().get(0).getY()).asDouble() + padding);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(0).getX()).asDouble() + yLeftScale.getScaleSize()
							- padding);
					text = text.attr("text-anchor", "end");
					break;
				case LEFT_OUTER_BOTTOM:
					text = text.attr("y", yScale.apply(group.getPoints().get(0).getY()).asDouble() + padding * 3);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(0).getX()).asDouble() + yLeftScale.getScaleSize()
							- padding);
					text = text.attr("text-anchor", "end");
					break;
				case RIGHT_OUTER_BOTTOM:
					text = text
							.attr("y", yScale.apply(group.getPoints().get(group.getPoints().size() - 1).getY()).asDouble() + padding * 3);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(group.getPoints().size() - 1).getX()).asDouble()
							+ yLeftScale.getScaleSize() + padding);
					text = text.attr("text-anchor", "begin");
					break;
				case RIGHT_OUTER_MIDDLE:
					text = text.attr("y", yScale.apply(group.getPoints().get(group.getPoints().size() - 1).getY()).asDouble() + padding);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(group.getPoints().size() - 1).getX()).asDouble()
							+ yLeftScale.getScaleSize() + padding);
					text = text.attr("text-anchor", "begin");
					break;
				case RIGHT_OUTER_TOP:
					text = text.attr("y", yScale.apply(group.getPoints().get(group.getPoints().size() - 1).getY()).asDouble() - padding);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(group.getPoints().size() - 1).getX()).asDouble()
							+ yLeftScale.getScaleSize() + padding);
					text = text.attr("text-anchor", "begin");
					break;
				case LEFT_INNER_TOP:
				default:
					text = text.attr("y", yScale.apply(group.getPoints().get(0).getY()).asDouble() - padding);
					text = text.attr("x", xTimeScale.apply(group.getPoints().get(0).getX()).asDouble() + yLeftScale.getScaleSize()
							+ padding);
					text = text.attr("text-anchor", "begin");
					break;
				}
				text = text.attr("class", ResourceBundle.INSTANCE.css().graphLabel());
				text = text.text(group.getLabel());
				labels.add(text);
			}
		}
	}
}
