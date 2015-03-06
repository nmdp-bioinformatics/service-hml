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

import javax.annotation.concurrent.Immutable;

import java.io.IOException;
import java.io.Reader;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nmdp.ngs.hml.HmlReader;

import org.nmdp.ngs.hml.jaxb.Hml;

import org.skife.jdbi.v2.StatementContext;

import org.skife.jdbi.v2.tweak.ResultSetMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HML mapper.
 */
@Immutable
public final class HmlMapper implements ResultSetMapper<Hml> {
    private static final Logger logger = LoggerFactory.getLogger(JdbiHmlService.class);

    @Override
    public Hml map(final int index, final ResultSet resultSet, final StatementContext statementContext) throws SQLException {
        try (Reader reader = resultSet.getClob("hml").getCharacterStream()) {
            return HmlReader.read(reader);
        }
        catch (IOException e) {
            logger.warn("could not read HML", e);
            throw new SQLException("could not read HML", e);
        }
    }
}
