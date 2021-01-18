package havis.net.ui.shared.client.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * FlexTable with Header Cells. Since the GWT FlexTable doesn't support the
 * &lt;thead&gt; section, we need to create and manage it on our own.
 * 
 */
public class HeaderFlexTable extends FlexTable {

	/** &lt;colgroup&gt; for simple styling */
	private Element colgroup;

	/** &lt;thead&gt;-element for further manipulation */
	private Element tHeadElement;

	/** List of the actual widgets in the header */
	private List<Widget> headerWidgets = new ArrayList<Widget>();

	public HeaderFlexTable() {
		super();
		// Creating the elements for the <head> section
		// and inserting them into the DOM
		colgroup = DOM.getChild(getElement(), 0);
	}

	private Element getTHeadElement() {
		if (tHeadElement == null) {
			tHeadElement = DOM.createTHead();
			DOM.insertChild(tHeadElement, DOM.createTR(), 0);
			DOM.insertChild(getElement(), tHeadElement, 1);
		}
		return tHeadElement;
	}

	private void addColgroupItem(int column) {
		int count = DOM.getChildCount(colgroup);
		for (int i = count; i <= column; i++) {
			DOM.appendChild(colgroup, DOM.createCol());
		}
	}

	private void removeColgroupItem() {
		Element col = DOM.getFirstChild(colgroup);
		colgroup.removeChild(col);
	}

	/**
	 * This method sets a widget for the specified header cell.
	 * 
	 * @param column
	 *            is a column number.
	 * @param widget
	 *            is a widget to be added to the cell.
	 */
	public void setHeaderWidget(int column, Widget widget) {
		prepareHeaderCell(column);

		if (widget != null) {
			widget.removeFromParent();

			Element th = DOM.getChild(DOM.getFirstChild(getTHeadElement()),
					column);
			internalClearCell(th, true);

			// Physical attach.
			DOM.appendChild(th, widget.getElement());

			addColgroupItem(column);

			if (headerWidgets.size() > column
					&& headerWidgets.get(column) != null)
				headerWidgets.set(column, widget);
			else
				headerWidgets.add(column, widget);

			adopt(widget);
		}
	}

	/**
	 * Gets the number of cells on a given row.
	 * 
	 * @param row
	 *            the row whose cells are to be counted
	 * @return the number of cells present
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public int getCellCount(int row) {
		checkRowBounds(row);
		return getDOMCellCount(row);
	}

	public int getHeaderCellCount() {
		return getDOMCellCount(getTHeadElement(), 0);
	}

	public Widget getHeaderWidget(int column) {
		return headerWidgets.get(column);
	}

	/**
	 * This method removes the header widget.
	 * 
	 * @param column
	 *            is a column number.
	 */
	public void removeHeaderWidget(int column) {
		if (column < 0)
			throw new IndexOutOfBoundsException(
					"Column number mustn't be negative");

		Element tr = DOM.getFirstChild(getTHeadElement());
		Element th = DOM.getChild(tr, column);
		tr.removeChild(th);

		removeColgroupItem();

		headerWidgets.remove(column);
	}

	/**
	 * This method prepares the header cell to be used.
	 * 
	 * @param column
	 *            is a column number.
	 */
	protected void prepareHeaderCell(int column) {
		if (column < 0) {
			throw new IndexOutOfBoundsException(
					"Cannot create a column with a negative index: " + column);
		}

		if (headerWidgets.size() <= column || headerWidgets.get(column) == null) {
			int required = column + 1
					- DOM.getChildCount(DOM.getChild(getTHeadElement(), 0));
			if (required > 0)
				addHeaderCells(getTHeadElement(), required);
		}
	}

	/**
	 * Removes any widgets, text, and HTML within the cell. This method assumes
	 * that the requested cell already exists.
	 * 
	 * @param row
	 *            the cell's row
	 * @param column
	 *            the cell's column
	 * @param clearInnerHTML
	 *            should the cell's inner html be cleared?
	 * @return element that has been cleaned
	 */
	private Element cleanHeaderCell(int column, boolean clearInnerHTML) {
		// Clear whatever is in the cell.
		Element th = DOM.getChild(DOM.getFirstChild(getTHeadElement()), column);
		internalClearCell(th, clearInnerHTML);
		return th;
	}

	/**
	 * Sets the HTML contents of the specified cell.
	 * 
	 * @param column
	 *            the cell's column
	 * @param html
	 *            the cell's HTML contents
	 * @throws IndexOutOfBoundsException
	 */
	public void setHeaderHTML(int column, String html) {
		prepareHeaderCell(column);
		Element th = cleanHeaderCell(column, html == null);
		if (html != null) {
			th.setInnerHTML(html);
		}
		addColgroupItem(column);
	}

	/**
	 * This native method is used to create TH tags instead of TD tags.
	 * 
	 * @param tHead
	 *            is a grid thead element.
	 * @param num
	 *            is a number of columns to create.
	 */
	protected native void addHeaderCells(Element tHead, int num)/*-{
																var rowElem = tHead.rows[0];
																for(var i = 0; i < num; i++){
																var cell = $doc.createElement("th");
																rowElem.appendChild(cell);
																}
																}-*/;

	public Iterator<Widget> iterator() {
		List<Widget> notAttachedWidgets = new ArrayList<Widget>();
		for (Widget widget : headerWidgets) {
			if (!widget.isAttached())
				notAttachedWidgets.add(widget);
		}
		return new AdvancedWidgetIterator(super.iterator(),
				notAttachedWidgets.iterator());
	}

	/************************************************************************/

	protected class AdvancedWidgetIterator implements Iterator<Widget> {
		private Iterator<Widget> parentIterator;
		private Iterator<Widget> headersIterator;
		private boolean endOfHeadersReached;

		public AdvancedWidgetIterator(Iterator<Widget> parentIterator,
				Iterator<Widget> headerIterator) {
			this.parentIterator = parentIterator;
			this.headersIterator = headerIterator;
		}

		public boolean hasNext() {
			return parentIterator.hasNext() || headersIterator.hasNext();
		}

		public Widget next() {
			if (!headersIterator.hasNext()) {
				endOfHeadersReached = true;
				return parentIterator.next();
			} else
				return headersIterator.next();
		}

		public void remove() {
			if (!endOfHeadersReached)
				headersIterator.remove();
			else
				parentIterator.remove();
		}
	}
}
