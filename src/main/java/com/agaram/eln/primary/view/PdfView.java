package com.agaram.eln.primary.view;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.agaram.eln.primary.util.MapUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class to use to create PDF file with provided input data
 * 
 * @author ATE153
 * @version 1.0.0
 * @since 07- Jan- 2020
 */
public class PdfView extends AbstractPdfView {

	@Override
	protected void buildPdfMetadata(Map<String, Object> model, PdfWriter writer, Document document,
			HttpServletRequest request) {

		final String title = (String) model.get("title");
		final String footerText = (String) model.get("footerText");
		final Font dataFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL, BaseColor.BLACK);
		float rotation = 0f;

		final HeaderAndFooterPdfPageEventHelper headerAndFooter = new HeaderAndFooterPdfPageEventHelper(title,
				footerText, dataFont, rotation);
		writer.setPageEvent(headerAndFooter);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		// change the file name
		response.setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");

		final List<Map<String, Object>> dataList = (List<Map<String, Object>>) model.get("exportDataList");
		final Map<String, Object> deepDataObject = (Map<String, Object>) model.get("deepDataObject");
		final String localeCode = (String) model.get("localeCode");
		final String timeZone = (String) model.get("timeZone");

		final String pattern = "dd-MM-yyyy HH:mm:ss";
		final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

//    	final String pattern = //"dd-MM-yyyy HH:mm:ss"; 
//    						  "dd-MM-yyyy HH:mm:ss z";
//    	final DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, 
//    							SimpleDateFormat.LONG, new Locale("en-us"));
//    			//new SimpleDateFormat(pattern, Locale.CHINESE);	  
//    	dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));    	

//    	DateTimeFormatter dateFormat =
//			    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.LONG)
//			    //.ofLocalizedDateTime( FormatStyle.SHORT )
//			                     .withLocale( new Locale("en-us") )
//			                     .withZone( ZoneId.of("Asia/Calcutta"));			                  

