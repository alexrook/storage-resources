<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:param name="UserName" select="vvs"/>

    <xsl:template match="/StorageReport">
	<report type="ByTypeFiles">
	<header>
	    <title>
		<xsl:value-of select="ReportHeader/@ReportTitle"/>
	    </title>
	    <generatedAt>
		<xsl:value-of select="ReportHeader/@GeneratedAt"/>
	    </generatedAt>
	    <generatedFor>
		<xsl:value-of select="$UserName"/>
	    </generatedFor>
	    <filters>
		<xsl:apply-templates select="ReportHeader/ReportFilters/Filter"/>
	    </filters>
	    <warnings>
		<xsl:apply-templates select="ReportHeader/ReportWarnings/Warning"/>
	    </warnings>
	</header>
        <groups>
	       <xsl:apply-templates select="ReportGroupData"/>
	</groups>
	</report>
    </xsl:template>

    <xsl:template match="Filter">
	<filter>
	    <name><xsl:value-of select="@Name"/></name>
	    <value><xsl:value-of select="@Value"/></value>
	    <value-ex><xsl:value-of select="@ValueEx"/></value-ex>
	</filter>
    </xsl:template>
    
    <xsl:template match="Warning">
	<warning>
	    <xsl:value-of select="."/>
	</warning>
    </xsl:template>
    
    
    <xsl:template match="ReportGroupData">
	<group>
	    <name><xsl:value-of select="@Name"/></name>
	    <files>
		<xsl:apply-templates select="Item"/>
	    </files>
	</group>
    </xsl:template>
    
    <xsl:template match="Item">
        <xsl:if test="contains(OwnerUsername,$UserName)">
		<file>
		    <name>
			<xsl:value-of select="Name"/>
		    </name>
		    <last-access><xsl:value-of select="LastAccessTimeString"/></last-access>
		    <owner>
			<xsl:value-of select="OwnerUsername"/>
		    </owner>
		    <path>
			<xsl:value-of select="RemotePath/Path"/>
		    </path>
		</file>
        </xsl:if>
    </xsl:template>
 
</xsl:stylesheet>
