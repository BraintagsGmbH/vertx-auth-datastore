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
package de.braintags.vertx.auth.datastore.impl;

import java.util.List;

import de.braintags.io.vertx.pojomapper.IDataStore;
import de.braintags.vertx.auth.datastore.IDatastoreAuth;
import de.braintags.vertx.auth.datastore.IHashStrategy;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;

/**
 * 
 * 
 * @author Michael Remme
 * 
 */
public class DataStoreAuth implements IDatastoreAuth {
  private static final io.vertx.core.logging.Logger LOGGER = io.vertx.core.logging.LoggerFactory
      .getLogger(DataStoreAuth.class);

  private IDataStore datastore;
  private String usernameField = DEFAULT_USERNAME_FIELD;
  private String passwordField = DEFAULT_PASSWORD_FIELD;
  private String roleField = DEFAULT_ROLE_FIELD;
  private String permissionField = DEFAULT_PERMISSION_FIELD;
  private String usernameCredentialField = DEFAULT_CREDENTIAL_USERNAME_FIELD;
  private String passwordCredentialField = DEFAULT_CREDENTIAL_PASSWORD_FIELD;
  private String saltField = DEFAULT_SALT_FIELD;
  private String mapperName = null;
  private JsonObject config;

  private IHashStrategy hashStrategy;

  public DataStoreAuth(IDataStore datastore, JsonObject config) {
    this.datastore = datastore;
    this.config = config;
    init();
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.ext.auth.AuthProvider#authenticate(io.vertx.core.json.JsonObject, io.vertx.core.Handler)
   */
  @Override
  public void authenticate(JsonObject arg0, Handler<AsyncResult<User>> resultHandler) {
    resultHandler.handle(Future.failedFuture(new UnsupportedOperationException()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#setCollectionName(java.lang.String)
   */
  @Override
  public IDatastoreAuth setMapperName(String mapperName) {
    this.mapperName = mapperName;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#setUsernameField(java.lang.String)
   */
  @Override
  public IDatastoreAuth setUsernameField(String usernameField) {
    this.usernameField = usernameField;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#setPasswordField(java.lang.String)
   */
  @Override
  public IDatastoreAuth setPasswordField(String passwordField) {
    this.passwordField = passwordField;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#setRoleField(java.lang.String)
   */
  @Override
  public IDatastoreAuth setRoleField(String roleField) {
    this.roleField = roleField;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#setPermissionField(java.lang.String)
   */
  @Override
  public IDatastoreAuth setPermissionField(String permissionField) {
    this.permissionField = permissionField;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#setUsernameCredentialField(java.lang.String)
   */
  @Override
  public IDatastoreAuth setUsernameCredentialField(String usernameCredentialField) {
    this.usernameCredentialField = usernameCredentialField;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#setPasswordCredentialField(java.lang.String)
   */
  @Override
  public IDatastoreAuth setPasswordCredentialField(String passwordCredentialField) {
    this.passwordCredentialField = passwordCredentialField;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#setSaltField(java.lang.String)
   */
  @Override
  public IDatastoreAuth setSaltField(String saltField) {
    this.saltField = saltField;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#getCollectionName()
   */
  @Override
  public String getMapperName() {
    return mapperName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#getUsernameField()
   */
  @Override
  public String getUsernameField() {
    return usernameField;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#getPasswordField()
   */
  @Override
  public String getPasswordField() {
    return passwordField;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#getRoleField()
   */
  @Override
  public String getRoleField() {
    return roleField;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#getPermissionField()
   */
  @Override
  public String getPermissionField() {
    return permissionField;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#getUsernameCredentialField()
   */
  @Override
  public String getUsernameCredentialField() {
    return usernameCredentialField;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#getPasswordCredentialField()
   */
  @Override
  public String getPasswordCredentialField() {
    return passwordCredentialField;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#getSaltField()
   */
  @Override
  public String getSaltField() {
    return saltField;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.braintags.vertx.auth.datastore.DatastoreAuth#setHashStrategy(de.braintags.vertx.auth.datastore.HashStrategy)
   */
  @Override
  public IDatastoreAuth setHashStrategy(IHashStrategy hashStrategy) {
    this.hashStrategy = hashStrategy;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#getHashStrategy()
   */
  @Override
  public IHashStrategy getHashStrategy() {
    return hashStrategy;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#insertUser(java.lang.String, java.lang.String, java.util.List,
   * java.util.List, io.vertx.core.Handler)
   */
  @Override
  public void insertUser(String username, String password, List<String> roles, List<String> permissions,
      Handler<AsyncResult<String>> resultHandler) {
    resultHandler.handle(Future.failedFuture(new UnsupportedOperationException()));
  }

}
