package com.agaram.eln.primary.model.cfr.embeddable;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class AudittrailconfigurationId implements Serializable   {
		
		private Integer auditcofigcode;
		
		public Integer getAuditcofigcode() {
			return auditcofigcode;
		}

		public void setAuditcofigcode(Integer auditcofigcode) {
			this.auditcofigcode = auditcofigcode;
		}

		public AudittrailconfigurationId()
		{
			
		}
		
		public AudittrailconfigurationId(Integer auditcofigcode) {
	        this.auditcofigcode = auditcofigcode;
	    }


		public int hashCode() {
	        return (int)this.auditcofigcode.hashCode();
	    }

	    public boolean equals(Object obj) {
	        if (obj == this) return true;
	        if (!(obj instanceof AudittrailconfigurationId)) return false;
	        AudittrailconfigurationId pk = (AudittrailconfigurationId) obj;
	        return pk.auditcofigcode.equals(this.auditcofigcode);
	    }

}
