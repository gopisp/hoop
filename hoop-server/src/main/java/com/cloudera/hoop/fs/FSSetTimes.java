/*
 * Copyright (c) 2011, Cloudera, Inc. All Rights Reserved.
 *
 * Cloudera, Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"). You may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 */
package com.cloudera.hoop.fs;

import com.cloudera.lib.service.Hadoop;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * Executor that performs a set-times Hadoop files system operation.
 */
public class FSSetTimes implements Hadoop.FileSystemExecutor<Void> {
  private Path path;
  private long mTime;
  private long aTime;

  /**
   * Creates a set-times executor.
   *
   * @param path path to set the times.
   * @param mTime modified time to set.
   * @param aTime access time to set.
   */
  public FSSetTimes(String path, long mTime, long aTime) {
    this.path = new Path(path);
    this.mTime = mTime;
    this.aTime = aTime;
  }

  /**
   * Executes the filesystem operation.
   *
   * @param fs filesystem instance to use.
   * @return void.
   * @throws IOException thrown if an IO error occured.
   */
  @Override
  public Void execute(FileSystem fs) throws IOException {
    fs.setTimes(path, mTime, aTime);
    return null;
  }

}
