package havis.net.ui.shared.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.shared.client.event.SectionExpandedEvent;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

public class ConfigurationSections extends FlowPanel implements SectionExpandedEvent.HasHandlers {
	private ConfigurationSection current;
	private int sectionCounter = 0;
	private int index = 0;
	private int currentOpenId = 0;
	private boolean singleSection = false;

	private ResourceBundle res = ResourceBundle.INSTANCE;
	private boolean multiOpen;

	public ConfigurationSection getCurrent() {
		return current;
	}

	public ConfigurationSections() {
		this.setStyleName(res.css().multi(), !singleSection);
	}

	private void showHeaders(boolean show) {
		for (int i = 0; i < getWidgetCount(); ++i) {
			Widget w = getWidget(i);
			if (w instanceof ConfigurationSection) {
				if (w != current) {
					((ConfigurationSection) w).showHeader(show);
				}
			}
		}
	}

	public void setCurrent(ConfigurationSection current) {
		this.current = current;
		this.currentOpenId = current.getIndex();

		if (current != null && singleSection) {
			showHeaders(false);
		}
		fireEvent(new SectionExpandedEvent(current, current.getIndex()));
	}

	public void closeCurrent(ConfigurationSection current) {
		if (singleSection) {
			showHeaders(true);
		}
		// currentOpenId is used to block fireEvent in case of a direct jump
		// from one open widget to another.
		// in such a case the close event follows the open event leading to a
		// wrong openId. ( 0 instead of the correct id)
		if (currentOpenId == current.getIndex()) {
			// sending 0 is important, do not alter current.setIndex(...) e.g.
			// to avoid an extra parameter.
			fireEvent(new SectionExpandedEvent(current, 0));
		}

	}

	@Override
	public void add(Widget w) {
		if (w instanceof ConfigurationSection) {
			((ConfigurationSection) w).setIndex(++sectionCounter);
		}
		super.add(w);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Open the specified ConfigurationSections
	 * 
	 * @param openInfo
	 */
	public void setOpen(String[] openInfo) {

		openDesiredWidgets(this, 0, openInfo);
	}

	/**
	 * Recursively called method to open nested widgets according the given openInfo
	 * e.g. "3-1"
	 * 
	 * @param owner
	 * @param recDepth
	 * @param openInfo
	 */
	private void openDesiredWidgets(ConfigurationSections owner, int recDepth, String[] openInfo) {
		int idx = Integer.valueOf(openInfo[recDepth]);
		for (Widget w : owner) {
			if (w instanceof ConfigurationSection && idx == ((ConfigurationSection) w).getIndex()) {
				ConfigurationSection currSec = ((ConfigurationSection) w);

				currSec.setOpen(true);
				if (openInfo.length > recDepth + 1) {
					// currently one only is supported.
					openDesiredWidgets(currSec.getChildConfigSections().get(0), ++recDepth, openInfo);
				}
				break;
			}
		}

	}

	/**
	 * Adds the ExpandedHandler using the correct place level
	 * 
	 * @param callback
	 */

	public void setSectionExpandedLevel(SectionExpandedCallback<String> callback) {

		addExpandedHandler(this, callback);
	}

	/**
	 * Adds the expanded handler to the ConfigurationSections
	 * 
	 * @param configSections
	 * @param level
	 */
	private void addExpandedHandler(ConfigurationSections configSections,
			final SectionExpandedCallback<String> callback) {

		configSections.addHandler(new SectionExpandedEvent.Handler() {
			@Override
			public void onSectionExpanded(SectionExpandedEvent event) {

				onSectionExpandedClicked(event, callback);
			}
		}, SectionExpandedEvent.getType());

		for (Widget w : configSections.getChildren()) {
			if (w instanceof ConfigurationSection) {

				ConfigurationSection currSec = ((ConfigurationSection) w);

				if (currSec != null) {
					List<ConfigurationSections> listChildSecs = currSec.getChildConfigSections();
					if (listChildSecs != null) {
						for (ConfigurationSections childSecs : listChildSecs) {
							addExpandedHandler(childSecs, callback);
						}
					}
				}
			}
		}

	}

	/**
	 * The onSectionExpanded click handler which updates the openWidgetId of the
	 * given place level
	 * 
	 * @param level - the current place level
	 * @param event - event information
	 */
	private void onSectionExpandedClicked(SectionExpandedEvent event, SectionExpandedCallback<String> callback) {
		List<String> list = new ArrayList<String>();
		String ret = "";

		// GWT.log("onSectionExpanded: " + event.getIndex().toString());

		list.add(event.getIndex().toString());

		registerPredecessors(event.getCurrentSection().getParent(), list);

		Collections.reverse(list);
		for (String s : list) {
			ret += s;
			ret += "-";
		}
		ret = ret.substring(0, ret.length() - 1);

		callback.onExpandedChanged(ret);
		// EditorPlace.updateOpenWidgetId(ret);
	}

	/**
	 * Recursively used method Looks for ConfigurationSections in the widget-tree
	 * and registers their currently used item
	 * 
	 * @param wdg  - the current widget
	 * @param list - the list to hold the found item indexes
	 */
	private void registerPredecessors(Widget wdg, List<String> list) {
		// ConfigurationSections is the owner of the two TriggerListWidget's

		if (wdg == null)
			return;
		Widget parWdg = wdg.getParent();
		if (parWdg instanceof ConfigurationSections
				&& parWdg.getStyleName().contains(ResourceBundle.INSTANCE.css().editorSections())) {
			ConfigurationSections cfgs = (ConfigurationSections) parWdg;
			list.add(cfgs.getCurrent().getIndex().toString());
		}

		registerPredecessors(parWdg, list);
	}

	@Override
	public HandlerRegistration addSectionExpandedHandler(SectionExpandedEvent.Handler handler) {
		return addHandler(handler, SectionExpandedEvent.getType());
	}

	public boolean isSingleSection() {
		return singleSection;
	}

	public void setSingleSection(boolean singleSection) {
		this.removeStyleName(res.css().multi());
		this.setStyleName(res.css().single(), singleSection);
		this.singleSection = singleSection;
		for (Widget w : this) {
			if (w instanceof ConfigurationSection) {
				((ConfigurationSection) w).setSingleSection(singleSection);
			}
		}
	}

	public void setMultiOpen(boolean multiOpen) {
		for (Widget w : this) {
			if (w instanceof ConfigurationSection) {
				((ConfigurationSection) w).setMultiOpen(multiOpen);
			}
		}
	}

	public boolean isMultiOpen() {
		return multiOpen;
	}
}