package com.example.robotto_opcua;

import android.content.Context;

import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.utils.CertificateUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ExampleKeys {
    private static final String PRIVKEY_PASSWORD = "password";


    /**
     * Load file certificate and private key from applicationName.der & .pfx - or create ones if they do not exist
     *
     * @return the KeyPair composed of the certificate and private key
     */

    public static KeyPair getCert(Context context, ApplicationDescription applicationDescription)
            throws ServiceResultException {

        String applicationName = applicationDescription.getApplicationName().getText();
        String applicationUri = applicationDescription.getApplicationUri();

        File certFile = new File(context.getFilesDir(), applicationName + ".der");
        File privKeyFile = new File(context.getFilesDir(), applicationName + ".pem");
        //Cert myCertificate;
        //PrivKey myPrivateKey;

        try {
            Cert myCertificate = Cert.load(certFile);
            PrivKey myPrivateKey = PrivKey.load(privKeyFile, PRIVKEY_PASSWORD);

            return new KeyPair(myCertificate, myPrivateKey);
        } catch(CertificateException e){
            throw new  ServiceResultException(e);
        }
        catch (IOException e) {
            try {
                String hostName = InetAddress.getLocalHost().getHostName();
                KeyPair keys = CertificateUtils.createApplicationInstanceCertificate(
                        applicationName, null, applicationUri, 3650, hostName);
                keys.getCertificate().save(certFile);
                keys.getPrivateKey().save(privKeyFile);

                return keys;
            } catch (Exception e1) {
                throw new ServiceResultException(e1);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceResultException(e);
        } catch (InvalidKeyException e) {
            throw new ServiceResultException(e);
        } catch (InvalidKeySpecException e) {
            throw new ServiceResultException(e);
        } catch (NoSuchPaddingException e) {
            throw new ServiceResultException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new ServiceResultException(e);
        } catch (IllegalBlockSizeException e) {
            throw new ServiceResultException(e);
        } catch (BadPaddingException e) {
            throw new ServiceResultException(e);
        } catch (InvalidParameterSpecException e) {
            throw new ServiceResultException(e);
        }
    }
}
