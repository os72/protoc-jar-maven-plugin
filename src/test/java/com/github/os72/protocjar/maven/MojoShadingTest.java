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

import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.os72.protocjar.ProtocVersion;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({"3.2.3"})
public class MojoShadingTest
{
	@Rule
	public final TestResources resources = new TestResources();
	public final MavenRuntime maven;
	
	public MojoShadingTest(MavenRuntimeBuilder mavenBuilder) throws Exception {
		this.maven = mavenBuilder.withCliOptions("-B", "-U", "-e").build();
	}

	@Test
	public void testShading360() throws Exception {
		File basedir = resources.getBasedir("shading-test");
		maven.forProject(basedir)
			.withCliOption("-Dprotobuf.version=360")
			.execute("verify")
			.assertErrorFreeLog();
	}

	@Test
	public void testShading241() throws Exception {
		File basedir = resources.getBasedir("shading-test");
		maven.forProject(basedir)
			.withCliOption("-Dprotobuf.version=241")
			.execute("verify")
			.assertErrorFreeLog();
	}
}
