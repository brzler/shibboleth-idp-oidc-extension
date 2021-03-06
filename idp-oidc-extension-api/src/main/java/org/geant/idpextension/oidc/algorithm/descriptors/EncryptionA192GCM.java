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

package org.geant.idpextension.oidc.algorithm.descriptors;

import javax.annotation.Nonnull;

import org.geant.idpextension.oidc.crypto.support.EncryptionConstants;
import org.opensaml.security.crypto.JCAConstants;
import org.opensaml.xmlsec.algorithm.BlockEncryptionAlgorithm;

/**
 * Algorithm descriptor for block encryption algorithm: 192GCM.
 */
public final class EncryptionA192GCM implements BlockEncryptionAlgorithm {

    /** {@inheritDoc} */
    @Nonnull
    public String getKey() {
        return JCAConstants.KEY_ALGO_AES;
    }

    /** {@inheritDoc} */
    @Nonnull
    public String getURI() {
        return EncryptionConstants.ALGO_ID_ENC_ALG_A192GCM;
    }

    /** {@inheritDoc} */
    @Nonnull
    public AlgorithmType getType() {
        return AlgorithmType.BlockEncryption;
    }

    /** {@inheritDoc} */
    @Nonnull
    public String getJCAAlgorithmID() {
        return String.format("%s/%s/%s", getKey(), getCipherMode(), getPadding());
    }

    /** {@inheritDoc} */
    @Nonnull
    public Integer getKeyLength() {
        return 192;
    }

    /** {@inheritDoc} */
    @Nonnull
    public String getCipherMode() {
        return JCAConstants.CIPHER_MODE_GCM;
    }

    /** {@inheritDoc} */
    @Nonnull
    public String getPadding() {
        return JCAConstants.CIPHER_PADDING_NONE;
    }

}
