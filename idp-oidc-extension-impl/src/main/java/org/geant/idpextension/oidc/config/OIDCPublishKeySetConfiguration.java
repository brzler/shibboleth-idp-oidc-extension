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

package org.geant.idpextension.oidc.config;

import javax.annotation.Nonnull;

import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;

/**
 * Profile configuration for publishing OP key set.
 */
public class OIDCPublishKeySetConfiguration extends AbstractOIDCProfileConfiguration {

    /** OIDC base protocol URI. Section 3 jwks_uri is the relevant. */
    public static final String PROTOCOL_URI =
            "https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata";

    /** ID for this profile configuration. */
    public static final String PROFILE_ID = "http://csc.fi/ns/profiles/oidc/keyset";

    /**
     * Constructor.
     */
    public OIDCPublishKeySetConfiguration() {
        this(PROFILE_ID);
    }

    /**
     * Creates a new configuration instance.
     *
     * @param profileId Unique profile identifier.
     */
    public OIDCPublishKeySetConfiguration(@Nonnull @NotEmpty final String profileId) {
        super(profileId);
    }

}
