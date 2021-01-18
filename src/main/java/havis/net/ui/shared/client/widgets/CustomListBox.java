package havis.net.ui.shared.client.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptGroupElement;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.ListBox;

/**
 * This class was created to have a listBox that can be used with Editor.class.
 * The ListBox.class does not provide this feature. The ValueListBox.class was
 * not used because it always selects the first item. Additionally this
 * CustomListBox.class can be used for cases in which asynchronous calls are
 * used to get the items after the selected value has been set.
 */
public class CustomListBox<T> extends Composite implements TakesValue<T>, LeafValueEditor<T>, HasFocusHandlers, HasBlurHandlers,
		HasChangeHandlers, HasValueChangeHandlers<T>, HasClickHandlers, HasVisibility, HasEnabled {

	/**
	 * Contains the value given by setValue(T value) if {@link #cacheValue} is
	 * true.
	 */
	private T store;
	/**
	 * Sets whether the value given by setValue(T value) will be cached.
	 * Required for async calls.
	 */
	private boolean cacheValue;
	private ArrayList<T> values = new ArrayList<T>();
	private ArrayList<T> silent = new ArrayList<T>();
	private ListBox listBox = new ListBox();
	private CustomRenderer<T> renderer = new CustomRenderer<T>() {

		@Override
		public String render(T value) {
			return value == null ? null : value.toString();
		}
	};

	/**
	 * Default Constructor. This constructor will use the default renderer:<br>
	 * {@code return value == null ? null : value.toString();}
	 */
	@UiConstructor
	public CustomListBox() {
		initWidget(listBox);
		addNullValue();
	}

	/**
	 * Default Constructor. This constructor will use the default renderer:<br>
	 * {@code return value == null ? null : value.toString();}
	 * 
	 * @param cacheValue
	 *            If true the value given by setValue(T value) will be stored.
	 *            After an item has been added the method setSelectedValue(T
	 *            value) will be called.
	 */
	public CustomListBox(boolean cacheValue) {
		this.cacheValue = cacheValue;
		initWidget(listBox);
		addNullValue();
	}

	/**
	 * Default Constructor.
	 * 
	 * @param renderer
	 *            converts the value to a string representation which will be
	 *            shown in the list
	 */
	public CustomListBox(CustomRenderer<T> renderer) {
		this.renderer = renderer;
		initWidget(listBox);
		addNullValue();
	}

	/**
	 * Default Constructor.
	 * 
	 * @param renderer
	 *            converts the value to a string representation which will be
	 *            shown in the list
	 * @param cacheValue
	 *            If true the value given by setValue(T value) will be stored.
	 *            After an item has been added the method setSelectedValue(T
	 *            value) will be called.
	 */
	public CustomListBox(CustomRenderer<T> renderer, boolean cacheValue) {
		this.renderer = renderer;
		this.cacheValue = cacheValue;
		initWidget(listBox);
		addNullValue();
	}

	/**
	 * @param renderer
	 *            converts the value to a string representation which will be
	 *            shown in the list
	 */
	public void setRenderer(CustomRenderer<T> renderer) {
		this.renderer = renderer;
	}

	/**
	 * If true the value given by setValue(T value) will be stored. After an
	 * item has been added the method setSelectedValue(T value) will be called.
	 * 
	 * @param cacheValue
	 */
	public void setCacheValue(boolean cacheValue) {
		this.cacheValue = cacheValue;
	}

	/**
	 * Adds an option which is selected, disabled and hidden
	 */
	private void addNullValue() {
		values.add(0, null);
		listBox.getElement().setInnerHTML("<option value='' selected disabled hidden></option>");
	}

	@Override
	public void setValue(T value) {
		if (silent.contains(value))
			setValue(value, false);
		else
			setValue(value, true);
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the value
	 * @param fireChangeEvent
	 *            if true the change event will be fired.
	 */
	public void setValue(T value, boolean fireChangeEvent) {
		store = value;
		if (values.contains(value)) {
			setSelectedValue(value);
			if (fireChangeEvent)
				ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(), listBox);
		}
	}

	@Override
	public T getValue() {
		return getSelectedValue();
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return listBox.addClickHandler(handler);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return listBox.addChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return listBox.addFocusHandler(handler);
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return listBox.addBlurHandler(handler);
	}

	private class ValueChangeEvent extends com.google.gwt.event.logical.shared.ValueChangeEvent<T> {

		public ValueChangeEvent(T value) {
			super(value);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<T> handler) {
		return listBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				handler.onValueChange(new ValueChangeEvent(getValue()));
			}
		});
	}

	/**
	 * Adds an item to the list box.
	 * 
	 * @param value
	 *            the item to be added
	 */
	public void addItem(T value) {
		if (value != null) {
			values.add(value);
			listBox.addItem(renderer.render(value));
			if (cacheValue) {
				setSelectedValue(store);
			}
		}
	}

	/**
	 * Adds an item to the list box.
	 * 
	 * @param value
	 *            the item to be added
	 * @param tagName
	 * @param enabled
	 */
	public void addItem(T value, String tagName, Boolean enabled) {
		if (value != null) {
			values.add(value);
			listBox.addItem(renderer.render(value));

			if (enabled != null) {
				NodeList<Element> nl = listBox.getElement().getElementsByTagName(tagName);
				if (nl != null) {
					Element el = nl.getItem(listBox.getItemCount() - 1);
					if (el != null && enabled == false) {
						el.setAttribute("disabled", "disabled");
					}
				}
			}

			if (cacheValue)
				setSelectedValue(store);
		}
	}

	/**
	 * Adds an item to the list box.
	 * 
	 * @param value
	 *            the item to be added
	 * @param groupName
	 *            the group in which the item shall be added
	 */
	public void addItem(T value, String groupName) {
		if (value != null) {
			values.add(value);

			OptionElement optionElement = Document.get().createOptionElement();
			optionElement.setValue(renderer.render(value));
			optionElement.setText(renderer.render(value));

			OptGroupElement optGroupElement = null;
			NodeList<Element> optgroups = listBox.getElement().getElementsByTagName("optgroup");
			for (int i = 0; i < optgroups.getLength(); i++) {
				Element e = optgroups.getItem(i);
				if (e != null && ((OptGroupElement) e).getLabel().equals(groupName)) {
					optGroupElement = (OptGroupElement) e;
					optGroupElement.appendChild(optionElement);
					break;
				}
			}
			if (optGroupElement == null) {
				optGroupElement = Document.get().createOptGroupElement();
				optGroupElement.setLabel(groupName);
				listBox.getElement().appendChild((Node) optGroupElement);
				optGroupElement.appendChild(optionElement);
			}

			if (cacheValue)
				setSelectedValue(store);
		}
	}

	/**
	 * Adds an item which won't fire a change event if its set by method
	 * setValue(T value)
	 * 
	 * @param value
	 *            the item to be added
	 * @param item
	 *            the text of the item to be added
	 */
	public void addSilentItem(T value, String item) {
		try {
			for (int i = 0; i < listBox.getItemCount(); i++) {
				if (listBox.getItemText(i).startsWith(item)) {
					return;
				}
			}

			values.add(value);
			silent.add(value);
			listBox.addItem(item, renderer.render(value));
		} catch (Exception e) {
			GWT.log("Exception.addSilentItem: " + e.getMessage());
		}
	}

	/**
	 * Removes the specified item
	 * 
	 * @param value
	 *            item that shall be removed
	 */
	public void removeSilentItem(T value) {
		silent.remove(value);
		int index = values.indexOf(value);
		if (index > -1 && index < values.size()) {
			try {
				removeItem(index);
			} catch (Exception e) {
				GWT.log("Exception.removeSilentItem: " + e.getMessage());
			}
		}
	}

	/**
	 * Adds the items to the list box.
	 * 
	 * @param values
	 *            the items to be added
	 */
	public void addItems(List<T> values) {
		if (values != null) {
			for (T value : values)
				addItem(value);
		}
	}

	/**
	 * Adds the items to the list box.
	 * 
	 * @param values
	 *            the items to be added
	 */
	public void addItems(T[] values) {
		if (values != null) {
			for (T value : values)
				addItem(value);
		}
	}

	/**
	 * Adds the items to the list box.
	 * 
	 * @param values
	 *            the items to be added. Map&lt;groupName, List&lt;T&gt;&gt;
	 */
	public void addItems(Map<String, List<T>> values) {
		if (values != null) {
			try {
				for (String key : values.keySet()) {
					if (key == null)
						addItems(values.get(key));
					else
						addItems(values.get(key), key);
				}
			} catch (Exception e) {
				GWT.log("Exception.addItems.Map,List: " + e.getMessage());
			}
		}
	}

	/**
	 * Adds the items to the list box.
	 * 
	 * @param values
	 *            the items to be added
	 * @param groupName
	 *            the group in which the items shall be added
	 */
	public void addItems(List<T> values, String groupName) {
		if (values != null) {
			for (T value : values)
				addItem(value, groupName);
		}
	}

	/**
	 * Adds the items to the list box.
	 * 
	 * @param values
	 *            the items to be added
	 * @param groupName
	 *            the group in which the items shall be added
	 */
	public void addItems(T[] values, String groupName) {
		if (values != null) {
			for (T value : values)
				addItem(value, groupName);
		}
	}

	/**
	 * Replaces the current list with the items.
	 * 
	 * @param values
	 *            the items to be added
	 */
	public void setItems(List<T> values) {
		clear();
		addItems(values);
	}

	/**
	 * Replaces the current list with the items.
	 * 
	 * @param values
	 *            the items to be added
	 */
	public void setItems(T[] values) {
		clear();
		addItems(values);
	}

	/**
	 * Replaces the current list with the items.
	 * 
	 * @param values
	 *            the items to be added
	 */
	public void setItems(Map<String, List<T>> values) {
		clear();
		addItems(values);
	}

	/**
	 * The first item in the list will be the automatically added null value.
	 * 
	 * @return An unmodifiable list of the items
	 */
	public List<T> getItems() {
		return Collections.unmodifiableList(values);
	}

	/**
	 * Checks if listbox contains the value. Null will be in the list by
	 * default.
	 * 
	 * @param item
	 *            The item to check
	 * @return true if item is in the list.
	 */
	public boolean containsItem(T item) {
		return values.contains(item);
	}

	/**
	 * Removes all items from the list box except the null value
	 */
	public void clear() {
		listBox.clear();
		silent.clear();
		values.clear();
		addNullValue();
	}

	/**
	 * Sets the currently selected index.
	 * 
	 * @param index
	 *            the index of the item to be selected
	 */
	public void setSelectedIndex(int index) {
		if (index < listBox.getItemCount()) {
			listBox.setSelectedIndex(index);
		} else {
			listBox.setSelectedIndex(listBox.getItemCount() - 1);
		}
	}

	/**
	 * Sets the currently selected value.
	 * 
	 * @param value
	 *            the item to be selected
	 */
	public void setSelectedValue(T value) {
		int index = values.indexOf(value);

		if (index < 0)
			index = 0;

		if (index < listBox.getItemCount()) {
			listBox.setSelectedIndex(index);
		} else {
			listBox.setSelectedIndex(listBox.getItemCount() - 1);
		}
	}

	/**
	 * Gets the value for currently selected value.
	 * 
	 * @return the value for selected item, or null if none is selected
	 */
	public T getSelectedValue() {
		int i = listBox.getSelectedIndex();
		try {
			T value = values.get(i);
			return value;
		} catch (Exception e) {
			GWT.log("getSelectedValue.Exception: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Gets the text for currently selected item.
	 * 
	 * @return the text for selected item, or null if none is selected
	 */
	public String getSelectedItemText() {
		if (listBox.getSelectedIndex() == 0)
			return null;
		return listBox.getSelectedItemText();
	}

	/**
	 * Gets the index for currently selected item.
	 * 
	 * @return the index for currently selected item.
	 */
	public int getSelectedIndex() {
		return listBox.getSelectedIndex();
	}

	@Override
	public boolean isEnabled() {
		return listBox.isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		listBox.setEnabled(enabled);
	}

	/**
	 * Removes the item at the specified index.
	 * 
	 * @param index
	 *            the index of the item to be removed
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 */
	public void removeItem(int index) throws IndexOutOfBoundsException, IllegalStateException {
		if (listBox.getItemCount() != values.size()) {
			throw new IllegalStateException("CustomListbox: listBox and values have different numbers of elements!");
		}

		if (index < listBox.getItemCount()) {
			listBox.removeItem(index);
			values.remove(index);
		}
	}

	/**
	 * Removes the item.
	 * 
	 * @param item
	 * @param removeAll
	 *            If false only the first occurrence of the item will be
	 *            removed.
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalStateException
	 */
	public void removeItem(T item, boolean removeAll) throws IndexOutOfBoundsException, IllegalStateException {
		int index = -1;
		do {
			index = values.indexOf(item);
			if (index > 0) {
				removeItem(index);
			}
		} while (index > -1 && removeAll);
	}

	@Override
	public void setVisible(boolean visible) {
		listBox.setVisible(visible);
	}

	/**
	 * Sets the visibility CSS property. If visibility is null the CSS property
	 * will be removed.
	 * 
	 * @param visibility
	 *            The visibility value
	 */
	public void setVisibility(Visibility visibility) {
		if (visibility == null) {
			listBox.getElement().getStyle().clearVisibility();
		} else {
			listBox.getElement().getStyle().setVisibility(visibility);
		}
	}

	/**
	 * Explicitly focus/unfocus this widget. Only one widget can have focus at a
	 * time, and the widget that does will receive all keyboard events.
	 * 
	 * @param focused
	 *            whether this widget should take focus or release it
	 */
	public void setFocus(boolean focused) {
		this.listBox.setFocus(focused);
	}
}
