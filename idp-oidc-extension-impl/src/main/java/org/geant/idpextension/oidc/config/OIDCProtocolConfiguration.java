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

package org.geant.idpextension.oidc.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;

import net.shibboleth.idp.profile.config.AbstractProfileConfiguration;
import net.shibboleth.utilities.java.support.annotation.constraint.NonnullElements;
import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.annotation.constraint.NotLive;
import net.shibboleth.utilities.java.support.annotation.constraint.Unmodifiable;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.component.InitializableComponent;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

/**
 * Base class for OIDC protocol configuration.
 */
public class OIDCProtocolConfiguration extends AbstractProfileConfiguration
    implements InitializableComponent {

    /** OIDC base protocol URI. */
    public static final String PROTOCOL_URI = "http://openid.net/specs/openid-connect-core-1_0.html";

    /** ID for this profile configuration. */
    public static final String PROFILE_ID = "http://csc.fi/ns/profiles/oidc/sso/browser";
    
    /** Initialization flag. */
    private boolean initialized;

    /** Flag to indicate whether attributes should be resolved for this profile. */
    private boolean resolveAttributes = true;
    
    /** Flag to indicate whether authorization code flow is supported by this profile. */
    private boolean authorizationCodeFlow = true;
    
    /** Flag to indicate whether implicit flow is supported by this profile. */
    private boolean implicitFlow = true;
    
    /** Flag to indicate whether hybrid flow is supported by this profile. */
    private boolean hybridFlow = true;
    
    /** Filters the usable authentication flows. */
    @Nonnull @NonnullElements private Set<String> authenticationFlows;
    
    /** Enables post-authentication interceptor flows. */
    @Nonnull @NonnullElements private List<String> postAuthenticationFlows;

    /**
     * Constructor.
     */
    public OIDCProtocolConfiguration() {
        this(PROFILE_ID);
    }
    
    /**
     * Creates a new configuration instance.
     *
     * @param profileId Unique profile identifier.
     */
    public OIDCProtocolConfiguration(@Nonnull @NotEmpty final String profileId) {
        super(profileId);
        authenticationFlows = Collections.emptySet();
        postAuthenticationFlows = Collections.emptyList();
    }

    /** {@inheritDoc} */
    @Override
    public void initialize() throws ComponentInitializationException {
        Constraint.isNotNull(getSecurityConfiguration(), "Security configuration cannot be null.");
        Constraint.isNotNull(getSecurityConfiguration().getIdGenerator(),
                "Security configuration ID generator cannot be null.");
        initialized = true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Checks whether the attributes are set to be resolved with this profile.
     *  
     * @return True if attribute resolution enabled for this profile, false otherwise. */
    public boolean isResolveAttributes() {
        return resolveAttributes;
    }

    /**
     * Enables or disables attribute resolution.
     *
     * @param resolve True to enable attribute resolution (default), false otherwise.
     */
    public void setResolveAttributes(final boolean resolve) {
        resolveAttributes = resolve;
    }
    
    /**
     * Checks whether the authorization code flow is enabled for this profile.
     * 
     * @return True if the flow is enabled for this profile, false otherwise.
     */
    public boolean isAuthorizationCodeFlow() {
        return authorizationCodeFlow;
    }
    
    /**
     * Enables or disables authorization code flow.
     * 
     * @param flow True to enable flow (default), false otherwise.
     */
    public void setAuthorizationCodeFlow(final boolean flow) {
        authorizationCodeFlow = flow;
    }

    /**
     * Checks whether the hybrid flow is enabled for this profile.
     * 
     * @return True if the flow is enabled for this profile, false otherwise.
     */
    public boolean isHybridFlow() {
        return hybridFlow;
    }
    
    /**
     * Enables or disables hybrid flow.
     * 
     * @param flow True to enable flow (default), false otherwise.
     */
    public void setHybridFlow(final boolean flow) {
        hybridFlow = flow;
    }

    /**
     * Checks whether the implicit flow is enabled for this profile.
     * 
     * @return True if the flow is enabled for this profile, false otherwise.
     */
    public boolean isImplicitFlow() {
        return implicitFlow;
    }
    
    /**
     * Enables or disables implicit flow.
     * 
     * @param flow True to enable flow (default), false otherwise.
     */
    public void setImplicitFlow(final boolean flow) {
        implicitFlow = flow;
    }

    /**
     * Get the enabled authentication flows.
     * 
     * @return enabled authentiaction flows.
     */
    @Nonnull @NonnullElements @NotLive @Unmodifiable public Set<String> getAuthenticationFlows() {
        return ImmutableSet.copyOf(authenticationFlows);
    }

    /**
     * Set the authentication flows to use.
     * 
     * @param flows   flow identifiers to use
     */
    public void setAuthenticationFlows(@Nonnull @NonnullElements final Collection<String> flows) {
        Constraint.isNotNull(flows, "Collection of flows cannot be null");
        
        authenticationFlows = new HashSet<>(StringSupport.normalizeStringCollection(flows));
    }
    
    /**
     * Get the ordered collection of enabled post-authentication interceptor flows.
     * 
     * @return enabled flow identifiers.
     */
    @Nonnull @NonnullElements @NotLive @Unmodifiable public List<String> getPostAuthenticationFlows() {
        return postAuthenticationFlows;
    }

    /**
     * Set the ordered collection of post-authentication interceptor flows to enable.
     * 
     * @param flows   flow identifiers to enable
     */
    public void setPostAuthenticationFlows(@Nonnull @NonnullElements final Collection<String> flows) {
        Constraint.isNotNull(flows, "Collection of flows cannot be null");
        
        postAuthenticationFlows = new ArrayList<>(StringSupport.normalizeStringCollection(flows));
    }
}
