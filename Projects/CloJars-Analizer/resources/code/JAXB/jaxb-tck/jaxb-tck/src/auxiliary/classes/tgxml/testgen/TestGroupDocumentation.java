/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.28 at 04:17:44 PM MSK 
//


package javasoft.sqe.jaxb.tgxml.testgen;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "description",
    "assertionRefOrInlineAssertion",
    "testedPackage",
    "testedClass",
    "memberSig",
    "docElem",
    "author"
})
@XmlRootElement(name = "TestGroupDocumentation")
public class TestGroupDocumentation {

    @XmlElement(name = "Title", required = true)
    protected String title;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElements({
        @XmlElement(name = "AssertionRef", type = AssertionRef.class),
        @XmlElement(name = "InlineAssertion", type = InlineAssertion.class)
    })
    protected List<Object> assertionRefOrInlineAssertion;
    @XmlElement(name = "TestedPackage")
    protected String testedPackage;
    @XmlElement(name = "TestedClass")
    protected String testedClass;
    @XmlElement(name = "MemberSig")
    protected String memberSig;
    @XmlElement(name = "DocElem")
    protected List<DocElem> docElem;
    @XmlElement(name = "Author")
    protected List<Author> author;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the assertionRefOrInlineAssertion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assertionRefOrInlineAssertion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssertionRefOrInlineAssertion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssertionRef }
     * {@link InlineAssertion }
     * 
     * 
     */
    public List<Object> getAssertionRefOrInlineAssertion() {
        if (assertionRefOrInlineAssertion == null) {
            assertionRefOrInlineAssertion = new ArrayList<Object>();
        }
        return this.assertionRefOrInlineAssertion;
    }

    /**
     * Gets the value of the testedPackage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestedPackage() {
        return testedPackage;
    }

    /**
     * Sets the value of the testedPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestedPackage(String value) {
        this.testedPackage = value;
    }

    /**
     * Gets the value of the testedClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestedClass() {
        return testedClass;
    }

    /**
     * Sets the value of the testedClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestedClass(String value) {
        this.testedClass = value;
    }

    /**
     * Gets the value of the memberSig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberSig() {
        return memberSig;
    }

    /**
     * Sets the value of the memberSig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberSig(String value) {
        this.memberSig = value;
    }

    /**
     * Gets the value of the docElem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the docElem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocElem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocElem }
     * 
     * 
     */
    public List<DocElem> getDocElem() {
        if (docElem == null) {
            docElem = new ArrayList<DocElem>();
        }
        return this.docElem;
    }

    /**
     * Gets the value of the author property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the author property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Author }
     * 
     * 
     */
    public List<Author> getAuthor() {
        if (author == null) {
            author = new ArrayList<Author>();
        }
        return this.author;
    }

}