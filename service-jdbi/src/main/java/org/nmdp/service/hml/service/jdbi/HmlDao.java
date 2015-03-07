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

import org.nmdp.ngs.hml.jaxb.Hml;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

/**
 * HML data access object (DAO).
 */
public interface HmlDao extends Transactional<HmlDao> {

    @Mapper(HmlMapper.class)
    @SqlQuery("select hml from hml where id = :id")
    Hml findHml(@Bind("id") String id);

    @SqlUpdate("insert into hml (id, hml) values (:id, :hml)")
    void insertHml(@Bind("id") String id, @Bind("hml") String hml);
}
