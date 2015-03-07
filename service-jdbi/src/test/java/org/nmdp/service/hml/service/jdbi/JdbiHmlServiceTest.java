/*

    hml-service-jdbi  JDBI HML service.
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
package org.nmdp.service.hml.service.jdbi;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.nmdp.service.hml.service.AbstractHmlServiceTest;
import org.nmdp.service.hml.service.HmlService;

/**
 * Unit test for JdbiHmlService.
 */
public final class JdbiHmlServiceTest extends AbstractHmlServiceTest {
    @Mock
    private HmlDao hmlDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp();
    }

    @Override
    protected HmlService createHmlService() {
        return new JdbiHmlService(hmlDao);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullHmlDao() {
        new JdbiHmlService(null);
    }
}
