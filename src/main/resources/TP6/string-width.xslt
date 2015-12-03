<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
		<xsl:template name="string-width" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
		<xsl:param name="s"/>
       <xsl:param name="letterWidth" select="6.15"/>
       <xsl:param name="cSmall" select="0.4"/>
       <xsl:param name="cMedium" select="0.6"/>
       <xsl:param name="cBig" select="1.25"/>
       <xsl:param name="cVeryBig" select="1.5"/>
		<xsl:choose>
         		<xsl:when test="string-length($s) = 0">
				<xsl:value-of select="$letterWidth * $cSmall"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="v1">
					<xsl:variable name="car" select="substring($s,1,1)"/>
					<xsl:choose>
                  				<xsl:when test="$car = 'i' or $car = 'l' or $car = 'I' or $car = &quot;'&quot; or
									$car='[' or $car=']' or $car='(' or $car=')' or	
									$car='.' or $car=':' or $car=';' or $car='!'  ">	
							<xsl:value-of select="$letterWidth * $cSmall"/>
						</xsl:when>
                  				<xsl:when test="$car = ',' or $car='&quot;'  or $car='\' or $car = 't' or $car = 'f' or $car='r' or $car = '/' ">	
							<xsl:value-of select="$letterWidth * $cMedium"/>
						</xsl:when>
                  				<xsl:when test="$car = 'A' or $car = 'B' or $car = 'C' or $car = 'E' or $car = 'F' or
									$car='G' or $car='H' or $car='G' or $car='K' or $car='L' or $car='N' or
									$car='P' or $car='Q' or $car='R' or $car='S' or $car='T'or $car='V' or 
									$car='X' or $car='Y' or $car='Z' or $car='T' or $car='_' or $car='#' ">
							<xsl:value-of select="$letterWidth * $cBig"/>
						</xsl:when>
                  				<xsl:when test="$car = 'M' or $car='m'  or $car='W' or $car='U'  or  $car = 'w' or
                  				                    $car='O' or $car = 'D' or $car = '@' or $car = '%'">	
							<xsl:value-of select="$letterWidth * $cVeryBig"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$letterWidth"/>
						</xsl:otherwise>
               			</xsl:choose>
				</xsl:variable>
				<xsl:variable name="v2">
					<xsl:call-template name="string-width">
						<xsl:with-param name="s" select="substring($s,2,string-length($s))"/>	
					</xsl:call-template>
				</xsl:variable>
				<xsl:value-of select="$v1 + $v2"/>
			</xsl:otherwise>
     		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
