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

import java.util.List;

import de.braintags.io.vertx.pojomapper.IDataStore;
import de.braintags.vertx.auth.datastore.impl.DataStoreAuth;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;

/**
 * An extension of AuthProvider which is using an {@link IDataStore}
 * 
 * @author Michael Remme
 */
@VertxGen
public interface IDatastoreAuth extends AuthProvider {

  /**
   * The property name to be used to set the name of the mapper inside the config
   */
  String PROPERTY_MAPPER_NAME = "mapperName";

  /**
   * The property name to be used to set the name of the field, where the username is stored inside
   */
  String PROPERTY_USERNAME_FIELD = "usernameField";

  /**
   * The property name to be used to set the name of the field, where the roles are stored inside
   */
  String PROPERTY_ROLE_FIELD = "roleField";

  /**
   * The property name to be used to set the name of the field, where the permissions are stored inside
   */
  String PROPERTY_PERMISSION_FIELD = "permissionField";

  /**
   * The property name to be used to set the name of the field, where the password is stored inside
   */
  String PROPERTY_PASSWORD_FIELD = "passwordField";

  /**
   * The property name to be used to set the name of the field, where the username for the credentials is stored inside
   */
  String PROPERTY_CREDENTIAL_USERNAME_FIELD = "usernameCredentialField";

  /**
   * The property name to be used to set the name of the field, where the password for the credentials is stored inside
   */
  String PROPERTY_CREDENTIAL_PASSWORD_FIELD = "passwordCredentialField";

  /**
   * The property name to be used to set the name of the field, where the SALT is stored inside
   */
  String PROPERTY_SALT_FIELD = "saltField";

  /**
   * The property name to be used to set the name of the field, where the salt style is stored inside
   * 
   * @see HashSaltStyle
   */
  String PROPERTY_SALT_STYLE = "saltStyle";

  /**
   * The default name of the property for the username, like it is stored in mongodb
   */
  String DEFAULT_USERNAME_FIELD = "username";

  /**
   * The default name of the property for the password, like it is stored in mongodb
   */
  String DEFAULT_PASSWORD_FIELD = "password";

  /**
   * The default name of the property for the roles, like it is stored in mongodb. Roles are expected to be saved as
   * JsonArray
   */
  String DEFAULT_ROLE_FIELD = "roles";

  /**
   * The default name of the property for the permissions, like it is stored in mongodb. Permissions are expected to be
   * saved as JsonArray
   */
  String DEFAULT_PERMISSION_FIELD = "permissions";

  /**
   * The default name of the property for the username, like it is transported in credentials by method
   * {@link #authenticate(JsonObject, Handler)}
   */
  String DEFAULT_CREDENTIAL_USERNAME_FIELD = DEFAULT_USERNAME_FIELD;

  /**
   * The default name of the property for the password, like it is transported in credentials by method
   * {@link #authenticate(JsonObject, Handler)}
   */
  String DEFAULT_CREDENTIAL_PASSWORD_FIELD = DEFAULT_PASSWORD_FIELD;

  /**
   * The default name of the property for the salt field
   */
  String DEFAULT_SALT_FIELD = "salt";

  /**
   * The prefix which is used by the method {@link User#isAuthorised(String, Handler)} when checking for role access
   */
  String ROLE_PREFIX = "role:";

  /**
   * Creates an instance of IDatastoreAuth by using the given {@link IDataStore} and configuration object. An example
   * for a
   * configuration object:
   * 
   * <pre>
   * JsonObject js = new JsonObject();
   * js.put(IDatastoreAuth.PROPERTY_MAPPER_NAME, "Member");
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
   * @param mapperName
   *          the name of the mapper to be used for storing and reading user data
   * @return the current instance itself for fluent calls
   */
  @Fluent
  IDatastoreAuth setMapperName(String mapperName);

  /**
   * Set the name of the field to be used for the username. Defaults to {@link #DEFAULT_USERNAME_FIELD}
   * 
   * @param fieldName
   *          the name of the field to be used
   * @return the current instance itself for fluent calls
   */
  @Fluent
  IDatastoreAuth setUsernameField(String fieldName);

  /**
   * Set the name of the field to be used for the password Defaults to {@link #DEFAULT_PASSWORD_FIELD}
   * 
   * @param fieldName
   *          the name of the field to be used
   * @return the current instance itself for fluent calls
   */
  @Fluent
  IDatastoreAuth setPasswordField(String fieldName);

  /**
   * Set the name of the field to be used for the roles. Defaults to {@link #DEFAULT_ROLE_FIELD}. Roles are expected to
   * be saved as JsonArray
   * 
   * @param fieldName
   *          the name of the field to be used
   * @return the current instance itself for fluent calls
   */
  @Fluent
  IDatastoreAuth setRoleField(String fieldName);

