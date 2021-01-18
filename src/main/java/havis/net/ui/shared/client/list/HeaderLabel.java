package havis.net.ui.shared.client.list;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class HeaderLabel extends Composite implements HasClickHandlers {

	@UiField
	protected FocusPanel header;
	@UiField
	protected InlineLabel text;
	@UiField
	protected Image button;

	private SortOrder sortOrder = SortOrder.NONE;
	private int column;
	private boolean sortable;

	private static HeaderLabelUiBinder uiBinder = GWT
			.create(HeaderLabelUiBinder.class);

	interface HeaderLabelUiBinder extends UiBinder<Widget, HeaderLabel> {
	}

	public HeaderLabel(String text, int column, boolean sortable) {
		initWidget(uiBinder.createAndBindUi(this));

		this.text.setText(text);
		this.column = column;
		this.sortable = sortable;

		button.setVisible(this.sortable);
	}

	@UiHandler("header")
	public void onSortClick(ClickEvent e) {
		switch (sortOrder) {
		case NONE:
			sortOrder = SortOrder.UP;
			break;
		case UP:
			sortOrder = SortOrder.DOWN;
			break;
		case DOWN:
			sortOrder = SortOrder.NONE;
			break;
		}

		button.setUrl(sortOrder.getImageURL());
	}

	public SortOrder getState() {
		return sortOrder;
	}

	public void setState(SortOrder order) {
		sortOrder = order;
		button.setUrl(sortOrder.getImageURL());
	}

	public int getColumn() {
		return column;
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return header.addClickHandler(handler);
	}
}