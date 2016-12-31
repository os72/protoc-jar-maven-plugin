/*
 * Copyright 2014 protoc-jar developers
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.os72.protocjar.maven;

import java.io.File;

/**
 * Specifies output target
 */
public class OutputTarget
{
	public OutputTarget() {
		type = "java";
		addSources = "main";
		cleanOutputFolder = false;
		pluginPath = null;
		outputDirectory = null;
		outputOptions = null;
	}

	/**
	 * Specifies output type.
	 * Options: "java",  "cpp", "python", "descriptor" (default: "java"); for proto3 also: "javanano", "csharp", "objc", "ruby", "js"
	 * 
	 * @parameter property="type" default-value="java"
	 */
	String type;

	/**
	 * Specifies whether to add outputDirectory to sources that are going to be compiled.
	 * Options: "main", "test", "none" (default: "main")
	 * 
	 * @parameter property="addSources" default-value="main"
	 */
	String addSources;
	
	/**
	 * If this parameter is set to "true" output folder is cleaned prior to the
	 * build. This will not let old and new classes coexist after package or
	 * class rename in your IDE cache or after non-clean rebuild. Set this to
	 * "false" if you are doing multiple plugin invocations per build and it is
	 * important to preserve output folder contents
	 * 
	 * @parameter property="cleanOutputFolder" default-value="false"
	 */
	boolean cleanOutputFolder;

	/**
	 * Path to the protoc plugin that generates code for the specified {@link #type}.
	 *
	 * @parameter property="pluginPath"
	 */
	String pluginPath;

	/**
	 * Maven artifact coordinates of the protoc plugin that generates code for the specified {@link #type}.
	 * Format: "groupId:artifactId:version" (eg, "io.grpc:protoc-gen-grpc-java:1.0.1")
	 *
	 * @parameter property="pluginArtifact"
	 */
	String pluginArtifact;

	/**
	 * Output directory for the generated files. Defaults to
	 * "${project.build.directory}/generated-sources" or
	 * "${project.build.directory}/generated-test-sources"
	 * depending on the addSources parameter
	 * 
	 * @parameter property="outputDirectory"
	 */
	File outputDirectory;

	/**
	 * If this parameter is set, append its value to the {@link #outputDirectory} path
	 * For example, value "protobuf" will set output directory to
	 * "${project.build.directory}/generated-sources/protobuf" or
	 * "${project.build.directory}/generated-test-sources/protobuf"
	 *
	 * @parameter property="outputDirectorySuffix"
	 */
	String outputDirectorySuffix;

	/**
	 * Output options. Used for example with type "js" to create protoc argument --js_out=[OPTIONS]:output_dir
	 *
	 * @parameter property="outputOptions"
     */
	String outputOptions;

	public String toString() {
		return type + ": " + outputDirectory + " (add: " + addSources + ", clean: " + cleanOutputFolder + ", plugin: " + pluginPath + ", outputOptions: " + outputOptions + ")";
	}
}
