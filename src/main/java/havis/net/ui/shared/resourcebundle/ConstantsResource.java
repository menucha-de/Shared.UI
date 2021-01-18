package havis.net.ui.shared.resourcebundle;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Messages;

public interface ConstantsResource extends Messages {
	
	static final ConstantsResource INSTANCE = GWT.create(ConstantsResource.class);

	String regionUndefined(); 
	String antenna(); 
	String found(); 
	String foundCap();
	String absolute(); 
	String startInventory(); 
	String stopInventory(); 
	String observe(); 
	String observing();
	String installUpdates(); 
	String current(); 
	String available(); 
	String delete(); 
	String select(); 
	String uploadFiles(); 
	String startUpdate();
	String selectAll(); 
	String configureLog(); 
	String clear(); 
	String expandList(); 
	String collapseList(); 
	String expandLog();
	String collapseLog();
	String date(); 
	String time(); 
	String message();
	String install();
	String management();

/*
	// For ETB
	String start();
	String starting();
	String stop();
	String stopping();
	String switchSelectBoxItemNone();
	String switchSelectBoxItemPin();
	String transponderListHeaderSwitch1();
	String transponderListHeaderSwitch2();
	String transponderListHeaderSwitch3();
	String transponderListHeaderSwitch4();
	String transponderListHeaderTransponder();
*/
	String moveUp();
	String moveDown();
	String deleteRow();
	String deleteReally(String name);
	String export();
	String importStr();
	String okButtonLabel();
	String cancelButtonLabel();
	String name();
	
	String activate();
	String clipboard();
	String toClipboard();
	String noProductKey();
	String requestWww();
	String requestEmail();
	String or();
	String contactSalesPerson();
	String product();
	String partNumber();
	String serialMica();
	String sn();
	String emailText(String product, String serial);
	String requestTitle(String product);
	String activateTitle(String product);

}
