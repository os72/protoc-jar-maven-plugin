protoc-jar-maven-plugin
=======================

Protocol Buffers maven plugin - performs protobuf code generation using multi-platform protoc-jar executable JAR.
Available on Maven Central: http://central.maven.org/maven2/com/github/os72/protoc-jar-maven-plugin/

[![Maven Central](https://img.shields.io/badge/maven%20central-3.0.0--b1-brightgreen.svg)](http://search.maven.org/#artifactdetails|com.github.os72|protoc-jar-maven-plugin|3.0.0-b1|)

Simple maven plugin to compile .proto files using protoc-jar embedded protoc compiler, providing some portability across the major platforms

Based on
* https://github.com/os72/protoc-jar
* https://github.com/igor-petruk/protobuf-maven-plugin
* https://github.com/google/protobuf

#### Usage

Documentation: http://os72.github.io/protoc-jar-maven-plugin/

Sample usage - compile in main cycle into target/generated-sources, add folder to pom:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.0.0-b1</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<protocVersion>2.4.1</protocVersion> <!-- 2.4.1, 2.5.0, 2.6.1, 3.0.0 -->
				<includeDirectories>
					<include>src/main/protobuf</include>
				</includeDirectories>
				<inputDirectories>
					<include>src/main/protobuf</include>
				</inputDirectories>
			</configuration>
		</execution>
	</executions>
</plugin>
```

Sample usage - generate python, don't alter pom:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>3.0.0-b1</version>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<protocVersion>2.4.1</protocVersion> <!-- 2.4.1, 2.5.0, 2.6.1, 3.0.0 -->
				<type>python</type>
				<addSources>none</addSources>
				<outputDirectory>src/main/python</outputDirectory>
				<includeDirectories>
					<include>src/main/protobuf</include>
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
	<version>3.0.0-b1</version>
	<executions>
		<execution>
			<phase>generate-test-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
				<protocVersion>2.4.1</protocVersion> <!-- 2.4.1, 2.5.0, 2.6.1, 3.0.0 -->
				<includeDirectories>
					<include>src/test/resources</include>
				</includeDirectories>
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
