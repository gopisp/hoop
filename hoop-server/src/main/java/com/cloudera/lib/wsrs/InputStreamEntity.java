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
package com.cloudera.lib.wsrs;

import com.cloudera.lib.io.IOUtils;

import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamEntity implements StreamingOutput {
  private InputStream is;
  private long offset;
  private long len;

  public InputStreamEntity(InputStream is, long offset, long len) {
    this.is = is;
    this.offset = offset;
    this.len = len;
  }

  public InputStreamEntity(InputStream is) {
    this(is, 0, -1);
  }

  @Override
  public void write(OutputStream os) throws IOException {
    IOUtils.copy(is, os, offset, len);
  }
}
