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

import de.braintags.vertx.auth.datastore.IAuthenticatable;
import de.braintags.vertx.auth.datastore.IDatastoreAuth;
import de.braintags.vertx.jomnigate.mapping.IField;
import de.braintags.vertx.jomnigate.mapping.IMapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

/**
 * An implementation of {@link User} for use with {@link IDatastoreAuth} is using the Object, which was loaded from the
 * datastore
 * 
 * @author Michael Remme
 */
public class DatastoreUser extends AbstractUser {
  private JsonObject principal;
  private IDatastoreAuth datastoreAuth;
  private IAuthenticatable userObject;

  public DatastoreUser() {
    // default constructor
  }

  public DatastoreUser(String username, IDatastoreAuth datastoreAuth) {
    this.principal = new JsonObject().put("email", username);
    this.datastoreAuth = datastoreAuth;
  }

  public DatastoreUser(IAuthenticatable userObject, IDatastoreAuth datastoreAuth) {
    this.userObject = userObject;
    this.datastoreAuth = datastoreAuth;
    createPrincipal();
  }

  private void createPrincipal() {
    this.principal = new JsonObject();
    IMapper mapper = datastoreAuth.getMapper();
    IField idField = mapper.getIdField().getField();
    Object idValue = idField.getPropertyAccessor().readData(userObject);
    principal.put(idField.getName(), idValue).put("email", userObject.getEmail());
  }

  /**
   * Get the {@link IAuthenticatable} which was loaded from the datastore during authentication
   * 
   * @return
   */
  public IAuthenticatable getAuthenticatable() {
    return userObject;
  }

  @Override
  public void doIsPermitted(String permissionOrRole, Handler<AsyncResult<Boolean>> resultHandler) {
    if (permissionOrRole != null && permissionOrRole.startsWith(IDatastoreAuth.ROLE_PREFIX)) {
      String roledef = permissionOrRole.substring(IDatastoreAuth.ROLE_PREFIX.length());
      doHasRole(roledef, resultHandler);
    } else {
      doHasPermission(permissionOrRole, resultHandler);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.ext.auth.User#principal()
   */
  @Override
  public JsonObject principal() {
    return principal;
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.ext.auth.User#setAuthProvider(io.vertx.ext.auth.AuthProvider)
   */
  @Override
  public void setAuthProvider(AuthProvider authProvider) {
    this.datastoreAuth = (IDatastoreAuth) authProvider;
  }

  /**
   * check wether the current user has the given role
   * 
   * @param role
   *          the role to be checked
   * @param resultHandler
   *          resultHandler gets true, if role is valid, otherwise false
   */
  protected void doHasRole(String role, Handler<AsyncResult<Boolean>> resultHandler) {
    try {
      resultHandler
          .handle(Future.succeededFuture(userObject.getRoles() != null && userObject.getRoles().contains(role)));
    } catch (Exception e) {
      resultHandler.handle(Future.failedFuture(e));
    }
  }

  /**
   * Check wether the current user has the given permission
   * 
   * @param permission
   *          the permission to be checked
   * @param resultHandler
   *          resulthandler gets true, if permission is valid, otherwise false
   * 
   */
  protected void doHasPermission(String permission, Handler<AsyncResult<Boolean>> resultHandler) {
    try {
      resultHandler.handle(Future
          .succeededFuture(userObject.getPermissions() != null && userObject.getPermissions().contains(permission)));
    } catch (Exception e) {
      resultHandler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public String toString() {
    return principal.toString();
  }
}
