SampleChatDaemonClient
======================

[2010] - [2014] SEAN D. MURRAY All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property
of SEAN D. MURRAY.

A small chat daemon (works with telnet) or with the sample client
included.

This small chat application was built in a few hours as a code sample.

Start/Main for Daemon is: com.sdm.chat.daemon.ChatServer.  Start/Main for
Client is: com.sdm.chat.client.Client.

What it does do.  It will allow a limited (configurable) number of
clients to connect to it.  It will can be deployed on any valid port.
You can connect to it using Telnet or using the included client.
Anything that any client sends to the server is displayed to all the
other clients and it is echoed back to the original client.

What it does not do.  It is not secure; there are no passwords, all data
is transmitted without encryption.  It is fine for perhaps a dozen users
and that is about it, it is a sample after all.

Other items of note.  It does include unit tests.  The code was built
originally for java 1.5 but works for 1.7. The code works with standard
java libraries only the unit tests require additional libraries.

The unit tests were built with the following libraries:
cglib-nodep-2.2.3.jar easymock-3.2.jar objenesis-tck-2.1.jar

.classpath and .project files are included for the Eclipse IDE which
assumes that java libs are external libs. You will prob have to change
where these references point to.
