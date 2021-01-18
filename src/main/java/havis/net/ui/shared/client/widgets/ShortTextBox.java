package havis.net.ui.shared.client.widgets;

import java.text.ParseException;

import com.google.gwt.text.shared.Parser;

public class ShortTextBox extends NumberTextBox<Short> {

	@Override
	protected Parser<Short> getParser() {
		return new Parser<Short>() {

			@Override
			public Short parse(CharSequence text) throws ParseException {
				if (text == null && isNullable()) {
					return null;
				} else {
					try {
						Short value = new Short(text + "");
						if (min != null && value < min) {
							throw new NumberFormatException("Invalid number " + value + ". Minimum value is " + min);
						}
						if (max != null && value > max) {
							throw new NumberFormatException("Invalid number " + value + ". Maxmimum value is " + max);
						}
						return value;
					} catch (NumberFormatException e) {
						throw new ParseException(e.getMessage(), 0);
					}
				}
			}

		};
	}
}
