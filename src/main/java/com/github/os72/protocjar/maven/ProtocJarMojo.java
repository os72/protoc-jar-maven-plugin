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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import kr.motd.maven.os.Detector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.sonatype.plexus.build.incremental.BuildContext;

import com.github.os72.protocjar.Protoc;
import com.github.os72.protocjar.ProtocVersion;

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
	 * Specifies the protoc version.
	 * 
	 * @parameter property="protocVersion"
	 */
	private String protocVersion;

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
	 * If "true", extract the included google.protobuf standard types and add them to protoc import path.
	 * 
	 * @parameter property="includeStdTypes" default-value="false"
	 */
	private boolean includeStdTypes;

	/**
	 * Specifies output type.
	 * Options: "java",  "cpp", "python", "descriptor" (default: "java"); for proto3 also: "javanano", "csharp", "objc", "ruby", "js"
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 * 
	 * @parameter property="type" default-value="java"
	 */
	private String type;

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
	 * Path to the protoc plugin that generates code for the specified {@link #type}.
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 *
	 * @parameter property="pluginPath"
	 */
	private String pluginPath;

	/**
	 * Maven artifact coordinates of the protoc plugin that generates code for the specified {@link #type}.
	 * Format: "groupId:artifactId:version" (eg, "io.grpc:protoc-gen-grpc-java:1.0.1")
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 *
	 * @parameter property="pluginArtifact"
	 */
	private String pluginArtifact;

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
	 * If this parameter is set, append its value to the {@link #outputDirectory} path
	 * For example, value "protobuf" will set output directory to
	 * "${project.build.directory}/generated-sources/protobuf" or
	 * "${project.build.directory}/generated-test-sources/protobuf"
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 *
	 * @parameter property="outputDirectorySuffix"
	 */
	private String outputDirectorySuffix;

	/**
	 * Output options. Used for example with type "js" to create protoc argument --js_out=[OPTIONS]:output_dir
	 * <p>
	 * Ignored when {@code <outputTargets>} is given
	 *
	 * @parameter property="outputOptions"
     */
	private String outputOptions;

	/**
	 * This parameter lets you specify multiple protoc output targets.
	 * OutputTarget parameters: "type", "addSources", "cleanOutputFolder", "outputDirectory", "outputDirectorySuffix", "outputOptions", "pluginPath".
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

	/**
	 * Maven artifact coordinates of the protoc binary to use
	 * Format: "groupId:artifactId:version" (eg, "com.google.protobuf:protoc:3.1.0")
	 *
	 * @parameter property="protocArtifact"
	 */
	private String protocArtifact;

	/**
	 * The Maven project.
	 * 
	 * @parameter property="project"
	 * @readonly
	 * @required
	 */
	private MavenProject project;

	/** 
	 * @parameter default-value="${localRepository}" 
	 * @readonly
	 * @required
	 */
	private ArtifactRepository localRepository;

	/** 
	 * @parameter default-value="${project.remoteArtifactRepositories}" 
	 * @readonly
	 * @required
	 */
	private List<ArtifactRepository> remoteRepositories;

	/** @component */
	private BuildContext buildContext;
	/** @component */
	private ArtifactFactory artifactFactory;
	/** @component */
	private ArtifactResolver artifactResolver;

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
			target.outputDirectorySuffix = outputDirectorySuffix;
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
			
			if (target.outputDirectorySuffix != null) {
				target.outputDirectory = new File(target.outputDirectory, target.outputDirectorySuffix);
			}
		}
		
		performProtoCompilation();
	}

	private void performProtoCompilation() throws MojoExecutionException {
		//getLog().info("Protoc version: " + protocVersion);
		
		String protocTemp = null;
		if ((protocCommand == null && protocArtifact == null) || includeStdTypes) {
			if (protocVersion == null || protocVersion.length() < 1) protocVersion = ProtocVersion.PROTOC_VERSION;
			getLog().info("Protoc version: " + protocVersion);
			
			try {
				File protocFile = Protoc.extractProtoc(protocVersion.replace(".", ""), includeStdTypes);
				protocTemp = protocFile.getAbsolutePath();
			}
			catch (IOException e) {
				throw new MojoExecutionException("Error extracting protoc for version " + protocVersion, e);
			}
			
			if (protocCommand == null && protocArtifact == null) protocCommand = protocTemp;
		}
		
		if (protocCommand == null && protocArtifact != null) {
			protocCommand = resolveArtifact(protocArtifact).getAbsolutePath();
		}
		
		/*
		if (protocCommand == null) {
			try {
				File protocFile = Protoc.extractProtoc(protocVersion.replace(".", ""), includeStdTypes);
				protocCommand = protocFile.getAbsolutePath();
			}
			catch (IOException e) {
				throw new MojoExecutionException("Error extracting protoc for version " + protocVersion, e);
			}
		}
		*/
		
		getLog().info("Protoc command: " + protocCommand);
		
		if (inputDirectories == null || inputDirectories.length == 0) {
			File inputDir = new File(project.getBasedir().getAbsolutePath() + DEFAULT_INPUT_DIR);
			inputDirectories = new File[] { inputDir };
		}
		getLog().info("Input directories:");
		for (File input : inputDirectories) getLog().info("    " + input);
		
		if (includeStdTypes) {
			File stdTypeDir = new File(new File(protocTemp).getParentFile().getParentFile(), "include");
			if (includeDirectories != null && includeDirectories.length > 0) {
				List<File> includeDirList = new ArrayList<File>();
				includeDirList.add(stdTypeDir);
				includeDirList.addAll(Arrays.asList(includeDirectories));
				includeDirectories = includeDirList.toArray(new File[0]);
			}
			else {
				includeDirectories = new File[] { stdTypeDir };
			}
		}
		
		if (includeDirectories != null && includeDirectories.length > 0) {
			getLog().info("Include directories:");
			for (File include : includeDirectories) getLog().info("    " + include);
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
				for (File protoFile : protoFiles) {
					if (target.cleanOutputFolder || buildContext.hasDelta(protoFile.getPath())) {
						processFile(protoFile, protocVersion, targetType, target.pluginPath, target.outputDirectory, target.outputOptions);
					}
					else {
						getLog().info("Not changed " + protoFile);
					}
				}
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

	private void processFile(File file, String version, String type, String pluginPath, File outputDir, String outputOptions) throws MojoExecutionException {
		getLog().info("    Processing ("+ type + "): " + file.getName());
		Collection<String> cmd = buildCommand(file, version, type, pluginPath, outputDir, outputOptions);
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

	private Collection<String> buildCommand(File file, String version, String type, String pluginPath, File outputDir, String outputOptions) throws MojoExecutionException {
		Collection<String> cmd = new ArrayList<String>();
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

	private File resolveArtifact(String artifactSpec) throws MojoExecutionException {
		try {
			Properties detectorProps = new Properties();
			new PlatformDetector().doDetect(detectorProps);
			getLog().info("*** "+detectorProps);

			String[] as = artifactSpec.split(":");
			String platform = detectorProps.getProperty("os.detected.classifier");
			Artifact artifact = artifactFactory.createDependencyArtifact(as[0], as[1], VersionRange.createFromVersionSpec(as[2]), "exe", platform, Artifact.SCOPE_RUNTIME);

			artifactResolver.resolve(artifact, remoteRepositories, localRepository);
			File artifactFile = artifact.getFile();
			getLog().info("*** "+artifactFile);
			
			return artifactFile;
		}
		catch (Exception e) {
			throw new MojoExecutionException("Error resolving artifact: " + artifactSpec, e);
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

	class PlatformDetector extends Detector
	{
		void doDetect(Properties props) {
	    	detect(props, null);
		}

		@Override
		protected void log(String msg) {
			//System.out.println(msg);
		}

		@Override
		protected void logProperty(String name, String value) {
			//log(name + ": " + value);
		}
	}
}
