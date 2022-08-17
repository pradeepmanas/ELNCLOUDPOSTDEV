package com.agaram.eln.primary.global;

public class Enumeration {
	public static final String DELIM = ".";

	public enum TransactionStatus {
		NA(-1), ALL(0), DELETED(-1), ACTIVE(1), DEACTIVE(2), YES(3), NO(4), LOCK(5), UNLOCK(6), RETIRED(7), DRAFT(8),
		MANUAL(9), SYSTEM(10), START(11), RESTART(12), STOP(13), RECEIVED(14), GOODS_IN(15), GOODS_RECEIVED(16),
		PREREGISTER(17), REGISTERED(18), RECIEVED_IN_LAB(19), ALLOTTED(20), PARTIALLY(21), COMPLETED(22), CHECKED(23),
		REVIEWED(24), VERIFIED(25), APPROVED(26), AUTHORISED(27), RELEASED(28), RECOM_RETEST(29), RECOM_RECALC(30),
		RETEST(31), RECALC(32), REJECTED(33), CANCELED(34), HOLD(35), RESERVED(36), QUARENTINE(37), SCHEDULED(38),
		RE_SCHEDULED(39), INVITED(40), ACCEPTED(41), INITIATED(42), ADD(43), EDIT(44), DELETE(45), DEFAULT(46),
		RECEIVE(47), ISSUE(48), AUTO(49), PASS(50), FAIL(51), WITHDRAWN(52), CORRECTION(53), EXPIRED(55),
		NEEDVALIDATIONRECALC(2), NEEDVALIDATIONRETEST(1), NEEDVALIDATION(0), CERTIFIED(54), CHECKLIST_TEMPLATE_VIEW(1),
		CHECKLIST_TEST_GROUP_TEMPLATE_VIEW(2), MASTER(1), TRANSACTION(2), CONDUCTED(68), ATTENDED(69),
		CHECKLIST_RESULTENTRY_VIEW(3), CALIBIRATION(64), UNDERCALIBIRATION(65), MAINTANENCE(66), DAYPERIOD(4),
		MONTHPERIOD(5), YEARPERIOD(6), ACCREDIT(70), NOTACCREDIT(71), UNDERMAINTANENCE(67), UNDERVALIDATION(63),
		VALIDATION(62), RETURN(72), NON_EMPTY(0), STARTED(75), DE_ACTIVATED(77), INPROGRESS(78), OBSOLETE(81),
		OUTDATED(10), INSTORE(85), DISCARD(84), TERMINATE(84), TESTSTARTED(86), REQUESTED(82), RESPONDED(83),
		CLOSED(90), LOGIN(170), LOGOUT(171), SETDEFAULT(123), SEQUENCEONE(1), INHOUSE(1),
		OUTSIDE(2),ALIQUOTREQUEST(98), ALIQUOTCREATE(99), PLATECREATED(100), EMPTY(101), PARTIALLYFILLED(102), SENDTOSTORE(103),
		INVENTORY(104), SAMPLEREGISTRATION(105), QUALITYCONTROL(106), REQUEST(107), SAMPLEISSUE(108),
		CHAINOFCUSTODY(109), ALIQUOTED(110), DEVIATION(111), PROCESSED(112), MOVED(113), FILLED(114),
		RUNSEQUENCECOMPLETED(115), QCADDED(116), PARTIALLY_COMPLETED(117),
		STEPSTARTED(118), STEPCOMPLETED(119), STEPINPROGRESS(120), RUNSTARTED(121), RUNCOMPLETED(141),
		CUSTODYRELASED(122), INPUT(142), OUTPUT(154), QC(152), NON_QC(153), INS_STARTED(123), INS_STOPPED(124),
		INS_TRAY_OPENED(125), INS_TRAY_CLOSED(126), INS_RUN_STARTED(127), INS_RUN_COMPLETED(128), INS_SCANNED(129),
		INS_SCANCOMPLETE(147), INS_CAPDECAP(130), Temperature(131), Door(132), Out_of_Temperature(133), High(134),
		Low(135), Open(136), Close(137), Case_Close(173), CentriSpeed(138), CentriDuration(139), CentriTemperature(140),
		SAMPLEREQUESTED(143), FOUND(145), SAMPLE_MISSING(148), SAMPLE_EMPTY(149), CHANGE_SAMPLE_REQUEST(150),
		PREPARATION_INPROGRESS(151), BIOBANK_REQUEST(144), NON_BIOBANK_REQUEST(146), SENDTOINCUBATE(185),
		VIALMOVEDTOPLATE(202), BLACKLIST(87), USED(91), SENT(78), NULLIFIED(79), CERTIFICATECORRECTION(75),
		STANDARDTYPE(1),VOLUMETRICTYPE(2),MATERIALINVENTORYTYPE(3);

