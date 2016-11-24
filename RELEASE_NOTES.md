protoc-jar-maven-plugin release notes
=====================================

#### 3.1.0.2 (24-Nov-2016)
* Support binary artifact download - for protoc binary, protoc plugins (issue #27)
* Make "protocVersion" optional, default to latest (currently 3.1.0) (issue #22)

#### 3.1.0.1 (27-Oct-2016)
* Upgrade Linux 3.1.0 binary to protoc 3.1.0-build2 (issue #16)

#### 3.1.0 (8-Oct-2016)
* Upgrade to protoc 3.1.0
* Map 3.0.0 to 3.1.0 for backward compatibility

#### 3.0.0.1 (28-Aug-2016)
* Package and support google.protobuf standard types out of the box (parameter "includeStdTypes")
* Support output directory suffix (parameter "outputDirectorySuffix")
* Extract protoc only once per execution (not for each file)

#### 3.0.0 (2-Aug-2016)
* Upgrade to protoc 3.0.0
* Support output options parameter ("outputOptions")

#### 3.0.0-b4 (27-Jul-2016)
* Upgrade to protoc 3.0.0-beta-4
* Support protoc plugin path parameter ("pluginPath")

#### 3.0.0-b3 (18-May-2016)
* Upgrade to protoc 3.0.0-beta-3
* Support shading of generated code for use with `protobuf-java-shaded-[241|250|261]` (type java-shaded)

#### 3.0.0-b2.1 (16-Mar-2016)
* Process proto files in input directory recursively
* Include protoc-jar fix for text file busy issue (reported for Ubuntu 14.04)

#### 3.0.0-b2 (11-Jan-2016)
* Upgrade to protoc 3.0.0-beta-2
* Supports proto3 Javascript type (js)

#### 3.0.0-b1 (17-Oct-2015)
* Upgrade to protoc 3.0.0-beta-1
* All 3.0.0-beta-1 binaries from Google (maven central)

#### 3.0.0-a3 (15-Jul-2015)
* Single package to include all protoc versions
* New parameter "protocVersion" to select version
* Add support for protoc 3.0.0-alpha-3
* Support proto3 output types (javanano, csharp, objc, ruby)

#### 2.4.1.4, 2.5.0.4, 2.6.1.4 (27-Mar-2015)
* Support all protoc output targets: java, cpp, python, descriptor
* Support multiple output targets in one execution
* Switch "cleanOutputFolder" default to false

#### 2.4.1.3, 2.5.0.3, 2.6.1.3 (31-Dec-2014)
* Rebuilt Linux binaries on older toolchain for better portability

#### 2.4.1.2, 2.5.0.2, 2.6.1.2 (30-Dec-2014)
* Add support for protoc 2.6.1
* Refactored into branches, retired builder pom.xml

#### 2.4.1.1, 2.5.0.1 (12-Jun-2014)
* Fix for Eclipse, include m2e/lifecycle-mapping-metadata.xml

#### 2.4.1.0, 2.5.0.0 (9-Jun-2014)
* Initial release, support protoc 2.4.1 and 2.5.0
