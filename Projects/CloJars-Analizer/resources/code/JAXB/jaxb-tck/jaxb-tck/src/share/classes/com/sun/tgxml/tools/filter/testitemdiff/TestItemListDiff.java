/*
 * Copyright (c) 2004, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.tgxml.tools.filter.testitemdiff;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.sun.tgxml.tjtf.api.exceptions.TestFileException;
import com.sun.tgxml.tjtf.resources.LibResHandler;
import com.sun.tgxml.tjtf.tools.ToolBase;
import com.sun.tgxml.tjtf.tools.options.BasicOption;
import com.sun.tgxml.tjtf.tools.options.OperandException;
import com.sun.tgxml.tjtf.tools.options.ParseArgumentException;
import com.sun.tgxml.tjtf.tools.options.StringOption;
import com.sun.tgxml.tjtf.tools.options.util.ArgReader;
import com.sun.tgxml.tjtf.tools.options.util.ParsedOption;

/**
 * A TestItem list file is generated by the Enhanced Filtering mechanism if the 
 * filters.test_item_list.out property is defined.
 * <p>
 * The TestItem Log Diff Tool takes as input two TestItem list files and reports
 * differences between them. The TestItem list file should be in format defined 
 * by Enhanced Filtering Mechanism - Software Design Document, 
 * Chapter 3.1.2. The Exclude List Filter. The level of reported details is 
 * customized via arguments.
 * <pre>
 * java  com.sun.tgxml.tools.filter.testitemdiff.TestItemListDiff  \
 *    [ options ] \
 *    -oldlist <file name> \
 *    -newlist <file name>
 * <pre>
 * The tool defines the following parameters:
 * <ul>
 *     <li> -help print this message
 *     <li> -oldlist &lt;file name&gt; - (mandatory) defines name of the old 
 *           TestItem list file
 *     <li> -newlist &lt;file name&gt; - (mandatory) defines name of the new 
 *           TestItem list file.
 *     <li> -new_items &lt;on|off&gt; - (Optional) turn on/off the reporting of 
 *           a new TestItems. By default it is switched off.
 *     <li> -variants &lt;on|off&gt; - (Optional) turn on/off the reporting of a 
 *           changed TestItem variant names. By default it is switched on.
 *     <li> -attributes &lt;on|off&gt; - (Optional) turn on/off the reporting of 
 *          a changed TestItem attributes names. The attribute changes are 
 *          reported only if the TestItem variant name is unchanged. 
 *          The generic exclude list can define additional attributes for a TestItem.
 *          Current version of the Enhanced Filtering Mechanism does not generate 
 *          specific attributes, but it can be changed in future releases. 
 *          By default it is switched off.
 *     <li> -libraries &lt;on|off&gt; - (Optional) turn on/off the reporting of
 *          the libraries. During redesing, libraries are often split added or removed.
 *          Therefore the build verification should not report the removed libraries.
 * </ul>
 * The tool returns 0 if no differences are reported, and 1 otherwise. 
 * The differences are grouped by type and sorted by TestItem name. 
 * The groups are reported in the following order:
 * <ul>
 *     <li> Missing TestItems
 *     <li> New TestItems
 *     <li> TestItems with a changed variant names. The report output contains 
 *          old and new variant name.
 *     <li> TestItems with a changed attributes. The report output contains old 
 *          and new attributes name. 
 * </ul>
 * 
 */
public class TestItemListDiff extends ToolBase {
    private static class StringListOption extends BasicOption {
        private ArrayList list = new ArrayList();
        public StringListOption(String sw, String usageInfo, boolean isObligatory) {
            super(sw, usageInfo, isObligatory);
        }

        public ArrayList getList() {
            return (ArrayList)list.clone();
        }
        public ArgReader getArgReader() {
            return new ArgReader(1, Integer.MAX_VALUE, getArgChecker());
        }
        
        public void calculateValue(ArrayList values) throws ParseArgumentException {
            ParsedOption val = (ParsedOption)values.get(values.size()-1);
            this.list = (ArrayList)val.getArguments().clone();
        }
   }

    protected StringOption reportNewOption = new StringOption(LibResHandler.getResStr("testitemlogdiff.option.new.mnem"),
            LibResHandler.getResStr("testitemlogdiff.option.new"),
            OPTIONAL);

