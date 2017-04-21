<?xml version="1.0" encoding="UTF-8"?> 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns="http://www.w3.org/1999/xhtml">
    
    <xsl:output method="xml"
                doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" 
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" 
		encoding="UTF-8" 
		indent="yes"/> 

	<!-- Company template -->
    <xsl:template match="company">
		<html>
			<body>
				<div style="font-family: arial">
					<h1>Company <xsl:value-of select="name"/></h1>
					<h2>Quick links</h2>
					<div style="border: thin dotted #88f; margin: 10px; padding: 5px; background: #ffd; color: #88f">
						<ul>
							<li>Divisions</li>
							<xsl:for-each select="division">
								<xsl:variable name="didAnchor" select="@did"/>
								<li>
									<a href="#{$didAnchor}"><xsl:value-of select="name"/></a>
								</li>
							</xsl:for-each>
						</ul>
						<ul>
							<li>Employee positions</li>
							<xsl:for-each select="position">
								<xsl:variable name="idAnchor" select="@id"/>
                            <li>
								<i><a href="#{$idAnchor}"><xsl:value-of select="description"/></a></i>
							</li>
							</xsl:for-each>
						</ul>
					</div>
					<h2>Divisions</h2>
					<xsl:apply-templates select="division"/>
					<h2>Positions</h2>
					<xsl:for-each select="position">
						<html>
							<body>
								<div style="border: thin dotted black; margin: 10px; padding: 5px; background: #ffdddd">
									<h3><i><xsl:value-of select="description"/></i></h3>
									<i><xsl:value-of select="responsibility"/></i>
									<ul>
										<li>Employees at this position</li>
										<xsl:if test="@id='tester'">
											<xsl:apply-templates select="../division" mode="tester"/>
										</xsl:if>
										<xsl:if test="@id='worker'">
											<xsl:apply-templates select="../division" mode="worker"/>
										</xsl:if>
										<xsl:if test="@id='developer'">
											<xsl:apply-templates select="../division" mode="developer"/>
										</xsl:if>
									</ul>
								</div>
							</body>
						</html>
					</xsl:for-each>
				</div> 
			</body> 
		</html>
    </xsl:template>

    <!-- Division template -->
    <xsl:template match="division">
        <html>
            <body>
                <div style="border: thin dotted black; margin: 10px; padding: 5px; background: #ddffdd">
					<xsl:variable name="didAnchorTemplate" select="@did"/>
					<div id="{$didAnchorTemplate}">
                        <h3><i><xsl:value-of select="name"/></i></h3>
                        <xsl:if test="head">
                            <i>Head: </i><b><xsl:apply-templates select="head"/></b>
                        </xsl:if>
                        <xsl:apply-templates select="employees"/>
                    </div>
				</div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="employees">
        <ul>
            <li>Employees</li>
            <xsl:for-each select="employee">
                <li>
                   <xsl:apply-templates select="person"/>
                   <xsl:text>(PID=</xsl:text><xsl:value-of select="person/@pid"/><xsl:text>)</xsl:text>
                   <xsl:text> , salary </xsl:text><i><xsl:value-of select="salary"/></i><xsl:text> CZK</xsl:text>
                </li>
            </xsl:for-each>
        </ul>
    </xsl:template>

    <!-- Tester template -->
    <xsl:template match="division" mode="tester">
		<div id="tester">
			<xsl:apply-templates select="employees" mode="tester"/>
		</div>
    </xsl:template>

    <xsl:template match="employees" mode="tester">
        <xsl:for-each select="employee">
			<xsl:if test="@position='tester'">
               <li>
                   <xsl:apply-templates select="person"/>
                   <xsl:text>(PID=</xsl:text><xsl:value-of select="person/@pid"/><xsl:text>)</xsl:text>
                   <xsl:text> , salary </xsl:text><i><xsl:value-of select="salary"/></i><xsl:text> CZK</xsl:text>
               </li>
			</xsl:if>
        </xsl:for-each>
    </xsl:template>

    <!-- Worker template -->
    <xsl:template match="division" mode="worker">
		<div id="worker">
            <xsl:apply-templates select="employees" mode="worker"/>
		</div>
    </xsl:template>

    <xsl:template match="employees" mode="worker">
        <xsl:for-each select="employee">
            <xsl:if test="@position='worker'">
               <li>
                   <xsl:apply-templates select="person"/>
                   <xsl:text>(PID=</xsl:text><xsl:value-of select="person/@pid"/><xsl:text>)</xsl:text>
                   <xsl:text> , salary </xsl:text><i><xsl:value-of select="salary"/></i><xsl:text> CZK</xsl:text>
               </li>
           </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <!-- Developer template -->
    <xsl:template match="division" mode="developer">
		<div id="developer">
            <xsl:apply-templates select="employees" mode="developer"/>
		</div>
    </xsl:template>

    <xsl:template match="employees" mode="developer">
        <xsl:for-each select="employee">
            <xsl:if test="@position='developer'">
               <li>
                   <xsl:apply-templates select="person"/>
                   <xsl:text>(PID=</xsl:text><xsl:value-of select="person/@pid"/><xsl:text>)</xsl:text>
                   <xsl:text> , salary </xsl:text><i><xsl:value-of select="salary"/></i><xsl:text> CZK</xsl:text>
               </li>
           </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <!-- Head template -->
    <xsl:template match="head">
        <xsl:apply-templates select="person"/>
        <xsl:text>(PID=</xsl:text><xsl:value-of select="person/@pid"/><xsl:text>)</xsl:text>
    </xsl:template>

    <!-- Person template -->
    <xsl:template match="person">
		<b><xsl:value-of select="name"/><xsl:text> </xsl:text><xsl:value-of select="surname"/></b>
    </xsl:template>

</xsl:stylesheet>
