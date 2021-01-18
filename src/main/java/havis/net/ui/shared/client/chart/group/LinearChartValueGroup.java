package havis.net.ui.shared.client.chart.group;

import havis.net.ui.shared.resourcebundle.ResourceBundle;

import com.github.gwtd3.api.core.Selection;
import com.github.gwtd3.api.svg.PathDataGenerator;

public class LinearChartValueGroup<X, Y> extends ChartValueGroup<X, Y> {

	public static interface PathDataGeneratorContainer {
		PathDataGenerator getPathDataGenerator();
	}

	private PathDataGeneratorContainer pathDataGeneratorContainer;

	protected Selection path;

	public LinearChartValueGroup(PathDataGeneratorContainer pathDataGeneratorContainer) {
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

}
