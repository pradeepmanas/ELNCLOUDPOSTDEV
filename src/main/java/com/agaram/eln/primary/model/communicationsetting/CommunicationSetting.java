package com.agaram.eln.primary.model.communicationsetting;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentType;


@Entity
@Table(name = "communicationsetting")
public class CommunicationSetting {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "cmmsettingcode")
	private Long cmmsettingcode;	
	private Integer nequipmentcode;	
	private String ipaddress;
	private Integer tcpportno;
	private Integer comportno;
	private String baudrate;	
	private String channalno;
	private String mindatapoint;
	private String maxdatapoint;
	private String mincurrent;
	private String maxcurrent;
	private Integer tidlesecond;
	private String databits;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parity")
	private Parity parity;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cmmtype")
	private InstrumentType instrumenttype;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conversiontype")
	private ConversionType conversiontype;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stopbit")
	private StopBits stopbits;
		
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "handshake")
	private HandShake handshake;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rsampleidf")
	private ResultSampleFrom rsampleidf;

	public Long getCmmsettingcode() {
		return cmmsettingcode;
	}

	public void setCmmsettingcode(Long cmmsettingcode) {
		this.cmmsettingcode = cmmsettingcode;
	}

	public Integer getNequipmentcode() {
		return nequipmentcode;
	}

	public void setNequipmentcode(Integer nequipmentcode) {
		this.nequipmentcode = nequipmentcode;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Integer getTcpportno() {
		return tcpportno;
	}

	public void setTcpportno(Integer tcpportno) {
		this.tcpportno = tcpportno;
	}

	public Integer getComportno() {
		return comportno;
	}

	public void setComportno(Integer comportno) {
		this.comportno = comportno;
	}

	public String getBaudrate() {
		return baudrate;
	}

	public void setBaudrate(String baudrate) {
		this.baudrate = baudrate;
	}

	public String getChannalno() {
		return channalno;
	}

	public void setChannalno(String channalno) {
		this.channalno = channalno;
	}

	public String getMindatapoint() {
		return mindatapoint;
	}

	public void setMindatapoint(String mindatapoint) {
		this.mindatapoint = mindatapoint;
	}

	public String getMaxdatapoint() {
		return maxdatapoint;
	}

	public void setMaxdatapoint(String maxdatapoint) {
		this.maxdatapoint = maxdatapoint;
	}

	public String getMincurrent() {
		return mincurrent;
	}

	public void setMincurrent(String mincurrent) {
		this.mincurrent = mincurrent;
	}

	public String getMaxcurrent() {
		return maxcurrent;
	}

	public void setMaxcurrent(String maxcurrent) {
		this.maxcurrent = maxcurrent;
	}

	public Integer getTidlesecond() {
		return tidlesecond;
	}

	public void setTidlesecond(Integer tidlesecond) {
		this.tidlesecond = tidlesecond;
	}

	public String getDatabits() {
		return databits;
	}

	public void setDatabits(String databits) {
		this.databits = databits;
	}

	public Parity getParity() {
		return parity;
	}

	public void setParity(Parity parity) {
		this.parity = parity;
	}

	public InstrumentType getInstrumenttype() {
		return instrumenttype;
	}

	public void setInstrumenttype(InstrumentType instrumenttype) {
		this.instrumenttype = instrumenttype;
	}

	public ConversionType getConversiontype() {
		return conversiontype;
	}

	public void setConversiontype(ConversionType conversiontype) {
		this.conversiontype = conversiontype;
	}

	public StopBits getStopbits() {
		return stopbits;
	}

	public void setStopbits(StopBits stopbits) {
		this.stopbits = stopbits;
	}

	public HandShake getHandshake() {
		return handshake;
	}

	public void setHandshake(HandShake handshake) {
		this.handshake = handshake;
	}

	public ResultSampleFrom getRsampleidf() {
		return rsampleidf;
	}

	public void setRsampleidf(ResultSampleFrom rsampleidf) {
		this.rsampleidf = rsampleidf;
	}
	
	
	
	
	
}
