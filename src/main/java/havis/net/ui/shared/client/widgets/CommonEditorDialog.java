package havis.net.ui.shared.client.widgets;

import java.util.Iterator;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.shared.client.event.DialogCloseEvent;
import havis.net.ui.shared.client.event.DialogCloseEvent.Handler;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

public class CommonEditorDialog extends Composite implements HasWidgets, DialogCloseEvent.HasHandlers {

	private SimplePanel background = new SimplePanel();
	private FlowPanel dialog = new FlowPanel();
	private FlowPanel widgets = new FlowPanel();
	private FlowPanel footer = new FlowPanel();
	private Button closeButton = new Button();
	private Button acceptButton = new Button();

	public CommonEditorDialog() {
		initWidget(background);

		background.setStyleName(ResourceBundle.INSTANCE.css().webuiEditorBackground());
		dialog.setStyleName(ResourceBundle.INSTANCE.css().webuiEditorDialog());
		widgets.setStyleName(ResourceBundle.INSTANCE.css().webuiEditorWidgets());
		footer.setStyleName(ResourceBundle.INSTANCE.css().webuiEditorFooter());
		acceptButton.setStyleName(ResourceBundle.INSTANCE.css().webuiApplyButton());
		acceptButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new DialogCloseEvent(true));
			}
		});
		closeButton.setStyleName(ResourceBundle.INSTANCE.css().closeButton());
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new DialogCloseEvent(false));
			}
		});
		background.add(dialog);
		footer.add(acceptButton);
		dialog.add(closeButton);
		dialog.add(widgets);
		dialog.add(footer);
		addScrollHandler(this);
		setTopPositon();
	}

	/**
	 * Add scrollHandler to move close button. Not working on local machine for
	 * debug purposes.
	 * 
	 * @param editorDialog
	 */
	private final static native void addScrollHandler(CommonEditorDialog dialog)/*-{
		window.parent.parent.document
				.addEventListener(
						"scroll",
						function(e) {
							if (dialog != null) {
								dialog.@havis.net.ui.shared.client.widgets.CommonEditorDialog::setCloseButtonPosition()();
							}
						});
	}-*/;

	/**
	 * Moving the close button.
	 */
	public void setCloseButtonPosition() {
		int cssDefaultValue = 44;
		double pos = 0;
		try {
			pos = Double.valueOf(this.getElement().getStyle().getTop().replaceAll("[^\\d.]", ""));
		} catch (Exception e) {

		}
		double top = Math.max(Util.getContentOffsetTop() - 50, 0);
		double value = Math.max(Util.getContentScrollTop() - top - pos, top);
		value = Math.min(value - top + 21,
				this.getElement().getOffsetHeight() - cssDefaultValue - closeButton.getElement().getOffsetHeight());
		value = Math.max(value, cssDefaultValue);
		closeButton.getElement().getStyle().setTop(value, Unit.PX);
	}

	/**
	 * Placing the view always in the visible area of the browser window.
	 */
	public void setTopPositon() {
		int scrollTop = Util.getContentScrollTop();
		int top = 0;
		if (scrollTop + 50 > Util.getContentOffsetTop()) {
			top = scrollTop - Util.getContentOffsetTop() + 50;
		}
		dialog.getElement().getStyle().setTop(top, Unit.PX);
		setCloseButtonPosition();
	}

	public void setButtonCaption(String caption) {
		acceptButton.setText(caption);
	}
	
	public void setButtonEnabled(boolean enabled) {
		acceptButton.setEnabled(enabled);
	}

	@Override
	public void add(Widget w) {
		widgets.add(w);
	}

	@Override
	public void clear() {
		widgets.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return widgets.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return widgets.remove(w);
	}

	@Override
	public HandlerRegistration addDialogCloseHandler(Handler handler) {
		return addHandler(handler, DialogCloseEvent.getType());
	}

}