		private final int transactionstatus;

		private TransactionStatus(int transactionstatus) {
			this.transactionstatus = transactionstatus;
		}

		public int gettransactionstatus() {
			return transactionstatus;
		}
	}

	public enum QualisForms {
		MyJob(107), JobAllocation(110), SAMPLEREGISTRATION(43), RESULTENTRY(56), CLOCKHISTORY(94),TESTWISEMYJOB(142);
		private final int qualisforms;

		private QualisForms(int qualisforms) {
			this.qualisforms = qualisforms;
		}

		public int getqualisforms() {
			return qualisforms;
		}
	}
	
	public enum ResultEntryResult {
		RESULTSTATUS_PASS(1), RESULTSTATUS_OOS(3), RESULTSTATUS_OOT(2), RESULTSTATUS_FIO(4);

		private final int resultentryresult;

		private ResultEntryResult(int resultentryresult) {
			this.resultentryresult = resultentryresult;
		}

		public int getresultentryresult() {
			return resultentryresult;
		}
	}

	public enum LoginType {
		INTERNAL(1), ADS(2);

		private final int nlogintype;

		private LoginType(int nlogintype) {
			this.nlogintype = nlogintype;
		}

		public int getnlogintype() {
			return nlogintype;
		}
	}

	public enum DashBoardStatus {
		STATUS_DELETE(0), STATUS_ACTIVE(1), STATUS_USER_LEVELS(1), STATUS_USERS(2), DATE_CONTROLCODE(5),
		TISSUEVERIFY(16);

		DashBoardStatus(int dashboardstatus) {
			this.dashboardstatus = dashboardstatus;
		}

		private final int dashboardstatus;

		public int getdashboardstatus() {
			return dashboardstatus;
		}
	}

