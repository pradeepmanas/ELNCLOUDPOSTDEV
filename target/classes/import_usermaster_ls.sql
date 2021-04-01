insert into LSSitemaster(sitename,siteaddress,contactperson,phoneno,faxno,email,istatus)values('Default',null,null,null,null,null,1)
--insert into LSSitemaster(sitecode, sitename,siteaddress,contactperson,phoneno,faxno,email,istatus)values(2,'Banglore',null,null,null,null,null,1)

insert into LSusergroup(createdby, createdon, modifiedby, modifiedon, usergroupname, usergroupstatus, lssitemaster_sitecode)values('administrator', NOW(),'administrator', NOW(),'Administrator', 'A',1)

INSERT into lsusermaster(userfullname, username, password, lastloggedon, passwordexpirydate, userstatus, lockcount, createddate, modifieddate, createdby, modifiedby, labsheet, emailid, profileimage, profileimagename, verificationcode, lssitemaster_sitecode, lsusergroup_usergroupcode,loginfrom,userretirestatus) VALUES (N'Administrator', N'Administrator', 'gyX57EHs08KuakyVhFVvGQ==', CAST(N'2020-03-18T18:57:44.127' AS date), NULL, N'A         ', 0, CAST(N'2020-01-21T18:35:23.080' AS date), NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, 1, 1,'0',0)

insert into LSdomainmaster(domainname,categories,domainstatus,createdby,createdon,modifiedby,modifiedon,lssitemaster_sitecode)values('ELN','DB',1,'U1',null,null,null,1)
--insert into LSdomainmaster(domaincode, domainname,categories,domainstatus,createdby,createdon,modifiedby,modifiedon,lssitemaster_sitecode)values(2,'ELN New','DB',1,'U1',null,null,null,2)

insert into LSworkflow (workflowname)values('New')
insert into LSsheetworkflow (workflowname)values('New')

insert into LSfile(createby_usercode, createdate,extension,filecontent,filenameuser, filenameuuid,isactive,versionno,lssitemaster_sitecode,modifiedby_usercode,lssheetworkflow_workflowcode,modifieddate,approved, rejected)values(1,NOW(),'.txt',null,null,null,1,0,1,1,1,NOW(),1,0)

insert into LSCFRReasons(comments,lssitemaster_sitecode) values ('Reviewed',1)
insert into LSCFRReasons(comments,lssitemaster_sitecode) values ('Declained',1)

insert into LSdocdirectory (docdirectorycode, directoryname, directorytype, parentdirectory, status, createdate, createdby, lssitemaster_sitecode) VALUES (1, 'root', 0, null, 1, NULL, 1, 1)
insert into LSdocdirectory (docdirectorycode, directoryname, directorytype, parentdirectory, status, createdate, createdby, lssitemaster_sitecode) VALUES (2, 'Generated Reports', 1, 1, 1, NULL, 1, 1)
insert into LSdocdirectory (docdirectorycode, directoryname, directorytype, parentdirectory, status, createdate, createdby, lssitemaster_sitecode) VALUES (3, 'Draft Reports', 1, 1, 1, NULL, 1, 1)
insert into LSdocdirectory (docdirectorycode, directoryname, directorytype, parentdirectory, status, createdate, createdby, lssitemaster_sitecode) VALUES (4, 'My Space', 1, 1, 1, NULL, 1, 1)
insert into LSdocdirectory (docdirectorycode, directoryname, directorytype, parentdirectory, status, createdate, createdby, lssitemaster_sitecode) VALUES (5, 'Team Space', 1, 1, 1, NULL, 1, 1) 

insert into LSpasswordpolicy (policycode, complexpasswrd, dbbased, lockpolicy, maxpasswrdlength, mincapitalchar, minnumericchar, minpasswrdlength,minsmallchar,minspecialchar,passwordexpiry,passwordhistory,lssitemaster_sitecode) VALUES (1,0,null,5, 10,0,0, 4,0,0, 90, 5,1)