  /**
   * Set the name of the field to be used for the permissions. Defaults to {@link #DEFAULT_PERMISSION_FIELD}.
   * Permissions are expected to be saved as JsonArray
   * 
   * @param fieldName
   *          the name of the field to be used
   * @return the current instance itself for fluent calls
   */
  @Fluent
  IDatastoreAuth setPermissionField(String fieldName);

  /**
   * Set the name of the field to be used as property for the username in the method
   * {@link #authenticate(JsonObject, io.vertx.core.Handler)}. Defaults to {@link #DEFAULT_CREDENTIAL_USERNAME_FIELD}
   * 
   * @param fieldName
   *          the name of the field to be used
   * @return the current instance itself for fluent calls
   */
  @Fluent
  IDatastoreAuth setUsernameCredentialField(String fieldName);

  /**
   * Set the name of the field to be used as property for the password of credentials in the method
   * {@link #authenticate(JsonObject, io.vertx.core.Handler)}. Defaults to {@link #DEFAULT_CREDENTIAL_PASSWORD_FIELD}
   * 
   * @param fieldName
   *          the name of the field to be used
   * @return the current instance itself for fluent calls
   */
  @Fluent
  IDatastoreAuth setPasswordCredentialField(String fieldName);

  /**
   * Set the name of the field to be used for the salt. Only used when {@link IHashStrategy#setSaltStyle(HashSaltStyle)}
   * is
   * set to {@link HashSaltStyle#COLUMN}
   * 
   * @param fieldName
   *          the name of the field to be used
   * @return the current instance itself for fluent calls
   */
  @Fluent
  IDatastoreAuth setSaltField(String fieldName);

  /**
   * The name of the collection used to store User objects inside. Defaults to {@link #DEFAULT_COLLECTION_NAME}
   * 
   * @return the collectionName
   */
  String getMapperName();

  /**
   * Get the name of the field to be used for the username. Defaults to {@link #DEFAULT_USERNAME_FIELD}
   * 
   * @return the usernameField
   */
  String getUsernameField();

  /**
   * Get the name of the field to be used for the password Defaults to {@link #DEFAULT_PASSWORD_FIELD}
   * 
   * @return the passwordField
   */
  String getPasswordField();

  /**
   * Get the name of the field to be used for the roles. Defaults to {@link #DEFAULT_ROLE_FIELD}. Roles are expected to
   * be saved as JsonArray
   * 
   * @return the roleField
   */
  String getRoleField();

  /**
   * Get the name of the field to be used for the permissions. Defaults to {@link #DEFAULT_PERMISSION_FIELD}.
   * Permissions are expected to be saved as JsonArray
   * 
   * @return the permissionField
   */
  String getPermissionField();

  /**
   * Get the name of the field to be used as property for the username in the method
   * {@link #authenticate(JsonObject, io.vertx.core.Handler)}. Defaults to {@link #DEFAULT_CREDENTIAL_USERNAME_FIELD}
   * 
   * @return the usernameCredentialField
   */
  String getUsernameCredentialField();

  /**
   * Get the name of the field to be used as property for the password of credentials in the method
   * {@link #authenticate(JsonObject, io.vertx.core.Handler)}. Defaults to {@link #DEFAULT_CREDENTIAL_PASSWORD_FIELD}
   * 
   * @return the passwordCredentialField
   */
  String getPasswordCredentialField();

  /**
   * Get the name of the field to be used for the salt. Only used when {@link IHashStrategy#setSaltStyle(HashSaltStyle)}
   * is
   * set to {@link HashSaltStyle#COLUMN}
   * 
   * @return the saltField
   */
  String getSaltField();

  /**
   * The HashStrategy which is used by the current instance
   * 
   * @param hashStrategy
   *          the {@link IHashStrategy} to be set
   * @return the current instance itself for fluent calls
   * 
   */
  @Fluent
  IDatastoreAuth setHashStrategy(IHashStrategy hashStrategy);

  /**
   * The HashStrategy which is used by the current instance
   * 
   * @return the defined instance of {@link IHashStrategy}
   */
  IHashStrategy getHashStrategy();

  /**
   * Insert a new user into mongo in the convenient way
   * 
   * @param username
   *          the username to be set
   * @param password
   *          the passsword in clear text, will be adapted following the definitions of the defined
   *          {@link IHashStrategy}
   * @param roles
   *          a list of roles to be set
   * @param permissions
   *          a list of permissions to be set
   * @param resultHandler
   *          the ResultHandler will be provided with the id of the generated record
   */
  void insertUser(String username, String password, List<String> roles, List<String> permissions,
      Handler<AsyncResult<String>> resultHandler);

}
