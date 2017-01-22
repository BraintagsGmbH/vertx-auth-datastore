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

import de.braintags.vertx.jomnigate.IDataStore;
import de.braintags.vertx.jomnigate.mapping.IMapper;
import de.braintags.vertx.auth.datastore.impl.DataStoreAuth;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

/**
 * An extension of AuthProvider which is using an {@link IDataStore} as source. The mapper, which is used, must be an
 * implementation of {@link IAuthenticatable}
 *
 * @author Michael Remme
 */
public interface IDatastoreAuth extends AuthProvider {

  /**
   * The name of the property for the username, like it is transported in credentials by method
   * {@link #authenticate(JsonObject, Handler)}
   */
  String CREDENTIAL_USERNAME_FIELD = "username";

  /**
   * The name of the property for the password, like it is transported in credentials by method
   * {@link #authenticate(JsonObject, Handler)}
   */
  String CREDENTIAL_PASSWORD_FIELD = "password";

  /**
   * The property name to be used to set the class name of the mapper inside the config
   */
  String PROPERTY_MAPPER_CLASS_NAME = "mapperClassName";

  /**
   * The prefix which is used by the method {@link User#isAuthorised(String, Handler)} when checking for role access
   */
  String ROLE_PREFIX = "role:";

  /**
   * Creates an instance of IDatastoreAuth by using the given {@link IDataStore} and configuration object. An example
   * for a configuration object:
   *
   * <pre>
   * JsonObject js = new JsonObject();
   * js.put(IDatastoreAuth.PROPERTY_MAPPER_NAME, "class.path.to.member");
   * </pre>
   *
   * @param datastore
   *          the datastore to be used
   * @param config
   *          the configuration object for the current instance
   * @return the created instance of {@link IDatastoreAuth}s
   */
  static IDatastoreAuth create(IDataStore datastore, JsonObject config) {
    return new DataStoreAuth(datastore, config);
  }

  /**
   * Set the name of the mapper to be used. There is no default
   *
   * @param mapper
   *          the mapper to be used for storing and reading user data
   * @return the current instance itself for fluent calls
   */
  @Fluent
  IDatastoreAuth setMapper(IMapper mapper);

  /**
   * The {@link IMapper} used to store User objects inside. Defaults to {@link #DEFAULT_COLLECTION_NAME}
   *
   * @return the collectionName
   */
  IMapper getMapper();

}
