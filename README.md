
# OSGi Configuration Admin command line client

**Note**: This is a fork of <https://bitbucket.org/pjtr/net.luminis.cmc>. It differs significantly from the original
version. Providing the same functionality though.

## Introduction

This bundle provides a command line interface to the standard OSGi Configuration Admin service, and can be used with the GoGo command line shell.

The main goal of this bundle, is to provide a simple and convenient interface to the Configuration Admin service that can be used for experimenting with the Configuration Admin service and for setting up a simple configuration quickly.

## Installation

Just download the bundle and install it in the OSGi framework of your choice. No configuration, the only dependency necessary is the GoGo shell.

Of course, the bundle has a dependency on the org.osgi.service.cm package, but assuming you have a Configuration Admin service installed already, that package will be there too.

## Usage

This bundle can be used together with the GoGo command shell.

Provided that one of the command shells listed above is installed in your framework (which usually is the case when you started either Felix or Equinox with its default configuration), just install (and start) this bundle and type `help -scope cm` on its command line.


The following is just a brief overview of the available commands:

~~~
createf - creates configuration for service factory
get     - show configuration for service
delete  - deletes configuration for service
clear   - removes key/value from configuration
list    - list all known configurations
create  - creates configuration for service
put     - set string value for service
~~~

You can execute `help cm:<command>` in order to get detailed information about a command:

~~~
osgi> help cm:create

create - creates configuration for service
   scope: cm
   parameters:
      String   The PID of the configuration
      String   The location. Use '?' for any.

create - creates configuration for service
   scope: cm
   parameters:
      String   The PID of the configuration

~~~

### Examples

To set the port of the Apache Felix Http service to 9389, enter

~~~
cm:create org.apache.felix.http
cm:put    -t i org.apache.felix.http  org.osgi.service.http.port 9389
~~~

To set the password for the truststore (string values can contain spaces and other special characters) on Felix:

~~~
cm:put    org.apache.felix.http  org.apache.felix.https.truststore.password  "This *is* a \"strong\" password!"
~~~

By default the `cm:put` command uses `String` as a type. However you can specify the type using the `-t`
parameter (or `--type`). The following types are available:

* `string` (default)
* `b`, `bool`, `boolean`
* `byte`
* `short`
* `i`, `int`, `integer`
* `l`, `long`
* `f`, `float`
* `d`, `double`
* `c`, `char`, `character`



**Note:** that according to the OSGi specification, neither the service pid nor the key can contain spaces.

### Creation

The Configuration Admin service creates a configuration when someone "gets" it. This command line tool requires you to create a configuration explicitly, as shown in the example above.

**Note:** The default location is `?`, which is the wildcard option and the recommended way to do things now in OSGi:

> Locations were a mistake in the Configuration Admin API. Ok, I’ve said it. They were a failed attempt to provide security at an unsuitable place. Mea culpa …
>
> [org.osgi.service.cm.html#locations][1]

[1]: http://enroute.osgi.org/services/org.osgi.service.cm.html#locations

If you insist on setting a "null" location, you can do so using the `-n` (`--null-location`) switch.

## Limitations

* ~~It is not possible to set values of type array.~~ – This can be done by using multiple arguments to the `cm:put` command. Of course all array element must have the same type.
* ~~On Felix it is not possible to set a string value that starts or ends with spaces.~~ This is possible be using quotes (`"`). If you want to use quotes as part of the string value, you need to escape them with `\`. (e.g. `"\""` -> `"`).

## Download

* Source can be found on GitHub: <https://github.com/ctron/net.luminis.cmc>
  * The original source can be found on Bitbucket: <https://bitbucket.org/pjtr/net.luminis.cmc/src>.
* The released binaries are available from [GitHub releases](https://github.com/ctron/net.luminis.cmc/releases), starting with version 0.5.0 or from Maven Central `de.dentrassi.osgi:net.luminis.cmc`.
  * The original released binaries can be downloaded from <https://bitbucket.org/pjtr/net.luminis.cmc/downloads>.

## Feedback

You can use GitHub issues or contact me (@ctron) for questions, problems and other feedback.

**Note:** The following is the original "Feedback" section. I will keep it in as a reference. But please do not contact the original author complaining about
changes made in this repository. Thanks!

Questions, problems, or other feedback? Mail the author (peter.doornbosch) at luminis.eu, or create an issue at <https://bitbucket.org/pjtr/net.luminis.cmc/issues>.
