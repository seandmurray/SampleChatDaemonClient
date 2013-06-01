SampleChatDaemonClient
======================

A small chat daemon (works with telnet) or with the sample client included.

This small chat application was built in a few hours as a code sample.

What id does do.
It will allow a limited (configurable) number of client to connect to it.
It will can be deployed on any valid port.
You can connect to it using Telnet or using the included client.
Anything that any client sends to the server is displayed to all the other clients and it is echoed back to the original client.

What id does not do.
It is not secure; there are no passwords, all data is transmitted without encryption.
It is fine for perhaps a dozen of users and that is about it, it is a sample after all.

Other items of note.
It does include unit test.
The code was built originally for java 1.5 but works for 1.7.
The unit test were built with the following libraries:
cglib-nodep-2.2.jar
easymock-2.5.2.jar
easymockclassextension-2.5.2.jar
objenesis-tck-1.2.jar
