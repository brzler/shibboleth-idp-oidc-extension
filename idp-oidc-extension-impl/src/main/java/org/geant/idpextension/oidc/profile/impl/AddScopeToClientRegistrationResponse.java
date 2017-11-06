/*
 * GÉANT BSD Software License
 *
 * Copyright (c) 2017 - 2020, GÉANT
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the GÉANT nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * Disclaimer:
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.geant.idpextension.oidc.profile.impl;

import javax.annotation.Nonnull;

import org.geant.idpextension.oidc.messaging.context.OIDCClientRegistrationResponseContext;
import org.opensaml.profile.context.ProfileRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.client.ClientMetadata;
import com.nimbusds.openid.connect.sdk.OIDCScopeValue;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientMetadata;

import net.shibboleth.utilities.java.support.logic.Constraint;

/**
 * Adds the {@link Scope} from the request metadata to the {@link OIDCClientRegistrationResponseContext}. If it is null
 * or empty, the configurable default {@link Scope} is set. By default, its value is 'openid'.
 */
@SuppressWarnings("rawtypes")
public class AddScopeToClientRegistrationResponse extends AbstractOIDCClientRegistrationResponseAction {

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(AddRedirectUrisToClientRegistrationResponse.class);
    
    /** The default {@link Scope} if it was not defined in the request. */
    private Scope defaultScope;

    /** Constructor. */
    public AddScopeToClientRegistrationResponse() {
        defaultScope = new Scope();
        defaultScope.add(OIDCScopeValue.OPENID);
    }
    
    /**
     * Set the default {@link Scope} to be used if it was not defined in the request.
     * @param scope The default {@link Scope} to be used if it was not defined in the request.
     */
    public void setDefaultScope(final Scope scope) {
        defaultScope = Constraint.isNotNull(scope, "The default scope cannot be null");
    }
    
    /** {@inheritDoc} */
    @Override
    protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext) {
        final ClientMetadata requestMetadata = getOidcClientRegistrationRequest().getClientMetadata();
        final OIDCClientMetadata metadata = getOidcClientRegistrationResponseContext().getClientMetadata();
        final Scope requestScope = requestMetadata.getScope();
        if (requestScope == null || requestScope.isEmpty()) {
            log.debug("{} Scope in the request was null, adding default scope", getLogPrefix());
            metadata.setScope(defaultScope);
        } else {
            metadata.setScope(requestMetadata.getScope());
        }
    }

}
