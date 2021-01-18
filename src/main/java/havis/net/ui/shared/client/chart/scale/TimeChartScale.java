package havis.net.ui.shared.client.chart.scale;

import com.github.gwtd3.api.D3;
import com.github.gwtd3.api.core.Value;
import com.github.gwtd3.api.functions.DatumFunction;
import com.github.gwtd3.api.svg.Axis;
import com.github.gwtd3.api.svg.Axis.Orientation;
import com.github.gwtd3.api.time.Interval;
import com.github.gwtd3.api.time.TimeScale;
import com.google.gwt.dom.client.Element;

public class TimeChartScale extends ChartScale {
	
	private String dateFormat;
	
	private Interval interval; 
	
	public TimeChartScale(){
		super();
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Interval getInterval() {
		return interval;
	}

	public void setInterval(Interval interval) {
		this.interval = interval;
	}
	
	public TimeScale getXTimeScale(double size, double offsetLeft, double offsetRight) {
		TimeScale timeScale = D3.time().scale();
		timeScale = timeScale.domain(getBeginValue(), getEndvalue());
		timeScale = timeScale.range(offsetLeft, size - offsetRight);
		return timeScale;
	}
	
	public Axis buildAxis(TimeScale scale, Orientation orientation) {
		Axis axis = D3.svg().axis().scale(scale);
		axis = axis.orient(orientation);
		axis = axis.tickPadding(10);
		axis = axis.tickSize(0);
		axis = axis.ticks(interval, getTicks());
		axis = axis.tickFormat(new DatumFunction<String>() {
			@Override
			public String apply(Element context, Value d, int index) {
				// Define time format for xAxis
				return D3.time().format(dateFormat).apply(d.asJsDate());
			}
		});
		return axis;
	}
}