		final Font blueFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD, BaseColor.BLUE);
		final Font redFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD, new CMYKColor(0, 255, 0, 0));
		final Font dataFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL, BaseColor.BLACK);
		final Font blackBoldFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD, BaseColor.BLACK);

		final List<String> columnList = new ArrayList<String>();

		for (Map.Entry<String, Object> dataEntry : dataList.get(0).entrySet()) {
			if (!dataEntry.getKey().equalsIgnoreCase("parsedXML") && !dataEntry.getKey().equalsIgnoreCase("xmlrecords")
					&& !dataEntry.getKey().equalsIgnoreCase("detailedData")) {
				if (!dataEntry.getKey().equalsIgnoreCase("select")) {
					columnList.add(dataEntry.getKey());
				}
			}
		}

		final PdfPTable tableHeader = new PdfPTable(columnList.size());
		tableHeader.setWidthPercentage(100f);

		for (String column : columnList) {
			Paragraph paragraph = new Paragraph(column.toUpperCase(), blueFont);
			PdfPCell cell = new PdfPCell(paragraph);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			tableHeader.addCell(cell);
		}
		document.add(tableHeader);

		for (Map<String, Object> data : dataList) {
			PdfPTable table = new PdfPTable(columnList.size());
			table.setWidthPercentage(100f);
			Map<String, Object> xmlData = new HashMap<String, Object>();
			for (Map.Entry<String, Object> dataEntry : data.entrySet()) {
				if (!dataEntry.getKey().equalsIgnoreCase("parsedXML")
						&& !dataEntry.getKey().equalsIgnoreCase("xmlrecords")
						&& !dataEntry.getKey().equalsIgnoreCase("detailedData")) {

					Paragraph para = null;

					if (dataEntry.getValue() instanceof Integer && !dataEntry.getKey().equalsIgnoreCase("select")) {
						para = new Paragraph(String.valueOf((Integer) dataEntry.getValue()), dataFont);
					} else if (dataEntry.getValue() instanceof java.util.LinkedHashMap) {
						String value = "";
						if (deepDataObject.containsKey(dataEntry.getKey())) {
							List<String> dataValueMap = (List<String>) deepDataObject.get(dataEntry.getKey());
							value = // maputil
									new MapUtil().getNestedValue((Map<String, Object>) dataEntry.getValue(),
											dataValueMap);
//	        					
						}
						para = new Paragraph(value, dataFont);
					} else {
						if (dataEntry.getKey().equalsIgnoreCase("createddate")) {

							para = new Paragraph(
									dateFormat.format(Date.from(Instant.parse((String) dataEntry.getValue()))),
									dataFont);

							// para = new Paragraph(
							// LocalDateTime.parse((String)dataEntry.getValue()).format(dateFormat),
							// dataFont);

							// para = new
							// Paragraph(dateFormatObj.zoneFormattedDateString((String)dataEntry.getValue(),
							// localeCode, timeZone), dataFont);

						} else {
							if (!dataEntry.getKey().equalsIgnoreCase("select")) {
								para = new Paragraph((String) dataEntry.getValue(), dataFont);
							}

						}

					}
					if(para != null)
       			 {
					PdfPCell cell = new PdfPCell(para);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					document.add(table);
       			 }
				}

				if (dataEntry.getKey().equalsIgnoreCase("parsedXML")
						|| dataEntry.getKey().equalsIgnoreCase("detailedData")) {
					xmlData = (Map<String, Object>) dataEntry.getValue();
				}
			}
			if (xmlData != null && xmlData.size() > 0) {
				copyXMLToPDF(xmlData, columnList, document, redFont, dataFont, blackBoldFont, dateFormat,
						deepDataObject, localeCode, timeZone);
			}
		}
	}

	/**
	 * This method is used to organize the xml formatted data or the map object of
	 * detailed content to a new row.
	 * 
	 * @param dataMap        [Map] object with content
	 * @param columnList     [List] header column list
	 * @param document       [Document] pdf document to write
	 * @param redFont        [Font] red colored font
	 * @param dataFont       [Font] font for content
	 * @param blackBoldFont  [Font] font with bold format
	 * @param deepDataObject [Map] object containing 'values to access deeply nested
	 *                       map object
	 * @param localeCode     [String] locale to convert
	 * @param timeZone       [String] TimeZone to convert
	 * @throws DocumentException thrown if caught on pdf conversion.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void copyXMLToPDF(Map<String, Object> dataMap, List<String> columnList, final Document document,
			Font redFont, Font dataFont, Font blackBoldFont, SimpleDateFormat dateFormat,
			Map<String, Object> deepDataObject, final String localeCode, final String timeZone)
			throws DocumentException {
		for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
			PdfPTable diffRowTitle = new PdfPTable(1);
			diffRowTitle.setWidthPercentage(97f);

			// Start - Difference data header Title
			PdfPCell cell = new PdfPCell(new Paragraph(entry.getKey().toUpperCase(), redFont));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setIndent(5f);
			diffRowTitle.addCell(cell);
			document.add(diffRowTitle);
			// End - Difference data header Title

			List<Map<String, Object>> mapList = (ArrayList) entry.getValue();

			int loopIndex = 1;
			for (Map<String, Object> map : mapList) {
				if (loopIndex == 1) {
					// Start - Difference data header columns
					PdfPTable diffRowHeader = new PdfPTable(map.keySet().size());
					diffRowHeader.setWidthPercentage(97f);
					for (String keyEntry : map.keySet()) {
						PdfPCell headerCell = new PdfPCell(new Paragraph(keyEntry.toUpperCase(), blackBoldFont));
						headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						diffRowHeader.addCell(headerCell);
					}
					document.add(diffRowHeader);
					// End- difference data header columns
				}
				;
				// Start - Difference data
				PdfPTable diffRowData = new PdfPTable(map.entrySet().size());
				diffRowData.setWidthPercentage(97f);

				for (Map.Entry<String, Object> valueEntry : map.entrySet()) {
					Paragraph paragraph = null;
					if (valueEntry.getKey().equalsIgnoreCase("createddate")) {
						paragraph = new Paragraph(
								// dateFormat.format(new Date((String)valueEntry.getValue())), dataFont);

								// dateFormat.format(
								// Date.from(Instant.parse((String)valueEntry.getValue()))), dataFont);
								// dateFormat.format(Instant.parse((String)valueEntry.getValue())), dataFont);
								(String) valueEntry.getValue(), dataFont);

//						 paragraph = new Paragraph(								
//								dateFormat.format(Instant.parse((String)valueEntry.getValue())), dataFont);
						// DateFormat - dateFormat.format(new Date((String)valueEntry.getValue())),
						// dataFont);
						// dateFormat.format(Instant.parse((String)valueEntry.getValue())), dataFont);
						// LocalDateTime.parse((String)valueEntry.getValue()).format(dateFormat),
						// dataFont);

						// dateFormatObj.zoneFormattedDateString((String)valueEntry.getValue(),
						// localeCode, timeZone), dataFont);
					} else {
						if (valueEntry.getValue() instanceof Integer) {
							paragraph = new Paragraph(String.valueOf((Integer) valueEntry.getValue()), dataFont);
						} else if (valueEntry.getValue() instanceof java.util.LinkedHashMap) {
							String value = "";
							if (deepDataObject.containsKey(valueEntry.getKey())) {
								List<String> dataValueMap = (List<String>) deepDataObject.get(valueEntry.getKey());
								value = new MapUtil().getNestedValue((Map<String, Object>) valueEntry.getValue(),
										dataValueMap);

							}

							paragraph = new Paragraph(value, dataFont);
						} else {
							paragraph = new Paragraph((String) valueEntry.getValue(), dataFont);
						}
					}

					PdfPCell dataCell = new PdfPCell(paragraph);
					dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					dataCell.setIndent(5f);
					diffRowData.addCell(dataCell);
				}
				document.add(diffRowData);
				// End - Difference data

				loopIndex++;
			}
		}
	}

}

/**
 * This class is used to set header and footer for Pdf document.
 * 
 * @author ATE153
 *
 */
