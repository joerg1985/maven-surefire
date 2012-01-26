package org.apache.maven.surefire.booter;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.PrintStream;
import java.util.Properties;
import org.apache.maven.plugin.surefire.runorder.StatisticsReporter;
import org.apache.maven.surefire.report.AbstractConsoleReporter;
import org.apache.maven.surefire.report.AbstractFileReporter;
import org.apache.maven.surefire.report.BriefConsoleReporter;
import org.apache.maven.surefire.report.BriefFileReporter;
import org.apache.maven.surefire.report.ConsoleOutputDirectReporter;
import org.apache.maven.surefire.report.ConsoleOutputFileReporter;
import org.apache.maven.surefire.report.ConsoleReporter;
import org.apache.maven.surefire.report.DetailedConsoleReporter;
import org.apache.maven.surefire.report.FileReporter;
import org.apache.maven.surefire.report.Reporter;
import org.apache.maven.surefire.report.XMLReporter2;

/**
 * All the parameters used to construct reporters
 * <p/>
 *
 * @author Kristian Rosenvold
 */
public class StartupReportConfiguration
{
    private final boolean useFile;

    private final boolean printSummary;

    private final String reportFormat;

    private final String reportNameSuffix;

    private final String configurationHash;

    private final boolean requiresRunHistory;

    private final boolean redirectTestOutputToFile;

    private final boolean disableXmlReport;

    private final File reportsDirectory;

    private final boolean trimStackTrace;

    private final Properties testVmSystemProperties = new Properties();

    public static final String BRIEF_REPORT_FORMAT = "brief";

    public static final String PLAIN_REPORT_FORMAT = "plain";

    public StartupReportConfiguration( boolean useFile, boolean printSummary, String reportFormat,
                                       boolean redirectTestOutputToFile, boolean disableXmlReport,
                                       File reportsDirectory, boolean trimStackTrace, String reportNameSuffix,
                                       String configurationHash, boolean requiresRunHistory )
    {
        this.useFile = useFile;
        this.printSummary = printSummary;
        this.reportFormat = reportFormat;
        this.redirectTestOutputToFile = redirectTestOutputToFile;
        this.disableXmlReport = disableXmlReport;
        this.reportsDirectory = reportsDirectory;
        this.trimStackTrace = trimStackTrace;
        this.reportNameSuffix = reportNameSuffix;
        this.configurationHash = configurationHash;
        this.requiresRunHistory = requiresRunHistory;
    }

    public static StartupReportConfiguration defaultValue()
    {
        File target = new File( "./target" );
        return new StartupReportConfiguration( true, true, "PLAIN", false, false, target, false, null, "TESTHASH",
                                               false );
    }

    public static StartupReportConfiguration defaultNoXml()
    {
        File target = new File( "./target" );
        return new StartupReportConfiguration( true, true, "PLAIN", false, true, target, false, null, "TESTHASHxXML",
                                               false );
    }

    public boolean isUseFile()
    {
        return useFile;
    }

    public boolean isPrintSummary()
    {
        return printSummary;
    }

    public String getReportFormat()
    {
        return reportFormat;
    }

    public String getReportNameSuffix()
    {
        return reportNameSuffix;
    }

    public boolean isRedirectTestOutputToFile()
    {
        return redirectTestOutputToFile;
    }

    public boolean isDisableXmlReport()
    {
        return disableXmlReport;
    }

    public File getReportsDirectory()
    {
        return reportsDirectory;
    }

    public XMLReporter2 instantiateXmlReporter()
    {
        if ( !isDisableXmlReport() )
        {
            return new XMLReporter2( trimStackTrace, reportsDirectory, reportNameSuffix );
        }
        return null;
    }

    public AbstractFileReporter instantiateFileReporter()
    {
        if ( isUseFile() )
        {
            if ( BRIEF_REPORT_FORMAT.equals( getReportFormat() ) )
            {
                return new BriefFileReporter( trimStackTrace, reportsDirectory, getReportNameSuffix() );
            }
            else if ( PLAIN_REPORT_FORMAT.equals( getReportFormat() ) )
            {
                return new FileReporter( trimStackTrace, reportsDirectory, getReportNameSuffix() );
            }
        }
        return null;
    }


    public AbstractConsoleReporter instantiateConsoleReporter()
    {
        if ( isUseFile() )
        {
            return isPrintSummary() ? new ConsoleReporter( trimStackTrace ) : null;
        }
        else if ( isRedirectTestOutputToFile() || BRIEF_REPORT_FORMAT.equals( getReportFormat() ) )
        {
            return new BriefConsoleReporter( trimStackTrace );
        }
        else if ( PLAIN_REPORT_FORMAT.equals( getReportFormat() ) )
        {
            return new DetailedConsoleReporter( trimStackTrace );
        }
        return null;
    }

    public Reporter instantiateConsoleOutputFileReporter( PrintStream originalSystemOut )
    {
        if ( isRedirectTestOutputToFile() )
        {
            return new ConsoleOutputFileReporter( reportsDirectory, getReportNameSuffix() );
        }
        else
        {
            return new ConsoleOutputDirectReporter( originalSystemOut );
        }
    }

    public StatisticsReporter instantiateStatisticsReporter()
    {
        if ( requiresRunHistory )
        {
            final File target = getStatisticsFile();
            return new StatisticsReporter( target );
        }
        return null;
    }

    public File getStatisticsFile()
    {
        return new File( reportsDirectory.getParentFile().getParentFile(), ".surefire-" + this.configurationHash );
    }


    public Properties getTestVmSystemProperties()
    {
        return testVmSystemProperties;
    }


    public boolean isTrimStackTrace()
    {
        return trimStackTrace;
    }

    public String getConfigurationHash()
    {
        return configurationHash;
    }

    public boolean isRequiresRunHistory()
    {
        return requiresRunHistory;
    }
}
