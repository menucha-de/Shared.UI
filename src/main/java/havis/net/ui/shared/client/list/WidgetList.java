package havis.net.ui.shared.client.list;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.HasScrollHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class WidgetList extends Composite implements ClickHandler, KeyDownHandler,
		HasScrollHandlers, HasMouseWheelHandlers {

	private static ListUiBinder uiBinder = GWT.create(ListUiBinder.class);

	interface ListUiBinder extends UiBinder<Widget, WidgetList> {
	}

	private final static String STYLE_SELECTED = "havis-List-selected";
	private final static int PAGE = 5;

	private boolean enable = true;
	private boolean styleExist = false;
	private boolean autoselect;
	private boolean sortable;
	private int sortedColumnIndex = -1;
	private SortOrder order = SortOrder.NONE;
	
	private SingleSelectionModel<Integer> model;
	private ArrayList<RowData> rows = new ArrayList<RowData>();

	@UiField protected FlexTable items;
	@UiField protected FlexTable header;
	@UiField protected FocusPanel panel;
	@UiField protected ScrollPanel itemsContainer;
	private String selectedStyle;

	public WidgetList() {
		initWidget(uiBinder.createAndBindUi(this));

		model = new SingleSelectionModel<Integer>();
		model.setSelected(-1, true);
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	
	public void setContentHeight(String height) {
		itemsContainer.setHeight(height);
	}

	public void addItem(Widget[] widgets) {
		setRow(items.getRowCount(), widgets);
	}
	
	public void insertRows(List<Widget[]> rows) {
		for (int i = 0; i < rows.size(); ++i) {
			items.insertRow(i);
			setRow(i, rows.get(i));
		}
	}
	
	public void insertRow(Widget[] row) {
		items.insertRow(0);
		setRow(0, row);
	}
	
	private void setRow(int row, Widget[] widgets) {
		int column = 0;
		for (Widget widget : widgets) {
			items.setWidget(row, column, widget);
			Style s = items.getRowFormatter().getElement(row).getStyle();
			if ((items.getRowCount() & 1) == 0) {
				s.setBackgroundColor("white");
			} else {
				s.setBackgroundColor("lightgrey");
			}
			items.getColumnFormatter().getElement(column);
			column++;
		}
		if (sortable) {
			addRowData(widgets);
			if (!order.equals(SortOrder.NONE)) {
				sortRows(sortedColumnIndex, order);
			}
		}
	}
	
	private void sortRows(int columnIndex, SortOrder order) {
		ArrayList<RowData> sorted = new ArrayList<RowData>();
		sorted.addAll(rows);

		switch (order) {
		case NONE:
			setRows(rows);
			break;
		case UP:
			Collections.sort(sorted);
			setRows(sorted);
			break;
		case DOWN:
			Collections.sort(sorted, Collections.reverseOrder());
			setRows(sorted);
			break;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void addRowData(Widget[] widgets) {
		RowData row = new RowData(this, rows.size());

		for (Widget widget : widgets) {
			row.addWidget((ComparableWidget<Widget>) widget);
		}
		
		rows.add(row);
	}

	private void setRows(ArrayList<RowData> rows) {
		for (int rowIdx = 0; rowIdx < rows.size(); ++rowIdx) {
			RowData row = rows.get(rowIdx);
			for (int colIdx = 0; colIdx < row.getColumnCount(); ++colIdx) {
				items.setWidget(rowIdx, colIdx, (Widget) row.getWidget(colIdx));
			}
		}
	}

	public void setHeader(Widget[] widgets) {
		if (widgets != null) {
			int column = 0;
			for (Widget widget : widgets) {
				header.setWidget(0, column, widget);
				//table.setHeaderWidget(column, widget);
				column++;
			}
		}
	}
	
	public void removeHeader() {
		header.removeAllRows();
	}
	
	public Widget[] getHeader() {
		Widget[] widgets = new Widget[header.getCellCount(0)];
		for (int i = 0; i < header.getCellCount(0); ++i) {
			widgets[i] = header.getWidget(0, i);
		}
		return widgets;
//		Widget[] widgets = new Widget[table.getHeaderCellCount()];
//		for (int i = 0; i < table.getHeaderCellCount(); ++i) {
//			widgets[i] = table.getHeaderWidget(i);
//		}
//		return widgets;
	}

	public void addHeaderCell(String text) {
		if (header.getRowCount() == 0) {
			header.insertRow(0);
		}
		final int colIdx = header.getCellCount(0);
		header.getColumnFormatter().getElement(colIdx);
		//final int colIdx = table.getHeaderCellCount();
		if (sortable) {
			final HeaderLabel lbl = new HeaderLabel(text, colIdx, sortable);
			header.setWidget(0, colIdx, lbl);
			//table.setHeaderWidget(colIdx, lbl);
			lbl.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					sortedColumnIndex = colIdx;
					order = lbl.getState();
					sortRows(colIdx, lbl.getState());
					resetSort();
				}
			});
		} else {
			header.setHTML(0, colIdx, text);
		}
	}

	private void resetSort() {
		for (int i = 0; i < header.getCellCount(0); ++i) {
			if (i != sortedColumnIndex) {
				((HeaderLabel) header.getWidget(0, i)).setState(SortOrder.NONE);
			}
		}
	}
	
	public void setItem(Widget[] widgets, int index) {
		int column = 0;
		for (Widget widget : widgets) {
			items.setWidget(index, column, widget);
			column++;
		}
		if (autoselect && getSelectedIndex() == -1) {
			setSelected(index);
		}
	}

	public Widget[] getItem(int index) {
		if (index > -1 && index < items.getRowCount()) {
			Widget[] widgets = new Widget[items.getCellCount(index)];
			for (int i = 0; i < items.getCellCount(index); i++) {
				widgets[i] = items.getWidget(index, i);
			}
			return widgets;
		} else {
			return null;
		}
	}

	public Widget getItem(int row, int column) {
		if (row > -1 && row < items.getRowCount()) {
			if (column > -1 && column < items.getCellCount(row)) {
				return items.getWidget(row, column);
			}
		}
		return null;
	}

	void setEnable(boolean enable) {
		this.enable = enable;
		if (enable) {
			if (styleExist) {
				removeStyleName("havis-List-disabled");
				styleExist = false;
			}
		} else {
			if (!styleExist) {
				addStyleName("havis-List-disabled");
				styleExist = true;
			}
		}
	}

	public void setAutoselect(boolean autoselect) {
		this.autoselect = autoselect;
	}

	public Widget[] getSelectedItem() {
		return getItem(model.getSelectedObject());
	}

	public void clear() {
		items.removeAllRows();
		rows.clear();
		model.setSelected(-1, true);
	}

	public int getItemCount() {
		return items.getRowCount();
	}

	public void removeRow(int row) {
		if (row > -1 && row < items.getRowCount()) {
			items.removeRow(row);
			if (row == getSelectedIndex()) {
				if (row == items.getRowCount()) {
					setSelected(row - 1);
				} else {
					setSelected(row);
				}
			}
		}
	}

	@UiHandler("panel")
	public void onClick(ClickEvent event) {
		if (enable) {
			Cell cell = items.getCellForEvent(event);
			if (cell != null) {
				setSelected(cell.getRowIndex());
			}
		}
	}

	@UiHandler("panel")
	public void onKeyDown(KeyDownEvent event) {
		if (enable) {
			int selected = getSelectedIndex();
			switch (event.getNativeKeyCode()) {
			case KeyCodes.KEY_DOWN:
				if (selected > -1) {
					int count = items.getRowCount();
					if (selected + 1 < count) {
						setSelected(selected + 1);
					}
				}
				event.stopPropagation();
				break;
			case KeyCodes.KEY_PAGEDOWN:
				if (selected > -1) {
					int count = items.getRowCount();
					if (selected + 1 < count) {
						if (selected + PAGE < count) {
							setSelected(selected + PAGE);
						} else {
							setSelected(count - 1);
						}
					}
				}
				event.stopPropagation();
				break;
			case KeyCodes.KEY_UP:
				if (selected > 0) {
					setSelected(selected - 1);
				}
				event.stopPropagation();
				break;
			case KeyCodes.KEY_PAGEUP:
				if (selected > 0) {
					if (selected - PAGE > -1) {
						setSelected(selected - PAGE);
					} else {
						setSelected(0);
					}
				}
				event.stopPropagation();
				break;
//			case KeyCodes.KEY_DELETE:
//				removeRow(selected);
//				event.stopPropagation();
//				break;
			case KeyCodes.KEY_HOME:
				if (selected > -1) {
					setSelected(0);
				}
				event.stopPropagation();
				break;
			case KeyCodes.KEY_END:
				if (selected > -1) {
					setSelected(items.getRowCount() - 1);
				}
				event.stopPropagation();
				break;
			}
		}
	}

	public HandlerRegistration addSelectionChangeHandler(Handler handler) {
		return model.addSelectionChangeHandler(handler);
	}

	public void setSelected(int index) {
		int selected = getSelectedIndex();
		RowFormatter formatter = items.getRowFormatter();
		
		String style = selectedStyle != null  ? selectedStyle : STYLE_SELECTED; 
		
		if (selected > -1 && selected < items.getRowCount()) {
			formatter.removeStyleName(selected, style);
		}
		if (index > -1) {
			formatter.addStyleName(index, style);
		}
		model.setSelected(index, true);
	}

	public void setSelectedStyle(String style) {
		this.selectedStyle = style;
	}
	
	public int getSelectedIndex() {
		return model.getSelectedObject();
	}

	int getSortedColumnIndex() {
		return sortedColumnIndex;
	}

	void setSortedColumnIndex(int sortedColumnIndex) {
		this.sortedColumnIndex = sortedColumnIndex;
	}

	@Override
	public HandlerRegistration addScrollHandler(ScrollHandler handler) {
		return itemsContainer.addScrollHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return panel.addMouseWheelHandler(handler);
	}

	public ScrollPanel getItemsContainter() {
		return itemsContainer;
	}
}