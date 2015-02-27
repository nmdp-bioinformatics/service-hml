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

import java.io.IOException;
import java.io.InputStream;

import java.lang.annotation.Annotation;

import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import javax.ws.rs.ext.MessageBodyReader;

import org.nmdp.ngs.hml.HmlReader;

import org.nmdp.ngs.hml.jaxb.Hml;

/**
 * HML message body reader.
 */
public final class HmlMessageBodyReader implements MessageBodyReader<Hml> {

    @Override
    public boolean isReadable(final Class<?> type,
                              final Type genericType,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return Hml.class.equals(type);
    }

    @Override
    public Hml readFrom(final Class<Hml> type,
                        final Type genericType,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, String> httpHeaders,
                        final InputStream entityStream) {
        try {
            return HmlReader.read(entityStream);
        }
        catch (IOException e) {
            throw new WebApplicationException(e.getCause());
        }
    }
}
