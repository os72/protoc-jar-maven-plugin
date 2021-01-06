protoc-jar-maven-plugin
=======================

Protocol Buffers protobuf maven plugin - performs protobuf code generation using [protoc-jar](https://github.com/os72/protoc-jar) multi-platform executable protoc JAR.
Available on Maven Central: https://repo.maven.apache.org/maven2/com/github/os72/protoc-jar-maven-plugin/3.11.4/

[![Maven Central](https://img.shields.io/badge/maven%20central-3.11.4-brightgreen.svg)](http://search.maven.org/#artifactdetails|com.github.os72|protoc-jar-maven-plugin|3.11.4|)
[![Join the chat at https://gitter.im/os72/community](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/os72/community)

---

Simple maven plugin to compile .proto files using [protoc-jar](https://github.com/os72/protoc-jar) embedded protoc compiler, providing some portability across the major platforms (Linux, Mac/OSX, and Windows). At build time the plugin detects the platform and executes the corresponding protoc binary.

Supports embedded protoc versions 2.4.1, 2.5.0, 2.6.1, 3.11.4, and any binaries (protoc and protoc plugins) available for download from maven central. Also supports pre-installed protoc binary

* Support for FreeBSD on x86 platform (freebsd-x86_64), thanks [kjopek](https://github.com/kjopek)
* Support for Solaris on x86 platform (sunos-x86_64), thanks [siepkes](https://github.com/siepkes)
* Support for Linux on POWER8 platform (linux-ppcle_64), now from Google
  * Older versions (up to 3.6.0), thanks to [Apache SystemML](https://github.com/apache/systemml) folks ([nakul02](https://github.com/nakul02))
* Support for Linux on ARM platform (linux-aarch_64), now from Google
  * Older versions (2.4.1, 2.6.1, 3.4.0), thanks [garciagorka](https://github.com/garciagorka)

See also
* https://github.com/os72/protoc-jar
* https://github.com/os72/protobuf-java-shaded-241
* https://github.com/os72/protobuf-java-shaded-250
* https://github.com/os72/protobuf-java-shaded-261
* https://github.com/os72/protobuf-java-shaded-351
* https://github.com/os72/protobuf-java-shaded-360
* https://github.com/os72/protobuf-java-shaded-3-11-1
* https://github.com/google/protobuf

Binaries
* https://repo.maven.apache.org/maven2/com/google/protobuf/protoc/
* https://repo.maven.apache.org/maven2/com/github/os72/protoc/
* https://oss.sonatype.org/content/repositories/snapshots/com/github/os72/protoc/

#### Usage

Documentation: see http://os72.github.io/protoc-jar-maven-plugin/, in particular [run-mojo](http://os72.github.io/protoc-jar-maven-plugin/run-mojo.html)

Sample usage - compile in main cycle into `target/generated-sources`, add generated sources to project, use default `protoc` version and default `src/main/protobuf` source folder:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.11.4</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
		</execution>
	</executions>
</plugin>
```

Sample usage - compile in main cycle into `target/generated-sources`, add generated sources to project, add all .proto sources to generated jar, include .proto files from direct maven dependencies, include additional imports:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.11.4</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<addProtoSources>all</addProtoSources>
				<includeMavenTypes>direct</includeMavenTypes>
				<includeDirectories>
					<include>src/main/more_proto_imports</include>
				</includeDirectories>
				<inputDirectories>
					<include>src/main/protobuf</include>
				</inputDirectories>
			</configuration>
		</execution>
	</executions>
</plugin>
```

Sample usage - download protoc and plugin binaries from maven repo, multiple output targets (example: gRPC):
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.11.4</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<protocArtifact>com.google.protobuf:protoc:3.0.0</protocArtifact>
				<inputDirectories>
					<include>src/main/resources</include>
				</inputDirectories>
				<outputTargets>
					<outputTarget>
						<type>java</type>
					</outputTarget>
					<outputTarget>
						<type>grpc-java</type>
						<pluginArtifact>io.grpc:protoc-gen-grpc-java:1.0.1</pluginArtifact>
					</outputTarget>
				</outputTargets>
			</configuration>
		</execution>
	</executions>
</plugin>
```

Sample usage - javalite, multiple output targets:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.11.4</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<inputDirectories>
					<include>src/main/resources</include>
				</inputDirectories>
				<outputTargets>
					<outputTarget>
						<type>java</type>
						<outputOptions>lite</outputOptions>
					</outputTarget>
					<outputTarget>
						<type>python</type>
						<outputOptions>lite</outputOptions>
					</outputTarget>
				</outputTargets>
			</configuration>
		</execution>
	</executions>
</plugin>
```

Sample usage - compile in test cycle, multiple output targets, don't alter project (`<addSources>: none`):
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.11.4</version>
	<executions>
		<execution>
			<phase>generate-test-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<protocVersion>2.4.1</protocVersion>
				<inputDirectories>
					<include>src/test/resources</include>
				</inputDirectories>
				<outputTargets>
					<outputTarget>
						<type>java</type>
						<addSources>none</addSources>
						<outputDirectory>src/test/java</outputDirectory>
					</outputTarget>
					<outputTarget>
						<type>descriptor</type>
						<addSources>none</addSources>
						<outputDirectory>src/test/resources</outputDirectory>
					</outputTarget>
				</outputTargets>
			</configuration>
		</execution>
	</executions>
</plugin>
```

Sample usage - generate java shaded for use with `protobuf-java-shaded-241`, don't alter project:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.11.4</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<protocVersion>2.4.1</protocVersion>
				<type>java-shaded</type>
				<addSources>none</addSources>
				<outputDirectory>src/main/java</outputDirectory>
				<inputDirectories>
					<include>src/main/protobuf</include>
				</inputDirectories>
			</configuration>
		</execution>
	</executions>
</plugin>
```

#### Credits

Originally based on
* https://github.com/igor-petruk/protobuf-maven-plugin
