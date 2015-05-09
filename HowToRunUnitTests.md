# Introduction #

Amazon-CXF contains several JUnit test cases that can be used as examples on how to use the library. However some steps need to be followed before trying to run them.


# Details #

Since you need a username/password in order to call the Amazon Web Services it wasn't possible to include them in the repository.

You can create your own AWS account by visiting http://aws.amazon.com/

## Changes to you local Maven Settings ##

Add the following to your HOME/.m2/settings.xml right before `</settings>` and replace the appropriate values. If you don't use a proxy then leave cxf.proxy as just <cxf.proxy></cxf.proxy>.

See the [Signing Requests](SigningRequests.md) page for information on keystore.alias and keystore.password

```
<activeProfiles>
	<activeProfile>amazon-cxf-default</activeProfile>
</activeProfiles>

<profiles>
	<profile>
		<id>amazon-cxf-default</id>
			<properties>
				<awsAccessKeyId>1Y8NC1MDWFVG8XQ35HR2</awsAccessKeyId>
				<associateTag>supervacacom-20</associateTag>
				<cxf.proxy>ProxyServerType="HTTP" ProxyServer="10.156.133.118" ProxyServerPort="80"</cxf.proxy>
				<keystore.alias>myAlias</keystore.alias>
				<keystore.password>myPassword</keystore.password>
		</properties>
	</profile>
</profiles>
```