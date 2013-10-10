<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" >

<xsl:template match="/information">
	This is file version <xsl:value-of select="@version" />.
	
	<xsl:for-each select="person">
		[<xsl:value-of select="@id" />] <xsl:value-of select="name" />.
	</xsl:for-each>
	
	FILES: 
	<xsl:for-each select="doc('listing:data')//file">
	*<xsl:text> </xsl:text><xsl:if test="is_dir = 'true'">[DIR]<xsl:text> </xsl:text></xsl:if><xsl:value-of select="name" />
	</xsl:for-each>

</xsl:template>

</xsl:stylesheet>

