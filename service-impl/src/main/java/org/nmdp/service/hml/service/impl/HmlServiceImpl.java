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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

import org.nmdp.ngs.hml.jaxb.Hml;

import org.nmdp.service.hml.service.HmlService;
import org.nmdp.service.hml.service.HmlValidationException;

/**
 * HML service implementation.
 */
final class HmlServiceImpl implements HmlService {
    private final Map<String, Hml> cache = new ConcurrentHashMap<String, Hml>();

    @Override
    public Hml getHml(final String id) {
        return id == null ? null : cache.get(id);
    }

    @Override
    public String registerHml(final Hml hml) {
        checkNotNull(hml);
        if (hml.getHmlid() == null) {
            throw new HmlValidationException("<hmlid> element must be present");
        }
        String root = hml.getHmlid().getRoot();
        String extension = hml.getHmlid().getExtension();
        if (extension == null || extension.trim().length() == 0) {
            cache.put(root, hml);
            return root;
        }
        else {
            String id = root + "/" + extension;
            cache.put(id, hml);
            return id;
        }
    }
}
