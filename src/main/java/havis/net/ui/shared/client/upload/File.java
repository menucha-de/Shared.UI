package havis.net.ui.shared.client.upload;

import elemental.js.html.JsBlob;
import elemental.js.html.JsFile;

public class File extends JsFile {

    protected File() {
        super();
    }

    public final native JsBlob slice() /*-{
        return this.slice();
    }-*/;

    public final native JsBlob slice(final double start) /*-{
        return this.slice(start);
    }-*/;

    public final native JsBlob slice(final double start, final double end) /*-{
        return this.slice(start, end);
    }-*/;

    public final native JsBlob slice(final double start, final double end, final String contentType) /*-{
        return this.slice(start, end, contentType);
    }-*/;

}