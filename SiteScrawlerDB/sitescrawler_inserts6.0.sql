INSERT INTO Filtermanager (maxfiltergruppe) VALUES (10);
INSERT INTO Filtermanager (maxfiltergruppe) VALUES (5);
INSERT INTO Filtermanager (maxfiltergruppe) VALUES (2);
INSERT INTO Filtermanager (maxfiltergruppe) VALUES (20);
INSERT INTO Filtermanager (maxfiltergruppe) VALUES (1);
INSERT INTO Filtermanager (maxfiltergruppe) VALUES (1);
INSERT INTO Filtermanager (maxfiltergruppe) VALUES (1);
INSERT INTO Filtermanager (maxfiltergruppe) VALUES (1);
INSERT INTO Filtermanager (maxfiltergruppe) VALUES (1);
INSERT INTO Filtermanager (maxfiltergruppe) VALUES (1);
INSERT INTO Filtermanager (maxfiltergruppe) VALUES (1);
COMMIT;
SELECT * FROM Filtermanager;
INSERT INTO Nutzer (identifikation, vorname, nachname, email, passwort, empfangehtmlmails) VALUES(10, 'Tooooobi', 'Burner', 'k.l.mueller@outlook.com','qwe',1);
INSERT INTO Nutzer (identifikation, vorname, nachname, email, passwort, empfangehtmlmails) VALUES(5, 'Haaans', 'Ricardi', 'elodril@gmx.de','qwe',1);
INSERT INTO Nutzer (identifikation, vorname, nachname, email, passwort, empfangehtmlmails) VALUES(6, 'Yvonne', 'Laberschtille', 'sitescrawler@gmx.de','qwe',1);
INSERT INTO Nutzer (identifikation, vorname, nachname, email, passwort, empfangehtmlmails) VALUES(7, 'Marcel', 'Vollbart', 'sitescrawler1@spoofmail.de','qwe',1);
INSERT INTO Nutzer (identifikation, vorname, nachname, email, passwort, empfangehtmlmails) VALUES(8, 'Konni', 'Müller', 'k.mueller96@gmail.com','qwe',1);
INSERT INTO Nutzer (identifikation, vorname, nachname, email, passwort, empfangehtmlmails) VALUES(9, 'Robien', 'Schmitt', 'sitescrawler2@spoofmail.de','qwe',1);
INSERT INTO Nutzer (identifikation, vorname, nachname, email, passwort, empfangehtmlmails) VALUES(11, 'Toni', 'van den Hof', 'sitescrawler3@spoofmail.de','qwe',1);
SELECT * FROM Nutzer;
COMMIT;

INSERT INTO Firma VALUES(2, 'Bizerba');
INSERT INTO Firma VALUES(3, 'DATEV');
INSERT INTO Firma VALUES(1, 'aformatik');
INSERT INTO Firma VALUES(4, 'Spirit21');
COMMIT;

INSERT INTO Mitarbeiter VALUES(10,3,'Administrator');
INSERT INTO Mitarbeiter VALUES(5,3,'Administrator');
INSERT INTO Mitarbeiter VALUES(5,2,'Mitarbeiter');
INSERT INTO Mitarbeiter VALUES(6,3,'Administrator');
INSERT INTO Mitarbeiter VALUES(7,1,'Administrator');
INSERT INTO Mitarbeiter VALUES(8,4,'Administrator');

COMMIT;

INSERT INTO Intervall (intervall) VALUES('MONATLICH');
INSERT INTO Intervall (intervall) VALUES('WOECHENTLICH');
INSERT INTO Intervall (intervall) VALUES('TAEGLICH');
INSERT INTO Intervall (intervall)  VALUES('ZWEIWOECHENTLICH');

COMMIT;

INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('Tobis Filterprofilgruppe',10,'2017-05-01 00:00:00', 'TAEGLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('Haaans Filterprofilgruppe',5,'2017-05-01 00:00:00', 'TAEGLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('Yvonnes Filterprofilgruppe',6,'2017-05-01 00:00:00', 'TAEGLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('Bizerbas Filterprofilgruppe1',2,'2017-05-01 00:00:00', 'ZWEIWOECHENTLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('Bizerbas Filterprofilgruppe2',2,'2017-05-01 00:00:00', 'MONATLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('DATEVs Filterprofilgruppe1',3,'2017-05-01 00:00:00', 'WOECHENTLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('aformatiks Filterprofilgruppe',1,'2017-05-01 00:00:00', 'WOECHENTLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('Spirit21s Filterprofilgruppe',4,'2017-05-01 00:00:00', 'TAEGLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('Robiens Filterprofilgruppe',9,'2017-05-01 00:00:00', 'TAEGLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('Marcels Filterprofilgruppe',7,'2017-05-01 00:00:00', 'TAEGLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('Konnis Filterprofilgruppe',8,'2017-05-01 00:00:00', 'TAEGLICH',1);
INSERT INTO Filterprofilgruppe (titel,Filtermanager_identifikation, letzteerstellung, Intervall_intervall, verschickeemail) VALUES('AvHs Filterprofilgruppe',11,'2017-05-01 00:00:00', 'TAEGLICH',1);

SELECT * FROM Filterprofilgruppe;
COMMIT;


INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(10,1);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(10,6);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(5,2);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(5,3);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(5,4);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(5,6);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(6,3);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(6,4);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(6,6);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(7,2);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(7,7);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(8,8);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(9,1);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(9,9);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(10,10);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(11,11);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(11,12);





INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(11,1);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(11,2);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(11,3);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(11,4);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(11,5);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(11,6);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(11,7);
INSERT INTO empfaenger (Nutzer_Filtermanager_identifikation, Filtergruppe_FilterprofilgruppeId) VALUES(11,8);
SELECT * FROM empfaenger;
COMMIT;

INSERT INTO Filterprofil (Filtermanager_identifikation,filterprofilname,tagstring) VALUES(10,'Tobi1','beschreibung: Deutschland');
INSERT INTO Filterprofil (Filtermanager_identifikation,filterprofilname,tagstring) VALUES(10,'Tobi2','beschreibung: Frankreich');
INSERT INTO Filterprofil (Filtermanager_identifikation,filterprofilname,tagstring) VALUES(5,'Hans1','beschreibung: USA');
INSERT INTO Filterprofil (Filtermanager_identifikation,filterprofilname,tagstring) VALUES(2,'Bizerba1','beschreibung: Deutschland');
INSERT INTO Filterprofil (Filtermanager_identifikation,filterprofilname,tagstring) VALUES(2,'Bizerba2','beschreibung: Werbung');
INSERT INTO Filterprofil (Filtermanager_identifikation,filterprofilname,tagstring) VALUES(3,'DATEV1','beschreibung: Deutschland');
INSERT INTO Filterprofil (Filtermanager_identifikation,filterprofilname,tagstring) VALUES(1,'aformatik1','beschreibung: Deutschland');
INSERT INTO Filterprofil (Filtermanager_identifikation,filterprofilname,tagstring) VALUES(4,'spirit211','beschreibung: Tennis');
SELECT * FROM Filterprofil;
COMMIT;



-- Tobis Filterprofile
INSERT INTO filterprofilgruppe_beinhaltet_filterprofil (Filterprofil_FilterprofilId, Filterprofilgruppe_FilterprofilgruppeId) VALUES(1,1);
INSERT INTO filterprofilgruppe_beinhaltet_filterprofil (Filterprofil_FilterprofilId, Filterprofilgruppe_FilterprofilgruppeId) VALUES(2,1);

-- Yvonne und Haaans interessieren sich für die USA
INSERT INTO filterprofilgruppe_beinhaltet_filterprofil (Filterprofil_FilterprofilId, Filterprofilgruppe_FilterprofilgruppeId) VALUES(3,2);
INSERT INTO filterprofilgruppe_beinhaltet_filterprofil (Filterprofil_FilterprofilId, Filterprofilgruppe_FilterprofilgruppeId) VALUES(3,3);

-- Firmenprofile
INSERT INTO filterprofilgruppe_beinhaltet_filterprofil (Filterprofil_FilterprofilId, Filterprofilgruppe_FilterprofilgruppeId) VALUES(4,4);
INSERT INTO filterprofilgruppe_beinhaltet_filterprofil (Filterprofil_FilterprofilId, Filterprofilgruppe_FilterprofilgruppeId) VALUES(5,5);
INSERT INTO filterprofilgruppe_beinhaltet_filterprofil (Filterprofil_FilterprofilId, Filterprofilgruppe_FilterprofilgruppeId) VALUES(6,6);
INSERT INTO filterprofilgruppe_beinhaltet_filterprofil (Filterprofil_FilterprofilId, Filterprofilgruppe_FilterprofilgruppeId) VALUES(7,7);
INSERT INTO filterprofilgruppe_beinhaltet_filterprofil (Filterprofil_FilterprofilId, Filterprofilgruppe_FilterprofilgruppeId) VALUES(8,8);
SELECT * FROM filterprofilgruppe_beinhaltet_filterprofil;
COMMIT;


INSERT INTO Rolle VALUES('Registrered');
INSERT INTO Rolle VALUES('Secure');
INSERT INTO Rolle VALUES('Admin');

COMMIT;

INSERT INTO Nutzer_hat_Rolle VALUES(10, 'Registrered');
INSERT INTO Nutzer_hat_Rolle VALUES(10, 'Secure');
INSERT INTO Nutzer_hat_Rolle VALUES(10, 'Admin');

INSERT INTO Nutzer_hat_Rolle VALUES(5, 'Registrered');
INSERT INTO Nutzer_hat_Rolle VALUES(5, 'Secure');
INSERT INTO Nutzer_hat_Rolle VALUES(5, 'Admin');

INSERT INTO Nutzer_hat_Rolle VALUES(6, 'Registrered');
INSERT INTO Nutzer_hat_Rolle VALUES(6, 'Secure');
INSERT INTO Nutzer_hat_Rolle VALUES(6, 'Admin');

INSERT INTO Nutzer_hat_Rolle VALUES(7, 'Registrered');

INSERT INTO Nutzer_hat_Rolle VALUES(7, 'Admin');

INSERT INTO Nutzer_hat_Rolle VALUES(8, 'Registrered');
INSERT INTO Nutzer_hat_Rolle VALUES(8, 'Secure');


INSERT INTO Nutzer_hat_Rolle VALUES(9, 'Registrered');
INSERT INTO Nutzer_hat_Rolle VALUES(9, 'Secure');
INSERT INTO Nutzer_hat_Rolle VALUES(9, 'Admin');

INSERT INTO Nutzer_hat_Rolle VALUES(11, 'Registrered');

COMMIT;

INSERT INTO Quelle (name,rsslink) VALUES('Spiegel', 'http://www.spiegel.de/schlagzeilen/tops/index.rss');

INSERT INTO Filterprofil_beinhaltet_Quelle VALUES(1,1);
INSERT INTO Filterprofil_beinhaltet_Quelle VALUES(2,1);
INSERT INTO Filterprofil_beinhaltet_Quelle VALUES(3,1);
INSERT INTO Filterprofil_beinhaltet_Quelle VALUES(4,1);
INSERT INTO Filterprofil_beinhaltet_Quelle VALUES(5,1);
INSERT INTO Filterprofil_beinhaltet_Quelle VALUES(6,1);
INSERT INTO Filterprofil_beinhaltet_Quelle VALUES(7,1);
INSERT INTO Filterprofil_beinhaltet_Quelle VALUES(8,1);