	public enum ReturnStatus {
		SUCCESS("Success"), ALREADYEXISTS("IDS_ALREADYEXISTS"), ALREADYDELETED("IDS_ALREADYDELETED"),
		ALREADYOPENED("IDS_ALREADYCLOSED"), ALREADYCLOSED("IDS_ALREADYCLOSED"), RETURNSTRING("rtn"),
		RETURNVALUE("value"), ALREADYAPPROVED("IDS_ALREADYAPPROVED"), ALREADYRETIRED("IDS_ALREADYRETIRED"),
		ALREADYCERTIFIED("IDS_ALREADYCERTIFIED"), NOTESTGROUP("IDS_NOTESTGROUP"),
		ALREADYRELEASED("IDS_ALREADYRELEASED"), SAMPLEREGSTATUS("SampleRegStatus"),
		ALREADYCOMPLETED("IDS_ALREADYCOMPLETED"), RECORD_UPDATE("IDS_RECORD_UPDATE"),
		RECORD_DELETE("IDS_RECORD_DELETE"), RECORD_COMPLETE("IDS_RECORD_COMPLETE"), APPROVED("IDS_APPROVED"),
		RECORD_UPDATE_TP_ATTENDED("IDS_RECORD_UPDATE_TP_ATTENDED"), BARCODEEXISTS("IDS_BARCODEEXISTS"),
		TOOMANYUSERS("IDS_TOOMANYUSERS"), ORDERCODENOTGOT("IDS_ORDERCODENOTGOT"), SAMPLERECEIVED("IDS_SAMPLERECEIVED"),
		SAMPLEALIQUOTED("IDS_SAMPLEALIQUOTED"), SAMPLERESERVED("IDS_SAMPLERESERVED"), FORANALYSIS("IDS_FORANALYSIS"),
		SAMPLEINSTORE("IDS_SAMPLEINSTORE"), DISCARD("IDS_SAMPLEDISCARD"), RETURN("IDS_SAMPLERETURN"),
		LICENSEEXEEDED("IDS_LICENSEEXEEDED"), CHECKLICENSECONFIG("IDS_CHECKLICENSECONFIG"),
		LICENSEMISMATCH("IDS_LICENSEMISMATCHED"), SAMEUSERANDROLELOGIN("IDS_SAMEUSERANDROLELOGIN"), FAILED("Failed"),
		DEFAULTCANNOTDELETE("IDS_DELETEDEFAULT"),
		DEFAULTROLECANNOTBEDELETED("IDS_DELETEDEFAULTROLE"), 
		DEFAULTUSERSITECANNOTDELETED("IDS_DEFAULTUSERSITECANNOTDELETED"), PLATEALREADYADDED("IDS_PLATEALREADYADDED"),
		QUANTITYGREATER("IDS_QUANTITYGREATER"), ENTERVALIDINVENTORYID("IDS_ENTERVALIDINVENTORYID"),
		DUPLICATEBARCODE("IDS_DUPLICATEBARCODE"), DRAFTRETIRED("IDS_DRAFTRETIRED"),
		ALREADYDEFAULT("IDS_ALREADYDEFAULT"), ADDQUESTIONTOAPPROVE("IDS_ADDQUESTIONTOAPPROVE"),
		NOTPOSSIBLETODELETEAPPROVE("IDS_NOTPOSSIBLETODELETEAPPROVE"), SELECTDRAFTVERSION("IDS_SELECTDRAFTVERSION"),
		ALREADYCANCELED("IDS_ALREADYCANCELED"), SELECTREGISTEREDSAMPLE("IDS_SELECTREGISTEREDSAMPLE"),
		INVALIDUSER("IDS_INVALIDUSER"), DEFAULTUSERROLEMUSTBETHERE("IDS_DEFAULTUSERROLEMUSTBETHERE"),
		INACTIVEDEFAULTROLE("IDS_INACTIVEDEFAULTROLE"), APPROVETHEPROJECT("IDS_APPROVETHEPROJECT"),
		OLDPASSWORDMISSMATCH("IDS_OLDPASSWORDMISSMATCH"),
		IDS_DUPNOALLOW("IDS_DUPNOALLOW"), IDS_PLATEALREADYFILL("IDS_PLATEALREADYFILL"),
		SELECTPLATETYPEFORMAT("IDS_SELECTPLATETYPEFORMAT"), ALREADYREGISTERED("IDS_ALREADYREGISTERED"),
		ALREADYSTARTTED("IDS_ALREADYSTARTTED"), SELECTSTARTTED("IDS_SELECTSTARTTED"),
		NODEFAULTRESULT("IDS_NODEFAULTRESULT"), ADDRESULTTOCOMPLETE("IDS_ADDRESULTTOCOMPLETE"),
		SELECTREGISTERSAMPLE("IDS_SELECTREGISTERSAMPLE"), SELECTSAMEINSTRUMENTCATTEST("IDS_SELECTSAMEINSCATTEST"),
		ALREADYTESTALLOTED("IDS_ALREADYTESTALLOTED"), ALREADYTESTSTARTTED("IDS_ALREADYTESTSTARTED"),
		ALLOTEDACCEPTEDTESTCANRESCHEDULE("IDS_ALLOTEDACCEPTEDTESTCANRESCHEDULE"),
		ALREADYREJECTED("IDS_ALREADYREJECTED"), SAMPLEINPROGRESS("IDS_SAMPLEINPROGRESS"), TABLOCK("WITH (TABLOCKx)"),
		SAMPLECODENOTAVAILABLE("IDS_SAMPLECODENOTAVAILABLE"), SAMPLEEXISTS("IDS_SAMPLEEXISTS"), NOSAMPLE("NOSAMPLE"),
		LOCATIONDELETED("IDS_LOCATIONDELETED"), CALIBRATED("AutoCalibrated"), UNDERCALIBRATED("AutoUnderCalibration"),
		MAINREPORTDELETEVAL("IDS_MAINREPORTDELETEVAL"), REPORTALREADYCONFIG("IDS_REPORTALREADYCONFIG"),
		REPORTNOTCONFIGURED("Selected Report is not Configured"), FILEALREADYEXISTS("File Already Exists"),
		NOTVALIDIN("Not Valid in"), SHOULDNOTEMPTY("Should Not Empty on row"), DEFAULTCANNOTCHANGED("IDS_EDITDEFAULT"), 
		UPLOAD_EXCEL("Upload Excel File Only.."), NORELATEDDATA("No Related Data Found"), ROWLOCK("WITH (ROWLOCK)"),
		STR_ENCRYPT_SECRET("AGARAM_SDMS_SCRT"),STANDARDMATERIALNAME("Standard Name")
		,VOLUMETRICMATERIALNAME("Volumetric Name"),MATERIALNAME("Material Name"),
		 ALREADYRECEIVED("IDS_ALREADYRECEIVED"),ALREADYATTENDED("IDS_ALREADYATTENDED"),ALREADYCOMPETENT("IDS_ALREADYCOMPETENT"),
		 ALREADYINVITED("IDS_ALREADYINVITED");

