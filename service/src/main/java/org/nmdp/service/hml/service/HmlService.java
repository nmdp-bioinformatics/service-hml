/*

    hml-service  HML service.
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
package org.nmdp.service.hml.service;

import org.nmdp.ngs.hml.jaxb.Hml;

/**
 * HML service.
 */
public interface HmlService {

    /**
     * Retrieve the HML document for the specified id, if any.
     *
     * @param id id, root or root/extension attributes of the <code>&lt;hmlid%gt;</code> element
     * @return the HML document for the specified id or <code>null</code> if no such HML document exists
     */
    public Hml getHml(final String id);

    /**
     * Register the specified HML document.  Note an <code>&lt;hmlid&gt;</code> element
     * must be present for MIRING compliance.
     *
     * @param hml hml, must not be null
     * @throws HmlValidationException if the HML document is not semantically valid
     * @return the id for the newly registered HML document, root or root/extension attributes of the
     *    <code>&lt;hmlid%gt;</code> element
     */
    public String registerHml(final Hml hml);
}