    protected StringOption reportVariants = new StringOption(LibResHandler.getResStr("testitemlogdiff.option.variants.mnem"), 
            LibResHandler.getResStr("testitemlogdiff.option.variants"),
            OPTIONAL);


    protected StringOption reportAttributes = new StringOption(LibResHandler.getResStr("testitemlogdiff.option.attributes.mnem"), 
            LibResHandler.getResStr("testitemlogdiff.option.attributes"),
            OPTIONAL);

    protected StringOption oldList = new StringOption(LibResHandler.getResStr("testitemlogdiff.option.oldlist.mnem"), 
            LibResHandler.getResStr("testitemlogdiff.option.oldlist"),
            OBLIGATORY);

    protected StringOption newList = new StringOption(LibResHandler.getResStr("testitemlogdiff.option.newlist.mnem"), 
            LibResHandler.getResStr("testitemlogdiff.option.newlist"),
            OBLIGATORY);
    
    protected StringListOption exclude = new StringListOption(LibResHandler.getResStr("testitemlogdiff.option.exclude.mnem"), 
            LibResHandler.getResStr("testitemlogdiff.option.exclude"),
            OPTIONAL);

    private boolean isNewReported = false;
    private boolean isVariantsReported = true;
    private boolean isAttributesReported = false;
    private HashSet excludedAttributes = new HashSet();

    private String oldListName;
    private String newListName;
    
    private static final String ON = "on";
    private static final String OFF = "off";
    
    private TestItemListParser parser;
    private TestItemDiffReporter reporter;
    
    /**
     * creates instance with a given output and error PrintStreams.
     */
    public TestItemListDiff(PrintStream out, PrintStream err) {
        super(out, err);
    }

    public static void main(String args[]) {
        TestItemListDiff c = new TestItemListDiff(System.out, System.err);
        System.exit(c.run(args));
    }

    /**
     * registers options required for the TestItemListDiff.
     */
    public void registerOptions() {
        addOption(reportNewOption);
        addOption(reportVariants);
        addOption(reportAttributes);
        addOption(oldList);
        addOption(newList);
        addOption(exclude);
        super.registerOptions();
    }

    protected void setToolUsageBottom() {
        this.toolUsageButtom = LibResHandler.getResStr("testitemlogdiff.usage.header");
    }

    protected void setToolUsageHeader() {
        this.toolUsageHeader = "";
    }    

    /**
     * verifies given operand defined either on or off value
     * @param opt given operand
     * @throws OperandException if the operand value is not "on" or "off".
     */
    private void checkOnOff(StringOption opt) throws OperandException {
        String value = (opt.isSet() ? opt.getStringValue() : null);
        if ((value != null) && !value.equals(ON) && !value.equals(OFF)) {
            throw new OperandException(LibResHandler.getResStr("testitemlogdiff.onoffviolation", 
                                       opt.toString()));  
        }
    }
    
