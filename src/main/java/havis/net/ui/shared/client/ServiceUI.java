package havis.net.ui.shared.client;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.TextCallback;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

import havis.net.rest.shared.data.ServiceAsync;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

public abstract class ServiceUI extends ConfigurationSection {

	private int retries;

	ResourceBundle res = ResourceBundle.INSTANCE;

	public ServiceUI(String name) {
		super(name);
	}

	@Override
	protected void initWidget(Widget widget) {
		super.initWidget(widget);
		setVisible(false);
	}

	public void checkAvailability(final ServiceAsync service, final int retriesCount) {
		service.isServiceAvailable(new TextCallback() {

			@Override
			public void onSuccess(Method method, String response) {
				setVisible(true);
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				retries++;
				if (retries < retriesCount) {
					new Timer() {

						@Override
						public void run() {
							checkAvailability(service, retriesCount);
						}
					}.schedule(1000);
				}
			}
		});
	}
}
