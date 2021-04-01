package com.agaram.eln.primary.model.instrumentDetails;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class LSresultvaluesId implements Serializable {

	@Column(columnDefinition = "numeric(20,0)",name = "RESULTDETID") 
	private String RESULTDETID;
	
	@Column(name = "ResSeqNo") 
	private Integer resseqno;
	
	public LSresultvaluesId() {
		
	}
	
	public LSresultvaluesId(String resultid, Integer resseqno) {
        this.RESULTDETID = resultid;
        this.resseqno = resseqno;
    }

	public String getResultid() {
		return RESULTDETID;
	}

	public void setResultid(String resultid) {
		this.RESULTDETID = resultid;
	}

	public Integer getResseqno() {
		return resseqno;
	}

	public void setResseqno(Integer resseqno) {
		this.resseqno = resseqno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resseqno == null) ? 0 : resseqno.hashCode());
		result = prime * result + ((RESULTDETID == null) ? 0 : RESULTDETID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LSresultvaluesId other = (LSresultvaluesId) obj;
		if (resseqno == null) {
			if (other.resseqno != null)
				return false;
		} else if (!resseqno.equals(other.resseqno))
			return false;
		if (RESULTDETID == null) {
			if (other.RESULTDETID != null)
				return false;
		} else if (!RESULTDETID.equals(other.RESULTDETID))
			return false;
		return true;
	}
	
}
