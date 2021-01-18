package havis.net.ui.shared.client.widgets;

import havis.net.ui.shared.resourcebundle.ResourceBundle;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * This class supports to define a different value as defined by the list.
 * Unfortunately there is no standard object for this feature. HTML5 offers the
 * input tag with a datalist but this is not supported by every browser.
 * Additionally the datalist does not support groups.
 */
public class CustomSuggestBox<T> extends FlowPanel implements TakesValue<T>, LeafValueEditor<T>, HasEnabled, HasChangeHandlers,
		HasValueChangeHandlers<T>, Focusable {

	private boolean valueChangeHandlerInitialized;
	private CustomListBox<T> listBox = new CustomListBox<T>();
	private SimplePanel textBoxFrame = new SimplePanel();
	private TextBox textBox = new TextBox();
	private Parser<T> parser;
	private Renderer<T> renderer;
	
	public CustomSuggestBox(Parser<T> parser) {
		this(new Renderer<T>() {

			@Override
			public String render(T object) {
				return object == null ? null : object.toString();
			}

			@Override
			public void render(T object, Appendable appendable) throws IOException {
				appendable.append(render(object));
			}
		}, parser);		
	}

	public CustomSuggestBox(Renderer<T> renderer, Parser<T> parser) {
		this.addStyleName(ResourceBundle.INSTANCE.css().customSuggestBox());
		this.parser = parser;
		this.renderer = renderer;
		this.add(listBox);
		this.add(textBoxFrame);
		textBoxFrame.add(textBox);

		// remove listbox from tabindex
		listBox.getElement().setTabIndex(-2);

		listBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				setValue(listBox.getValue(), true);
			}
		});

		textBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				T item = null;
				try {
					item = CustomSuggestBox.this.parser.parse(textBox.getValue());
				} catch (ParseException e) {
					
				}
				setValue(item);
			}
		});

		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				T item = null;
				try {
					item = CustomSuggestBox.this.parser.parse(textBox.getValue());
				} catch (ParseException e) {
					
				}
				setValue(item);
				DomEvent.fireNativeEvent(Document.get().createKeyUpEvent(event.isControlKeyDown(), event.isAltKeyDown(), event.isShiftKeyDown(), event.isMetaKeyDown(), event.getNativeKeyCode()), listBox);
			}
		});
		
		textBox.getElement().getStyle().setBorderWidth(0, Unit.PX);
		textBox.getElement().getStyle().setBackgroundColor("transparent");
		this.getElement().getStyle().setWidth(100, Unit.PCT);

		// frame
		this.getElement().getStyle().setPosition(Position.RELATIVE);

		// inputframe
		String[] textBoxFrameStyles = new String[9];
		textBoxFrameStyles[0] = "position:absolute";
		textBoxFrameStyles[1] = "top:0";
		textBoxFrameStyles[2] = "left:0";
		textBoxFrameStyles[3] = "width:100%";
		textBoxFrameStyles[4] = "height:100%";
		textBoxFrameStyles[5] = "padding-right:35px";
		textBoxFrameStyles[6] = "font-size:1em";
		textBoxFrameStyles[7] = "box-sizing:border-box";
		textBoxFrameStyles[8] = "pointer-events:none";
		String textBoxFrameStyle = ";";
		for (String s : textBoxFrameStyles) {
			textBoxFrameStyle += s + ";";
		}
		textBoxFrame.getElement().setAttribute("style", textBoxFrameStyle);

		// input
		String[] textBoxStyles = new String[8];
		textBoxStyles[0] = "width:100%";
		textBoxStyles[1] = "height:100%";
		textBoxStyles[2] = "box-sizing:border-box";
		textBoxStyles[3] = "background:transparent";
		textBoxStyles[4] = "border:0";
		textBoxStyles[5] = "font-size:1em";
		textBoxStyles[6] = "box-shadow:none";
		textBoxStyles[7] = "pointer-events:all";
		String textBoxStyle = ";";
		for (String s : textBoxStyles) {
			textBoxStyle += s + ";";
		}
		textBox.getElement().setAttribute("style", textBoxStyle);

		// select
		String[] listBoxStyles = new String[5];
		listBoxStyles[0] = "width:100%";
		listBoxStyles[1] = "height:100%";
		listBoxStyles[2] = "box-sizing:border-box";
		listBoxStyles[3] = "cursor:pointer";
		listBoxStyles[4] = "font-size:1em";
		String listBoxStyle = ";";
		for (String s : listBoxStyles) {
			listBoxStyle += s + ";";
		}
		listBox.getElement().setAttribute("style", listBoxStyle);

	}
	
