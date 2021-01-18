package havis.net.ui.shared.client.chart.scale;

import java.util.List;

import com.github.gwtd3.api.D3;
import com.github.gwtd3.api.scales.OrdinalScale;
import com.github.gwtd3.api.svg.Axis;
import com.github.gwtd3.api.svg.Axis.Orientation;

public class OrdinalChartScale extends ChartScale {
	
	private List<String> xAxisDefinitions;
	
	public OrdinalChartScale(){
		super();
	}

	public List<String> getxAxisDefinitions() {
		return xAxisDefinitions;
	}

	public void setxAxisDefinitions(List<String> xAxisDefinitions) {
		this.xAxisDefinitions = xAxisDefinitions;
	}
	
	public OrdinalScale getOrdinalScale(double size, double offsetLeft, double offsetRight) {
		String[] names = new String[xAxisDefinitions.size()];
		double[] positions = new double[xAxisDefinitions.size()];
		for (int i = 0; i < xAxisDefinitions.size(); i++) {
			names[i] = xAxisDefinitions.get(i);
			positions[i] = (double) i;
		}
		
		OrdinalScale scale = D3.scale.ordinal();
		scale = scale.domain(names);
		scale = scale.range(positions);
		scale = scale.rangeBands(offsetLeft, size - offsetRight);
		return scale;
	}
	
	public Axis buildAxis(OrdinalScale scale, Orientation orientation) {
		Axis axis = D3.svg().axis().scale(scale);
		axis = axis.orient(orientation);
		axis = axis.tickPadding(10);
		axis = axis.tickSize(0);

		axis = axis.ticks(getTicks());
		return axis;
	}

}
