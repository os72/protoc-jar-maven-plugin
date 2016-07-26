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
		outputDirectory = null;
		pluginPath = null;
	}

	/**
	 * Specifies output type.
	 * Options: "java",  "cpp", "python", "descriptor" (default: "java"); for proto3 also: "javanano", "csharp", "objc", "ruby", "js"
	 * 
	 * @parameter property="type" default-value="java"
	 */
	String type;

	/**
	 * If this parameter is set, the path to the plugin to generate the specified {@link #type} is explicitly set.
	 *
	 * @parameter property="pluginPath"
	 */
	String pluginPath;

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
	 * Output directory for the generated files. Defaults to
	 * "${project.build.directory}/generated-sources/protobuf" or
	 * "${project.build.directory}/generated-test-sources/protobuf"
	 * depending on the addSources parameter
	 * 
	 * @parameter property="outputDirectory"
	 */
	File outputDirectory;

	public String toString() {
		return type + ": " + outputDirectory + " (add: " + addSources + ", clean: " + cleanOutputFolder + ", plugin: " + pluginPath + ")";
	}
}