		private final String returnStatus;

		private ReturnStatus(String returnStatus) {
			this.returnStatus = returnStatus;
		}

		public String getreturnstatus() {
			return returnStatus;

		}

	}

	public enum SentToOOS {
		ALREADYSENTOOS("Selected Test(s) Already Sent to OOSInvestigation..!!"),
		SUCCESSFULLYSENTOOS("Test Sent to OOSInvestigation SuccessFully...!!");

		private final String status;

		public String getstatus() {
			return status;
		}

		private SentToOOS(String status) {
			this.status = status;
		}

	}

	public enum ModuleType {
		LIMS(1), STABILITY(2);

		private final int moduletype;

		private ModuleType(int moduletype) {
			this.moduletype = moduletype;
		}

		public int getModuletype() {
			return moduletype;
		}
	}

	public enum JobWorkFlow {
		DEFAULTMODE(1005), MANUALMODE(1006), AUTOMODE(1007);

		private final int jobworkflow;

		private JobWorkFlow(int jobworkflow) {
			this.jobworkflow = jobworkflow;
		}

		public int getJobWorkFlow() {
			return jobworkflow;
		}
	}

	public enum ParameterType {
		NUMERIC(1), PREDEFINED(2), CHARACTER(3), ATTACHEMENT(4);

		private final int parametertype;

		private ParameterType(int parametertype) {
			this.parametertype = parametertype;
		}

		public int getparametertype() {
			return parametertype;
		}

	}

	public enum Grade {
		NA(-1), PASS(1), OOT(2), OOS(3), FIO(4);

		private final int Grade;

		private Grade(int Grade) {
			this.Grade = Grade;
		}

		public int getGrade() {
			return Grade;
		}

	}

	public enum PasswordValidate {
		PASS(-1), ID_MISMATCH(1), PWD_MISMATCH(2), RETIRED(3), LOCK(4), DEACTIVE(5), NEW_USER(6), RESET_USER(7),
		CHECKPASSWORDPOLICYROLE(10), LOGINLIMITEXCEED(11), CHECKLICENSECONFIG(12), SAMEUSERLOGIN(16),
		SAMEUSERANDROLELOGIN(14), LICENSEMISMATCH(15), USER_LOCK(5), EXPIRED(55);

		private final int paswordvalidate;

		private PasswordValidate(int paswordvalidate) {
			this.paswordvalidate = paswordvalidate;
		}

		public int getPaswordvalidate() {
			return paswordvalidate;
		}
	}

	public enum RegistrationType {
		INSTRUMENT(4), MATERIAL(5), BATCH(1), NONBATCH(2), PLASMAPOOL(3), ROUTINE(6);

		private final int regtype;

		private RegistrationType(int regtype) {
			this.regtype = regtype;
		}

		public int getregtype() {
			return regtype;
		}
	}

	public enum RegistrationSubType {
		BATCH_EU(1), BATCH_NONEU(2), BATCH_PROTOCOL(5), NONBATCH_EU(3), NONBATCH_NONEU(4), PLASMA_EU(6),
		PLASMA_NONEU(7), MATERIAL(10), EXTERNALPOOL(10), ROUTINE(13), STABILITY(2), INSTRUMENT(11), CLINICAL(14);

