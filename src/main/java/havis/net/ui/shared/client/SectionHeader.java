package havis.net.ui.shared.client;

import havis.net.ui.shared.resourcebundle.ResourceBundle;

import com.google.gwt.user.client.ui.FlowPanel;

public class SectionHeader extends FlowPanel {

	public static final String STYLE_HEADER_WIDGETS = "section-header-widgets";
	
	private ResourceBundle res = ResourceBundle.INSTANCE;
	
	public SectionHeader() {
		this.setStylePrimaryName(res.css().sectionHeaderWidgets());
	}
}
