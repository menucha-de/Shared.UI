package havis.net.ui.shared.client.list;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;

public class RowData implements Comparable<RowData>,
		Iterable<ComparableWidget<Widget>> {

	private WidgetList parent;

	private ArrayList<ComparableWidget<Widget>> columns = new ArrayList<ComparableWidget<Widget>>();

	public RowData(WidgetList parent, int rowIndex) {
		this.parent = parent;
	}

	public void addWidget(ComparableWidget<Widget> widget) {
		columns.add(widget);
	}

	public void setWidget(int index, ComparableWidget<Widget> widget) {
		if (index >= columns.size()) {
			prepareColumns(index);
		}
		columns.set(index, widget);
	}

	public ComparableWidget<Widget> getWidget(int index) {
		return columns.get(index);
	}

	private void prepareColumns(int index) {
		for (int i = columns.size(); i <= index; ++i) {
			columns.add(null);
		}
	}

	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public int compareTo(RowData row) {
		if (row == null) {
			return -1;
		}

		ComparableWidget<Widget> thisValue = this.getWidget(parent
				.getSortedColumnIndex());
		return thisValue.compareTo((Widget) row.getWidget(parent
				.getSortedColumnIndex()));
	}

	@Override
	public Iterator<ComparableWidget<Widget>> iterator() {
		return columns.iterator();
	}

}