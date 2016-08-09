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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.sonatype.plexus.build.incremental.BuildContext;

import com.github.os72.protocjar.Protoc;

/**
 * Compiles .proto files using protoc-jar embedded protoc compiler (external protoc executable also supported)
 * 
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
	 * Specifies protoc version.
	 * 
	 * @parameter property="protocVersion"
	 */
	String protocVersion;

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
	 * Specifies output type.
	 * Options: "java",  "cpp", "python", "descriptor" (default: "java"); for proto3 also: "javanano", "csharp", "objc", "ruby", "js"
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 * 
	 * @parameter property="type" default-value="java"
	 */
	String type;

	/**
	 * Specifies whether to add outputDirectory to sources that are going to be compiled.
	 * Options: "main", "test", "none" (default: "main")
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 * 
	 * @parameter property="addSources" default-value="main"
	 */
	private String addSources;

	/**
	 * If this parameter is set to "true" output folder is cleaned prior to the
	 * build. This will not let old and new classes coexist after package or
	 * class rename in your IDE cache or after non-clean rebuild. Set this to
	 * "false" if you are doing multiple plugin invocations per build and it is
	 * important to preserve output folder contents
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 * 
	 * @parameter property="cleanOutputFolder" default-value="false"
	 */
	private boolean cleanOutputFolder;

	/**
	 * Path to protoc plugin that generates code for the specified {@link #type}.
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 *
	 * @parameter property="pluginPath"
	 */
	String pluginPath;

	/**
	 * Output directory for the generated java files. Defaults to
	 * "${project.build.directory}/generated-sources" or
	 * "${project.build.directory}/generated-test-sources"
	 * depending on the addSources parameter
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 * 
	 * @parameter property="outputDirectory"
	 */
	private File outputDirectory;

	/**
	 * Output options. Used for example with type "js" to create protoc argument --js_out=[OPTIONS]:output_dir
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 *
	 * @parameter property="outputOptions"
     */
	String outputOptions;

	/**
	 * This parameter lets you specify multiple protoc output targets.
	 * OutputTarget parameters: "type", "addSources", "cleanOutputFolder", "outputDirectory".
	 * Type options: "java", "cpp", "python", "descriptor" (default: "java"); for proto3 also: "javanano", "csharp", "objc", "ruby", "js"
	 * 
	 * <pre>
	 * {@code
	 * <outputTargets>
	 * 	<outputTarget>
	 * 		<type>java</type>
	 * 		<addSources>none</addSources>
	 * 		<cleanOutputFolder>false</cleanOutputFolder>
	 * 		<outputDirectory>src/main/java</outputDirectory>
	 * 	</outputTarget>
	 * 	<outputTarget>
	 * 		<type>python</type>
	 * 		<addSources>none</addSources>
	 * 		<cleanOutputFolder>false</cleanOutputFolder>
	 * 		<outputDirectory>src/main/python</outputDirectory>
	 * 	</outputTarget>
	 * 	<outputTarget>
	 * 		<type>dart</type>
	 * 		<addSources>none</addSources>
	 * 		<cleanOutputFolder>false</cleanOutputFolder>
	 * 		<outputDirectory>lib/protobuf</outputDirectory>
	 * 		<pluginPath>protoc-gen-dart.exe</pluginPath>
	 * 	</outputTarget>
	 * <outputTargets>
	 * }
	 * </pre>
	 * 
	 * @parameter property="outputTargets"
	 */
	private OutputTarget[] outputTargets;

	/**
	 * Default extension for protobuf files
	 * 
	 * @parameter property="extension" default-value=".proto"
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
		
		if (outputTargets == null || outputTargets.length == 0) {
			OutputTarget target = new OutputTarget();
			target.type = type;
			target.addSources = addSources;
			target.cleanOutputFolder = cleanOutputFolder;
			target.pluginPath = pluginPath;
			target.outputDirectory = outputDirectory;
			target.outputOptions = outputOptions;
			outputTargets = new OutputTarget[] {target};
		}

		for (OutputTarget target : outputTargets) {
			target.addSources = target.addSources.toLowerCase().trim();
			if ("true".equals(target.addSources)) target.addSources = "main";
			
			if (target.outputDirectory == null) {
				String subdir = "generated-" + ("test".equals(target.addSources) ? "test-" : "") + "sources";
				target.outputDirectory = new File(project.getBuild().getDirectory() + File.separator + subdir + File.separator);
			}
		}
		
		performProtoCompilation();
	}

	private void performProtoCompilation() throws MojoExecutionException {	
		getLog().info("Protoc version: " + protocVersion);
		
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
		
		getLog().info("Output targets:");
		for (OutputTarget target : outputTargets) getLog().info("    " + target);
		for (OutputTarget target : outputTargets) preprocessTarget(target);
		for (OutputTarget target : outputTargets) processTarget(target);
	}

	private void preprocessTarget(OutputTarget target) throws MojoExecutionException {
		File f = target.outputDirectory;
		if (!f.exists()) {
			getLog().info(f + " does not exist. Creating...");
			f.mkdirs();
		}
		
		if (target.cleanOutputFolder) {
			try {
				getLog().info("Cleaning " + f);
				FileUtils.cleanDirectory(f);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processTarget(OutputTarget target) throws MojoExecutionException {		
		boolean shaded = false;
		String targetType = target.type;
		if (targetType.equals("java-shaded") || targetType.equals("java_shaded")) {
			targetType = "java";
			shaded = true;
		}
		
		FileFilter fileFilter = new FileFilter(extension);
		for (File input : inputDirectories) {
			if (input == null) continue;
			
			if (input.exists() && input.isDirectory()) {
				Collection<File> protoFiles = FileUtils.listFiles(input, fileFilter, TrueFileFilter.INSTANCE);
				List<Collection<String>> cmds = new ArrayList<Collection<String>>(protoFiles.size());
				List<File> protoFilesToProcess = new ArrayList<File>(protoFiles.size());
				for (File protoFile : protoFiles) {
					if (target.cleanOutputFolder || buildContext.hasDelta(protoFile.getPath())) {
						//processFile(protoFile, protocVersion, targetType, target.pluginPath, target.outputDirectory, target.outputOptions);
						Collection<String> cmd = buildCommand(protoFile, protocVersion, targetType, target.pluginPath, target.outputDirectory, target.outputOptions);
						cmds.add(cmd);
						protoFilesToProcess.add(protoFile);
					}
					else {
						getLog().info("Not changed " + protoFile);
					}
				}

				processFiles(protoFilesToProcess, cmds);
			}
			else {
				if (input.exists()) getLog().warn(input + " is not a directory");
				else getLog().warn(input + " does not exist");
			}
		}
		
		if (shaded) {
			try {
				getLog().info("    Shading (version " + protocVersion + "): " + target.outputDirectory);
				Protoc.doShading(target.outputDirectory, protocVersion.replace(".", ""));
			}
			catch (IOException e) {
				throw new MojoExecutionException("Error occurred during shading", e);
			}
		}
		
		boolean mainAddSources = "main".endsWith(target.addSources);
		boolean testAddSources = "test".endsWith(target.addSources);
		
		if (mainAddSources) {
			getLog().info("Adding generated classes to classpath");
			project.addCompileSourceRoot(target.outputDirectory.getAbsolutePath());
		}
		if (testAddSources) {
			getLog().info("Adding generated classes to test classpath");
			project.addTestCompileSourceRoot(target.outputDirectory.getAbsolutePath());
		}
		if (mainAddSources || testAddSources) {
			buildContext.refresh(target.outputDirectory);
		}
	}

	private void processFiles(List<File> files, List<Collection<String>> cmds) throws MojoExecutionException {
		try {
			String[][] arrayCmds = new String[cmds.size()][];
			for (int i = 0; i < cmds.size(); i++) {
				arrayCmds[i] = cmds.get(i).toArray(new String[0]);
			}

			int[] exitCodes;
			if (protocCommand == null){
				exitCodes = Protoc.runProtoc(arrayCmds);
			}
			else {
				exitCodes = new int[cmds.size()];
				for (int i = 0; i < cmds.size(); i++) {
					exitCodes[i] = Protoc.runProtoc(protocCommand, arrayCmds[i]);
				}
			}

			for (int i = 0; i < exitCodes.length; i++) {
				int ret = exitCodes[i];
				if (ret != 0) throw new MojoExecutionException("protoc-jar failed for " + files.get(i) + ". Exit code " + ret);
			}
		}
		catch (InterruptedException e) {
			throw new MojoExecutionException("Interrupted", e);
		}
		catch (IOException e) {
			throw new MojoExecutionException("Unable to execute protoc-jar", e);
		}
	}

	private Collection<String> buildCommand(File file, String version, String type, String pluginPath, File outputDir, String outputOptions) throws MojoExecutionException {
		Collection<String> cmd = new LinkedList<String>();
		populateIncludes(cmd);
		cmd.add("-I" + file.getParentFile().getAbsolutePath());
		if ("descriptor".equals(type)) {
			File outFile = new File(outputDir, file.getName());
			cmd.add("--descriptor_set_out=" + FilenameUtils.removeExtension(outFile.toString()) + ".desc");
			cmd.add("--include_imports");
		}
		else {
			if (outputOptions != null) {
				cmd.add("--" + type + "_out=" + outputOptions + ":" + outputDir);
			}
			else {
				cmd.add("--" + type + "_out=" + outputDir);
			}

			if (pluginPath != null) {
				cmd.add("--plugin=protoc-gen-" + type + "=" + pluginPath);
			}
		}
		cmd.add(file.toString());
		if (version != null) cmd.add("-v" + version);
		return cmd;
	}

	private void populateIncludes(Collection<String> args) throws MojoExecutionException {
		for (File include : includeDirectories) {
			if (!include.exists()) throw new MojoExecutionException("Include path '" + include.getPath() + "' does not exist");
			if (!include.isDirectory()) throw new MojoExecutionException("Include path '" + include.getPath() + "' is not a directory");
			args.add("-I" + include.getPath());
		}
	}

	class FileFilter implements IOFileFilter
	{
		String extension;

		public FileFilter(String extension) {
			this.extension = extension;
		}

		public boolean accept(File dir, String name) {
			return name.endsWith(extension);
		}

		public boolean accept(File file) {
			return file.getName().endsWith(extension);
		}
	}
}
