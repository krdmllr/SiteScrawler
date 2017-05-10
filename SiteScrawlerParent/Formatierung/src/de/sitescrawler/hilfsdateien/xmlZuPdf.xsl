<?xml version="1.0" encoding="UTF-8"?>
<!-- Licensed to the Apache Software Foundation (ASF) under one or more contributor 
	license agreements. See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership. The ASF licenses this file to 
	You under the Apache License, Version 2.0 (the "License"); you may not use 
	this file except in compliance with the License. You may obtain a copy of 
	the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required 
	by applicable law or agreed to in writing, software distributed under the 
	License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS 
	OF ANY KIND, either express or implied. See the License for the specific 
	language governing permissions and limitations under the License. -->
<!-- $Id$ -->
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">
	<xsl:output method="xml" version="1.0" omit-xml-declaration="no"
		indent="yes" />
	<xsl:param name="logoSiteScrawler" />
	<xsl:param name="titelPDF" />
	<xsl:param name="aktuellesDatum" />
	<xsl:param name="titel" select="'Titel'" />
	<xsl:param name="autor" select="'Autor'" />
	<xsl:param name="datum" select="'Datum'" />
	<xsl:param name="beschreibung" select="'Beschreibung'" />
	<xsl:param name="link" select="'Link'" />
	<xsl:param name="footerText" />
	<xsl:param name="linkZuSiteScrawler" />


	<!-- =========================== -->
	<!-- root element: Archiveintrag -->
	<!-- =========================== -->
	<xsl:template match="archiveintrag">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="29.7cm" page-width="21cm" margin-top="2cm"
					margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simpleA4">
				<fo:flow flow-name="xsl-region-body">

					<!-- Block für Header -->
					<fo:block padding="15px" background-color="#384f94" color="#ddd" padding-bottom="10px"
						margin-bottom="2px">
						<fo:table table-layout="fixed" width="100%" text-align="center">
							<fo:table-column column-width="30%" border-style="none" />
							<fo:table-column column-width="70%" border-style="none" />
							<fo:table-header font-weight="bold">
								<fo:table-row>
									<fo:table-cell>
										<fo:block></fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block></fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-body>
								<fo:table-row border-style="none">
									<fo:table-cell>
										<fo:block float="right">
											<fo:external-graphic src="{$logoSiteScrawler}"
												content-width="4cm" content-height="4cm" />
										</fo:block>
									</fo:table-cell>

									<fo:table-cell padding="2pt">
										<fo:block font-size="22pt" text-align="center"
											padding-bottom="10pt" font-weight="bold">
											<xsl:value-of select="$titelPDF" />
										</fo:block>
										<fo:block font-size="16pt" text-align="center"
											padding-bottom="10pt">
											<xsl:value-of select="$aktuellesDatum" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>

					<!-- Block für Daten -->
					<fo:block>
						<xsl:apply-templates select="artikel" />
					</fo:block>

					<!-- Block für Footer -->
					<fo:block padding="15px" background-color="#384f94" color="#ddd"
						text-align="right" margin-top="2px">
						<fo:block font-size="8pt">
							<xsl:value-of select="$footerText" />
						</fo:block>
						<fo:block font-size="8pt">
							<fo:inline>
								<xsl:text>Zu SiteScrawler: </xsl:text>
								<xsl:value-of select="$linkZuSiteScrawler"></xsl:value-of>
							</fo:inline>
						</fo:block>
					</fo:block>


				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!-- Template für die Artikel -->
	<xsl:template match="artikel">
		<fo:block border="2px solid #449d44" padding="13px"
			text-align="left" margin-top="2px" margin-bottom="2px">
			<fo:block font-size="16pt" font-weight="bold" padding-bottom="3px">
				<xsl:value-of select="titel" />
			</fo:block>
			<fo:block font-size="8pt" padding-bottom="3px">
				<xsl:text>Von </xsl:text>
				<xsl:value-of select="autor" />
				<xsl:text> am </xsl:text>
				<xsl:value-of select="datum" />
				<xsl:text> veröffentlicht. </xsl:text>
			</fo:block>
			<fo:block font-size="12pt" padding-bottom="3px">
				<xsl:value-of select="beschreibung" />
			</fo:block>
			<fo:block font-size="8pt">
				<fo:inline>
					<xsl:text>Zum ganzen Artikel: </xsl:text>
					<fo:block color="#384f94">
						<xsl:value-of select="link"></xsl:value-of>
					</fo:block>
				</fo:inline>
			</fo:block>
		</fo:block>
	</xsl:template>

</xsl:stylesheet>