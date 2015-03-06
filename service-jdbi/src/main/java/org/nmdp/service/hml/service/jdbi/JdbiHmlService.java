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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.concurrent.Immutable;

import java.io.IOException;
import java.io.StringWriter;

import com.google.inject.Inject;

import org.nmdp.ngs.hml.HmlWriter;

import org.nmdp.ngs.hml.jaxb.Hml;

import org.nmdp.service.hml.service.HmlService;
import org.nmdp.service.hml.service.HmlValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBI HML service.
 */
@Immutable
final class JdbiHmlService implements HmlService {
    private final HmlDao hmlDao;
    private static final Logger logger = LoggerFactory.getLogger(JdbiHmlService.class);

    @Inject
    JdbiHmlService(final HmlDao hmlDao) {
        checkNotNull(hmlDao);
        this.hmlDao = hmlDao;
    }

    @Override
    public Hml getHml(final String id) {
        return hmlDao.findHml(id);
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
            hmlDao.insertHml(root, toString(hml));
            return root;
        }
        else {
            String id = root + "/" + extension;
            hmlDao.insertHml(id, toString(hml));
            return id;
        }
    }

    static String toString(final Hml hml) {
        StringWriter writer = new StringWriter();
        try {
            HmlWriter.write(hml, writer);
        }
        catch (IOException e) {
            logger.warn("could not write HML", e);
        }
        return writer.toString();
    }
}
