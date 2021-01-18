package havis.net.ui.shared.client.chart.group;

import havis.net.ui.shared.resourcebundle.ResourceBundle;

import com.github.gwtd3.api.core.Selection;
import com.github.gwtd3.api.svg.PathDataGenerator;

public abstract class TimeChartValueGroup<X, Y> extends ChartValueGroup<X, Y> {

	public static interface PathDataGeneratorContainer {
		PathDataGenerator getPathDataGenerator();
	}

	private PathDataGeneratorContainer pathDataGeneratorContainer;

	protected Selection path;

	public TimeChartValueGroup(PathDataGeneratorContainer pathDataGeneratorContainer) {
		super();
		this.pathDataGeneratorContainer = pathDataGeneratorContainer;
	}

	@Override
	public void append(Selection parent, double marginLeft) {
		path = parent.append("g").append("path");
		setAppended(true);
		path = path.attr("class", ResourceBundle.INSTANCE.css().group());
		path = path.style("stroke", color);
		path = path.attr("transform", "translate(" + marginLeft + ",0)");
		refresh();
	}

	@Override
	public void refresh() {
		if (path != null) {
			path.attr("d", pathDataGeneratorContainer.getPathDataGenerator().generate(getPoints()));
		}
	}

	@Override
	public void setColor(String color) {
		super.setColor(color);
		if (path != null) {
			path = path.style("stroke", color);
		}
	}
	
	/**
	 * 
	 * @param below
	 *            Defines whether values below shall be removed.
	 * @param belowX
	 *            Defines the boundary value.
	 * @param above
	 *            Defines whether values above shall be removed.
	 * @param aboveX
	 *            Defines the boundary value.
	 */
	public abstract void removePointsWhereX(boolean below, double belowX, boolean above, double aboveX);

	/**
	 * 
	 * @param below
	 *            Defines whether values below shall be removed.
	 * @param belowY
	 *            Defines the boundary value.
	 * @param above
	 *            Defines whether values above shall be removed.
	 * @param aboveY
	 *            Defines the boundary value.
	 */
	public abstract void removePointsWhereY(boolean below, double belowY, boolean above, double aboveY);
}