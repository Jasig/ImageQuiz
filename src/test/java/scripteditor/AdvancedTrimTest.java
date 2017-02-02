/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package scripteditor;

import junit.framework.TestCase;

/**
 * This test case documents the behavior of AdvancedTrim.trim() as discovered.
 */
public class AdvancedTrimTest extends TestCase {

    /**
     * Test that trim() returns alphanumerics unchanged.
     */
    public void testTrimIsANoOpOnAlphanumerics() {

        final String alphanumeric = "abcABC123";

        assertEquals(alphanumeric, AdvancedTrim.trim(alphanumeric));

    }

    /**
     * Test that trim() trims a String with multiple trailing space characters down to one
     * trailing space character.
     */
    public void testTrimsToOneTrailingWhitespace() {

        final String severalSpacesTrail = "whitespace-follows   ";
        final String justOneSpaceTrails = "whitespace-follows ";

        assertEquals(justOneSpaceTrails, AdvancedTrim.trim(severalSpacesTrail));

    }

    /**
     * Test that trim() trims a String with multiple leading space characters down to one leading
     * space character.
     */
    public void testTrimsToOneLeadingWhitespace() {

        final String severalSpacesLead = "   whitespace-led";
        final String justOneSpaceLeads = " whitespace-led";

        assertEquals(justOneSpaceLeads, AdvancedTrim.trim(severalSpacesLead));
    }

    /**
     * Test that trim() trims a String containing multiple sequences of multiple space characters
     * down to one space character in place of each such sequence.
     */
    public void testTrimsToOneEmbeddedWhitespaceSequences() {

        final String severalSequencesOfSeveralSpacesEmbedded =
            "unit    testing is   good for the    soul";

        final String onlyIsolatedSpacesRemain =
            "unit testing is good for the soul";

        assertEquals(onlyIsolatedSpacesRemain,
            AdvancedTrim.trim(severalSequencesOfSeveralSpacesEmbedded));
    }

}
