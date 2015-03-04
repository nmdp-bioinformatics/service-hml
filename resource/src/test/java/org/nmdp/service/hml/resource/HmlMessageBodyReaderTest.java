/*

    hml-resource  HML resources.
    Copyright (c) 2015 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.gnu.org/licenses/lgpl.html

*/
package org.nmdp.service.hml.resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.WebApplicationException;

import org.junit.Before;
import org.junit.Test;

import org.nmdp.ngs.hml.jaxb.Hml;

/**
 * Unit test for HmlMessageBodyReader.
 */
public final class HmlMessageBodyReaderTest {
    private HmlMessageBodyReader messageBodyReader;

    @Before
    public void setUp() {
        messageBodyReader = new HmlMessageBodyReader();
    }

    @Test
    public void testConstructor() {
        assertNotNull(messageBodyReader);
    }

    @Test
    public void testIsReadableObject() {
        assertFalse(messageBodyReader.isReadable(Object.class, null, null, null));
    }

    @Test
    public void testIsReadableHml() {
        assertTrue(messageBodyReader.isReadable(Hml.class, null, null, null));
    }

    @Test(expected=WebApplicationException.class)
    public void testReadFromEmpty() throws Exception {
        messageBodyReader.readFrom(null, null, null, null, null, getClass().getResourceAsStream("empty.xml"));
    }

    @Test(expected=WebApplicationException.class)
    public void testReadFromInvalidSyntax() throws Exception {
        messageBodyReader.readFrom(null, null, null, null, null, getClass().getResourceAsStream("invalid-syntax.xml"));
    }

    @Test(expected=WebApplicationException.class)
    public void testReadFromInvalidSchema() throws Exception {
        messageBodyReader.readFrom(null, null, null, null, null, getClass().getResourceAsStream("invalid-schema.xml"));
    }

    @Test
    public void testReadFrom() {
        assertNotNull(messageBodyReader.readFrom(null, null, null, null, null, getClass().getResourceAsStream("valid.xml")));
    }
}
