package havis.net.ui.shared.client.widgets;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.shared.resourcebundle.ResourceBundle;

public class LabelRow extends Composite implements HasWidgets {
	
	ResourceBundle res = ResourceBundle.INSTANCE;
	
	private Label label = new Label();
	private FlowPanel row = new FlowPanel();
	
	public LabelRow() {
		label.setStyleName(res.css().webuiProperty());
		row.setStyleName(res.css().webuiPropertyRow());
		row.add(label);
		initWidget(row);
	}

	public String getText() {
		return label.getText();
	}

	public void setText(String text) {
		label.setText(text);
	}

	@Override
	public void add(Widget w) {
		if (row.getWidgetCount() > 1) {
			row.remove(1);
		}
		row.add(w);
	}

	@Override
	public void clear() {
		row.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return row.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return row.remove(w);
	}
	
}
