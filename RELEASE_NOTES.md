protoc-jar-maven-plugin release notes
=====================================

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
