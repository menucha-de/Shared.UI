package havis.net.ui.shared.client.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.shared.client.table.ChangeRowEvent.Handler;
import havis.net.ui.shared.resourcebundle.ConstantsResource;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

public class CustomTable extends Composite implements DeleteRowEvent.HasHandlers,
	CreateRowEvent.HasHandlers, ChangeRowEvent.HasHandlers, HasEnabled {
	private FlexTable table = new FlexTable();
	
	//Backup list without the Up/Down/Delete control elements and without the head line
	private ArrayList<CustomWidgetRow> widgetRows = new ArrayList<CustomWidgetRow>();
	
	private boolean hasHeader;
	private boolean enableUpDown;
	private String upDownElemWidth = "1.5em";
	private boolean dirty;
	private ConstantsResource res = ConstantsResource.INSTANCE;
	
	private ClickHandler addClick = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			if (enabled) {
				fireEvent(new CreateRowEvent());
			}
		}
	};

	private boolean enabled = true;

	private boolean enableDelete = true;
	
	public CustomTable() {
		initWidget(table);
		table.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTable());
		dirty = false;
	}
	
	public void addRow(final CustomWidgetRow row) {
		int count = table.getRowCount();
		
		if (count > 0) count--;
		else GWT.log("WARNING CustomTable may have no header!");
		
		row.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int index = widgetRows.indexOf(row);
				if (index >= 0) {
					fireEvent(new ChangeRowEvent(index, row));
				}
			}
		});
		setRow(row, table.insertRow(count));
	}

	public void setHeader(List<String> header) {
		if (!hasHeader) {
			int column = 0;
			int rowCount = table.getRowCount();
			
			int colWidthPerCent = 100;
			if(header.size() > 1){
				colWidthPerCent = 100 / header.size();
			}
			
			for (String label : header) {
				SimplePanel panel = new SimplePanel();
				Label headerLabel = new Label(label);
				headerLabel.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableLabel());
				headerLabel.addClickHandler(addClick);
				panel.add(headerLabel);
				table.setWidget(rowCount, column, panel);
				table.getColumnFormatter().getElement(column);
				table.getColumnFormatter().getElement(column).getStyle().setProperty("width", colWidthPerCent + "%");
				++column;
			}
			if (enableUpDown) {
				addEmptyCols(rowCount, column++);
				addEmptyCols(rowCount, column++);
			}
			table.setWidget(rowCount, column, new HTML("&nbsp;"));
//			table.getColumnFormatter().getElement(column);
			hasHeader = true;
		}
	}
	
	private void addEmptyCols(int rowCount, int column) {
		table.setWidget(rowCount, column, new HTML("&nbsp;"));
		table.getCellFormatter().getElement(rowCount, column).getStyle().setProperty("borderLeft", "0");
	}
	
	private void setRow(final CustomWidgetRow row, final int rowIndex) {
		widgetRows.add(rowIndex, row);
		int column = 0;
		for (Widget widget : row) {
			table.setWidget(rowIndex, column, widget);
			table.getColumnFormatter().getElement(column); //used to create the <colgroup> list in html
			++column;
		}
		if(enableUpDown){
			column = addUpDownColumns(column, rowIndex);
		}
		addDeleteColumn(row, column, rowIndex);
	}
	
	/**
	 * Adds the Up/Down columns to the row
	 * (table.setWidget replaces any previously existing widgets) 
	 * @param column
	 * @param rowIndex
	 * @return updated column
	 */
	private int addUpDownColumns(int column, final int rowIndex){

		//---Up--- 
		HTML moveUpElement = new HTML("&nbsp;");
		moveUpElement.getElement().setClassName("webui-CustomTable-MoveUp");
		if(rowIndex != 0){
			moveUpElement.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableUp());
			moveUpElement.setTitle(res.moveUp());
			moveUpElement.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(rowIndex > 0){
						moveRowUp(rowIndex);
					}
				}
			});
		}
		
		table.setWidget(rowIndex, column, moveUpElement);
		
		ColumnFormatter frm0 = table.getColumnFormatter();
		frm0.getElement(column); //used to create the <colgroup> list in html
		
		table.getCellFormatter().getElement(rowIndex, column).getStyle().setProperty("borderLeft", "0");
		table.getCellFormatter().getElement(rowIndex, column).getStyle().setProperty("width", upDownElemWidth);
		column++;
		
		//---Down---
		HTML moveDownElement = new HTML("&nbsp;");
		moveDownElement.getElement().setClassName("webui-CustomTable-MoveDown");
		moveDownElement.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(rowIndex < getCustomWidgetCount()-1){
					moveRowDown(rowIndex);
				}
			}
		});
		
		//on initialize...
		if(rowIndex-1 >= 0){
			HTML prevMoveDownElement = (HTML) table.getWidget(rowIndex-1, column);
			prevMoveDownElement.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableDown());
			prevMoveDownElement.setTitle(res.moveDown());
		}
		
		//if move up/down clicked
		if(rowIndex < getCustomWidgetCount()-1){
			moveDownElement.setStyleName(ResourceBundle.INSTANCE.css().webuiCustomTableDown());
			moveDownElement.setTitle("Move Down");
		}
		
		table.setWidget(rowIndex, column, moveDownElement);
		
		ColumnFormatter frm1 = table.getColumnFormatter();
		frm1.getElement(column);
		
		table.getCellFormatter().getElement(rowIndex, column).getStyle().setProperty("borderLeft", "0");
		table.getCellFormatter().getElement(rowIndex, column).getStyle().setProperty("width", upDownElemWidth);
		
		column++;
		
		return column;
	}
	
	/**
	 * Adds the Delete column to the row
	 * @param row
	 * @param column
	 * @param rowIndex
	 * @return updated column
	 */
	private int addDeleteColumn(final CustomWidgetRow row, int column, final int rowIndex){
		HTML deleteELement = new HTML("&nbsp;");
		
		if (!enableDelete) {
			deleteELement.getElement().getStyle().setBackgroundImage("none");
		} else {
			deleteELement.setTitle(res.deleteRow());
			deleteELement.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					int index = widgetRows.indexOf(row);
					if (index >= 0 && enabled) {
						fireEvent(new DeleteRowEvent(index, row));
					}
				}
			});
		}
		
		table.setWidget(rowIndex, column, deleteELement);
		table.getColumnFormatter().getElement(column);
		++column;
		
		return column;
	}
	
	
	public boolean isEnableDelete() {
		return enableDelete;
	}

	public void setEnableDelete(boolean enableDelete) {
		this.enableDelete = enableDelete;
	}

	public void insertRow(CustomWidgetRow row, int index) {
		setRow(row, index);
	}
	
	public void deleteRow(int index) {
		table.removeRow(index);
		widgetRows.remove(index);
	}
	
	public void deleteRow(CustomWidgetRow row) {
		table.removeRow(widgetRows.indexOf(row));
		widgetRows.remove(row);
	}
	
	public void clear() {
		widgetRows.clear();
		table.clear();
		table.removeAllRows();
		hasHeader = false;
	}
	
	public CustomWidgetRow getRow(int index){
		return widgetRows.get(index);
	}
	
	@Override
	public HandlerRegistration addCreateRowHandler(CreateRowEvent.Handler handler) {
		return addHandler(handler, CreateRowEvent.getType());
	}

	@Override
	public HandlerRegistration addDeleteRowHandler(DeleteRowEvent.Handler handler) {
		return addHandler(handler, DeleteRowEvent.getType());
	}
	
	@Override
	public HandlerRegistration addChangeRowHandler(Handler handler) {
		return addHandler(handler, ChangeRowEvent.getType());
	}
	
	
	public void setColumnWidth(int column, int width, Unit unit) {
		table.getColumnFormatter().getElement(column).getStyle().setWidth(width, unit);
	}
	
	public int getRowCount(){
		return table.getRowCount();
	}
	
	public int getCustomWidgetCount(){
		return widgetRows.size();
	}
	
	public boolean getEnableUpDown(){
		return enableUpDown;
	}
	
	public void setEnableUpDown(boolean showUpDown){
		this.enableUpDown = showUpDown;
	}
	
	public boolean isDirty(){
		return dirty;
	}

	private void moveRowUp(int clickedIndex){
		dirty = true;
		
		int prevIndex = clickedIndex-1;
		
		CustomWidgetRow prevWidget = widgetRows.get(prevIndex);
		CustomWidgetRow orgWidget = widgetRows.get(clickedIndex);
		
		//clicked to previous...
		int column = 0;
		for(int i = 0; i < orgWidget.widgets.size(); i++){
			Widget orgColumn = orgWidget.widgets.get(i);
			table.setWidget(prevIndex, i, orgColumn);
			table.getColumnFormatter().getElement(i);
			column++;
		}
		column = addUpDownColumns(column, prevIndex);
		addDeleteColumn(widgetRows.get(prevIndex), column, prevIndex);
		
		//previous to clicked...
		column = 0;
		for(int i = 0; i < prevWidget.widgets.size(); i++){
			Widget prevColumn = prevWidget.widgets.get(i);
			table.setWidget(clickedIndex, i, prevColumn);
			table.getColumnFormatter().getElement(i);
			column++;
		}
		column = addUpDownColumns(column, clickedIndex);
		addDeleteColumn(widgetRows.get(clickedIndex), column, clickedIndex);
		
		//change the backup widgets within their list
		widgetRows.set(prevIndex, orgWidget);
		widgetRows.set(clickedIndex, prevWidget);
	}
	
	
	private void moveRowDown(int clickedIndex){
		dirty = true;
		
		int nextIndex = clickedIndex+1;
		
		CustomWidgetRow nextWidget = widgetRows.get(nextIndex);
		CustomWidgetRow orgWidget = widgetRows.get(clickedIndex);
		
		//clicked to next...
		int column = 0;
		for(int i = 0; i < orgWidget.widgets.size(); i++){
			Widget orgColumn = orgWidget.widgets.get(i);
			table.setWidget(nextIndex, i, orgColumn);
			table.getColumnFormatter().getElement(i);
			column++;
		}
		column = addUpDownColumns(column, nextIndex);
		addDeleteColumn(widgetRows.get(nextIndex), column, nextIndex);
		
		//next to clicked...
		column = 0;
		for(int i = 0; i < nextWidget.widgets.size(); i++){
			Widget nextColumn = nextWidget.widgets.get(i);
			table.setWidget(clickedIndex, i, nextColumn);
			table.getColumnFormatter().getElement(i);
			column++;
		}
		column = addUpDownColumns(column, clickedIndex);
		addDeleteColumn(widgetRows.get(clickedIndex), column, clickedIndex);
		
		//change the backup widgets within their list
		widgetRows.set(nextIndex, orgWidget);
		widgetRows.set(clickedIndex, nextWidget);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		for (CustomWidgetRow row : widgetRows) {
			row.setEnabled(enabled);
		}
	}

}
