package havis.net.ui.shared.client.widgets;

import java.io.IOException;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.TextBox;

public abstract class NumberTextBox<T> extends FocusWidget implements LeafValueEditor<T>, HasChangeHandlers, HasValueChangeHandlers<T> {

	private boolean valueChangeHandlerInitialized;

	private boolean isNullable = true;

	protected TextBox textBox;

	protected CustomValueBox<T> value;

	protected Renderer<T> renderer = new Renderer<T>() {

		@Override
		public String render(T object) {
			return object == null ? null : object + "";
		}

		@Override
		public void render(T object, Appendable appendable) throws IOException {
			appendable.append(render(object));
		}

	};

	private Parser<T> parser;

	protected T min;

	protected T max;

	@UiConstructor
	public NumberTextBox() {
		this(new TextBox());
	}

	public NumberTextBox(Parser<T> parser) {
		this(new TextBox(), parser);
	}

	public NumberTextBox(Renderer<T> renderer) {
		this(new TextBox(), renderer);
	}

	public NumberTextBox(Renderer<T> renderer, Parser<T> parser) {
		this(new TextBox(), renderer, parser);
	}

	protected NumberTextBox(TextBox textBox) {
		super(textBox.getElement());
		this.textBox = textBox;
		this.parser = getParser();
		updateTextBox();
	}

	protected NumberTextBox(TextBox textBox, Parser<T> parser) {
		super(textBox.getElement());
		this.textBox = textBox;
		this.parser = parser;
		updateTextBox();
	}

	protected NumberTextBox(TextBox textBox, Renderer<T> renderer) {
		super(textBox.getElement());
		this.textBox = textBox;
		this.parser = getParser();
		this.renderer = renderer;
		updateTextBox();
	}

	protected NumberTextBox(TextBox textBox, Renderer<T> renderer, Parser<T> parser) {
		super(textBox.getElement());
		this.textBox = textBox;
		this.parser = parser;
		this.renderer = renderer;
		updateTextBox();
	}

	private void updateTextBox() {
		value = new CustomValueBox<T>(textBox, renderer, parser);
		value.setInputType("number");
	}

	public void setRenderer(Renderer<T> renderer) {
		this.renderer = renderer;
		updateTextBox();
	}

	public void setParser(Parser<T> parser) {
		this.parser = parser;
		updateTextBox();
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	@Override
	public void setValue(T value) {
		this.value.setValue(value);
	}

	@Override
	public T getValue() {
		return this.value.getValue();
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
		// Initialization code
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent event) {
					ValueChangeEvent.fire(NumberTextBox.this, getValue());
				}
			});
		}
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * If a keyboard event is currently being handled on this text box, calling
	 * this method will suppress it. This allows listeners to easily filter
	 * keyboard input.
	 */
	public void cancelKey() {
		textBox.cancelKey();
		value.cancelKey();
	}

	/**
	 * Sets the minimum value
	 * 
	 * @param min
	 *            The minimum value
	 */
	public void setMin(T min) {
		if (min == null) {
			textBox.getElement().removeAttribute("min");
		} else {
			textBox.getElement().setAttribute("min", min + "");
		}
	}

	/**
	 * Sets the maximum value
	 * 
	 * @param max
	 *            The maximum value
	 */
	public void setMax(T max) {
		if (max == null) {
			textBox.getElement().removeAttribute("max");
		} else {
			textBox.getElement().setAttribute("max", max + "");
		}
	}

	public void setInputType(String type) {
		value.setInputType(type);
	}

	protected abstract Parser<T> getParser();

}
