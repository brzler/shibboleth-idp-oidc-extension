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

package org.geant.idpextension.oidc.token.support;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.openid.connect.sdk.ClaimsRequest;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.claims.ACR;
import com.nimbusds.openid.connect.sdk.claims.ClaimsSet;

import net.minidev.json.JSONArray;
import net.shibboleth.utilities.java.support.security.DataSealer;
import net.shibboleth.utilities.java.support.security.DataSealerException;
import net.shibboleth.utilities.java.support.security.IdentifierGenerationStrategy;
import java.net.URI;
import java.text.ParseException;

/** Class wrapping claims set for authorize code. */
public final class AuthorizeCodeClaimsSet extends TokenClaimsSet {

    /** Value of authorize code claims set type. */
    public static final String VALUE_TYPE_AC = "ac";

    /** Class logger. */
    @Nonnull
    private Logger log = LoggerFactory.getLogger(AuthorizeCodeClaimsSet.class);

    /**
     * Constructor for authorize code claims set.
     * 
     * @param idGenerator Generator for pseudo unique identifier for the code. Must not be NULL.
     * @param clientID Client Id of the rp. Must not be NULL.
     * @param issuer OP issuer value. Must not be NULL.
     * @param userPrincipal User Principal of the authenticated user. Must not be NULL.
     * @param subject Subject of the authenticated user. Must not be NULL
     * @param acr Authentication context class reference value of the authentication. May be NULL.
     * @param iat Issue time of the authorize code. Must not be NULL.
     * @param exp Expiration time of the authorize code. Must not be NULL.
     * @param nonce Nonce of the authentication request. May be NULL.
     * @param authTime Authentication time of the user. Must not be NULL.
     * @param redirectURI Validated redirect URI of the authentication request. Must not be NULL.
     * @param scope Scope of the authentication request. Must not be NULL.
     * @param claims Claims request of the authentication request. May be NULL.
     * @param dlClaims Token delivery claims delivered both for id token and userinfo response. May be NULL.
     * @param dlClaimsID Token delivery claims delivered for id token. May be NULL.
     * @param dlClaimsUI Token delivery claims delivered for userinfo response. May be NULL.
     * @param consentableClaims consentable claims. May be NULL.
     * @param consentedClaims consented claims. May be NULL.
     * @param codeChallenge Code Challenge. May be NULL.
     * @throws RuntimeException if called with nonallowed null parameters
     */
    private AuthorizeCodeClaimsSet(@Nonnull IdentifierGenerationStrategy idGenerator, @Nonnull ClientID clientID,
            @Nonnull String issuer, @Nonnull String userPrincipal, @Nonnull String subject, @Nonnull ACR acr,
            @Nonnull Date iat, @Nonnull Date exp, @Nullable Nonce nonce, @Nonnull Date authTime,
            @Nonnull URI redirectURI, @Nonnull Scope scope, @Nullable ClaimsRequest claims,
            @Nullable ClaimsSet dlClaims, @Nullable ClaimsSet dlClaimsID, @Nullable ClaimsSet dlClaimsUI,
            @Nullable JSONArray consentableClaims, @Nullable JSONArray consentedClaims, @Nullable String codeChallenge) {
        super(VALUE_TYPE_AC, idGenerator.generateIdentifier(), clientID, issuer, userPrincipal, subject, acr, iat, exp,
                nonce, authTime, redirectURI, scope, claims, dlClaims, dlClaimsID, dlClaimsUI, consentableClaims,
                consentedClaims, codeChallenge);
    }

    /**
     * Private constructor for the parser.
     * 
     * @param authzCodeClaimsSet authorize code claims set
     */
    private AuthorizeCodeClaimsSet(JWTClaimsSet authzCodeClaimsSet) {
        tokenClaimsSet = authzCodeClaimsSet;
    }

    /**
     * Parses authz code from string (JSON).
     * 
     * @param authorizeCodeClaimsSet JSON String representation of the code
     * @return AuthorizeCodeClaimsSet instance if parsing is successful.
     * @throws ParseException if parsing fails for example due to incompatible types.
     */
    public static AuthorizeCodeClaimsSet parse(String authorizeCodeClaimsSet) throws ParseException {
        JWTClaimsSet acClaimsSet = JWTClaimsSet.parse(authorizeCodeClaimsSet);
        // Throws exception if parsing result is not expected one.
        verifyParsedClaims(VALUE_TYPE_AC, acClaimsSet);
        return new AuthorizeCodeClaimsSet(acClaimsSet);
    }

    /**
     * Parses authz code from sealed authorization code.
     * 
     * @param wrappedAuthCode wrapped code
     * @param dataSealer sealer to unwrap the code
     * @return authorize code
     * @throws ParseException is thrown if unwrapped code is not understood
     * @throws DataSealerException is thrown if unwrapping fails
     */
    public static AuthorizeCodeClaimsSet parse(@Nonnull String wrappedAuthCode, @Nonnull DataSealer dataSealer)
            throws ParseException, DataSealerException {
        return parse(dataSealer.unwrap(wrappedAuthCode));
    }

    /** Builder to create instance of AuthorizeCodeClaimsSet. */
    public static class Builder extends TokenClaimsSet.Builder<AuthorizeCodeClaimsSet> {

        /**
         * Constructor for authorize code builder.
         * 
         * @param idGenerator Generator for pseudo unique identifier for the code. Must not be NULL.
         * @param clientID Client Id of the rp. Must not be NULL.
         * @param issuer OP issuer value. Must not be NULL.
         * @param userPrincipal User Principal of the authenticated user. Must not be NULL.
         * @param subject subject of the authenticated user. Must not be NULL
         * @param issuedAt Issue time of the authorize code. Must not be NULL.
         * @param expiresAt Expiration time of the authorize code. Must not be NULL.
         * @param authenticationTime Authentication time of the user. Must not be NULL.
         * @param redirectURI Validated redirect URI of the authentication request. Must not be NULL.
         * @param scope Scope of the authentication request. Must not be NULL.
         */
        public Builder(@Nonnull IdentifierGenerationStrategy idGenerator, @Nonnull ClientID clientID,
                @Nonnull String issuer, @Nonnull String userPrincipal, @Nonnull String subject, @Nonnull Date issuedAt,
                @Nonnull Date expiresAt, @Nonnull Date authenticationTime, @Nonnull URI redirectURI,
                @Nonnull Scope scope) {
            super(idGenerator, clientID, issuer, userPrincipal, subject, issuedAt, expiresAt, authenticationTime,
                    redirectURI, scope);
        }

        /**
         * Builds AuthorizeCodeClaimsSet.
         * 
         * @return AuthorizeCodeClaimsSet instance.
         */
        public AuthorizeCodeClaimsSet build() {
            return new AuthorizeCodeClaimsSet(idGen, rpId, iss, usrPrincipal, sub, acr, iat, exp, nonce, authTime,
                    redirect, reqScope, claims, dlClaims, dlClaimsID, dlClaimsUI, cnsntlClaims, cnsntdClaims, codeChallenge);
        }

    }

}
