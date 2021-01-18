package havis.net.ui.shared.client.upload;

import elemental.js.html.JsFileList;

public class FileList extends JsFileList {
    protected FileList() {}

    public final native File html5Item(final int index) /*-{
        return this.item(index);
    }-*/;
}