		private final int regsubtype;

		private RegistrationSubType(int regsubtype) {
			this.regsubtype = regsubtype;
		}

		public int getregsubtype() {
			return regsubtype;
		}
	}

	public enum ApprovalSubType {
		STUDYPLANAPPROVAL(1), TESTRESULTAPPROVAL(2), BATCHAPPROVAL(3), PRODUCTAPPROVAL(4), PRODUCTMAHAPPROVAL(5);

		private final int nsubtype;

		private ApprovalSubType(int nsubtype) {
			this.nsubtype = nsubtype;
		}

		public int getnsubtype() {
			return nsubtype;
		}
	}

	public enum FTP {
		UPLOAD_PATH("\\webapps\\ROOT\\SharedFolder\\FileUpload\\");
		public static final String DELIM = ".";

		private final String FTP;

		private FTP(String FTP) {
			this.FTP = FTP;
		}

		public String getFTP() {
			return FTP;
		}
	}

	public enum DesignComponent {
		TEXTBOX(1), TEXTAREA(2), COMBOBOX(3), DATEFEILD(4), NUMBER(5), CHECKBOX(6);

		private final int type;

		private DesignComponent(int type) {
			this.type = type;
		}

		public int gettype() {
			return type;
		}

	}
	
	public enum LinkType {
		CUSTOM(1), EXISTING(2), NONLIST(-1);

		private LinkType(int type) {
			this.type = type;
		}

		private final int type;

		public int getType() {
			return type;
		}

	}

	public enum SampleType {

		PRODUCT(1), MATERIAL(3), INSTRUMENT(2), STORAGECATEGORY(4);
		private SampleType(int type) {
			this.type = type;
		}

		private final int type;

		public int getType() {
			return type;
		}

	}

	public enum Sequenceintialvalue {

		ZERO(0), ONE(1);

		private Sequenceintialvalue(int sequence) {
			this.sequence = sequence;
		}

		private int sequence;

		public int getSequence() {
			return sequence;
		}

		public void setSequence(int sequence) {
			this.sequence = sequence;
		}

	}

	public enum Deletevalidator {

		SUCESS(0), ERORR(1), NOVALIDATION(-1);

		private int returnvalue;

		private Deletevalidator(int returnvalue) {
			this.returnvalue = returnvalue;
		}

		public int getReturnvalue() {
			return returnvalue;
		}

		public void setReturnvalue(int returnvalue) {
			this.returnvalue = returnvalue;
		}

	}

	public static final int STABILITY_RETURNQTY = 1;
	public static final int STABILITY_ADDITIONQTY = 2;
	public static final int STABILITY_APPROVALCOMMENTED = 3;
	public static final int STABILITY_STFAPPROVALCOMMENTED = 4;
	public static final int STABILITY_STARTSTOPAPPROVALCOMMENTED = 5;
	public static final int STABILITY_STYRETURNQTY = 6;

	public static final int STATUS_ACTIVE = 1;

	public enum auditEdit {
		VALUE(true);

		private auditEdit(boolean returnvalue) {
			this.returnvalue = returnvalue;
		}

		private boolean returnvalue;

		public boolean getReturnvalue() {
			return returnvalue;
		}

		public void setReturnvalue(boolean returnvalue) {
			this.returnvalue = returnvalue;
		}

	}

	public enum FTPImage {
		UPLOAD_PATH("\\webapps\\ROOT\\SharedFolder\\Report Images\\");
		public static final String DELIM = ".";

		private final String FTPImage;

		private FTPImage(String FTPImage) {
			this.FTPImage = FTPImage;
		}

		public String getFTPImage() {
			return FTPImage;
		}
	}

	public enum AttachmentType {
		FTP(1), LINK(2), FILETABLE(3);

		private final int type;

		private AttachmentType(int type) {
			this.type = type;
		}

		public int gettype() {
			return type;
		}
	}

	public enum FTPReplycodes {

