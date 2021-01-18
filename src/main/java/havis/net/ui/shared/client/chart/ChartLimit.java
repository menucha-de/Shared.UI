package havis.net.ui.shared.client.chart;

public class ChartLimit {
	private double value;

	private String color = "#000000";

	private String label;

	private LabelPosition labelPosition = LabelPosition.LEFT_INNER_TOP;

	public ChartLimit(double value, String color, String label) {
		this.value = value;
		this.color = color;
		this.label = label;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public LabelPosition getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(LabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}
}
