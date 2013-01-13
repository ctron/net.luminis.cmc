
# OSGi Configuration Admin command line client


## Introduction

This bundle provides a command line interface to the standard OSGi Configuration Admin service, and can be used with the command line shell's that are bundled with most popular OSGi frameworks like Apache Felix and Equinox.

The main goal of this bundle, is to provide a simple and convenient interface to the Configuration Admin service that can be used for experimenting with the Configuration Admin service and for setting up a simple configuration quickly. As such, it doesn't have any other dependencies.


## Installation

Just download the bundle and install it in the OSGi framework of your choice. No configuration, no dependencies, just use it!

(Of course, the bundle has a dependency on the org.osgi.service.cm package, but assuming you have a Configuration Admin service installed already, that package will be there too.)


## Usage

This bundle can be used together with the Apache Felix or Equinox command shell.

Provided that one of the command shells listed above is installed in your framework (which usually is the case when you started either Felix or Equinox with its default configuration), just install (and start) this bundle and type cm help on its command line. It will print the help message as shown below, which should speak for itself.

```
Usage:
 cm help                  print this help message
 cm list                  list all known configurations
 cm get <pid>             show configuration for service <pid>
 cm getv <pid>            verbose get (shows value types also)
 cm put <pid> key value   set string value for service <pid>
 cm puts <pid> key value  set "simple" value for service <pid>: value is "true", "false",
                          a char in single quotes, an int, or a number, with appended: 
                          i (Integer), l (Long), f (Float), d (Double), b (Byte), s (Short)
 cm del <pid>             deletes configuration for service <pid>
 cm create <pid> [<loc>]  creates configuration for service <pid> (with optional bundle location)
 cm createf <factoryPid> [<loc>] creates configuration for service factory <factoryPid> (with optional bundle location)
```

### Examples
To set the port of the Apache Felix Http service to 9389, enter

```
cm create org.apache.felix.http
cm put    org.apache.felix.http  org.osgi.service.http.port  9389
```

To set the password for the trustore (string values can contain spaces and other special characters) on Felix:

```
cm put    org.apache.felix.http  org.apache.felix.https.truststore.password  This *is* a "strong" password!
```

and on Equinox:

```
cm put    org.apache.felix.http  org.apache.felix.https.truststore.password  "This *is* a "strong" password!"
```
Note that according to the OSGi specification, neither the service pid nor the key can contain spaces.

### Creation
The Configuration Admin service creates a configuration when someone "gets" it. This command line tool requires you to create a configuration explicitely, as shown in the example above.


## Limitations

* It is not possible to set values of type array.
* On Felix it is not possible to set a string value that starts or ends with spaces.


## Download

* Source can be found on Bitbucket: <https://bitbucket.org/pjtr/net.luminis.cmc/src>.
* You can download release binaries directly from <https://bitbucket.org/pjtr/net.luminis.cmc/downloads>.


##Feedback

Questions, problems, or other feedback? Mail the author (peter.doornbosch) at luminis.eu, or create an issue at <https://bitbucket.org/pjtr/net.luminis.cmc/issues>.
