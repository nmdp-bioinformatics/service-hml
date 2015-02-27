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

import javax.annotation.concurrent.Immutable;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import retrofit.RestAdapter;

import retrofit.converter.SimpleXMLConverter;

/**
 * Hml service module.
 */
@Immutable
public final class HmlServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        // empty
    }

    @Provides @Singleton
    static HmlService createHmlService(@EndpointUrl final String endpointUrl) {
        return new RestAdapter.Builder()
            .setEndpoint(endpointUrl)
            .setConverter(new SimpleXMLConverter())
            .build().create(HmlService.class);
    }
}
