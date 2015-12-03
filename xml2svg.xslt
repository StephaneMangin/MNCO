<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:fs="http://www.ifsic.fr/xmlizer/fileSystem" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:exsl="http://exslt.org/common"
                extension-element-prefixes="exsl"
                exclude-result-prefixes="exsl fs"
>
  <!--	<xsl:output method="xml" version="1.0" encoding="ISO-8859-1" indent="yes" 
		omit-xml-declaration="no" 
		doctype-system="http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd" 
		doctype-public="-//W3C//DTD SVG 1.0//EN"/> -->
  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>
  <xsl:include href="src/main/resources/TP6/examples/string-width.xslt"/>
  <xsl:include href="src/main/resources/TP6/examples/trunk.xslt"/>
  <xsl:param name="maxProf" select="-1"/>
  <xsl:param name="text.style" select="'stroke:none;'"/>
  <xsl:param name="graph.style" select='concat("fill-rule:nonzero;clip-rule:nonzero;",
      "stroke-miterlimit:4; font-family:ArialMT;font-size:12;")'/>
  <xsl:param name="box.color" select="'#FFFFD0'"/>
  <xsl:param name="text.color" select="'black'"/>
  <xsl:param name="handle.color" select="'black'"/>
  <xsl:param name="handle.path.style" select="'stroke-width:1;stroke-miterlimit:3;fill:none;'"/>
  <xsl:param name="stripping.string.length" select="80"/>
  <xsl:param name="handles" select="'true'"/>
  <xsl:param name="root.handle" select="'false'"/>
  <xsl:param name="boxes" select="'true'"/>
  <xsl:param name="text" select="'true'"/>
  <xsl:param name="letterHeight" select="8"/>
  <xsl:param name="letterWidth" select="8"/>
  <xsl:param name="x0" select="20"/>
  <xsl:param name="y0" select="20"/>
  <xsl:param name="rowHeight" select="20"/>
  <xsl:param name="columnWidth" select="20"/>
  <xsl:param name="boxHeight" select="16"/>
  <!--
	<xsl:param name="boxLengh" select="20"/>-->
  <xsl:template match="text()">
    <xsl:param name="tab" select="0"/>
    <xsl:param name="premiere.ligne" select="0"/>
    <!--  <xsl:message>texte() : tab = <xsl:value-of select="$tab"/>ligne = <xsl:value-of select="$premiere.ligne"/></xsl:message> -->
    <xsl:variable name="texte.cousins.precedants">
      <xsl:choose>
        <xsl:when test="$text='true'">
          <xsl:value-of select="count(preceding-sibling::text() | 
                 preceding-sibling::*/descendant::text())"/>
        </xsl:when>
        <xsl:otherwise>0</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="cousins.precedants" select="count(preceding-sibling::* | 
                 preceding-sibling::*/descendant::*)+$texte.cousins.precedants"/>
    <xsl:if test="$text='true'">
      <xsl:variable name="texte" select="normalize-space(.)"/>
      <xsl:call-template name="genNode">
        <xsl:with-param name="tab" select="$tab"/>
        <xsl:with-param name="cousins.precedants" select="$cousins.precedants"/>
        <xsl:with-param name="ligne" select="$premiere.ligne+$cousins.precedants"/>
        <xsl:with-param name="texte">
          <xsl:choose>
            <xsl:when test="string-length($texte) &lt;= $stripping.string.length">
              <xsl:value-of select="$texte"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="concat(substring($texte,1,$stripping.string.length),' ...')"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  <xsl:template match="/">
    <svg>
      <g style="{$graph.style};fill:{$box.color};">
        <xsl:choose>
          <xsl:when test="maxProf = -1">
            <xsl:apply-templates/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:variable name="passe1">
              <xsl:apply-templates mode="trunk"/>
            </xsl:variable>
            <xsl:apply-templates select="exsl:node-set($passe1)/*"/>
           </xsl:otherwise>
        </xsl:choose>
      </g>
    </svg>
  </xsl:template>
  <xsl:template match="*">
    <xsl:param name="tab" select="0"/>
    <xsl:param name="premiere.ligne" select="0"/>
    <xsl:variable name="cousins.precedants.texte">
      <xsl:choose>
        <xsl:when test="$text='true'">
          <xsl:value-of select="count(preceding-sibling::text()
                     | preceding-sibling::*/descendant::text())"/>
        </xsl:when>
        <xsl:otherwise>0</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="cousins.precedants" select="count(preceding-sibling::* | 
                     preceding-sibling::*/descendant::*)+
                     $cousins.precedants.texte"/>
    <xsl:call-template name="genNode">
      <xsl:with-param name="tab" select="$tab"/>
      <xsl:with-param name="cousins.precedants" select="$cousins.precedants"/>
      <xsl:with-param name="ligne" select="$premiere.ligne+$cousins.precedants"/>
    </xsl:call-template>
    <xsl:apply-templates>
      <xsl:with-param name="tab" select="$tab+1"/>
      <xsl:with-param name="premiere.ligne" select="$premiere.ligne+1+
				$cousins.precedants"/>
    </xsl:apply-templates>
  </xsl:template>
  <xsl:template name="genNode">
    <xsl:param name="tab" select="0"/>
    <xsl:param name="ligne" select="0"/>
    <!--        <xsl:message><xsl:value-of select="name()"/> : tab = <xsl:value-of select="$tab"/>, ligne = <xsl:value-of select="$ligne"/></xsl:message> -->
    <xsl:param name="cousins.precedants" select="0"/>
    <xsl:param name="texte" select="name()"/>
    <xsl:variable name="x" select="$x0 + $tab * $columnWidth "/>
    <xsl:variable name="y" select="$y0 + $ligne*$rowHeight + 
               ($letterHeight div 2)"/>
    <xsl:variable name="boxLength">
      <xsl:call-template name="string-width">
        <xsl:with-param name="s" select="$texte"/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="self::fs:f[@name]">
        <xsl:variable name="txtLength">
          <xsl:call-template name="string-width">
            <xsl:with-param name="s" select="@name"/>
          </xsl:call-template>
        </xsl:variable>
        <xsl:processing-instruction name="xmlizer">tab="<xsl:value-of select="$tab"/>" ligne="<xsl:value-of select="$ligne"/>" xMax="<xsl:value-of select="$x0+$x+$columnWidth+$txtLength"/>" yMax="<xsl:value-of select="$y+$rowHeight"/>"</xsl:processing-instruction>
        <text x="{$x}" y="{$y}" style="{$text.style};fill:{$text.color};">
          <!-- L'origine d'un texte est le point sur sa ligne 
						d'écriture à gauche du premier caractère.
						note : les lettres comme g et p dépassent sous cette ligne -->
          <xsl:value-of select="@name"/>
        </text>
      </xsl:when>
      <xsl:when test="self::fs:d[@name]">
        <g>
          <xsl:variable name="retraitV" select="'3'"/>
          <xsl:variable name="retraitH" select="'2'"/>
          <rect x="{$x - $letterWidth div 2}" y="{$y - $boxHeight + $letterHeight div 2 + $retraitV}" width="{$boxLength + $letterWidth}" height="{$boxHeight - $retraitV -2}" stroke="black"/>
          <path style="{$handle.path.style}" d="{concat('M',$x - $letterWidth div 2,',',
                				$y - $boxHeight + $letterHeight div 2 + $retraitV,
                				'L',$x - $letterWidth div 2 + $retraitH,',',
                				$y - $boxHeight + $letterHeight div 2,
                				'h',$columnWidth div 2 - 4,
                				'L',$x - $letterWidth div 2 + $retraitH + 
                				$columnWidth div 2 - 4 + $retraitH,',',
                				$y - $boxHeight + $letterHeight div 2 + $retraitV)}" stroke="{$handle.color}" fill="{$box.color}"/>
          <path style="{$handle.path.style}" d="{concat('M',$x + $boxLength + $letterWidth div 2,',',
               				$y - $letterHeight div 2,'h',$columnWidth div 2)}" stroke="{$handle.color}"/>
          <xsl:variable name="txtLength">
            <xsl:call-template name="string-width">
              <xsl:with-param name="s" select="@name"/>
            </xsl:call-template>
          </xsl:variable>
          <xsl:variable name="xText" select="$x + $boxLength + $letterWidth div 2 + 
							$columnWidth div 2 + 5"/>
          <xsl:processing-instruction name="xmlizer">tab="<xsl:value-of select="$tab"/>" ligne="<xsl:value-of select="$ligne"/>" xMax="<xsl:value-of select="$xText+$txtLength"/>" yMax="<xsl:value-of select="$y+$rowHeight"/>"</xsl:processing-instruction>
          <text x="{$xText}" y="{$y}" style="{$text.style};fill:{$text.color};">
            <!-- L'origine d'un texte est le point sur sa ligne 
              d'écriture à gauche du premier caractère.
          note : les lettres comme g et p dépassent sous cette ligne -->
            <xsl:value-of select="@name"/>
          </text>
        </g>
      </xsl:when>
      <xsl:otherwise>
        <!-- cas normal -->
        <xsl:if test="not(self::text())">
          <xsl:if test="$boxes = 'true'">
            <!-- génération d'un cadre rectangle -->
            <rect x="{$x - $letterWidth div 2}" y="{$y - $boxHeight + $letterHeight div 2}" width="{$boxLength + $letterWidth}" height="{$boxHeight}" stroke="black"/>
            <!-- L'origine d'un rectangle est son coin en haut à gauche -->
          </xsl:if>
        </xsl:if>
        <xsl:variable name="xmax" select="$boxLength+$x+$letterWidth+$x0"/>
        <xsl:processing-instruction name="xmlizer">tab="<xsl:value-of select="$tab"/>" ligne="<xsl:value-of select="$ligne"/>" xMax="<xsl:value-of select="$xmax"/>" yMax="<xsl:value-of select="$y+$rowHeight"/>"</xsl:processing-instruction>
        <!-- génération du texte -->
        <text x="{$x}" y="{$y}" style="{$text.style};fill:{$text.color};">
          <!-- L'origine d'un texte est le point sur sa ligne 
              d'écriture à gauche du premier caractère.
          note : les lettres comme g et p dépassent sous cette ligne -->
          <xsl:value-of select="$texte"/>
        </text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:if test="($handles = 'true') and 
             ($tab!=0 or ($tab=0 and $root.handle='true'))">
      <!-- génération du filet horizontal -->
      <path style="{$handle.path.style}" d="{concat('M',string($x - $letterWidth div 2),',',
               string($y - $letterHeight div 2),'h',string(- $columnWidth div 2))}" stroke="{$handle.color}"/>
      <!-- génération du filet vertical -->
      <xsl:if test="count(following-sibling) = 0">
        <path style="{$handle.path.style}" d="{concat('M',
                    string($x - $letterWidth div 2 - $columnWidth div 2),',',
                		string($y - $letterHeight div 2),
                		'v',string(- ($rowHeight * $cousins.precedants + 
                		$boxHeight div 2 + $letterHeight div 2)))}" stroke="{$handle.color}"/>
      </xsl:if>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
