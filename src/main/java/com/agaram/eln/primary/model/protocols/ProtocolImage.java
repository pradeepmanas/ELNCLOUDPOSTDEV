package com.agaram.eln.primary.model.protocols;

import javax.persistence.Transient;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.agaram.eln.primary.model.cfr.LScfttransaction;

@Document(collection = "ProtocolImage")
public class ProtocolImage {
	 @Id
	    private Integer id;
	    
	    private String name;
	    
	    private String fileid;
        
	        
	    private Binary image;

	    @Transient
		LScfttransaction objsilentaudit;

		public String getFileid() {
			return fileid;
		}

		public void setFileid(String fileid) {
			this.fileid = fileid;
		}

		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public Binary getImage() {
			return image;
		}

		public LScfttransaction getObjsilentaudit() {
			return objsilentaudit;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setImage(Binary image) {
			this.image = image;
		}

		public void setObjsilentaudit(LScfttransaction objsilentaudit) {
			this.objsilentaudit = objsilentaudit;
		}
	    
	    
}
