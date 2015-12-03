<?xml version="1.0" encoding="ISO-8859-1"?> 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1">
<xsl:output indent="yes" encoding="ISO-8859-1"/>
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<xsl:template match="/">
	<html><head></head>
		<body style="font-family:sans-serif">
 <xsl:apply-templates></xsl:apply-templates>
 </body></html>
</xsl:template>

<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<xsl:template match="regle">
	<dl>
 <xsl:apply-templates></xsl:apply-templates>
 </dl>
</xsl:template>
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<xsl:template match="gauche">
	<dt>
 <xsl:apply-templates></xsl:apply-templates> 	<span style="color:#990066;">&#8594; </span>
</dt>
</xsl:template>
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<xsl:template match="pd[1]">
  <dd><xsl:apply-templates></xsl:apply-templates></dd>
</xsl:template>
<xsl:template match="pd">
  <dd><span style="color:#990066;">|</span> <xsl:apply-templates></xsl:apply-templates></dd>
</xsl:template>
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<xsl:template match="selection">
	<span style="color=#99CC33;">
{<span style="font-size=x-small;"><xsl:value-of select="."/></span>}
    </span>
</xsl:template>
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<xsl:template match="corps">
<xsl:apply-templates></xsl:apply-templates>
</xsl:template>
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<xsl:template match="text()">
	<xsl:call-template name="traduire"><xsl:with-param name="texte"><xsl:value-of select="."></xsl:value-of></xsl:with-param></xsl:call-template>
</xsl:template>
<!-- ++++++++++++++++++++++++++++++++++++++++ -->
<xsl:template name="traduire">
	<xsl:param name="texte"/>
    <xsl:variable name="debut">
	  <xsl:choose>	
		<xsl:when test="substring-before($texte ,' ')">
		    <xsl:value-of select="substring-before($texte ,' ')"/>
		</xsl:when>
		<xsl:otherwise >
			<xsl:choose>	
				<xsl:when test="substring-after($texte ,' ')">
					<xsl:value-of select="substring-after($texte ,' ')"/>
				</xsl:when>
				<xsl:otherwise ><xsl:value-of select="$texte"/></xsl:otherwise>
	  </xsl:choose>	
		</xsl:otherwise>
	  </xsl:choose>
	</xsl:variable>
	<xsl:variable name="fin">
	  <xsl:choose>	
		<xsl:when test="substring-before($texte ,' ')">
		    <xsl:value-of select="substring-after($texte ,' ')"/>
		</xsl:when>
		<xsl:otherwise ></xsl:otherwise>
	  </xsl:choose>	
	</xsl:variable>
	<xsl:variable name="type">
		<xsl:value-of select="translate(substring($debut,1,1), '$ABCDEFGHIJKLMNOPQRSTWXYZ','$AAAAAAAAAAAAAAAAAAAAAAAAAA')"></xsl:value-of>
	</xsl:variable>	
	<!--
	TX<xsl:value-of select="$texte"></xsl:value-of>XT
	DE<xsl:value-of select="$debut"></xsl:value-of>ED
	FI<xsl:value-of select="$fin"></xsl:value-of>IF
	TY<xsl:value-of select="$type"></xsl:value-of>YT
	-->
	<xsl:choose>
		<xsl:when test="$debut!=''">
		    <xsl:choose>
				<xsl:when test="$type='A'">&lt;<xsl:value-of select="$debut"/>&gt; </xsl:when>
				<xsl:when test="$type='$'"><span style="color:#FF9933;font-size=x-small;"><xsl:value-of select="$debut"/><xsl:text> </xsl:text></span></xsl:when>
				<xsl:when test="$debut='vide'"><span style="color:#3399FF;font-size=x-small;"><xsl:value-of select="$debut"/><xsl:text> </xsl:text></span></xsl:when>
				<xsl:otherwise ><span style="font-weight:bold;"><xsl:value-of select="$debut"/><xsl:text> </xsl:text></span></xsl:otherwise>
	        </xsl:choose>
			<xsl:call-template name="traduire"><xsl:with-param name="texte"><xsl:value-of select="$fin"></xsl:value-of></xsl:with-param></xsl:call-template>
		</xsl:when>
		<xsl:otherwise ></xsl:otherwise>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
