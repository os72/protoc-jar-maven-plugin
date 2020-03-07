protoc-jar-maven-plugin release notes
=====================================

#### 3.11.4 (7-Mar-2020)
* Upgrade to protoc 3.11.4
* Preserve native plugin artifact extension (issue #89)
* Expose option "includeImports" (only applies to type "descriptor") (issue #88)
* Shading fix for version 3.11.1 or higher (protoc-jar issue #78)
* Improve log output when using default maven settings (protoc-jar issue #77)

#### 3.11.1 (10-Dec-2019)
* Upgrade to protoc 3.11.1
* Support using .proto files in dependencies that aren't packaged (issue #79)
* Small enhancement for shading naming convention

#### 3.10.1 (30-Nov-2019)
* Upgrade to protoc 3.10.1

#### 3.9.2 (29-Nov-2019)
* Upgrade to protoc 3.9.2
* Add test and documentation for javalite output option (issue #80)
* Small fix for optimizeCodegen option

#### 3.8.0 (9-Jun-2019)
* Upgrade to protoc 3.8.0
* Support custom classifier extensions for plugin artifacts (issue #75)

#### 3.7.1 (17-May-2019)
* Upgrade to protoc 3.7.1
* Fix: regenerate code if missing an output directory (issue #72)
* Fix: error extracting maven include types (issue #74)

#### 3.7.0.2 (3-Apr-2019)
* Some fixes for option "optimizeCodegen" (issue #41, issue #71)

#### 3.7.0.1 (28-Mar-2019)
* New option "optimizeCodegen" to skip code generation when .proto input files appear unchanged (issue #41)
* Support download via maven mirror and/or proxy (issue #68, protoc-jar issue #57)

#### 3.7.0 (8-Mar-2019, 13-Mar-2019)
* Upgrade to protoc 3.7.0
* New option "compileMavenTypes" to compile .proto files from maven dependencies
* Update maven central download URL (https://repo.maven.apache.org/maven2/)
* Supports FreeBSD/x86, Solaris/x86 (freebsd-x86_64, sunos-x86_64)
* Supports Linux/ARM, Linux/POWER8, now provided by Google (linux-aarch_64, linux-ppcle_64)

#### 3.6.0.2 (16-Dec-2018)
* Fix: NPE when includeStdTypes is set to true (issue #60)
* Add integration tests (issue #31)
* Fix: download can fail/freeze silently (protoc-jar issue #56)
* Set download connection timeout (5 sec)
* Supports FreeBSD/x86, Solaris/x86, Linux/POWER8 (freebsd-x86_64, sunos-x86_64, linux-ppcle_64)
* Supports Linux/ARM, provided by Google (linux-aarch_64)

#### 3.6.0.1 (1-Jul-2018)
* Fix: handle errors when parsing protoc warnings/errors

#### 3.6.0 (30-Jun-2018)
* Upgrade to protoc 3.6.0
* New option "includeMavenTypes" to import .proto files from maven dependencies (issue #19)
* Add Eclipse (m2e) warnings/errors
* Supports FreeBSD/x86, Linux/POWER8 (freebsd-x86_64, linux-ppcle_64)
* Supports Linux/ARM, provided by Google (linux-aarch_64)

#### 3.5.1.1 (27-Jan-2018)
* Fix regression in shading (due to incorrect version formatting)

#### 3.5.1 (21-Jan-2018)
* Upgrade to protoc 3.5.1
* New option "addProtoSources" to add source .proto files to the generated jar file (issue #44)
* Supports FreeBSD/x86, Linux/POWER8 (freebsd-x86_64, linux-ppcle_64)
* Supports Linux/ARM, provided by Google (linux-aarch_64)

#### 3.5.0 (28-Nov-2017)
* Upgrade to protoc 3.5.0
* Supports FreeBSD/x86, Linux/POWER8 (freebsd-x86_64, linux-ppcle_64)
* Supports Linux/ARM, provided by Google now (linux-aarch_64)

#### 3.4.0.2 (12-Nov-2017)
* Support for Linux on ARM platform (linux-aarch_64; 2.4.1, 2.6.1, 3.4.0)
* Fix: download would not replace existing file (eg, maven-metadata.xml)

#### 3.4.0.1 (29-Sep-2017)
* Support for Linux on POWER8 platform (linux-ppcle_64)
* Support for FreeBSD on x86 platform (freebsd-x86_64)
* Support unbundled binaries with automatic download from maven central
* Remove 3.x version map to 3.4.0

#### 3.4.0 (29-Aug-2017)
* Upgrade to protoc 3.4.0
* Map previous 3.x versions to 3.4.0 for backward compatibility
* Support POWER8 (ppc64le) platform (protoc 3.4.0 only)

#### 3.3.0.1 (22-Jun-2017)
* Use alternative dir (user.home) if execution in temp dir fails (issue #39)
* Fix "includeStdTypes" logic that could break "protocCommand" (issue #40)

#### 3.3.0 (28-May-2017)
* Upgrade to protoc 3.3.0
* Map 3.0.0, 3.1.0, 3.2.0 to 3.3.0 for backward compatibility
* Fix for shading when tmp dir and source code are on different filesystems (type java-shaded)

#### 3.2.0.1 (2-Apr-2017)
* More flexibility for parameter "outputOptions" when generating descriptors (issue #36)
* Separate proto3 and proto2 standard types (parameter "includeStdTypes")
* Better error message for unsupported versions

#### 3.2.0 (15-Feb-2017)
* Upgrade to protoc 3.2.0
* Map 3.0.0, 3.1.0 to 3.2.0 for backward compatibility
* Implement retry as workaround for text file busy issue #33
* Use embedded or downloaded protoc as fallback if given "protocCommand" fails

#### 3.1.0.5 (17-Jan-2017)
* Fix PlatformDetector NPE on Linux

#### 3.1.0.4 (15-Jan-2017)
* Make binary artifacts executable, copy to executable temp file (issue #32)

#### 3.1.0.3 (5-Jan-2017)
* Fix to actually map 3.0.0 to 3.1.0, minor internal changes

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