//	/**
//	 * If readonly is true. 
//	 * @param readonly
//	 */
//	public void setReadonly(boolean readonly){
//		if(readonly){
////			textBox.getElement().setTabIndex(-2);
//			textBox.getElement().setAttribute("readonly", "readonly");
////			listBox.getElement().removeAttribute("tabindex");
//		} else {
////			listBox.getElement().setTabIndex(-2);
////			textBox.getElement().removeAttribute("tabindex");
//			textBox.getElement().removeAttribute("readonly");
//		}
//	}

	/**
	 * @return The TextBox widget which holds the current value.
	 */
	public TextBox getTextBox() {
		return textBox;
	}

	/**
	 * @return The ListBox widget which holds the suggestions
	 */
	public CustomListBox<T> getListBox() {
		return listBox;
	}

	@Override
	public void setValue(T value) {
		setValue(value, false);
	}

	public void setValue(T value, boolean fireEvent) {
		boolean changed = !Objects.equals(textBox.getValue(), renderer.render(value));
		textBox.setValue(renderer.render(value), fireEvent);
		if (listBox.containsItem(value)) {
			listBox.setValue(value, false);
		} else {
			listBox.setValue(null, false);
		}
		if (fireEvent && changed) {
			DomEvent.fireNativeEvent(Document.get().createChangeEvent(), textBox);
		}
	}

	@Override
	public T getValue() {
		try {
			return parser.parse(textBox.getValue());
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public boolean isEnabled() {
		return textBox.isEnabled() && listBox.isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		textBox.setEnabled(enabled);
		listBox.setEnabled(enabled);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return textBox.addChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
		// Initialization code
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent event) {
					ValueChangeEvent.fire(CustomSuggestBox.this, getValue());
				}
			});
		}
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * Adding style names to the list box widget
	 * 
	 * @param names
	 *            The style names separated by a whitespace
	 */
	public void setAddListBoxStyleNames(String names) {
		if (names != null && !names.isEmpty()) {
			String[] styles = names.split("\\s+");
			for (String style : styles) {
				listBox.addStyleName(style);
			}
		}
	}

	/**
	 * Adding style names to the text box widget
	 * 
	 * @param names
	 *            The style names separated by a whitespace
	 */
	public void setAddTextBoxStyleNames(String names) {
		if (names != null && !names.isEmpty()) {
			String[] styles = names.split("\\s+");
			for (String style : styles) {
				textBox.addStyleName(style);
			}
		}
	}

	@Override
	public int getTabIndex() {
		return this.getElement().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		this.getElement().setPropertyString("accessKey", "" + key);
	}

	@Override
	public void setFocus(boolean focused) {
		textBox.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		this.textBox.setTabIndex(index);
	}

	/**
	 * If a keyboard event is currently being handled on this text box, calling
	 * this method will suppress it. This allows listeners to easily filter
	 * keyboard input.
	 */
	public void cancelKey() {
		textBox.cancelKey();
	}

	/**
	 * Sets the placeholder value. If value is null the attribute will be
	 * removed.
	 * 
	 * @param placeholder
	 *            The placeholder value.
	 */
	public void setPlaceholder(String placeholder) {
		if (placeholder == null) {
			textBox.getElement().removeAttribute("placeholder");
		} else {
			textBox.getElement().setAttribute("placeholder", placeholder);
		}
	}

	/**
	 * @return Default parser for type java.lang.String
	 */
	public static Parser<String> getStringParser() {
		return new Parser<String>() {

			@Override
			public String parse(CharSequence text) throws ParseException {
				return text == null ? null : text.toString();
			}
		};
	}
}
