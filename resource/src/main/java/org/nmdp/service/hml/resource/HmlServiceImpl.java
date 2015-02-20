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

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

import org.nmdp.ngs.hml.jaxb.Hml;

/**
 * HML service implementation.
 */
final class HmlServiceImpl implements HmlService {
    private final Map<String, Hml> cache = new ConcurrentHashMap<String, Hml>();

    @Override
    public Hml getHml(final String id) {
        return cache.get(id);
    }

    @Override
    public void registerHml(final Hml hml) {
        cache.put(hml.getHmlid().getExtension(), hml);
    }
}