class HeaderAndFooterPdfPageEventHelper extends PdfPageEventHelper {

	private String title;
	private String footerText;
	private Font dataFont;
	private float rotation;

	public HeaderAndFooterPdfPageEventHelper() {
	}

	/**
	 * Constructor to initialize properties.
	 * 
	 * @param title      [String] title for the document
	 * @param footerText [String] text to be included in footer
	 * @param dataFont   [Font] content font format
	 * @param rotation   [float] rotation of text in display
	 */
	public HeaderAndFooterPdfPageEventHelper(final String title, final String footerText, final Font dataFont,
			final float rotation) {
		this.title = title;
		this.footerText = footerText;
		this.dataFont = dataFont;
		this.rotation = rotation;
	}

	public void onStartPage(PdfWriter pdfWriter, Document document) {
		Rectangle rect = pdfWriter.getBoxSize("rectangle");

		// TOP LEFT
//	      ColumnText.showTextAligned(pdfWriter.getDirectContent(),
//	               Element.ALIGN_CENTER, new Phrase("TOP LEFT"), rect.getLeft(),
//	               rect.getTop()+30, 0);
		// ColumnText.showTextAligned(canvas, alignment, phrase, x, y, rotation);

		// TOP MEDIUM
		ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER, new Phrase(this.title),
				rect.getRight() / 2, rect.getTop() + 20, rotation);

		// TOP RIGHT
//	      ColumnText.showTextAligned(pdfWriter.getDirectContent(),
//	               Element.ALIGN_CENTER, new Phrase("TOP RIGHT"), rect.getRight(),
//	               rect.getTop(), 0);
	}

	@SuppressWarnings("deprecation")
	public void onEndPage(PdfWriter pdfWriter, Document document) {
		Rectangle rect = pdfWriter.getBoxSize("rectangle");
		// BOTTOM LEFT
		ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase(this.footerText, dataFont), rect.getLeft() + 60, rect.getBottom() - 15, rotation);

		// BOTTOM MEDIUM
		ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase(String.valueOf(pdfWriter.getPageNumber()), dataFont), rect.getRight() / 2,
				rect.getBottom() - 15, rotation);

		// BOTTOM RIGHT
		ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase(new Date().toGMTString(), dataFont), rect.getRight() - 30, rect.getBottom() - 15, rotation);
	}
}
