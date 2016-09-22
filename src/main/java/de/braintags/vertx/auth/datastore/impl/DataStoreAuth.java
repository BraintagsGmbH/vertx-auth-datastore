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
import de.braintags.io.vertx.pojomapper.dataaccess.query.IQuery;
import de.braintags.io.vertx.pojomapper.exception.NoSuchMapperException;
import de.braintags.io.vertx.pojomapper.mapping.IMapper;
import de.braintags.io.vertx.pojomapper.util.QueryHelper;
import de.braintags.io.vertx.util.exception.InitException;
import de.braintags.io.vertx.util.exception.ParameterRequiredException;
import de.braintags.io.vertx.util.security.crypt.IEncoder;
import de.braintags.vertx.auth.datastore.AuthenticationException;
import de.braintags.vertx.auth.datastore.IAuthenticatable;
import de.braintags.vertx.auth.datastore.IDatastoreAuth;
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
  private JsonObject config;
  private IMapper mapper;
  private IEncoder encoder;

  public DataStoreAuth(IDataStore datastore, JsonObject config) {
    this.datastore = datastore;
    this.config = config;
    init();
  }

  /**
   * Initializes the current provider by using the current config object
   */
  private void init() {
    initMapper();
  }

  /**
   * 
   */
  @SuppressWarnings("rawtypes")
  private void initMapper() {
    String mapperName = config.getString(PROPERTY_MAPPER_CLASS_NAME, null);
    if (mapperName == null) {
      throw new ParameterRequiredException(PROPERTY_MAPPER_CLASS_NAME);
    } else {
      try {
        Class<? extends IAuthenticatable> mapperClass = (Class<? extends IAuthenticatable>) Class.forName(mapperName);
        LOGGER.info("Mapper for auth is " + mapperName);
        this.mapper = datastore.getMapperFactory().getMapper(mapperClass);
        encoder = mapper.getField("password").getEncoder();
      } catch (ClassNotFoundException e) {
        throw new NoSuchMapperException(mapperName, e);
      } catch (ClassCastException e) {
        throw new InitException("mapper must be an instance of IAuthenticatable");
      }
    }
  }

  @Override
  public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
    String username = authInfo.getString(CREDENTIAL_USERNAME_FIELD);
    String password = authInfo.getString(CREDENTIAL_PASSWORD_FIELD);
    if (username == null) {
      resultHandler
          .handle(Future.failedFuture(new ParameterRequiredException("Username must be set for authentication.")));
      return;
    } else if (password == null) {
      resultHandler
          .handle(Future.failedFuture(new ParameterRequiredException("Password must be set for authentication.")));
      return;
    } else {
      AuthToken token = new AuthToken(username, password);
      IQuery<IAuthenticatable> query = createQuery(username);
      QueryHelper.executeToList(query, res -> {
        try {
          if (res.succeeded()) {
            LOGGER.debug("found users: " + res.result().size());
            User user = handleSelection(res, token);
            resultHandler.handle(Future.succeededFuture(user));
          } else {
            resultHandler.handle(Future.failedFuture(res.cause()));
          }
        } catch (Exception e) {
          LOGGER.warn("", e);
          resultHandler.handle(Future.failedFuture(e));
        }
      });
    }
  }

  /**
   * Examine the selection of found users and return one, if password is fitting,
   * 
   * @param resultList
   * @param authToken
   * @return
   */
  private User handleSelection(AsyncResult<List<?>> resultList, AuthToken authToken) throws AuthenticationException {
    switch (resultList.result().size()) {
    case 0:
      throw new AuthenticationException("No account found for user [" + authToken.username + "]");

    case 1:
      DatastoreUser user = new DatastoreUser((IAuthenticatable) resultList.result().get(0), this);
      if (examinePassword(user, authToken))
        return user;
      else {
        throw new AuthenticationException("Invalid username/password [" + authToken.username + "]");
      }

    default:
      // More than one row returned!
      String message = "More than one user found for [" + authToken.username + "( " + resultList.result().size()
          + " )]. Usernames must be unique.";
      throw new AuthenticationException(message);
    }

  }

  /**
   * Examine the given user object. Returns true, if object fits the given authentication
   * 
   * @param user
   * @param authToken
   * @return
   */
  private boolean examinePassword(DatastoreUser user, AuthToken authToken) {
    String storedPassword = user.getAuthenticatable().getPassword();
    String givenPassword = authToken.password;
    if (encoder != null) {
      return encoder.matches(givenPassword, storedPassword);
    } else {
      return storedPassword != null && storedPassword.equals(givenPassword);
    }
  }

  /**
   * The default implementation uses the usernameField as search field
   * 
   * @param username
   * @return
   */
  @SuppressWarnings("unchecked")
  protected IQuery<IAuthenticatable> createQuery(String username) {
    IQuery<IAuthenticatable> query = (IQuery<IAuthenticatable>) datastore.createQuery(mapper.getMapperClass());
    query.field("email").is(username);
    return query;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#setCollectionName(java.lang.String)
   */
  @Override
  public IDatastoreAuth setMapper(IMapper mapper) {
    this.mapper = mapper;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.vertx.auth.datastore.DatastoreAuth#getCollectionName()
   */
  @Override
  public IMapper getMapper() {
    return mapper;
  }

  /**
   * The incoming data from an authentication request
   * 
   * @author mremme
   */
  static class AuthToken {
    String username;
    String password;

    AuthToken(String username, String password) {
      this.username = username;
      this.password = password;
    }
  }
}
