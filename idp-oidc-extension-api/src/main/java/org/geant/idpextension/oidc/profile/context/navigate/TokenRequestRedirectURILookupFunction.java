/*
 * Copyright (c) 2017 - 2020, GÉANT
 *
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.geant.idpextension.oidc.profile.context.navigate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.oauth2.sdk.TokenRequest;

/**
 * For Token endpoint.
 * 
 * A function that returns redirect uri of the request via a lookup function. This default lookup locates uri from oidc
 * token request if available. If information is not available, null is returned.
 */
public class TokenRequestRedirectURILookupFunction extends AbstractTokenRequestLookupFunction<URI> {

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(TokenRequestRedirectURILookupFunction.class);

    /** {@inheritDoc} */
    @Override
    URI doLookup(TokenRequest req) {
        List<String> redirectURIs = req.getAuthorizationGrant().toParameters().get("redirect_uri");
        if (redirectURIs == null || redirectURIs.isEmpty()) {
            log.warn("No redirect_uri parameter");
            return null;
        }
        String redirectURI = redirectURIs.get(0);
        if (redirectURI == null) {
            log.warn("No redirect_uri parameter");
            return null;
        }
        URI uri = null;
        try {
            uri = new URI(redirectURI);
        } catch (URISyntaxException e) {
            log.error("Unable to parse uri from token request redirect_uri {}", redirectURI);
        }
        return uri;
    }
}