/*

    hml-client  HML service client library.
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
package org.nmdp.service.hml.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.lang.reflect.Type;

import org.nmdp.ngs.hml.HmlReader;
import org.nmdp.ngs.hml.HmlWriter;

import org.nmdp.ngs.hml.jaxb.Hml;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;

import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * HML converter.
 */
public final class HmlConverter implements Converter {
    private static final String CHARSET = "UTF-8";
    private static final String MIME_TYPE = "application/xml; charset=" + CHARSET;

    @Override
    public Object fromBody(final TypedInput body, final Type type) throws ConversionException {
        try {
            return HmlReader.read(body.in());
        }
        catch (IOException e) {
            throw new ConversionException("could not convert HML", e);
        }
    }

    @Override
    public TypedOutput toBody(final Object object) {
        OutputStreamWriter osw = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            osw = new OutputStreamWriter(bos, CHARSET);
            HmlWriter.write((Hml) object, osw);
            osw.flush();
            return new TypedByteArray(MIME_TYPE, bos.toByteArray());
        }
        catch (Exception e) {
            // empty
        }
        finally {
            try {
                osw.close();
            }
            catch (Exception e) {
                // empty
            }
        }
        return null;
    }
}
