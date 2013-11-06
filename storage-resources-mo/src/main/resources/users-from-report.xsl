<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

     <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/StorageReport">
	<users>
		<xsl:apply-templates select="ReportSummary/SizeByOwner/GroupInfo"/>
	</users>
    </xsl:template>

    <xsl:template match="GroupInfo">
	<user>
	    <name>
	        <xsl:value-of select="@Name"/>
	    </name>
	</user>
    </xsl:template>
 
</xsl:stylesheet>
