<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:param name="UserName" select="vvs"/>

    <xsl:template match="/StorageReport">
	<report type="DuplicateFile">
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
        <dublicate-groups>
	       <xsl:apply-templates select="ReportData/DuplicateGroup"/>
	</dublicate-groups>
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
    
    <xsl:template match="DuplicateGroup">
	<xsl:if test="count(DuplicateFile/Owner[contains(.,$UserName)])>0">
	    <group>
	        <xsl:attribute name="id">
	            <xsl:value-of select="@ID"/>
		</xsl:attribute>
		<name>
		    <xsl:value-of select="DuplicateFile[1]/Name"/>
		</name>
		<count>
		    <xsl:value-of select="FileCount"/>
		</count>
		<file-size>
		    <xsl:value-of select="FileSize"/>
		</file-size>
		<files>
		    <xsl:apply-templates select="DuplicateFile"/>
		</files>
	    </group>
	</xsl:if>
    </xsl:template>
    
        
    <xsl:template match="DuplicateFile">
	<file>
	    <last-access><xsl:value-of select="LastAccessTime"/></last-access>
	    <owner><xsl:value-of select="Owner"/></owner>
	    <path><xsl:value-of select="RemotePath/Path"/></path>
	</file>
    </xsl:template>
 
</xsl:stylesheet>
