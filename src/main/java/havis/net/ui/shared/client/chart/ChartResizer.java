package havis.net.ui.shared.client.chart;

import havis.net.ui.shared.client.chart.group.ChartValueGroup;
import havis.net.ui.shared.client.chart.scale.ChartScale;

import java.util.List;

public class ChartResizer {
	protected boolean resizeBelowZero = false;
	protected double offset = 0;

	private ChartScale[] scales;

	public ChartResizer(ChartScale... scales) {
		this.scales = scales;
	}

	public boolean isResizeBelowZero() {
		return resizeBelowZero;
	}

	public void setResizeBelowZero(boolean resizeBelowZero) {
		this.resizeBelowZero = resizeBelowZero;
	}

	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		if (offset < 0) {
			throw new IllegalArgumentException();
		}
		this.offset = offset;
	}

	public void setScales(ChartScale... scales) {
		this.scales = scales;
	}

	/**
	 * 
	 * @param chartValueGroups
	 * @return True if a scale must be redrawn
	 */
	public boolean resize(List<ChartValueGroup<?, ?>> chartValueGroups) {
		boolean draw = false;
		if (scales != null) {
			for (ChartScale scale : scales) {
				double min = scale.getBeginValue() + offset;
				double max = scale.getEndvalue() - offset;

				// calulate min and max value
				for (ChartValueGroup<?, ?> g : chartValueGroups) {
					for (ChartPoint<?, ?> c : g.getPoints()) {
						if (c.getY() != null) {
							min = Math.min(min, c.getY());
							max = Math.max(max, c.getY());
						}
					}
				}

				// check if chart must be redrawn
				if (min - offset < scale.getBeginValue()) {
					if (min - offset < 0) {
						if (resizeBelowZero) {
							draw = true;
							scale.setBeginValue(min - offset);
						} else if (scale.getBeginValue() != 0) {
							draw = true;
							scale.setBeginValue(0);
						}
					} else {
						draw = true;
						scale.setBeginValue(min - offset);
					}
				}
				if (max + offset > scale.getEndvalue()) {
					draw = true;
					scale.setEndvalue(max + offset);
				}
			}
		}
		return draw;
	}

}
