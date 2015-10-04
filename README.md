## OsgiWorlds

OsgiWorlds is a set of Java files to allow a Vaadin OSGi application running on a Domino server running OpenNTF Domino API (release 2.0.0 +). There is a configuration file that allows production mode to pull from a setting in the web.xml to define the current user.

The application expects org.openntf.domino.xsp plugin to be running on the server. It requires at least version 2.0.0 - there is additional functionality for checking whether ODAPlatform is started (it should be automatically as part of XPages runtime).

## Detailed Explanation of Packages

The org.openntf.osgiworlds package provides four files:
- **ApplicationConfiguration.java** is an interface defining variables expected in the web.xml for signer, default developmentMode user and session identity. It also defines the contract for certain methods expected  by any implementation of the interface.
- **BaseApplicationConfigurator** is an implementation of the ApplicationConfiguration interface. This adds some basic implementations of methods, but will need extending for any actual configuration.
- **DefaultDominoApplicationConfig** is a default, working configurator, adding devMode, user, signer and server org.openntf.domino.Sessions.
- **ODA_VaadinServlet** is an extension to VaadinServlet that wraps a Domino and ODA session for every HTTPServletRequest.

The org.openntf.osgiworlds.model package contains Domino-specific wrappers for Vaadin, e.g. Calendar, ViewEntry.

The com.timtripcony package is based on Tim Tripcony's SmartDocumentModel code demoed on NotesIn9. It has also been extended to provide additional functionality like using case insensitive checks on field names, to support historical data where LotusScript did not require case sensitivity when interacting with the document.

## Setup

1. Copy the Java files into your OSGi application's source code folder. This assumes your OSGi application already includes all Vaadin jar files required.
2. In your WebContent\WEB-INF\web.xml file, the following configuration can be used:
  - A context-param with the name "osgiworlds.developermode" and the value "true" should be added to set developerMode.
  - A context-param with the name "org.openntf.osgiworlds.devtimename" and a value mapping to a valid, hierarchical Notes Name (e.g. "CN=Mickey Mouse/O=Disney") should be added to define a specific developer name to use.
  - In the servlet definition, the servlet-class should map to the ODA_VaadinServlet class or any extension you use.
  So the relevant settings might look like:<br/>
  &lt;context-param&gt;<br/>
    	&lt;param-name&gt;osgiworlds.developermode&lt;/param-name&gt;<br/>
    	&lt;param-value&gt;true&lt;/param-value&gt;<br/>
    &lt;/context-param&gt;<br/>
	&lt;context-param&gt;<br/>
		&lt;param-name&gt;org.openntf.osgiworlds.devtimename&lt;/param-name&gt;<br/>
		&lt;param-value&gt;CN=Micky Mouse/O=Disney&lt;/param-value&gt;<br/>
	&lt;/context-param&gt;<br/>
	&lt;servlet&gt;<br/>
    &lt;servlet-name&gt;KeyDatesServlet&lt;/servlet-name&gt;<br/>
    &lt;servlet-class&gt;org.openntf.osgiworlds.ODA_VaadinServlet&lt;/servlet-class&gt;<br/>
    &lt;init-param&gt;<br/>
      &lt;param-name&gt;UI&lt;/param-name&gt;<br/>
      &lt;param-value&gt;uk.co.intec.keyDatesApp.MainUI&lt;/param-value&gt;<br/>
    &lt;/init-param&gt;<br/>
  &lt;/servlet&gt;
3. In the plugin.xml, org.openntf.domino and org.openntf.domino.xsp will need to be added as required plugins.

You should then be good to go.

## Using with CrossWorlds
CrossWorlds already includes all the relevant HTTPServletWrappers. For CrossWorlds, developermode is set at server level. The web.xml configuration for devtime name are virtually identical, just **org.openntf.crossworlds.devtimename**. The com.timtripcony and org.openntf.osgiworlds.model packages can be copied from here into a CrossWorlds Vaadin app to add those model classes. No changes to the packages is required.

## Pull Requests
If you add additional functionality or build additional model classes you would like to contribute back, please do so. The aim of this project is to make it easier for Domino developers to use Vaadin in OSGi applications or web applications on Websphere Liberty running CrossWorlds.
