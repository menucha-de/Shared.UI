package havis.net.ui.shared.client.widgets;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import havis.net.ui.shared.resourcebundle.ResourceBundle;

public class LoadingSpinner extends PopupPanel {
	public LoadingSpinner() {
		super(false, true);
		setStyleName(ResourceBundle.INSTANCE.css().webuiSpinnerOuter());

		setGlassEnabled(true);
		setGlassStyleName(ResourceBundle.INSTANCE.css().webuiSpinnerBackground());

		SimplePanel loader = new SimplePanel();
		loader.setStyleName(ResourceBundle.INSTANCE.css().webuiSpinnerBounce());
		this.add(loader);
	}
}
