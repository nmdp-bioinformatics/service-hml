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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.concurrent.Immutable;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import org.nmdp.ngs.hml.jaxb.Hml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hml resource.
 */
@Path("/hml")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
@Immutable
public final class HmlResource {
    private final HmlService hmlService;
    private static final Logger logger = LoggerFactory.getLogger(HmlResource.class);

    @Inject
    public HmlResource(final HmlService hmlService) {
        checkNotNull(hmlService);
        this.hmlService = hmlService;
    }

    @GET
    @Path("{id}")
    public Hml getHml(final @PathParam("id") String id) {
        return hmlService.getHml(id);
    }

    @POST
    public void registerHml(final Hml hml) {
        hmlService.registerHml(hml);
    }
}