		REQUIRED_PASSWORD(331), SERVER_RESPONSE_OK(200), SERVER_USERLOGGED_RESPONSE(230), NEED_VALID_CREDENTIALS(332),
		CANT_OPEN_CONNECTION(425), SERVER_TIMEOUT(10060), SWITCH_CONNECTION_PORT(10061), TOO_MANY_USER_ACTIVE(10068),
		NOT_LOGGED_IN(530), INVALID_CREDENTIALS(430), HOST_UNAVAILABLE(434);

		private final int ReplyCode;

		private FTPReplycodes(int ReplyCode) {
			this.ReplyCode = ReplyCode;
		}

		public int getReplyCode() {
			return ReplyCode;
		}

	}

	public enum FTP_DIR {
		REPORT("JRXMLREPORT");

		private final String ftp_directory;

		public String getFtp_directory() {
			return ftp_directory;
		}

		private FTP_DIR(String ftp_directory) {
			this.ftp_directory = ftp_directory;
		}

	}

	public enum QueryType 
	{
		DASHBOARD(1), ALERTS(2), BARCODE(3), FILTER(5), OTHERS(-1);

		private final int Querytype;

		private QueryType(int qtype) {
			this.Querytype = qtype;
		}

		public int getQuerytype() {
			return Querytype;
		}

	}

	public enum Precision // Added for Precision
	{
		ONE("11,1"), TWO("11,2"), THREE("11,3"), FOUR("11,4"), FIVE("12,5"), SIX("12,6"), SEVEN("18,7"), EIGHT("18,8");

		private final String Precision;

		private Precision(String qtype) {
			this.Precision = qtype;
		}

		public String getPrecision() {
			return Precision;
		}

	}

	public enum TransAction {
		RECIEVEDBY("Sample Recieved In "), ALLIQUOTEDTO("Sample Alliquoted For "), REGISTERED("Sample Registered "),
		TESTINITIATED("Sample Used For "), RESERVEDFOR("Reserved For Store"), SENDTOSRORE("Sent To Store");

		private final String TransAction;

		public String getTransAction() {
			return TransAction;
		}

		private TransAction(String TransAction) {
			this.TransAction = TransAction;
		}

	}

	public enum ReportQueryFormat 			
	{
		PLSQL(2), SQL(1);

		private final int reportQueryFormat;

		private ReportQueryFormat(int reportQueryFormat) {
			this.reportQueryFormat = reportQueryFormat;
		}

		public int getReportQueryFormat() {
			return reportQueryFormat;
		}

	}

	public static String RTNSUCCESS = "IDS_SUCCESS";
	public static String INSERT = "insert";
	public static String UPDATE = "update";
	public static String DELETE = "delete";
	public static String REPORTVIEW = "reportview\\";

	public enum FormCode {
		MATERIALCATEGORY(23), PRODUCTCATEGORY(24), INSTRUMENTCATEGORY(27), TESTGROUP(62), RESULTENTRY(56), APPROVAL(61),
		MYJOB(107), JOBALLOCATION(110), REPORTCONFIG(77), BATCHCREATION(67), USERS(3), BARCODE(108), TESTMASTER(41),
		CLOCKMONITORING(94),RELEASE(142);

		private final int formCode;

		private FormCode(int formCode) {
			this.formCode = formCode;
		}

		public int getFormCode() {
			return formCode;
		}
	}

	public enum ModuleCode {
		USERMANAGEMENT(3),
		BATCH(16);

		private final int moduleCode;

		private ModuleCode(int moduleCode) {
			this.moduleCode = moduleCode;
		}

		public int getModuleCode() {
			return moduleCode;
		}
	}

	public enum UserRoleLevel {
		LEVEL1(1);

		private final int level;

		private UserRoleLevel(int level) {
			this.level = level;
		}

		public int getUserRoleLevel() {
			return level;
		}

	}

	public enum ReportType {
		COA(1), MIS(2), BATCH(3), SAMPLE(4), CONTROLBASED(5);

		private final int reporttype;

		public int getReporttype() {
			return reporttype;
		}

		private ReportType(int reporttype) {
			this.reporttype = reporttype;
		}

	}

