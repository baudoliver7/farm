<?xml version="1.0"?>
<!--
  Copyright (c) 2016-2019 Zerocracy

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to read
  the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
  merge, publish, distribute, sublicense, and/or sell copies of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
  -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="2.0">
  <xsl:output method="html" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:strip-space elements="*"/>
  <xsl:include href="/xsl/inner-layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>Health</xsl:text>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="inner">
    <p>
      <ul>
        <li>
          <xsl:text>AWS-S3: </xsl:text>
          <xsl:value-of select="aws-s3"/>
        </li>
        <li>
          <xsl:text>AWS-SQS: </xsl:text>
          <xsl:value-of select="aws-sqs"/>
        </li>
        <li>
          <xsl:text>GitHub: </xsl:text>
          <xsl:value-of select="github"/>
        </li>
        <li>
          <xsl:text>Stakeholders: </xsl:text>
          <xsl:value-of select="brigade"/>
        </li>
      </ul>
    </p>
  </xsl:template>
</xsl:stylesheet>
