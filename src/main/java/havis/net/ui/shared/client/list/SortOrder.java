package havis.net.ui.shared.client.list;

import havis.net.ui.shared.resourcebundle.ResourceBundle;

public enum SortOrder {

	NONE, UP, DOWN;

	ResourceBundle res = ResourceBundle.INSTANCE;

	public String getImageURL() {
		switch (this) {
		case UP:
			return res.llrpListSortUp().getSafeUri().asString();
		case DOWN:
			return res.llrpListSortDown().getSafeUri().asString();
		case NONE:
			return res.llrpListSortNone().getSafeUri().asString();
		}
		return null;
	}
}