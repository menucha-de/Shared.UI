package havis.net.ui.shared.client.upload;

import com.google.gwt.user.client.ui.FileUpload;

/**
 * Extends GWT's Fileupload class as that one does not support the HTML5 feature
 * of a multiple-select uploader.
 */
public class MultipleFileUpload extends FileUpload {

    public MultipleFileUpload() {
        super();
        this.getElement().setAttribute("multiple", "multiple");
        // DOM.setElementAttribute(this.getElement(), "multiple", "multiple");
        // enable multiple select for browsers that support it
    }

    /**
     * This is an HTML5 feature which returns {@code null} if the current
     * browser does not support the file api.
     *
     * @return the file list
     */
    public final FileList getFileList() {
        final Object fileObject = this.getElement().getPropertyObject("files");
        if (fileObject instanceof elemental.html.FileList) {
            return (FileList) fileObject;
        } else {
            return null;
        }
    }

    /**
     * remove the last selection of files.
     * (does not work on Internet Explorer,
     *  use Form.reset() instead).
     */
    public final void reset() {
        this.getElement().setPropertyObject("value", null);
    }
    
    public void setAccept(String accept) {
    	this.getElement().setAttribute("accept", accept);
    }

}