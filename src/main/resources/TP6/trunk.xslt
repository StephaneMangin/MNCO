<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" version="1.0" encoding="iso-8859-1" indent="yes" cdata-section-elements="programlisting" omit-xml-declaration="yes"/>
  <xsl:template match="*" mode="trunk">
    <xsl:param name="deepth" select="0"/>
    <xsl:if test="$maxProf = -1 or $deepth &lt; $maxProf">
      <!-- <xsl:message>
        <xsl:value-of select="$maxProf"/>
      </xsl:message> -->
      <xsl:element name="{name()}">
        <xsl:for-each select="@*">
          <xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
        </xsl:for-each>
        <xsl:apply-templates mode="trunk">
          <xsl:with-param name="deepth" select="$deepth+1"/>
        </xsl:apply-templates>
      </xsl:element>
    </xsl:if>
  </xsl:template>
  <xsl:template match="text()" mode="trunk">
    <xsl:value-of select="normalize-space(.)"/>
  </xsl:template>
</xsl:stylesheet>
