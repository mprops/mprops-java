# Multiline properties format for Java
[![Build Status](https://travis-ci.org/mprops/mprops-java.svg?branch=master)](https://travis-ci.org/mprops/mprops-java)
[![Coverage Status](https://coveralls.io/repos/github/mprops/mprops-java/badge.svg)](https://coveralls.io/github/mprops/mprops-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mprops/mprops/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mprops/mprops)
[![Intellij IDEA plugin](https://img.shields.io/badge/plugin-Intellij%20IDEA-blue.svg)](https://github.com/mprops/mprops-idea)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Format
<pre>
<i>Any text before the first key
is considered as a comment</i>
<b>~ key1. Keys are 1-line strings started with '~'. Leading/trailing whitespaces are removed</b>
Multiline
Value1
<b>~ key2</b>
Multiline
Value2
</pre>

## Usage
```java
Map<String, String> properties = new MPropsParser().parse(text);
```
or
```java
Map<String, String> properties = new MPropsParser().parse(new FileReader("path-to-file"));
```
or with a streaming
```java
new MPropsParser().parse(new FileReader("path-to-file"), new BiConsumer<String, String>(){...});
```

### Using custom key token
```java                                  
// All keys now starts with '>' token: example: "> key \n value".
MPropsParser parser = new MPropsParser(">");
```

### Escaping values
To escape any value use starting ' ' (space) character on any value line that starts with key token.
This extra leading space will be removed (not-returned) by parser. 

## Maven
```xml
    <dependency>
        <groupId>com.github.mprops</groupId>
        <artifactId>mprops</artifactId>
        <version>1.1.0</version>
    </dependency>
```
