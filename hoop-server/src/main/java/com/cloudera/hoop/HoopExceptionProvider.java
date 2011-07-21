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
package com.cloudera.hoop;

import com.cloudera.lib.service.HadoopException;
import com.cloudera.lib.wsrs.ExceptionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * JAX-RS <code>ExceptionMapper</code> implementation that maps Hoop's
 * exceptions to HTTP status codes.
 */
@Provider
public class HoopExceptionProvider extends ExceptionProvider {
  private static Logger AUDIT_LOG = LoggerFactory.getLogger("hoopaudit");
  private static Logger LOG = LoggerFactory.getLogger(HoopExceptionProvider.class);

  /**
   * Maps different exceptions thrown by Hoop to HTTP status codes.
   * <p/>
   * <ul>
   *   <li>SecurityException : HTTP UNAUTHORIZED</li>
   *   <li>FileNotFoundException : HTTP NOT_FOUND</li>
   *   <li>IOException : INTERNAL_HTTP SERVER_ERROR</li>
   *   <li>UnsupporteOperationException : HTTP BAD_REQUEST</li>
   *   <li>all other exceptions : HTTP INTERNAL_SERVER_ERROR </li>
   * </ul>
   * @param throwable exception thrown.
   * @return mapped HTTP status code
   */
  @Override
  public Response toResponse(Throwable throwable) {
    Response.Status status;
    if (throwable instanceof HadoopException) {
      throwable = throwable.getCause();
    }
    if (throwable instanceof SecurityException) {
      status = Response.Status.UNAUTHORIZED;
    }
    else if (throwable instanceof FileNotFoundException) {
      status = Response.Status.NOT_FOUND;
    }
    else if (throwable instanceof IOException) {
      status = Response.Status.INTERNAL_SERVER_ERROR;
    }
    else if (throwable instanceof UnsupportedOperationException) {
      status = Response.Status.BAD_REQUEST;
    }
    else {
      status = Response.Status.INTERNAL_SERVER_ERROR;
    }
    return createResponse(status, throwable, false);
  }

  /**
   * Logs the HTTP status code and exception in Hoop's log.
   *
   * @param status HTTP status code.
   * @param throwable exception thrown.
   */
  @Override
  protected void log(Response.Status status, Throwable throwable) {
    String method = MDC.get("method");
    String path = MDC.get("path");
    String message = getOneLineMessage(throwable);
    AUDIT_LOG.warn("FAILED [{}:{}] response [{}] {}", new Object[]{method, path, status, message});
    LOG.warn("[{}:{}] response [{}] {}", new Object[]{method, path, status, message, throwable});
  }

}
