package com.agaram.eln.primary.view;

 import org.springframework.web.servlet.view.AbstractView;

 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import java.util.Map;

 /**
  * This abstract class holds methods that are used to create CSV files
  * @author ATE128
  * @version 1.0.0
  * @since   07- Jan- 2020
  */
public abstract class AbstractCsvView extends AbstractView {

    private static final String CONTENT_TYPE = "text/csv";

    /**
     * This constructor sets the appropriate content type "application/csv".
     *  Generated documents should have a ".csv" extension.
     */
    public AbstractCsvView() {
        setContentType(CONTENT_TYPE);
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());
        buildCsvDocument(model, request, response);
    }
	/**
	 * Subclasses must implement this method to build an iText csv document,
     * given the model. The built PDF document itself
     * will automatically get written to the response after this method returns.
	 * @param model the model Map
	 * @param request in case we need locale etc. Shouldn't look at attributes.
	 * @param response in case we need to set cookies. Shouldn't write to it.
	 * @throws Exception any exception that occurred during document building
	 */
    protected abstract void buildCsvDocument(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception;


}