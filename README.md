# Multiline properties format for Java
[![Build Status](https://travis-ci.org/mprops/mprops-java.svg?branch=master)](https://travis-ci.org/mprops/mprops-java)
[![Coverage Status](https://coveralls.io/repos/github/mprops/mprops-java/badge.svg?branch=master)](https://coveralls.io/github/mprops/mprops-java?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mprops/mprops/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mprops/mprops)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Format:
<pre>
<b>~ key1 (leading and trailing whitespaces are trimmed, this text in parens is a part of the key!)</b>
Multiline
Property
Value1
<b>~ key2</b>
Multiline
Property
Value2
</pre>

## Usage:
```java
Map<String, String> properties = new MPropsParser().parse(text);
```
or
```java
Map<String, String> properties = new MPropsParser().parse(new FileReader("path-to-file"));
```


## Maven
```xml
    <dependency>
        <groupId>com.github.mprops</groupId>
        <artifactId>mprops</artifactId>
        <version>1.0.0</version>
    </dependency>
```