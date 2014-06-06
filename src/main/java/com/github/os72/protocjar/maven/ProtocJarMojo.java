/*
 * Copyright 2014 protoc-jar developers
 * 
 * Incorporates code derived from https://github.com/igor-petruk/protobuf-maven-plugin
 * Copyright 2012, by Yet another Protobuf Maven Plugin Developers
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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.sonatype.plexus.build.incremental.BuildContext;

import com.github.os72.protocjar.Protoc;

/**
 * @goal run
 * @phase generate-sources
 * @requiresDependencyResolution
 */
public class ProtocJarMojo extends AbstractMojo
{
	private static final String DEFAULT_INPUT_DIR = "/src/main/protobuf/".replace('/', File.separatorChar);

	/**
	 * The Maven project.
	 * 
	 * @parameter property="project"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/** @component */
	private BuildContext buildContext;

	/**
	 * Input directories that have *.proto files (or the configured extension).
	 * If none specified then <b>src/main/protobuf</b> is used.
	 * 
	 * @parameter property="inputDirectories"
	 */
	private File[] inputDirectories;

	/**
	 * This parameter lets you specify additional include paths to protoc.
	 * 
	 * @parameter property="includeDirectories"
	 */
	private File[] includeDirectories;

	/**
	 * If this parameter is set to "true" output folder is cleaned prior to
	 * build. This will not let old and new classes coexist after package or
	 * class rename in your IDE cache or after non-clean rebuild. Set this to
	 * "false" if you are doing multiple plugin invocations per build and it is
	 * important to preserve output folder contents
	 * 
	 * @parameter property="cleanOutputFolder" default-value="true"
	 * @required
	 */
	private boolean cleanOutputFolder;

	/**
	 * Specifies a mode for plugin whether it should add outputDirectory to
	 * sources that are going to be compiled Can be "main", "test" or "none"
	 * 
	 * @parameter property="addSources" default-value="main"
	 * @required
	 */
	private String addSources;

	/**
	 * Output directory, that generated java files would be stored Defaults to
	 * "${project.build.directory}/generated-sources/protobuf" or
	 * "${project.build.directory}/generated-test-sources/protobuf" depending
	 * addSources parameter
	 * 
	 * @parameter property="outputDirectory"
	 */
	private File outputDirectory;

	/**
	 * Default extension for protobuf files
	 * 
	 * @parameter property="extension" default-value=".proto"
	 * @required
	 */
	private String extension;

	/**
	 * This parameter allows to use a protoc binary instead of the protoc-jar bundle
	 * 
	 * @parameter property="protocCommand"
	 */
	private String protocCommand;

	public void execute() throws MojoExecutionException {
		if (project.getPackaging() != null && "pom".equals(project.getPackaging().toLowerCase())) {
			getLog().info("Skipping 'pom' packaged project");
			return;
		}
		
		addSources = addSources.toLowerCase().trim();
		if ("true".equals(addSources)) addSources = "main";
		
		if (outputDirectory == null) {
			String subdir = "generated-" + ("test".equals(addSources) ? "test-" : "") + "sources";
			outputDirectory = new File(project.getBuild().getDirectory() + File.separator + subdir + File.separator);
		}
		
		performProtoCompilation();
	}

	private void performProtoCompilation() throws MojoExecutionException {
		if (includeDirectories != null && includeDirectories.length > 0) {
			getLog().info("Include directories:");
			for (File include : includeDirectories) getLog().info("    " + include);
		}
		
		getLog().info("Input directories:");
		for (File input : inputDirectories) getLog().info("    " + input);
		
		if (includeDirectories == null || inputDirectories.length == 0) {
			File inputDir = new File(project.getBasedir().getAbsolutePath() + DEFAULT_INPUT_DIR);
			getLog().info("    " + inputDir + " (using default)");
			inputDirectories = new File[] { inputDir };
		}
		
		getLog().info("Output directory: " + outputDirectory);
		File f = outputDirectory;
		if (!f.exists()) {
			getLog().info(f + " does not exist. Creating...");
			f.mkdirs();
		}
		
		if (cleanOutputFolder) {
			try {
				getLog().info("Cleaning " + f);
				FileUtils.cleanDirectory(f);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileFilter fileFilter = new FileFilter(extension);
		for (File input : inputDirectories) {
			if (input == null) continue;
			
			if (input.exists() && input.isDirectory()) {
				File[] files = input.listFiles(fileFilter);
				for (File file : files) {
					if (cleanOutputFolder || buildContext.hasDelta(file.getPath())) processFile(file, outputDirectory);
					else getLog().info("Not changed " + file);
				}
			}
			else {
				if (input.exists()) getLog().warn(input + " is not a directory");
				else getLog().warn(input + " does not exist");
			}
		}
		
		boolean mainAddSources = "main".endsWith(addSources);
		boolean testAddSources = "test".endsWith(addSources);
		
		if (mainAddSources) {
			getLog().info("Adding generated classes to classpath");
			project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
		}
		if (testAddSources) {
			getLog().info("Adding generated classes to test classpath");
			project.addTestCompileSourceRoot(outputDirectory.getAbsolutePath());
		}
		if (mainAddSources || testAddSources) {
			buildContext.refresh(outputDirectory);
		}
	}

	private void processFile(File file, File outputDir) throws MojoExecutionException {
		getLog().info("    Processing " + file.getName());
		Collection<String> cmd = buildCommand(file, outputDir);
		try {
			int ret = 0;
			if (protocCommand == null) ret = Protoc.runProtoc(cmd.toArray(new String[0]));
			else ret = Protoc.runProtoc(protocCommand, cmd.toArray(new String[0]));
			if (ret != 0) throw new MojoExecutionException("protoc-jar failed for " + file + ". Exit code " + ret);
		}
		catch (InterruptedException e) {
			throw new MojoExecutionException("Interrupted", e);
		}
		catch (IOException e) {
			throw new MojoExecutionException("Unable to execute protoc-jar for " + file, e);
		}
	}

	private Collection<String> buildCommand(File file, File outputDir) throws MojoExecutionException {
		Collection<String> cmd = new LinkedList<String>();
		populateIncludes(cmd);
		cmd.add("-I" + file.getParentFile().getAbsolutePath());
		cmd.add("--java_out=" + outputDir);
		cmd.add(file.toString());
		return cmd;
	}

	private void populateIncludes(Collection<String> args) throws MojoExecutionException {
		for (File include : includeDirectories) {
			if (!include.exists()) throw new MojoExecutionException("Include path '" + include.getPath() + "' does not exist");
			if (!include.isDirectory()) throw new MojoExecutionException("Include path '" + include.getPath() + "' is not a directory");
			args.add("-I" + include.getPath());
		}
	}

	class FileFilter implements FilenameFilter
	{
		String extension;

		public FileFilter(String extension) {
			this.extension = extension;
		}

		public boolean accept(File dir, String name) {
			return name.endsWith(extension);
		}
	}
}
