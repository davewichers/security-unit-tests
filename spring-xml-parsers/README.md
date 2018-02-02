# Spring XML Parsers

This is a project created to test parsers provided by the Spring Framework for XXE.

Spring Boot was used to jumpstart development, but Spring Framework 5.0.3
libraries are overridden in the pom.xml.

The goal of the project is to create endpoints which will accept XML
input and parse it. Each endpoint is configured to use a different
method for parsing. The endpoints will be requested by the parent project.

Currently the following endpoints are set up:
- Spring StaxUtils createDefensiveInputFactory()
- Spring OXM CastorMarshaller
- Spring OXM Jaxb2Marshaller
- Spring OXM XStreamMarshaller

A JibxMarshaller also exists in the Spring OXM library, but
troubles were encountered while attempting to configure it.

The following options are available for server configuration:
- address
- port

These options are configurable through src/main/resources/application.config

Additionally, these configuration options will be set by the setExternalSpringConfig.sh
shell script in the parent project.
