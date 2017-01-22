/*-
 * #%L
 * vertx-auth-datastore
 * %%
 * Copyright (C) 2017 Braintags GmbH
 * %%
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * #L%
 */

/**
 * Authentication across an {@link de.braintags.vertx.jomnigate.IDataStore}
 * Crypt of passwords is ruled by mapper specifications by using the
 * {@link de.braintags.vertx.jomnigate.annotation.field.Encoder} annotation
 *
 * @author Michael Remme
 *
 */
@Document(fileName = "index.adoc")
package de.braintags.vertx.auth.datastore;

import io.vertx.docgen.Document;
