package havis.net.ui.shared.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.shared.resourcebundle.ResourceBundle;

public class ConfigurationSection extends Composite
		implements HasValueChangeHandlers<Boolean>, HasClickHandlers, HasEnabled, HasText {

	private FlowPanel widget = new FlowPanel();
	private FlowPanel header = new FlowPanel();
	private SectionToggle sectionToggle = new SectionToggle();

	private ResourceBundle res = ResourceBundle.INSTANCE;

	private SectionHeader headerWidgets;
	private ComplexPanel content;

	private String name;
	private int index;
	private int contentHeight = -1;
	private boolean singleSection;
	private boolean multiOpen;

	public void setSingleSection(boolean singleSection) {
		this.singleSection = singleSection;
	}

	public ConfigurationSection(String name) {
		this.name = name;
	}

	public ConfigurationSection() {

	}

	@Override
	protected void initWidget(Widget widget) {
		super.initWidget(this.widget);

		initHeaderWidgets();

		addHeaderButton(name);

		if (!(widget instanceof Panel)) {
			throw new IllegalStateException("Widget must be of type ComplexPanel");
		}
		ComplexPanel widgets = (ComplexPanel) widget;

		for (Widget w : widgets) {
			if (w instanceof SectionHeader) {
				String style = w.getStyleName().replaceAll(SectionHeader.STYLE_HEADER_WIDGETS, "");
				if (!style.isEmpty()) {
					this.header.addStyleName(style);
				}
				headerWidgets = (SectionHeader) w;
			}
		}
		attachHeaderWidgets();
		attachContent(widgets);
	}

	private void initHeaderWidgets() {
		this.header.setStylePrimaryName(res.css().sectionHeader());
		this.widget.add(header);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return sectionToggle.addClickHandler(handler);
	}

	private void addHeaderButton(String name) {
		sectionToggle.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (content != null) {
					boolean open = event.getValue();

					if (!(getParent() instanceof ConfigurationSections)) {
						return;
					}

					ConfigurationSections cs = (ConfigurationSections) getParent();
					if (open) {
						ValueChangeEvent.fire(ConfigurationSection.this, true);
						onOpenSection();
						cs.setCurrent(ConfigurationSection.this);
						if (!multiOpen) {
							for (Widget widget : cs) {
								if (widget instanceof ConfigurationSection && widget != cs.getCurrent()) {
									((ConfigurationSection) widget).setOpen(false);
								}
							}
						}
					} else {
						cs.closeCurrent(ConfigurationSection.this);
						ValueChangeEvent.fire(ConfigurationSection.this, false);
						onCloseSection();
					}
				}
			}
		});
		sectionToggle.setText(name);
		sectionToggle.setStylePrimaryName(res.css().sectionToggle());
		header.add(sectionToggle);
	}

	private void hideWidget(Widget w) {
		w.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		w.getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	private void showWidget(Widget w) {
		w.getElement().getStyle().clearVisibility();
		w.getElement().getStyle().clearPosition();
	}

	public interface AnimationEndHandler extends EventHandler {
		void onAnimationEnd();
	}

	private native void registerAnimationEndHandler(final Element element, final AnimationEndHandler handler) /*-{
		var callback = function() {
			handler.@havis.net.ui.shared.client.ConfigurationSection.AnimationEndHandler::onAnimationEnd()();
		}
		element.addEventListener("transitionend", callback, false); // Mozilla
	}-*/;

	private void attachContent(Widget widget) {
		content = (ComplexPanel) widget;
		content.addStyleName(res.css().sectionContent());
		this.widget.add(content);
		registerAnimationEndHandler(content.getElement(), new AnimationEndHandler() {

			@Override
			public void onAnimationEnd() {
				if (!singleSection) {
					if (sectionToggle.getValue()) {
						content.getElement().getStyle().clearHeight();
					}
				}
			}
		});
		registerAnimationEndHandler(getElement(), new AnimationEndHandler() {

			@Override
			public void onAnimationEnd() {
				if (singleSection) {
					if (sectionToggle.getValue()) {
						getElement().getStyle().clearHeight();
					}
				}
			}
		});
		for (Widget w : content) {
			if (w instanceof ConfigurationSections) {
				((ConfigurationSections) w).setIndex(index);
			}
		}
		if (!sectionToggle.getValue()) {
			hideWidget(content);
		} else {
			showWidget(content);
		}
	}

	private void attachHeaderWidgets() {
		if (headerWidgets != null) {
			this.header.add(headerWidgets);
		}
	}

	public void setOpen(boolean open) {
		sectionToggle.setValue(open, true);
	}

	public boolean getOpen() {
		return sectionToggle.getValue();
	}

	public ErrorPanel getErrorPanel() {
		return new ErrorPanel(sectionToggle.getAbsoluteLeft(), sectionToggle.getAbsoluteTop());
	}

	@Override
	public boolean isEnabled() {
		return sectionToggle.isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		sectionToggle.setEnabled(enabled);
	}

	@Override
	public String getText() {
		return name;
	}

	@Override
	public void setText(String text) {
		this.name = text;
		sectionToggle.setText(text);
	}

	protected FlowPanel getHeader() {
		return header;
	}

	protected SectionHeader getHeaderWidgets() {
		return headerWidgets;
	}

	protected ComplexPanel getContent() {
		return content;
	}

	protected boolean hasContentWidgets() {
		return content.getWidgetCount() > 0;
	}

	protected void onOpenSection() {
		if (singleSection) {
			widget.setStyleName(res.css().open(), true);
			if (contentHeight < 1) {
				contentHeight = content.getOffsetHeight() + getOffsetHeight();
			}
			showWidget(content);
			new Timer() {
				@Override
				public void run() {
					if (contentHeight > 0) {
						setHeight(contentHeight + "px");
					}
				}
			}.schedule(10);
		} else {
			if (contentHeight < 1) {
				contentHeight = content.getOffsetHeight();
				if (contentHeight == 0) {
					content.getElement().getStyle().clearHeight();
					showWidget(content);
				} else {
					content.setHeight("0");
				}
			}
			showWidget(content);
			new Timer() {
				@Override
				public void run() {
					if (contentHeight > 0) {
						content.setHeight(contentHeight + "px");
					}
				}
			}.schedule(10);
		}
	}

	protected void onCloseSection() {
		ConfigurationSections cs = (ConfigurationSections) getParent();
		if (cs.isSingleSection()) {
			contentHeight = getOffsetHeight();
			widget.setStyleName(res.css().open(), false);
		} else {
			contentHeight = content.getOffsetHeight();
			content.setHeight(contentHeight + "px");
			new Timer() {
				@Override
				public void run() {
					content.setHeight("0");
				}
			}.schedule(10);
		}
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<ConfigurationSections> getChildConfigSections() {
		List<ConfigurationSections> list = null;

		for (Widget w : this.getContent()) {
			if (w instanceof ConfigurationSections) {
				if (list == null) {
					list = new ArrayList<ConfigurationSections>();
				}
				list.add((ConfigurationSections) w);
			}
		}
		return list;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	void showHeader(boolean show) {
		widget.setStyleName(res.css().hidden(), !show);
	}

	public void setMultiOpen(boolean multiOpen) {
		this.multiOpen = multiOpen;
	}
}
