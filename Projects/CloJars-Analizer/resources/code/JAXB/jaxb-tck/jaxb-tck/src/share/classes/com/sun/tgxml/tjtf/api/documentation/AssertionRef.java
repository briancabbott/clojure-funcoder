/*
 * Copyright (c) 2002, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.tgxml.tjtf.api.documentation;

// <importgen> Generated imports for class: com.sun.tgxml.tjtf.api.documentation.AssertionRef
import com.sun.tgxml.tjtf.api.exceptions.TestFileException;
// </importgen>

/**
 * AssertionRef - 
 *
 * <b>AssertionRef</b> is an external reference to a portion of a specification that describes a statement
 *  (assertion) that is to be tested. An AssertionRef contains an <em>assertion reference-ID</em> which
 * can be used to document and identify the assertion statement.
 *
 * @version 	1.0, 04/26/2001
 * @author  Kevin T. Looney
 */


/*
 * ============================================================================================
 *    AssertionRef
 * ============================================================================================
 */


public interface AssertionRef extends Assertion {


    /*
     * ============================================================================================
     *    Methods
     * ============================================================================================
     */

   

  /**
    *   Validator.
    *  <p>
    *   Validates that the string is a properly formed reference ID.
    *  <p>
    * @param     id  The AssertionRef ID value
    * @return   false of the ID is not a properly formed ID.
    */
    public boolean valid (String id);



    //------------------------------------------------------------------------------
    //  Operations
    //------------------------------------------------------------------------------



  /**
    *   Get the reference ID associated with this assertion.
    *  <p>
    * @return     The value associated with this assertion
    * @see #setRef
    */
    public String getRef();
     
   /**
    *   Set the reference ID for this Assertion.
    *  <p>
    * @param     id The reference ID for this Assertion.
    * @throws    TestFileException if the ID is invalid.
    * @see #getRef
    */
    public void setRef(String id) throws TestFileException;

}
