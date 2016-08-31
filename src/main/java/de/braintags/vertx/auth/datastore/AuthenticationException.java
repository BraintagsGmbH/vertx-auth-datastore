/*
 * #%L
 * vertx-auth-datastore
 * %%
 * Copyright (C) 2016 Braintags GmbH
 * %%
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * #L%
 */
package de.braintags.vertx.auth.datastore;

/**
 * Signals an error inside the authentication process <br>
 * 
 * @author Michael Remme
 */
public class AuthenticationException extends RuntimeException {

  public AuthenticationException() {
  }

  public AuthenticationException(String message) {
    super(message);
  }

  public AuthenticationException(Throwable cause) {
    super(cause);
  }

  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

  public AuthenticationException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