    public void startTool() {
        try {
            compareLogFiles(parser.parse(new InputStreamReader(new FileInputStream(oldListName))), 
                            parser.parse(new InputStreamReader(new FileInputStream(newListName))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Thrown: " + e);
        }
    }
    
    /**
     * compares an old and a new TestItem lists and reports differences using reporter. 
     */
    protected void compareLogFiles(TestItemList oldLog, TestItemList newLog) 
        throws TestFileException {
        for (Iterator i = oldLog.getKeysIterator(); i.hasNext();) {
            String key = (String)i.next();
            Entry oldEntry = oldLog.getEntry(key);
            if (!isReported(oldEntry)) {
                continue;
            }
            Entry newEntry = newLog.getEntry(key);
            
            if (newEntry == null) {
                setResultCode(1);
                reporter.reportMissing(key, oldEntry);
            } else if (isVariantsReported
                       && !equals(oldEntry.getVariant(), newEntry.getVariant())) {
                setResultCode(1);
                reporter.reportChangedVariant(key, oldEntry, newEntry);
            } else if (isAttributesReported
                       && !equals(oldEntry.getAttributes(), newEntry.getAttributes())) {
                setResultCode(1);
                reporter.reportChangedAttributes(key, oldEntry, newEntry);
            }
        }
        
        if (isNewReported) {
            for (Iterator i = newLog.getKeysIterator(); i.hasNext();) {
                String key = (String)i.next();
                if ((oldLog.getEntry(key) == null) 
                        && isReported(newLog.getEntry(key))) {
                        setResultCode(1);
                        reporter.reportNew(key, newLog.getEntry(key));
                }
            }
        }
        reporter.finalizeReport();
    }
    
    private boolean isReported(Entry entry) {
        String attrs = entry.getAttributes();
        if (attrs == null) {
            return true;
        }
        String[] list = attrs.split("[ \n\t;]+");
        for (int i = 0; i < list.length; i++) {
            if (excludedAttributes.contains(list[i])) {
                return false;
            }
        }
        return true;
    }

   
    protected boolean equals(Object left, Object right) {
        return (left == null) ? (left == right) : left.equals(right);
    }
    
    /**
     * sets fields according to the arguments values.
     */
    public void applyOptionsValues() throws ParseArgumentException {
        checkOnOff(reportNewOption);
        checkOnOff(reportVariants);
        checkOnOff(reportAttributes);

        parser = createTestItemListParser();
        reporter = createTestItemDiffReporter();
        oldListName = oldList.getStringValue();
        newListName = newList.getStringValue();
        isNewReported = reportNewOption.isSet() 
                        ? reportNewOption.getStringValue().equals(ON) : isNewReported;
        isVariantsReported = reportVariants.isSet()
                             ? reportVariants.getStringValue().equals(ON) : isVariantsReported;
        isAttributesReported = reportAttributes.isSet()
                               ? reportAttributes.getStringValue().equals(ON) : isAttributesReported;

        for (Iterator i = exclude.getList().iterator(); i.hasNext();) {
            Object current = i.next();
            excludedAttributes.add(current);
        }
        super.applyOptionsValues();
    }
    
    /**
     * executes the tool for the given arguments and returns true if there are
     * no differences reported and false otherwise.
     * @param oldListName name of an old TestItemList file.
     * @param newListName name of a new TestItemList file.
     * @param isNewReported flags if new TestItems should be reported.
     * @param isVariantsReported flags if variant name changes should be reported.
     * @param isAttributesReported flags if attribute changes should be reported.
     * @param reporter TestItemDiffReporter instance used to report detected differences.
     * @return true if no differences are reported and false otherwise.
     */
    public boolean run(String oldListName, String newListName,
                       boolean isNewReported,
                       boolean isVariantsReported,
                       boolean isAttributesReported,
                       TestItemDiffReporter reporter) {
        this.parser = createTestItemListParser();
        this.oldListName = oldListName;
        this.newListName = newListName;
        this.isNewReported = isNewReported;
        this.isVariantsReported = isVariantsReported;
        this.isAttributesReported = isAttributesReported;
        this.reporter = reporter;
        setResultCode(0);
        startTool();
        return (getResultCode() == 0);
    }
    
    /**
     * executes the tool for the given arguments and returns true if there are
     * no differences reported and false otherwise. The method uses TestItemDiffReporter
     * instance created by <code>createTestItemDiffReporter()</code>
     * @param oldListName name of an old TestItemList file.
     * @param newListName name of a new TestItemList file.
     * @param isNewReported flags if new TestItems should be reported.
     * @param isVariantsReported flags if variant name changes should be reported.
     * @param isAttributesReported flags if attribute changes should be reported.
     * @return true if no differences are reported and false otherwise.
     */
    public boolean run(String oldListName, String newListName,
                       boolean isNewReported,
                       boolean isVariantsReported,
                       boolean isAttributesReported) {
        
        return run(oldListName, newListName, isNewReported, 
                   isVariantsReported, isAttributesReported,
                   createTestItemDiffReporter());
    }
    
    /**
     * creates TestItemListParser used for TestItem list file parsing. 
     * The created instance parses test item list in in format defined 
     * by Enhanced Filtering Mechanism - Software Design Document, 
     * Chapter 3.1.2. The Exclude List Filter. 
     */
    protected TestItemListParser createTestItemListParser() {
        return new TestItemListParser();
    }
    
    /**
     * creates TestItemDiffReporter used for differences reporting. 
     * The created instance reports the differences in plain text format.
     */
    protected TestItemDiffReporter createTestItemDiffReporter() {
        m_loggingEnabled = true;
        return new TestItemDiffReporter() {
            protected void logMessage(String data) throws TestFileException {
                log(data);
            }
        };
    }
}
