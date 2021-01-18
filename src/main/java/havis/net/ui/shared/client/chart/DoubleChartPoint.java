package havis.net.ui.shared.client.chart;

public class DoubleChartPoint extends ChartPoint<Double, Double> {

	public DoubleChartPoint() {
		
	}
	
	public DoubleChartPoint(Double x, Double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Double getX() {
		return x;
	}

	@Override
	public Double getY() {
		return y;
	}

}
