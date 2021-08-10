package com.agaram.eln.primary.model.methodsetup;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.stereotype.Service;

/**
 * This class is used to handle date objects by converting them to
 * appropriate format to display while marshalling and unmarshalling of xml data
 * @author ATE153
 * @version 1.0.0
 * @since   11- Nov- 2019
 */
@Service
public class InstantDateAdapter extends XmlAdapter<String, Instant>{	
	
	
	
	/**
	 * This overridden method is used to convert string date to Instant date
	 * while unmarshalling
	 * @param stringDate [String] date value to convert
	 * @return instant date
	 * @throws Exception exception thrown while unmarshalling
	 */
	@Override 
	public Instant unmarshal(final String stringDate) throws Exception{ 		
		final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
		Instant result = Instant.from(formatter.parse(stringDate));
		return result;
	} 
	
	/**
	 * This overridden method is used to convert instant date to string formatted date
	 * while marshalling
	 * @param instantDate [String] date value to convert
	 * @return converted instant date
	 * @throws Exception exception thrown while marshalling
	 */
	@Override 
	public String marshal(final Instant instantDate) throws Exception {
		
//		DateTimeFormatter formatter =
//			    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT, FormatStyle.LONG )
//			                     .withLocale( Locale.US )
//			                     .withZone( ZoneId.systemDefault());			                  
//		return formatter.format( instantDate );
		
		//List<Site> siteList = siteRepo.findByStatus(1);
			
		
		return instantDate.toString();
	} 
//	private String getDate()
//	{
//		TransactionStatusDetailService detailService =  new TransactionStatusDetailService();
//		List<TransactionStatusDetail> siteList = detailService.getTransDetailByTransStatus(1);

//		return "date";
//	}
}


