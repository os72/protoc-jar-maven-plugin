protoc-jar-maven-plugin
=======================

Protocol Buffers maven plugin - performs protobuf code generation using multi-platform `protoc-jar` executable JAR.
Available on Maven Central: http://central.maven.org/maven2/com/github/os72/protoc-jar-maven-plugin/

[![Maven Central](https://img.shields.io/badge/maven%20central-3.1.0.1-brightgreen.svg)](http://search.maven.org/#artifactdetails|com.github.os72|protoc-jar-maven-plugin|3.1.0.1|)

Simple maven plugin to compile .proto files using `protoc-jar` embedded protoc compiler, providing some portability across the major platforms (Linux, Mac/OSX, and Windows). At build time the plugin detects the platform and executes the corresponding protoc binary. Supports protoc versions 2.4.1, 2.5.0, 2.6.1, 3.1.0

See also
* https://github.com/os72/protoc-jar
* https://github.com/os72/protobuf-java-shaded-241
* https://github.com/os72/protobuf-java-shaded-250
* https://github.com/os72/protobuf-java-shaded-261
* https://github.com/google/protobuf

#### Usage

Documentation: see http://os72.github.io/protoc-jar-maven-plugin/, particular [run-mojo](http://os72.github.io/protoc-jar-maven-plugin/run-mojo.html).

Sample usage - compile in main cycle into `target/generated-sources`, add folder to pom:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.1.0.1</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<protocVersion>3.1.0</protocVersion> <!-- 2.4.1, 2.5.0, 2.6.1, 3.1.0 -->
				<inputDirectories>
					<include>src/main/protobuf</include>
				</inputDirectories>
			</configuration>
		</execution>
	</executions>
</plugin>
```

Sample usage - compile in main cycle into `target/generated-sources`, add folder to pom, include `google.protobuf` standard types, include additional imports:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.1.0.1</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<protocVersion>3.1.0</protocVersion> <!-- 2.4.1, 2.5.0, 2.6.1, 3.1.0 -->
				<includeStdTypes>true</includeStdTypes>
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

Sample usage - compile in test cycle, multiple output targets:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.1.0.1</version>
	<executions>
		<execution>
			<phase>generate-test-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<protocVersion>2.4.1</protocVersion> <!-- 2.4.1, 2.5.0, 2.6.1, 3.1.0 -->
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

Sample usage - generate java shaded for use with `protobuf-java-shaded-241`, don't alter pom:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.1.0.1</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<protocVersion>2.4.1</protocVersion> <!-- 2.4.1, 2.5.0, 2.6.1, 3.1.0 -->
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
