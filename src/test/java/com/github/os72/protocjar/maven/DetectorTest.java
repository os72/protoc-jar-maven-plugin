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

import java.util.Properties;

import org.junit.Test;

import com.github.os72.protocjar.PlatformDetector;

public class DetectorTest extends PlatformDetector
{
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
