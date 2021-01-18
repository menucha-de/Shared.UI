package havis.net.ui.shared.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class ErrorPanel extends PopupPanel {
	
	private Label messageLabel = new Label();
	
	public ErrorPanel(int left, int top) {
		super();
		
		SimplePanel dot = new SimplePanel();
		dot.setStylePrimaryName("error-dot");

		FlowPanel content = new FlowPanel();
		content.add(messageLabel);
		content.add(dot);
		
		setWidget(content);
		
		setPopupPosition(left, top);
	}
	
	public void showErrorMessage(String message) {
		addStyleName("message");
		setAutoHideEnabled(true);
		messageLabel.addStyleName("error");
		messageLabel.setText(message);
		show();
	}
	
	public void showWarningMessage(String message) {
		addStyleName("long-warning");
		setAutoHideEnabled(false);
		messageLabel.addStyleName("warning");
		messageLabel.setText(message);
		show();
	}
}
