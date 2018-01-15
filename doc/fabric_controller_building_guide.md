# Fabric Controller Building Guide
Version 1.0

December.26.2017

NTT Confidential

Copyright (c) 2017 NTT corp. All Rights Reserved.

## Table of Contents

- [*1．Overview*](#1-overview)

  - [*1.1 Necessary software and library*](#11-necessary-software-and-library)

- [*2．Build method of FC*](#2-build-method-of-fc)

  - [*2.1 Working environment*](#21-working-environment)

    - [*2.1.1 Set Java library and Apache Ant*](#211-set-java-library-and-apache-ant)

      - [*2.1.1.1 JDK installation*](#2111-jdk-installation)

      - [*2.1.1.2 Apache Ant installation*](#2112-apache-ant-installation)

      - [*2.1.1.3 Set Gson library*](#2113-set-gson-library)

      - [*2.1.1.4 Set Jetty library*](#2114-set-jetty-library)

      - [*2.1.1.5 Set JDBC library*](#2115-set-jdbc-library)

      - [*2.1.1.6 Set Apache Commons IO library*](#2116-set-apache-commons-io-library)

      - [*2.1.1.7 Set Apache Commons Lang library*](#2117-set-apache-commons-lang-library)

      - [*2.1.1.8 Set Apache Commons Collections library*](#2118-set-apache-commons-collections-library)

      - [*2.1.1.9 Set Hibernate library*](#2119-set-hibernate-library)

      - [*2.1.1.10 Set Jersey library*](#21110-set-jersey-library)

      - [*2.1.1.11 Set Log4J library*](#21111-set-log4j-library)

  - [*2.2 Build execution environment and procedure of FC*](#22-build-execution-environment-and-procedure-of-fc)

    - [*2.2.1 Build execution environment*](#221-build-execution-environment)

    - [*2.2.2 Build execution procedure*](#222-build-execution-procedure)  


## Revision History  
|Version   |Date            |Contents        |
|:---------|:---------------|:---------------|
|1.0       |Dec. 26, 2017   |First edition   |

## 1. Overview

This manual describes how to build the Fabric controller (FC) (hereinafter it is referred to as “FC”)
from source and generate a binary for FC installation\*.  
\* Binary for FC installation is a compressed tar file of necessary files for FC installation.

### 1.1 Necessary software and libraries

Necessary software and libraries to build FC from source are shown in Table 1-1.  
Use particular version of software which listed on Table 1-1 for Java
libraries excluding JAXB.

**Table 1-1 Necessary software and libraries**

|No.  |Item        |Software                  |Version|Software is available from|
|:----|:-----------|:-------------------------|:------|:-------------------------|
|1    |Middleware  |Java                      |Oracle JDK 8 <br> Update 101 or later (Oracle JDK 9 or later is not applicable) |<http://www.oracle.com/> <br> Download file: jdk-8u101-linux-x64.rpm|
|2    |Build tool  |Apache Ant                |1.10.1 |<http://ant.apache.org/bindownload.cgi> <br> Download file: apache-ant-1.10.1-bin.tar.gz|
|3    |Java library|Jetty                     |9.3.11|<http://archive.eclipse.org/jetty/9.3.11.v20160721/dist/> <br> Download file:jetty-distribution-9.3.11.v20160721.tar.gz
|     |            |Gson                      |2.7|<https://repo1.maven.org/maven2/com/google/code/gson/gson/2.7/> <br> Download file:gson-2.7.jar
|     |            |JAXB                      |2.2.8|It is included in Java.
|     |            |Jersey                    |2.23.2|<http://repo1.maven.org/maven2/org/glassfish/jersey/bundles/jaxrs-ri/2.23.2/> <br>Download file:jaxrs-ri-2.23.2.tar.gz
|     |            |Hibernate                 |5.0.10|<https://sourceforge.net/projects/hibernate/files/hibernate-orm/5.0.10.Final/hibernate-release-5.0.10.Final.tgz/download> <br> Download file:hibernate-release-5.0.10.Final.tgz|
|     |            |SLF4J                     |1.6.1|<https://sourceforge.net/projects/unirods/files/lib/slf4j-nop-1.6.1.jar/download> <br>Download file:slf4j-nop-1.6.1.jar <br> \* Additional library for Hibernate
|     |            |Log4J                     |2.6.2|<http://archive.apache.org/dist/logging/log4j/2.6.2> <br> Download file:apache-log4j-2.6.2-bin.tar.gz
|     |            |Apache Commons IO         |2.5|<https://commons.apache.org/proper/commons-io/download_io.cgi> <br> Download file:commons-io-2.5-bin.tar.gz
|     |            |Apache Commons Lang       |2.6|<https://commons.apache.org/proper/commons-lang/download_lang.cgi> <br> Download file:commons-lang-2.6-bin.tar.gz
|     |            |Apache Commons Collections|3.2.2|<https://commons.apache.org/proper/commons-collections/download_collections.cgi> <br> Download file:commons-collections-3.2.2-bin.tar.gz
|     |            |JDBC                      |9.4.1209|<https://jdbc.postgresql.org/download.html> <br> Download file:postgresql-9.4.1209.jar

## 2. Build method of FC

FC is built using Apache Ant build tool.

### 2.1 Working environment

Working machine is hereinafter referred to as working server* in this manual.
This manual refers to a working user as general user “msfctrl”.
Replace these with appropriate machine and user based on your environment.    

\* Use general OS(Linux) for OS of working server. This manual uses CentOS7 (1511) to describe procedure.

#### 2.1.1 Set Java libraries and Apache Ant

This section gives the instructions for JDK installation, Apache Ant installation and preparation of Java libraries.

In the example below, Java libraries are placed under “\~/java\_lib/”.  

Directory configuration under “\~/java\_lib/” is shown below.

```
/home/msfctrl               Home directory for working user
└-- java_lib                Preparation directory for Java library
    ├-- gson                Store directory for Gson library
    ├-- jetty               Store directory for Jetty library
    ├-- postgresql          Store directory for JDBC library
    ├-- apache-commons      Store directory for Apache Commons library
    ├-- hibernate           Store directory for Hibernate library
    ├-- jersey              Store directory for Jersey library
    └-- log4j               Store directory for Log4J library
```

##### 2.1.1.1 JDK installation

(1) Download rpm file of JDK from <http://www.oracle.com/>.  
    \* In the example below, the downloaded rpm file is referred to as
    “jdk-8u101-linux-x64.rpm”.  
    \* Use JDK 8 update 101 or later version. However, JDK 9 or later
    version is not applicable for current FC server.

(2) Set the downloaded rpm file to the arbitrary location on FC server.  
    In the example below, the rpm file is set under “\~/rpm/”.

(3) Change the working directory to the rpm directory and install JDK.

~~~console
$ cd ~/rpm/
$ sudo rpm -ivh jdk-8u101-linux-x64.rpm
~~~

(4) Set Java version as default.  
    \* Select a number of /usr/java/jdk1.8.0\_101/jre/bin/java.

~~~console
$ sudo alternatives --config java
~~~

##### 2.1.1.2 Apache Ant installation

(1)	Download apache-ant-1.10.1-bin.tar.gz from <http://ant.apache.org/bindownload.cgi>.

(2)	Set the downloaded apache-ant-1.10.1-bin.tar.gz to the arbitrary location on FC server.  
    In the example below, the file is set under “~/download/”.

(3)	Unzip apache-ant-1.10.1-bin.tar.gz and place the unzipped directory under “/usr/share/”
    with a directory name of ant.

~~~console
$ cd ~/download/
$ tar xvfz apache-ant-1.10.1-bin.tar.gz
$ sudo mv apache-ant-1.10.1 /usr/share/ant
~~~

(4)	Place the link of ant command under “/usr/bin/” to make it available.

~~~console
$ sudo ln -s /usr/share/ant/bin/ant /usr/bin/ant
~~~

(5)	Make sure that ant command can be executed at the arbitrary location.
    The installation is successful if the version of Apache Ant is displayed.

~~~console
$ ant -version
Apache Ant(TM) version 1.10.1 compiled on February 2 2017
~~~

##### 2.1.1.3 Set Gson library

(1) Download gson-2.7.ja from
    <https://repo1.maven.org/maven2/com/google/code/gson/gson/2.7/>.

(2) Set the downloaded gson-2.7.jar to the arbitrary location on FC server.  
    In the example below, the file is set under “\~/download/”.

(3) Place gson-2.7.jar under “\~/java\_lib/gson/”.

~~~console
$ cd ~/download/
$ mv gson-2.7.jar ~/java_lib/gson/
~~~

##### 2.1.1.4 Set Jetty library

(1) Download jetty-distribution-9.3.11.v20160721.tar.gz from
    <http://archive.eclipse.org/jetty/9.3.11.v20160721/dist/>.

(2) Set the downloaded jetty-distribution-9.3.11.v20160721.tar.gz to the arbitrary location on FC server.  
    In the example below, the file is set under “\~/download/”.

(3) Unzip jetty-distribution-9.3.11.v20160721.tar.gz and place a lib
    directory in the unzipped directory under “\~/java\_lib/jetty/”.

~~~console
$ cd ~/download/
$ tar xvfz jetty-distribution-9.3.11.v20160721.tar.gz
$ cd jetty-distribution-9.3.11.v20160721/
$ mv lib ~/java_lib/jetty/
~~~

##### 2.1.1.5 Set JDBC library

(1) Download postgresql-9.4.1209.jar from
    <https://jdbc.postgresql.org/download.html>.

(2) Set the downloaded postgresql-9.4.1209.jar to the arbitrary location on FC server.  
    In the example below, the file is set under “\~/download/”.

(3) Set postgresql-9.4.1209.jar under “\~/java\_lib/postgresql”.

~~~console
$ cd ~/download/
$ mv postgresql-9.4.1209.jar ~/java_lib/postgresql/
~~~

##### 2.1.1.6 Set Apache Commons IO library

(1) Download commons-io-2.5-bin.tar.gz from
    <https://commons.apache.org/proper/commons-io/download_io.cgi>.

(2) Set the downloaded commons-io-2.5-bin.tar.gz to the arbitrary location on FC server.  
    In the example below, the file is set under “\~/download/”.

(3) Unzip commons-io-2.5-bin.tar.gz and place commons-io-2.5.jar
    in the unzipped directory under “\~/java\_lib/apache-commons/”.

~~~console
$ cd ~/download/
$ tar xvfz commons-io-2.5-bin.tar.gz
$ cd commons-io-2.5/
$ mv commons-io-2.5.jar ~/java_lib/apache-commons/
~~~

##### 2.1.1.7 Set Apache Commons Lang library

(1) Download commons-lang-2.6-bin.tar.gz from
    <https://commons.apache.org/proper/commons-lang/download_lang.cgi>.

(2) Set the downloaded commons-lang-2.6-bin.tar.gz to the arbitrary location on FC server.  
    In the example below, the file is set under “\~/download/”.

(3) Unzip commons-lang-2.6-bin.tar.gz and place commons-lang-2.6.jar
    in the unzipped directory under “\~/java\_lib/apache-commons/”.

~~~console
$ cd ~/download/
$ tar xvfz commons-lang-2.6-bin.tar.gz
$ cd commons-lang-2.6/
$ mv commons-lang-2.6.jar ~/java_lib/apache-commons/
~~~

##### 2.1.1.8 Set Apache Commons Collections library

(1) Download commons-collections-3.2.2-bin.tar.gz from
    <https://commons.apache.org/proper/commons-collections/download_collections.cgi>.

(2) Set the downloaded commons-collections-3.2.2-bin.tar.gz to the arbitrary location on FC server.  
    In the example below, the file is set under “\~/download/”.

(3) Unzip commons-collections-3.2.2-bin.tar.gz and place commons-collections-3.2.2.jar
    in the unzipped directory under “\~/java\_lib/apache-commons/”.

~~~console
$ cd ~/download/
$ tar xvfz commons-collections-3.2.2-bin.tar.gz
$ cd commons-collections-3.2.2/
$ mv commons-collections-3.2.2.jar ~/java_lib/apache-commons/
~~~

##### 2.1.1.9 Set Hibernate library

Prepare a main unit and additional library for Hibernate library.

(1) Download hibernate-release-5.0.10.Final.tgz from
    <https://sourceforge.net/projects/hibernate/files/hibernate-orm/5.0.10.Final/hibernate-release-5.0.10.Final.tgz/download>.

(2) Set the downloaded hibernate-release-5.0.10.Final.tgz to the arbitrary location on FC server.  
    In the example below, the file is set under “\~/download/”.

(3) Unzip hibernate-release-5.0.10.Final.tgz and place a lib directory  
    in the unzipped directory under “\~/java\_lib/hibernate/”.

~~~console
$ cd ~/download/
$ tar xvfz hibernate-release-5.0.10.Final.tgz
$ cd hibernate-release-5.0.10.Final/
$ mv lib ~/java_lib/hibernate/
~~~

(4) Add a necessary library to Hibernate.  
    Download slf4j-nop-1.6.1.jar from
    <https://sourceforge.net/projects/unirods/files/lib/slf4j-nop-1.6.1.jar/download>.

(5) Set the downloaded file to the arbitrary location on FC server.  
    In the example below, the file is set under “\~/download/”.

(6) Set slf4j-nop-1.6.1.jar under “\~/java\_lib/hibernate/lib/optional/ehcache”.

~~~console
$ cd ~/download/
$ mv slf4j-nop-1.6.1.jar ~/java_lib/hibernate/lib/optional/ehcache/
~~~

##### 2.1.1.10 Set Jersey library

(1) Download jaxrs-ri-2.23.2.tar.gz from  
    <http://repo1.maven.org/maven2/org/glassfish/jersey/bundles/jaxrs-ri/2.23.2/>.

(2) Set the downloaded jaxrs-ri-2.23.2.tar.gz to the arbitrary location on FC server.  
    In the example below, the file is set under “\~/download/”.

(3) Unzip jaxrs-ri-2.23.2.tar.gz and place all files and directories
    in the unzipped directory under “\~/java\_lib/jersey/”.

~~~console
$ cd ~/download/
$ tar xvfz jaxrs-ri-2.23.2.tar.gz
$ cd jaxrs-ri/
$ mv * ~/java_lib/jersey/
~~~

##### 2.1.1.11 Set Log4J library

(1) Download apache-log4j-2.6.2-bin.tar.gz from
    <http://archive.apache.org/dist/logging/log4j/2.6.2>.

(2) Set the downloaded apache-log4j-2.6.2-bin.tar.gz to the arbitrary location on FC server.  
    In the example below, the file is set under “\~/download/”.

(3) Unzip apache-log4j-2.6.2-bin.tar.gz and
    place log4j-1.2-api-2.6.2.jar, log4j-api-2.6.2.jar, log4j-core-2.6.2.jar
    in the unzipped directory under “\~/java\_lib/log4j/”.

~~~console
$ cd ~/download/
$ tar xvfz apache-log4j-2.6.2-bin.tar.gz
$ cd apache-log4j-2.6.2-bin/
$ mv log4j-1.2-api-2.6.2.jar ~/java_lib/log4j/
$ mv log4j-api-2.6.2.jar ~/java_lib/log4j/
$ mv log4j-core-2.6.2.jar ~/java_lib/log4j/
~~~

### 2.2 Build environment and procedure of FC

Build environment and procedure of FC are described below.

#### 2.2.1 Build environment

Build environment (main files and store directories for libraries) is shown below. This environment can be prepared by unzipping the FC source store tar file (msf-controller-src.tar.gz) to working directory for build.
Working directory for build is referred to as “~/msf-controller/” in this manual.



Working directory for build is referred to as “~/msf-controller/” in this manual.

```
/home/msfctrl                       Home directory for working user
└-- msf-controller                  Working directory for build
    ├-- src                         Store directory for FC source
    |  └-- msf                      Main directory for FC source
    ├-- conf                        Configuration directory
    |  ├-- fc_system.xml            FC system setting configuration
    |  ├-- fc_data.xml              FC initial setting configuration
    |  ├-- fc_develop.xml           FC internal operability setting configuration
    |  ├-- fc_log4j2.xml            Log4J configuration
    |  └-- hibernate.cfg.xml        Hibernate configuration
    ├-- script                      Store directory for FC startup/shutdown script
    |  ├-- fc_ctl.sh                FC startup/shutdown script main unit
    |  ├-- mfcfc_ctl.sh             FC startup/shutdown script library
    |  └-- fc                       Resource agent for FC
    ├-- lib                         FC main unit and store directories for libraries *1
    |   ├-- gson                    Store directory for Gson library
    |   ├-- jetty                   Store directory for Jetty library
    |   ├-- postgresql              Store directory for JDBC library
    |   ├-- apache-commons          Store directory for Apache Commons IO library
    |   ├-- hibernate               Store directory for Hibernate library
    |   ├-- jersey                  Store directory for Jersey library
    |   └-- log4j                   Store directory for Log4J library
    ├-- sql                         Store directory for FC DB table schema
    |  └-- msf_fc.sql               FC DB table schema
    ├-- .classpath                  Class path
    ├-- build.xml                   Build file
    ├-- classes                     Store directory for class file *2
    └-- release                     Store directory for release file *3
```
\*1. Place the libraries which are prepared in section [*2.1.1*](#211-set-java-library-and-apache-ant).    
\*2. Directory for storing the output of compiling FC source.
     This directory is automatically generated by build.  
\*3. Directory for storing the output files of building FC (binary for FC installation).
     This directory is automatically generated by build.

#### 2.2.2 Build procedure

(1) Make sure that no files or directories exist under working directory for build. Delete all the files and directories, if any.

(2)	Download the FC source store tar file (msf-controller-src.tar.gz) from  <https://github.com/multi-service-fabric/fabric-controller/****>.

(3) Set the FC source store tar file (msf-controller-src.tar.gz) to the working directory for build.

(4) Unzip the FC source store tar file.  

~~~console
$ cd ~/msf-controller/
$ tar xvfz msf-controller-src.tar.gz
~~~

(5) Move the Java libraries to working directory for build.
    Change the directory name from “java_lib” to “lib”.

~~~console
$ mv ~/java_lib/ ~/msf-controller/lib/
~~~

(6) Build to generate binary for FC installation.

~~~console
$ cd ~/msf-controller/
$ ant
~~~

(7) Make sure that binary for FC installation is generated.
    File name of generated binary for FC installation is “msf-controller.tar.gz”.

~~~console
$ cd ~/msf-controller/release
$ ls
msf-controller.tar.gz
~~~
