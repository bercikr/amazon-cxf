# Introduction #
All Product Advertising API developers will be required to authenticate all calls to the Product Advertising API by August 15, 2009.

See the [Developer's Guide](http://docs.amazonwebservices.com/AWSECommerceService/latest/DG/index.html?RequestAuthenticationArticle.html) for more information. In this case we need to sign the SOAP requests.

To sign SOAP requests in CXF I used the following resources:

[Amazon SOAP Product Advertising API from Java – Including Signing of Requests with WS-Security](http://www.mularien.com/blog/2009/08/13/tutorial-amazon-soap-product-advertising-api-from-java-including-signing-of-requests-with-ws-security/)

# Create a new X.509 Certificate from Amazon #
  * Log into your AWS account and navigate to the “Access Identifiers” section.
  * Create a new X.509 certificate
  * Download both the public and private keys. Note that the public certificate filename starts with “cert-” and the private key starts with “pk-”.

# Create a P12 file from both the public and private keys #
You'll need OpenSSL which is very likely included with Linux and Mac, or you can download a [Windows version](http://www.slproweb.com/products/Win32OpenSSL.html). Replace the cert-XXX.pem and pk-XXX.pem with your own filenames.

```
openssl pkcs12 -export -name amaws -out amazon_ws.p12 -in cert-XXX.pem -inkey pk-XXX.pem
```

# Add amazon-ws.p12 to the project resources #
For security reasons I'm not including amazon-ws.p12 as part of the sources as you need to use your own.

Save it in src/main/resources/jks and also be sure to update the keystore.alias and keystore.password properties in the [Maven Settings](HowToRunUnitTests.md)