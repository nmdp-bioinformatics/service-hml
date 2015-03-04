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

import javax.ws.rs.core.Response;

import org.nmdp.ngs.hml.jaxb.Hml;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.POST;

import rx.Observable;

/**
 * HML service client.
 */
public interface HmlService {

    @GET("/hml/${id}")
    Hml getHml(@Path("id") String id);

    @POST("/hml")
    Response registerHml(@Body Hml hml);

    @POST("/hml")
    Observable<Response> registerHmlObservable(@Body Hml hml);
}
