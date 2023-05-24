package com.agaram.eln.primary.model.protocols;

import javax.persistence.Transient;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.agaram.eln.primary.model.cfr.LScfttransaction;

@Document(collection = "Protocolvideos")
public class Protocolvideos {
	 @Id
	    private Integer id;
	    
	    private String name;
	    
	    private String fileid;
     
	        
	    private Binary video;

	    @Transient
		LScfttransaction objsilentaudit;

		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getFileid() {
			return fileid;
		}

		public Binary getVideo() {
			return video;
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

		public void setFileid(String fileid) {
			this.fileid = fileid;
		}

		public void setVideo(Binary video) {
			this.video = video;
		}

		public void setObjsilentaudit(LScfttransaction objsilentaudit) {
			this.objsilentaudit = objsilentaudit;
		}
}
