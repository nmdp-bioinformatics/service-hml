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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.WebApplicationException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.xml.bind.UnmarshalException;

import org.junit.Before;
import org.junit.Test;

import org.nmdp.service.hml.service.HmlValidationException;

/**
 * Unit test for HmlExceptionMapper.
 */
public final class HmlExceptionMapperTest {
    private HmlExceptionMapper exceptionMapper;

    @Before
    public void setUp() {
        exceptionMapper = new HmlExceptionMapper();
    }

    @Test
    public void testConstructor() {
        assertNotNull(exceptionMapper);
    }

    @Test
    public void testToResponseHmlValidationException() {
        Response response = exceptionMapper.toResponse(new HmlValidationException("message"));
        assertEquals(400, response.getStatus());
        // todo: version 2.0 only; do we have the right version of jaxrs?
        //assertEquals(Response.Status.BAD_REQUEST, response.getStatusCode());
        //assertEquals(MediaType.APPLICATION_XML, response.getMediaType());
        //assertTrue(response.getEntity().contains("message"));
    }

    @Test
    public void testToResponseWebApplicationExceptionNestedUnmarshalException() {
        Response response = exceptionMapper.toResponse(new WebApplicationException(new UnmarshalException("message")));
        assertEquals(400, response.getStatus());
        //assertEquals(Response.Status.BAD_REQUEST, response.getStatusCode());
        //assertEquals(MediaType.APPLICATION_XML, response.getMediaType());
        //assertTrue(response.getEntity().contains("message"));
    }

    @Test
    public void testToResponseWebApplicationException() {
        Response response = exceptionMapper.toResponse(new WebApplicationException());
        assertEquals(500, response.getStatus());
        //assertEquals(Response.Status.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals(MediaType.APPLICATION_XML, response.getMediaType());
        //assertFalse(response.getEntity().contains("message"));
    }

    @Test
    public void testToResponseRuntimeException() {
        Response response = exceptionMapper.toResponse(new RuntimeException("message"));
        assertEquals(500, response.getStatus());
        //assertEquals(Response.Status.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals(MediaType.APPLICATION_XML, response.getMediaType());
        //assertFalse(response.getEntity().contains("message"));
    }
}
