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

import javax.ws.rs.WebApplicationException;

import javax.ws.rs.ext.ExceptionMapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.xml.bind.UnmarshalException;

import org.nmdp.service.hml.service.HmlValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HML exception mapper.
 */
public final class HmlExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Logger logger = LoggerFactory.getLogger(HmlExceptionMapper.class);

    @Override
    public Response toResponse(final RuntimeException runtimeException) {
        // expected to be thrown from HmlServiceImpl validation failure
        if (runtimeException instanceof HmlValidationException) {
            if (logger.isWarnEnabled()) {
                logger.warn("invalid HML request: " + runtimeException.getMessage());
            } 

            return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_XML)
                .entity("<?xml version=\"1.0\" encoding=\"UTF-8\"><error code=\"400\" message=\"invalid HML request: \"" + runtimeException.getMessage() + "\"/>")
                .build();
        }
        // expected to be thrown from who knows where on XML parsing or validation failure
        else if (runtimeException instanceof WebApplicationException) {
            Throwable cause = runtimeException.getCause();
            if (cause instanceof UnmarshalException) {

                // try one nested cause deep to find error message
                String message = cause.getMessage();
                if (message == null && cause.getCause() != null) {
                    message = cause.getCause().getMessage();
                }

                if (logger.isWarnEnabled()) {
                    logger.warn("invalid HML syntax: " + message);
                }

                return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_XML)
                    .entity("<?xml version=\"1.0\" encoding=\"UTF-8\"><error code=\"400\" message=\"invalid HML syntax: \"" + message + "\"/>")
                    .build();
            }
        }

        if (logger.isErrorEnabled()) {
            logger.error("unexpected runtime exception of type " + runtimeException.getClass(), runtimeException);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .type(MediaType.APPLICATION_XML)
            .entity("<?xml version=\"1.0\" encoding=\"UTF-8\"><error code=\"500\" message=\"Internal server error\"/>")
            .build();
    }
}
