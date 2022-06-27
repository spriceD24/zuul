package com.csg.gm.cct.csclientportal.proxyzuul.utils;

 

import javax.crypto.Cipher;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;

import java.security.GeneralSecurityException;

import java.util.*;

 

public class EncryptUtil {

 

    //TBD Agree on these items

    private static String H_KEY =   "F6G2278B45578K1m";

    private static String I_KEY =   "2L18hpvgl547Q48x";

    private static String DELIMITER =   "-";

 

 

 

    protected static String encrypt( String encryptionValue, String skey, String ivx )

    {

        byte[] encrypted;

        try

        {

            SecretKeySpec keySpec = new SecretKeySpec( skey.getBytes(), "AES" );

            IvParameterSpec ivSpec = new IvParameterSpec( ivx.getBytes() );

            //TBD Agree on this

            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );

            cipher.init( Cipher.ENCRYPT_MODE , keySpec, ivSpec );

            byte[] stringBytes = encryptionValue.getBytes( "UTF8" );

            encrypted = cipher.doFinal( stringBytes );

        }

        catch( UnsupportedEncodingException e )

        {

            // UTF-8 is built in

            throw new RuntimeException( "Unexpected runtime exception: " + e, e );

        }

        catch( GeneralSecurityException e )

        {

            // Cipher should be avilable

            throw new RuntimeException( "Unexpected runtime exception: " + e, e );

        }

        return Base64.getEncoder()

                .encodeToString(encrypted);

    }

 

    protected static String decrypt( String encryptedValue, String skey, String ivx )

    {

        try

        {

            SecretKeySpec keySpec = new SecretKeySpec( skey.getBytes(), "AES" );

            IvParameterSpec ivSpec = new IvParameterSpec( ivx.getBytes() );

            //TBD Agree on this

            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );

            cipher.init( Cipher.DECRYPT_MODE , keySpec, ivSpec );

            byte[] plainText = cipher.doFinal(Base64.getDecoder()

                    .decode(encryptedValue));

            return new String(plainText);

        }

        catch( GeneralSecurityException e )

        {

            // Cipher should be avilable

            throw new RuntimeException( "Unexpected runtime exception: " + e, e );

        }

    }

 

 

    //Optional - Utility Methods can be elsewhere

    private static String delimiterSeparatedValues(Collection<? extends Object> c, String delimiter )

    {

        if( c == null ) return null;

 

        StringBuilder builder = new StringBuilder();

        appendDelimiterSeparatedValues( builder, c, delimiter );

        return builder.toString();

    }

 

 

    //Optional - Utility Methods can be elsewhere

    private static void appendDelimiterSeparatedValues( StringBuilder builder, Collection <? extends Object> c, String delimiter  )

    {

        if( c != null )

        {

            boolean first = true;

            for( Object o : c )

            {

                if(c!=null) {

                    if (first) first = false;

                    else builder.append(delimiter);

                    builder.append(o);

                }

            }

        }

    }

 

    //Optional - Utility Methods can be elsewhere

    public static boolean isBlank( String str )

    {

        int strLen;

        if( str == null || (strLen = str.length()) == 0 )

        {

            return true;

        }

        for( int i = 0; i < strLen; i++ )

        {

            if( !Character.isWhitespace( str.charAt( i ) ) )

            {

                return false;

            }

        }

        return true;

    }

 

    //Optional - Utility Methods can be elsewhere

    public static List<String> delimiterSeparatedValuesToList( String delimitedList, String delimiter )

    {

        String[] array = null;

        if( !isBlank( delimitedList ) )

        {

            array = delimitedList.split( delimiter );

        }

        return array == null ? Collections.<String>emptyList() : Arrays.asList( array );

    }

 

 

    public static String getEmailURI(EncryptedURIMetaData encryptedURIMetaData) throws Exception

    {

        String fullURI = delimiterSeparatedValues(Arrays.asList(encryptedURIMetaData.format,encryptedURIMetaData.authenticated,encryptedURIMetaData.contactID,encryptedURIMetaData.docID),DELIMITER);

        String encrypted = encrypt(fullURI,H_KEY,I_KEY);

        log("Full URI = "+encrypted+" for data = ["+encryptedURIMetaData.toString()+"]",null);

        return encrypted;

    }

 

    public static EncryptedURIMetaData getEmailMetaData(String encrypted) throws Exception

    {

        String fullURI = decrypt(encrypted,H_KEY,I_KEY);

        EncryptedURIMetaData metaData = new EncryptedURIMetaData();

        log("Full URI = "+fullURI+" for data = []",null);

        List<String> consituents = delimiterSeparatedValuesToList(fullURI,DELIMITER);

        if(consituents.size() > 0)

        {

            metaData.format = Integer.valueOf(consituents.get(0));

        }

        if(consituents.size() > 1)

        {

            metaData.authenticated = Integer.valueOf(consituents.get(1));

        }

        if(consituents.size() > 2)

        {

            metaData.contactID = consituents.get(2);

        }

        if(consituents.size() > 3)

        {

            metaData.docID = Long.valueOf(consituents.get(3));

        }

        return metaData;

    }

 

    public static void main(String args[])

    {

        try {

            //Test 1

            EncryptedURIMetaData test = new EncryptedURIMetaData();

            test.docID = 1234567891234567L;

            test.contactID = "smith1234";

            test.format = 0;

            test.authenticated=0;

            String encryptedURI = EncryptUtil.getEmailURI(test);

            EncryptUtil.log("Encrypted = "+encryptedURI,null);

            //now test decryption

            EncryptedURIMetaData response = getEmailMetaData(encryptedURI);

            EncryptUtil.log("Decrypted = "+response,null);

            EncryptUtil.log("------------------",null);

 

            //Test 2

            test = new EncryptedURIMetaData();

            test.docID = 9876543291234567L;

            test.contactID = "126681234";

            test.format = 3;

            test.authenticated=1;

            encryptedURI = EncryptUtil.getEmailURI(test);

            EncryptUtil.log("Encrypted = "+encryptedURI,null);

            //now test decryption

            response = getEmailMetaData(encryptedURI);

            EncryptUtil.log("Decrypted = "+response,null);

 

        }catch(Exception e)

        {

            //proper logging here

            e.printStackTrace(System.out);

        }

    }

 

    public static void log(String msg, String level)

    {

        //replace with proper logging approach

        System.out.println(msg);

    }

 

}

 

class EncryptedURIMetaData

{

    Long docID;

    String contactID;

    int format;

    int authenticated;

 

    @Override

    public String toString()

    {

        return "docID = "+docID+", contactID = "+contactID+", format = "+format+", authenticated = "+authenticated;

    }

}

 