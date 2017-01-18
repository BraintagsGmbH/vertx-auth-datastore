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
/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package de.braintags.vertx.auth.datastore.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.braintags.io.vertx.pojomapper.dataaccess.query.IQuery;
import de.braintags.io.vertx.pojomapper.testdatastore.DatastoreBaseTest;
import de.braintags.io.vertx.util.ErrorObject;
import de.braintags.vertx.auth.datastore.AuthenticationException;
import de.braintags.vertx.auth.datastore.IAuthenticatable;
import de.braintags.vertx.auth.datastore.IDatastoreAuth;
import de.braintags.vertx.auth.datastore.test.model.TestMember;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

/**
 * @author mremme
 */

public abstract class DatastoreAuthBaseTest extends DatastoreBaseTest {
  private static final io.vertx.core.logging.Logger LOGGER = io.vertx.core.logging.LoggerFactory
      .getLogger(DatastoreAuthBaseTest.class);

  public static final String USERNAME = "email";
  public static final String PASSWORD = "password";

  private IDatastoreAuth datastoreAuth;

  protected IDatastoreAuth getAuth(TestContext context) {
    if (datastoreAuth == null) {
      initDemoData(context);
      JsonObject config = new JsonObject().put(IDatastoreAuth.PROPERTY_MAPPER_CLASS_NAME, getMapperClass().getName());
      datastoreAuth = IDatastoreAuth.create(getDataStore(context), config);
    }
    return datastoreAuth;
  }

  protected Class getMapperClass() {
    return TestMember.class;
  }

  /**
   * Initialize the demo data needed for the tests
   * 
   * @throws Exception
   *           any Exception by submethods
   */
  public void initDemoData(TestContext context) {
    clearTable(context, TestMember.class);
    saveRecords(context, createUserList());
    IQuery<TestMember> q = getDataStore(context).createQuery(TestMember.class);
    find(context, q, 5);
  }

  protected List<IAuthenticatable> createUserList() {
    List<IAuthenticatable> users = new ArrayList<>();
    users.add(new TestMember("Michael", "ps1", null, null));
    users.add(new TestMember("Doublette", "ps1", null, null));
    users.add(new TestMember("Doublette", "ps2", null, null));
    users.add(new TestMember("Doublette", "ps2", null, null));

    users.add(new TestMember("tim", "sausages", Arrays.asList("morris_dancer", "superadmin", "developer"),
        Arrays.asList("commit_code", "merge_pr", "do_actual_work", "bang_sticks")));
    return users;
  }

  protected void expectFound(TestContext context, JsonObject authInfo) {
    Async async = context.async();
    ErrorObject err = new ErrorObject<>(null);
    try {
      getAuth(context).authenticate(authInfo, result -> {
        try {
          if (result.failed()) {
            err.setThrowable(result.cause());
          } else {
            context.assertNotNull(result.result(), "Did not find user; User is null");
          }
        } finally {
          async.complete();
        }
      });
    } catch (Exception e) {
      err.setThrowable(e);
      async.complete();
    }
    async.await();
    if (err.isError()) {
      throw err.getRuntimeException();
    }
  }

  protected void expectNotFound(TestContext context, JsonObject authInfo) {
    Async async = context.async();
    ErrorObject err = new ErrorObject<>(null);
    try {
      getAuth(context).authenticate(authInfo, result -> {
        try {
          if (result.failed()) {
            Throwable ex = result.cause();
            context.assertTrue(ex instanceof AuthenticationException,
                "expected AuthenticationException when a user was not found");
            String message = ex.getMessage();
            context.assertTrue(
                message.contains("No account found for user") || message.contains("Invalid username/password"),
                "wrong message: " + message);
          } else {
            err.setThrowable(new IllegalArgumentException("expected AuthenticationException cause user was not found"));
          }
        } finally {
          async.complete();
        }
      });
    } catch (Exception e) {
      err.setThrowable(e);
      async.complete();
    }
    async.await();
    if (err.isError()) {
      throw err.getRuntimeException();
    }
  }

  protected void expectDublette(TestContext context, JsonObject authInfo) {
    Async async = context.async();
    getAuth(context).authenticate(authInfo, result -> {
      try {
        if (result.failed()) {
          context.assertTrue(result.cause() instanceof AuthenticationException,
              "expected AuthenticationException when a user was not found");
          context.assertTrue(result.cause().getMessage().contains("More than one user found"),
              "wrong message: " + result.cause().getMessage());
        } else {
          context.fail("expected AuthenticationException cause user was not found");
        }
      } finally {
        async.complete();
      }
    });
    async.await();
  }

  protected void expectAuthorized(TestContext context, JsonObject authInfo, String roleOrPermission, boolean expect) {
    Async async = context.async();
    getAuth(context).authenticate(authInfo, result -> {
      if (result.failed()) {
        context.fail(result.cause());
        async.complete();
      } else {
        User user = result.result();
        context.assertNotNull(user, "Did not find user; User is null");
        user.isAuthorised(roleOrPermission, res -> {
          try {
            if (res.failed()) {
              context.fail(res.cause());
            } else {
              if (expect) {
                context.assertTrue(res.result(), "Expected user to be authorized");
              } else {
                context.assertFalse(res.result(), "Expected user to be NOT authorized");
              }
            }
          } finally {
            async.complete();
          }
        });
      }
    });
    async.await();
  }

}
