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
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.when;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.Before;
import org.junit.Test;

import org.nmdp.ngs.hml.jaxb.Hml;
import org.nmdp.ngs.hml.jaxb.Hmlid;

import org.nmdp.service.hml.service.HmlService;

/**
 * Unit test for HmlResource.
 */
public final class HmlResourceTest {
    private Hml hml;
    private HmlResource hmlResource;

    @Mock
    private HmlService hmlService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Hml hml = new Hml();
        Hmlid hmlid = new Hmlid();
        hmlid.setRoot("root");
        hmlid.setExtension("extension");
        hml.setHmlid(hmlid);

        hmlResource = new HmlResource(hmlService);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullHmlService() {
        new HmlResource(null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(hmlResource);
    }

    @Test
    public void testGetHml() {
        when(hmlService.getHml("root/extension")).thenReturn(hml);
        assertEquals(hml, hmlResource.getHml("root/extension"));
    }

    @Test
    public void testRegisterHml() {
        when(hmlService.registerHml(hml)).thenReturn("root/extension");
        Response response = hmlResource.registerHml(hml);
        assertEquals(201, response.getStatus());
        // todo: version 2.0 only; do we have the right version of jaxrs?
        //assertEquals(Response.Status.CREATED, response.getStatusCode());
        //assertEquals(MediaType.APPLICATION_XML, response.getMediaType());
        //assertNotNull(response.getCreated());
        //assertNotNull(response.getEntity());
    }
}
