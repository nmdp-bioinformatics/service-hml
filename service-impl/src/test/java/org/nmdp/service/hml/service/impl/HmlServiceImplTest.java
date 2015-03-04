/*

    hml-service-impl  HML service implementation.
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
package org.nmdp.service.hml.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.nmdp.service.hml.service.AbstractHmlServiceTest;
import org.nmdp.service.hml.service.HmlService;
import org.nmdp.service.hml.service.HmlValidationException;

import org.nmdp.ngs.hml.jaxb.Hml;
import org.nmdp.ngs.hml.jaxb.Hmlid;

/**
 * Unit test for HmlServiceImpl.
 */
public final class HmlServiceImplTest extends AbstractHmlServiceTest {

    @Override
    protected HmlService createHmlService() {
        return new HmlServiceImpl();
    }

    @Test
    public void testGetHmlNullId() {
        assertNull(hmlService.getHml(null));
    }

    @Test
    public void testGetHmlEmptyId() {
        assertNull(hmlService.getHml(""));
    }

    @Test
    public void testGetHmlCacheMiss() {
        assertNull(hmlService.getHml("not a valid id"));
    }

    @Test(expected=HmlValidationException.class)
    public void testRegisterHmlMissingHmlid() {
        hmlService.registerHml(new Hml());
    }

    @Test
    public void testRegisterHmlNullExtension() {
        Hml hml = new Hml();
        Hmlid hmlid = new Hmlid();
        hmlid.setRoot("root");
        hmlid.setExtension(null);
        hml.setHmlid(hmlid);

        assertEquals("root", hmlService.registerHml(hml));
    }

    @Test
    public void testRegisterHmlEmptyExtension() {
        Hml hml = new Hml();
        Hmlid hmlid = new Hmlid();
        hmlid.setRoot("root");
        hmlid.setExtension("");
        hml.setHmlid(hmlid);

        assertEquals("root", hmlService.registerHml(hml));
    }

    @Test
    public void testRegisterHmlRootExtension() {
        Hml hml = new Hml();
        Hmlid hmlid = new Hmlid();
        hmlid.setRoot("root");
        hmlid.setExtension("extension");
        hml.setHmlid(hmlid);

        assertEquals("root/extension", hmlService.registerHml(hml));
    }

    @Test
    public void testGetHmlCacheHit() {
        Hml hml = new Hml();
        Hmlid hmlid = new Hmlid();
        hmlid.setRoot("root");
        hmlid.setExtension("extension");
        hml.setHmlid(hmlid);

        String id = hmlService.registerHml(hml);
        assertEquals(hml, hmlService.getHml(id));
    }
}
