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

import io.vertx.codegen.annotations.VertxGen;

/**
 * Password hash salt configuration.
 */
@VertxGen
public enum HashSaltStyle {

  /**
   * Password hashes are not salted
   */
  NO_SALT,

  /**
   * Salt is in a separate column for each user in the database
   */
  COLUMN,

  /**
   * Salt is NOT stored in the database, but defined as external value like application preferences or so
   */
  EXTERNAL
}
