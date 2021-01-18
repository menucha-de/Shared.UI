package havis.net.ui.shared.resourcebundle;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;

public interface ResourceBundle extends ClientBundle {

	public static final ResourceBundle INSTANCE = GWT.create(ResourceBundle.class);

	@Source("resources/CssResources.css")
	CssResources css();

	@Source("resources/LLRP_List_Loading.png")
	ImageResource llrpListLoading();

	@Source("resources/LLRP_Power_Scale_BT_Minus.png")
	ImageResource llrpPowerScaleBTMinus();

	@Source("resources/LLRP_Power_Scale_BT_Plus.png")
	ImageResource llrpPowerScaleBTPlus();

	@Source("resources/service_started.png")
	ImageResource serviceStarted();

	@Source("resources/service_starting.png")
	ImageResource serviceStarting();

	@Source("resources/service_stopped.png")
	ImageResource serviceStopped();

	@Source("resources/LLRP_List_Found_Yes.png")
	DataResource llrpListFoundYes();

	@Source("resources/LLRP_List_Found_No.png")
	DataResource llrpListFoundNo();

	@Source("resources/LLRP_List_Sort_Down.png")
	ImageResource llrpListSortDown();

	@Source("resources/LLRP_List_Sort_None.png")
	ImageResource llrpListSortNone();

	@Source("resources/LLRP_List_Sort_Up.png")
	ImageResource llrpListSortUp();

	@Source("resources/list_refresh.png")
	ImageResource listRefresh();

	@Source("resources/LLRP_List_Export.png")
	ImageResource llrpListExport();

	@Source("resources/LLRP_List_Signal_Scale_ActiveUnit.png")
	ImageResource llrpListSignalScaleActiveUnit();

	@Source("resources/LLRP_List_Signal_Scale.png")
	ImageResource llrpListSignalScale();

	// For ETB
	@Source("resources/BT_Round_40x40_Add.png")
	ImageResource listAdd();

	@Source("resources/BT_Round_40x40_Delete.png")
	ImageResource listRemove();

	// From the macio theme
	@Source("resources/SETTINGS_BT_Menu_closed.png")
	DataResource menuClosed();

	@Source("resources/SETTINGS_BT_Menu_open.png")
	DataResource menuOpen();

	@Source("resources/Icons_App.png")
	DataResource iconsApp();

	@Source("resources/HOME_Icon_Settings.png")
	DataResource iconSettings();
	
	@Source("resources/Icon_Settings.png")
	DataResource iconSettingsSmall();

	@Source("resources/list_overview.png")
	DataResource listOverview();

	@Source("resources/LLRP_Region_Dropdown_Arrow_disabled.png")
	DataResource dropDownArrowDisabled();

	@Source("resources/LLRP_Region_Dropdown_Arrow.png")
	DataResource dropDownArrow();

	@Source("resources/delete_row.png")
	DataResource deleteRow();

	@Source("resources/list-scroll-up.png")
	DataResource listScrollUp();

	@Source("resources/list-scroll-down.png")
	DataResource listScrollDown();

	@Source("resources/icon_error.png")
	ImageResource errorIcon();
	
	@Source("resources/icon_warning.png")
	ImageResource warningIcon();
	
	@Source("resources/icon_info.png")
	ImageResource infoIcon();
	
	@Source("resources/spec_item_run.png")
	DataResource specItemRun();
	
	@Source("resources/spec_item_add.png")
	DataResource specItemAdd();
	@Source("resources/spec_item_delete.png")
	DataResource specItemDelete();
	@Source("resources/spec_item_export.png")
	DataResource specItemExport();
	@Source("resources/spec_item_import.png")
	DataResource specItemImport();
	@Source("resources/spec_item_list_refresh.png")
	DataResource specItemListRefresh();
	@Source("resources/spec_item_menu.png")
	DataResource specItemMenu();
	@Source("resources/editor_close.png")
	DataResource editorClose();
	@Source("resources/common_editor_close.png")
	DataResource commonEditorClose();
	
	@Source("resources/app_license.png")
	DataResource appLicense();

	@Source("resources/app_license_big.png")
	DataResource appLicenseBig();

	@Source("resources/app_license_email.png")
	DataResource appLicenseEmail();

	@Source("resources/app_license_copy.png")
	DataResource appLicenseCopy();
	
	@Source("resources/ledOff.png")
	DataResource ledOff();
	
	@Source("resources/ledOn.png")
	DataResource ledOn();
}
