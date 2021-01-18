package havis.net.ui.shared.client.widgets;

import java.text.ParseException;

import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class CustomValueBox<T> extends ValueBoxBase<T> {

	private T defaultValue;
	private Parser<T> parser;

	public CustomValueBox(ValueBoxBase<?> box, Renderer<T> renderer, Parser<T> parser) {
		super(box.getElement(), renderer, parser);
		this.parser = parser;
	}

	public CustomValueBox(ValueBoxBase<?> box, Renderer<T> renderer, Parser<T> parser, T defaultValue) {
		super(box.getElement(), renderer, parser);
		this.parser = parser;
		this.defaultValue = defaultValue;
	}

	@Override
	public T getValueOrThrow() throws ParseException {
		String text = getText();
		T value = parser.parse(text);
		if (value != null)
			return value;
		return defaultValue;
	}

	public void setInputType(String type) {
		getElement().setAttribute("type", type);
	}

	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}
}
