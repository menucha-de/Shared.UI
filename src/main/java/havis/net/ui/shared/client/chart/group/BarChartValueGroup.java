package havis.net.ui.shared.client.chart.group;

import havis.net.ui.shared.client.chart.BarChart;
import havis.net.ui.shared.client.chart.ChartPoint;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

import com.github.gwtd3.api.arrays.Array;
import com.github.gwtd3.api.core.Selection;
import com.google.gwt.dom.client.Element;

public class BarChartValueGroup<X, Y> extends ChartValueGroup<X, Y> {

	private Selection parent;
	private Selection g;

	private BarChart barChart;

	public BarChartValueGroup(BarChart barChart) {
		super();
		this.barChart = barChart;
	}

	@Override
	public void refresh() {
		int chartHeight = barChart.getChartHeight();
		int maxBarWidth = barChart.getMaxBarWidth();

		if (points != null) {
			
			Selection s = parent.select("#xaxis").selectAll(".tick");
			Array<Array<Element>> array = s.asElementArray();
			for (ChartPoint<X, Y> p : points) {
				if (p.getY() != null) {					
					String transformV = array.get(0).get(p.getX().intValue()).getAttribute("transform");
					double transform = Double.valueOf(transformV.split(",")[0].substring("translate(".length()));
					transform -= maxBarWidth / 2;

					Selection rect = g.append("rect");
					rect = rect.attr("class", ResourceBundle.INSTANCE.css().group());
					rect = rect.style("stroke", color);
					rect = rect.style("fill", color);
					rect = rect.attr("transform", "translate(" + transform + ",0)");
					rect = rect.attr("y", "" + (chartHeight - p.getY()));
					rect = rect.attr("width", maxBarWidth);
					rect = rect.attr("height", p.getY());
				}
			}
		}
	}

	@Override
	public void append(Selection parent, double marginLeft) {
		this.parent = parent;
		g = parent.append("g");
		g = g.attr("transform", "translate(" + marginLeft + ",0)");
		refresh();
	};
}
