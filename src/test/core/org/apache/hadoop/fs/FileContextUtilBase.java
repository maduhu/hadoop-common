/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.fs;

import java.util.Arrays;

import org.apache.hadoop.util.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.apache.hadoop.fs.FileContextTestHelper.*;

/**
 * <p>
 * A collection of Util tests for the {@link FileContext#util()}.
 * This test should be used for testing an instance of {@link FileContext#util()}
 *  that has been initialized to a specific default FileSystem such a
 *  LocalFileSystem, HDFS,S3, etc.
 * </p>
 * <p>
 * To test a given {@link FileSystem} implementation create a subclass of this
 * test and override {@link #setUp()} to initialize the <code>fc</code> 
 * {@link FileContext} instance variable.
 * 
 * </p>
 */
public abstract class FileContextUtilBase {
  protected FileContext fc;
  
  {
    try {
      ((org.apache.commons.logging.impl.Log4JLogger)FileSystem.LOG).getLogger()
      .setLevel(org.apache.log4j.Level.DEBUG);
    } catch(Exception e) {
      System.out.println("Cannot change log level\n"
          + StringUtils.stringifyException(e));
    }
  }

  @Before
  public void setUp() throws Exception {
    fc.mkdir(getTestRootPath(fc), FileContext.DEFAULT_PERM, true);
  }

  @After
  public void tearDown() throws Exception {
    fc.delete(getTestRootPath(fc), true);
  }
  
  @Test
  public void testFcCopy() throws Exception{
    final String ts = "some random text";
    Path file1 = getTestRootPath(fc, "file1");
    Path file2 = getTestRootPath(fc, "file2");
    
    writeFile(fc, file1, ts.getBytes());
    assertTrue(fc.util().exists(file1));
    fc.util().copy(file1, file2);

    // verify that newly copied file2 exists
    assertTrue("Failed to copy file2  ", fc.util().exists(file2));
    // verify that file2 contains test string
    assertTrue("Copied files does not match ",Arrays.equals(ts.getBytes(),
        readFile(fc,file2,ts.getBytes().length)));
  }
}