	public enum COAReportType {
		SAMPLEWISE(1), TESTWISE(2), CLIENTWISE(3), SAMPLECERTIFICATE(7), SAMPLECERTIFICATEPRIVIEW(4), BATCH(5),
		BATCHPREVIEW(6), BATCHSTUDY(8);

		private final int coaReportType;

		public int getcoaReportType() {
			return coaReportType;
		}

		private COAReportType(int coaReportType) {
			this.coaReportType = coaReportType;
		}

	}

	public enum QualisMenu {
		MASTER(1), TRANSACTION(2), REPORTS(3);

		private final int qualismenu;

		public int getQualismenu() {
			return qualismenu;
		}

		private QualisMenu(int qualismenu) {
			this.qualismenu = qualismenu;
		}

	}

	public enum ChartType {
		GRID(-2), AREA(1), BAR(2), BUBBLE(3), COLUMN(5), DONUT(6), PIE(8);

		private final int chartType;

		private ChartType(int chartType) {
			this.chartType = chartType;
		}

		public int getChartType() {
			return chartType;
		}
	}

	public enum DynamicRecordDetailType {
		SAMPLE(1), SUBSAMPLE(2), TEST(3);

		private final int recorddetailtype;

		public int getRecorddetailtype() {
			return recorddetailtype;
		}

		private DynamicRecordDetailType(int recorddetailtype) {
			this.recorddetailtype = recorddetailtype;
		}

	}

	public enum InterFaceType {

		LOGILAB(1), INTERFACER(2);

		private final int interFaceType;

		public int getInterFaceType() {
			return interFaceType;
		}

		private InterFaceType(int interFaceType) {
			this.interFaceType = interFaceType;
		}
	}


	public enum ReportSettings {

		REPORT_PATH(1), REPORT_PDF_PATH(4), REPORT_DOWNLOAD_URL(5),SMTLP(12);

		private final int nreportsettingcode;

		public int getNreportsettingcode() {
			return nreportsettingcode;
		}

		private ReportSettings(int nreportsettingcode) {
			this.nreportsettingcode = nreportsettingcode;
		}

	}

	
	public enum CertificateReportType {
		NA(-1), PASS(1), FAIL(2), WITHDRAWNBLOOD(3), WITHDRAWNVACCINE(4), NULLIFICATION(5), PLASMAPOOL_PASS(6),
		PLASMAPOOL_FAIL(7), PLASMAPOOL_WITHDRAWN(8);

		private final int ncertificatereporttype;

		public int getNCertificateReportType() {
			return ncertificatereporttype;
		}

		private CertificateReportType(int ncertificatereporttype) {
			this.ncertificatereporttype = ncertificatereporttype;
		}

	}
	
	public enum Condition {
		EQUALS(1),
	    NOTEQUALS(2),
	    CONTAINS(3),
	    NOTCONTAINS(4),
	    STARTSWITH(5),
	    ENDSWITH(6),
	    INCLUDES(7),
	    LESSTHAN(8),
	    LESSTHANOREQUALS(9),
	    GREATERTHAN(10),
	    GREATERTHANEQUALS(11);
	    private final int condition;

		public int getCondition() {
			return condition;
		}
		private Condition(int condition) {
			this.condition = condition;
		}
	    
	}
	
	public enum TemplateType {

		PRODUCT(1), MATERIAL(3), INSTRUMENT(2), MASTERS(4),SUBSAMPLE(-1);
		private TemplateType(int type) {
			this.type = type;
		}

		private final int type;

		public int getType() {
			return type;
		}

	}
	
	
	public enum DesignProperties {
		 LABEL(1), VALUE(2), LISTITEM(3), SINGLEITEMDATA(4),
		 GRIDITEM(5), GRIDEXPANDABLEITEM(6), LISTMOREITEM(7), LISTMAINFIELD(8),
		 COLOUR(9);

		private final int type;

		private DesignProperties(int type) {
			this.type = type;
		}

		public int gettype() {
			return type;
		}

	}
	

	public enum ApprovalStatusFunction {
		SAMPLEREGISTRATIONFILTER(9);

		private final int nstatustype;

		private ApprovalStatusFunction(int nstatustype) {
			this.nstatustype = nstatustype;
		}

		public int getNstatustype() {
			return nstatustype;
		}
	}
}
