/*
 * #%L
 * vertx-pojongo
 * %%
 * Copyright (C) 2015 Braintags GmbH
 * %%
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * #L%
 */
package de.braintags.vertx.auth.datastore.test;

import org.junit.Test;

import de.braintags.vertx.auth.datastore.IDatastoreAuth;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.unit.TestContext;

/**
 * 
 * 
 * @author Michael Remme
 * 
 */

public class NoSaltTest extends DatastoreAuthBaseTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(NoSaltTest.class);

  @Test
  public void testAuthenticate(TestContext context) {
    JsonObject authInfo = new JsonObject();
    authInfo.put(IDatastoreAuth.CREDENTIAL_USERNAME_FIELD, "tim").put(IDatastoreAuth.CREDENTIAL_PASSWORD_FIELD,
        "sausages");
    expectFound(context, authInfo);
  }

  @Test
  public void testAuthenticateFailBadPwd(TestContext context) {
    JsonObject authInfo = new JsonObject();
    authInfo.put(IDatastoreAuth.CREDENTIAL_USERNAME_FIELD, "tim").put(IDatastoreAuth.CREDENTIAL_PASSWORD_FIELD, "eggs");
    expectNotFound(context, authInfo);
  }

  @Test
  public void testAuthenticateFailDublette(TestContext context) {
    JsonObject authInfo = new JsonObject();
    authInfo.put(IDatastoreAuth.CREDENTIAL_USERNAME_FIELD, "Doublette").put(IDatastoreAuth.CREDENTIAL_PASSWORD_FIELD,
        "ps1");
    expectDublette(context, authInfo);
  }

  @Test
  public void testAuthenticateFailBadUser(TestContext context) {
    JsonObject authInfo = new JsonObject();
    authInfo.put(IDatastoreAuth.CREDENTIAL_USERNAME_FIELD, "blah").put(IDatastoreAuth.CREDENTIAL_PASSWORD_FIELD,
        "whatever");
    expectNotFound(context, authInfo);
  }

  @Test
  public void testAuthoriseHasRole(TestContext context) {
    JsonObject authInfo = new JsonObject();
    authInfo.put(IDatastoreAuth.CREDENTIAL_USERNAME_FIELD, "tim").put(IDatastoreAuth.CREDENTIAL_PASSWORD_FIELD,
        "sausages");
    expectAuthorized(context, authInfo, IDatastoreAuth.ROLE_PREFIX + "developer", true);
  }

  @Test
  public void testAuthoriseNotHasRole(TestContext context) {
    JsonObject authInfo = new JsonObject();
    authInfo.put(IDatastoreAuth.CREDENTIAL_USERNAME_FIELD, "tim").put(IDatastoreAuth.CREDENTIAL_PASSWORD_FIELD,
        "sausages");
    expectAuthorized(context, authInfo, IDatastoreAuth.ROLE_PREFIX + "manager", false);
  }

  @Test
  public void testAuthoriseHasPermission(TestContext context) {
    JsonObject authInfo = new JsonObject();
    authInfo.put(IDatastoreAuth.CREDENTIAL_USERNAME_FIELD, "tim").put(IDatastoreAuth.CREDENTIAL_PASSWORD_FIELD,
        "sausages");
    expectAuthorized(context, authInfo, "commit_code", true);
  }

  @Test
  public void testAuthoriseNotHasPermission(TestContext context) {
    JsonObject authInfo = new JsonObject();
    authInfo.put(IDatastoreAuth.CREDENTIAL_USERNAME_FIELD, "tim").put(IDatastoreAuth.CREDENTIAL_PASSWORD_FIELD,
        "sausages");
    expectAuthorized(context, authInfo, "eat_sandwich", false);
  }

}
