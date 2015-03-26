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

```xml
<plugin>
	<groupId>com.github.os72</groupId>
	<artifactId>protoc-jar-maven-plugin</artifactId>
	<version>2.4.1.3</version> <!-- for protobuf 2.4.1 -->
	<!-- <version>2.5.0.3</version> --> <!-- for protobuf 2.5.0 -->
	<!-- <version>2.6.1.3</version> --> <!-- for protobuf 2.6.1 -->
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
