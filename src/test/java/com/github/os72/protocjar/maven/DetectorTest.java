/*
 * Copyright 2019 protoc-jar developers
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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Properties;

import org.junit.Test;

import com.github.os72.protocjar.PlatformDetector;

public class DetectorTest extends PlatformDetector
{
	@Test
	public void testArtifactSpec() throws Exception {
		String[] as;
		String str;
		
		as = ProtocJarMojo.parseArtifactSpec("com.google.protobuf:protoc:3.0.0", "windows-x86_64");
		str = Arrays.asList(as).toString();
		log(str);
		assertEquals("[com.google.protobuf, protoc, 3.0.0, exe, windows-x86_64]", str);
		
		as = ProtocJarMojo.parseArtifactSpec("io.grpc:protoc-gen-grpc-java:1.0.1", "windows-x86_64");
		str = Arrays.asList(as).toString();
		log(str);
		assertEquals("[io.grpc, protoc-gen-grpc-java, 1.0.1, exe, windows-x86_64]", str);
		
		as = ProtocJarMojo.parseArtifactSpec("com.thesamet.scalapb:protoc-gen-scalapb:0.9.0-M5:sh:unix", "windows-x86_64");
		str = Arrays.asList(as).toString();
		log(str);
		assertEquals("[com.thesamet.scalapb, protoc-gen-scalapb, 0.9.0-M5, sh, unix]", str);
		
		as = ProtocJarMojo.parseArtifactSpec("com.thesamet.scalapb:protoc-gen-scalapb:0.9.0-M5:bat:windows", "windows-x86_64");
		str = Arrays.asList(as).toString();
		log(str);
		assertEquals("[com.thesamet.scalapb, protoc-gen-scalapb, 0.9.0-M5, bat, windows]", str);
    }

	@Test
	public void testDetect() throws Exception {
		Properties props = new Properties();
		detect(props, null);
		log(props);
	}

	@Override
	protected void log(String msg) {
		System.out.println(msg);
	}

	@Override
	protected void logProperty(String name, String value) {
		log(name + ": " + value);
	}

    static void log(Object o) {
    	System.out.println(o);
    }
}
