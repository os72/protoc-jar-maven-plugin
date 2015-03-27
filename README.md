protoc-jar-maven-plugin
=======================

Protocol Buffers codegen plugin - based on protoc-jar executable JAR. Available on Maven Central: http://central.maven.org/maven2/com/github/os72/protoc-jar-maven-plugin/

Simple maven plugin to compile .proto files using protoc-jar embedded protoc compiler. See the Protocol Buffers site for details: https://github.com/google/protobuf

Based on
* https://github.com/os72/protoc-jar
* https://github.com/igor-petruk/protobuf-maven-plugin

Branches
* https://github.com/os72/protoc-jar-maven-plugin/tree/protobuf_241 (protobuf 2.4.1)
* https://github.com/os72/protoc-jar-maven-plugin/tree/protobuf_250 (protobuf 2.5.0)
* https://github.com/os72/protoc-jar-maven-plugin/tree/protobuf_261 (protobuf 2.6.1)

#### Usage

Documentation: http://os72.github.io/protoc-jar-maven-plugin/

Sample usage - compile in main cycle into target/generated-sources, add folder to pom:
```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>2.4.1.4</version> <!-- for protobuf 2.4.1 -->
	<!-- <version>2.5.0.4</version> --> <!-- for protobuf 2.5.0 -->
	<!-- <version>2.6.1.4</version> --> <!-- for protobuf 2.6.1 -->
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
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
	<version>2.4.1.4</version> <!-- for protobuf 2.4.1 -->
	<!-- <version>2.5.0.4</version> --> <!-- for protobuf 2.5.0 -->
	<!-- <version>2.6.1.4</version> --> <!-- for protobuf 2.6.1 -->
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
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
	<version>2.4.1.4</version> <!-- for protobuf 2.4.1 -->
	<!-- <version>2.5.0.4</version> --> <!-- for protobuf 2.5.0 -->
	<!-- <version>2.6.1.4</version> --> <!-- for protobuf 2.6.1 -->
	<executions>
		<execution>
			<phase>generate-test-sources</phase>
			<goals>
				<goal>run</goal>
			</goals>
			<configuration>
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
