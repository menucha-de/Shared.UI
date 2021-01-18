package havis.net.ui.shared.data;

import com.google.gwt.http.client.Response;

public enum HttpMethod {
	OPTIONS, HEAD, GET, PUT, POST, DELETE;
	
	private static final String HEADER_ALLOW = "Allow";
	
	public boolean isAllowed(Response response) {
		if (response != null) {
			return response.getHeader(HEADER_ALLOW).contains(this.toString());
		}
		return false;
	}
}
