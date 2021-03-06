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

package org.geant.idpextension.oidc.profile.impl;

import java.net.URISyntaxException;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import net.shibboleth.idp.profile.ActionTestingSupport;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;

import org.geant.idpextension.oidc.profile.context.navigate.DefaultUserInfoSigningAlgLookupFunction;
import org.opensaml.profile.action.EventIds;
import org.opensaml.security.credential.Credential;
import org.opensaml.xmlsec.SignatureSigningParameters;
import org.opensaml.xmlsec.context.SecurityParametersContext;
import org.springframework.webflow.execution.Event;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.id.Subject;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.nimbusds.jwt.SignedJWT;

/** {@link SignUserInfoResponse} unit test. */
public class SignUserInfoResponseTest extends BaseOIDCResponseActionTest {

    private SignUserInfoResponse action = new SignUserInfoResponse();

    SecurityParametersContext spCtx;

    private void init(String algo, Credential credential) throws ComponentInitializationException, URISyntaxException {
        metadataCtx.getClientInformation().getOIDCMetadata().setUserInfoJWSAlg(JWSAlgorithm.parse(algo));
        action.initialize();
        spCtx = new SecurityParametersContext();
        SignatureSigningParameters params = new SignatureSigningParameters();
        spCtx.setSignatureSigningParameters(params);
        params.setSigningCredential(credential);
        params.setSignatureAlgorithm(algo);
        profileRequestCtx.addSubcontext(spCtx);
        respCtx.setUserInfo(new UserInfo(new Subject("sub")));
    }

    /**
     * Test that action does nothing if there is no sufficient underlying contexts or not requested.
     */
    @Test
    public void testNoCredential() throws ComponentInitializationException, URISyntaxException {
        init("RS256", credentialRSA);
        spCtx.getSignatureSigningParameters().setSigningCredential(null);
        Event event = action.execute(requestCtx);
        ActionTestingSupport.assertProceedEvent(event);
        Assert.assertNull(respCtx.getProcessedToken());
    }

    /**
     * Test that action does nothing if there is no sufficient underlying contexts or not requested.
     */
    @Test
    public void testNoParameters() throws ComponentInitializationException, URISyntaxException {
        init("RS256", credentialRSA);
        spCtx.setSignatureSigningParameters(null);
        Event event = action.execute(requestCtx);
        ActionTestingSupport.assertProceedEvent(event);
        Assert.assertNull(respCtx.getProcessedToken());
    }

    /**
     * Test that action does nothing if there is no sufficient underlying contexts or not requested.
     */
    @Test
    public void testNoCtxt() throws ComponentInitializationException, URISyntaxException {
        init("RS256", credentialRSA);
        profileRequestCtx.removeSubcontext(SecurityParametersContext.class);
        Event event = action.execute(requestCtx);
        ActionTestingSupport.assertProceedEvent(event);
        Assert.assertNull(respCtx.getProcessedToken());
    }

    /**
     * Test that action does nothing if there is no sufficient underlying contexts or not requested.
     */
    @Test
    public void testNotInstructed() throws ComponentInitializationException, URISyntaxException {
        init("RS256", credentialRSA);
        metadataCtx.getClientInformation().getOIDCMetadata().setUserInfoJWSAlg(null);
        Event event = action.execute(requestCtx);
        ActionTestingSupport.assertProceedEvent(event);
        Assert.assertNull(respCtx.getProcessedToken());
    }

    /**
     * Test that action fails if there is no user info
     */
    @Test
    public void testNoUserInfo() throws ComponentInitializationException, URISyntaxException {
        init("RS256", credentialRSA);
        respCtx.setUserInfo(null);
        final Event event = action.execute(requestCtx);
        ActionTestingSupport.assertEvent(event, EventIds.INVALID_MSG_CTX);
    }

    private void testSuccessMessage(JWSVerifier verifier)
            throws ComponentInitializationException, URISyntaxException, JOSEException, ParseException {
        final Event event = action.execute(requestCtx);
        ActionTestingSupport.assertProceedEvent(event);
        Assert.assertNotNull(respCtx.getProcessedToken());
        Assert.assertTrue(((SignedJWT) respCtx.getProcessedToken()).verify(verifier));
    }

    /**
     * Test that action is able to form success message.
     */
    @Test
    public void testSuccessMessageRS256()
            throws ComponentInitializationException, URISyntaxException, JOSEException, ParseException {
        init("RS256", credentialRSA);
        testSuccessMessage(new RSASSAVerifier((RSAPublicKey) credentialRSA.getPublicKey()));
    }

    /**
     * Test that action is able to form success message.
     */
    @Test
    public void testSuccessMessageRS384()
            throws ComponentInitializationException, URISyntaxException, JOSEException, ParseException {
        init("RS384", credentialRSA);
        testSuccessMessage(new RSASSAVerifier((RSAPublicKey) credentialRSA.getPublicKey()));
    }

    /**
     * Test that action is able to form success message.
     */
    @Test
    public void testSuccessMessageRS512()
            throws ComponentInitializationException, URISyntaxException, JOSEException, ParseException {
        init("RS512", credentialRSA);
        testSuccessMessage(new RSASSAVerifier((RSAPublicKey) credentialRSA.getPublicKey()));
    }

    /**
     * Test that action is able to form success message.
     */
    @Test
    public void testSuccessMessageES256()
            throws ComponentInitializationException, URISyntaxException, JOSEException, ParseException {
        init("ES256", credentialEC256);
        testSuccessMessage(new ECDSAVerifier((ECPublicKey) credentialEC256.getPublicKey()));
    }

    /**
     * Test that action is able to form success message.
     */
    @Test
    public void testSuccessMessageES384()
            throws ComponentInitializationException, URISyntaxException, JOSEException, ParseException {
        init("ES384", credentialEC384);
        testSuccessMessage(new ECDSAVerifier((ECPublicKey) credentialEC384.getPublicKey()));
    }

    /**
     * Test that action is able to form success message.
     */
    @Test
    public void testSuccessMessageES512()
            throws ComponentInitializationException, URISyntaxException, JOSEException, ParseException {
        init("ES512", credentialEC521);
        testSuccessMessage(new ECDSAVerifier((ECPublicKey) credentialEC521.getPublicKey()));
    }

    /**
     * Test that action is able to form success message.
     */
    @Test
    public void testSuccessMessageHS256()
            throws ComponentInitializationException, URISyntaxException, JOSEException, ParseException {
        init("HS256", credentialHMAC);
        testSuccessMessage(new MACVerifier(credentialHMAC.getSecretKey()));
    }

    /**
     * Test that action is able to form success message.
     */
    @Test
    public void testSuccessMessageHS384()
            throws ComponentInitializationException, URISyntaxException, JOSEException, ParseException {
        init("HS384", credentialHMAC);
        testSuccessMessage(new MACVerifier(credentialHMAC.getSecretKey()));
    }

    /**
     * Test that action is able to form success message.
     */
    @Test
    public void testSuccessMessageHS512()
            throws ComponentInitializationException, URISyntaxException, JOSEException, ParseException {
        init("HS512", credentialHMAC);
        testSuccessMessage(new MACVerifier(credentialHMAC.getSecretKey()));
    }

}