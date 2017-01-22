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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.braintags.vertx.jomnigate.dataaccess.query.IQuery;
import de.braintags.vertx.auth.datastore.IAuthenticatable;
import de.braintags.vertx.auth.datastore.IDatastoreAuth;
import de.braintags.vertx.auth.datastore.test.model.TestMemberEncrypted;
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

public class EncryptedPasswordTest extends DatastoreAuthBaseTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(EncryptedPasswordTest.class);
  public static final String SALT = "saltandpepper";

  public static final String CHECKPASSWORD = "ps1";
  public static final String CHECKPASSWORD2 = "ps2";
  public static final String CHECKPASSWORD3 = "sausages";

  @Test
  public void testAuthenticate(TestContext context) {
    JsonObject authInfo = new JsonObject();
    authInfo.put(IDatastoreAuth.CREDENTIAL_USERNAME_FIELD, "tim").put(IDatastoreAuth.CREDENTIAL_PASSWORD_FIELD,
        CHECKPASSWORD3);
    expectFound(context, authInfo);
  }

  @Test
  public void testAuthenticateFailBadPwd(TestContext context) {
    JsonObject authInfo = new JsonObject();
    authInfo.put(IDatastoreAuth.CREDENTIAL_USERNAME_FIELD, "tim").put(IDatastoreAuth.CREDENTIAL_PASSWORD_FIELD, "eggs");
    expectNotFound(context, authInfo);
  }

  @Override
  protected Class getMapperClass() {
    return TestMemberEncrypted.class;
  }

  @Override
  public void initDemoData(TestContext context) {
    LOGGER.info("Create demodata");
    clearTable(context, TestMemberEncrypted.class);
    saveRecords(context, createUserList());
    IQuery<TestMemberEncrypted> q = getDataStore(context).createQuery(TestMemberEncrypted.class);
    find(context, q, 5);

    IQuery<TestMemberEncrypted> q2 = getDataStore(context).createQuery(TestMemberEncrypted.class);
    q2.setSearchCondition(q2.isEqual("email", "Michael"));
    TestMemberEncrypted m = (TestMemberEncrypted) findFirst(context, q2);
    context.assertNotEquals(CHECKPASSWORD, m.getPassword());
    LOGGER.info("Create demodata FINISHED");
  }

  @Override
  protected List<IAuthenticatable> createUserList() {
    List<IAuthenticatable> users = new ArrayList<>();
    users.add(new TestMemberEncrypted("Michael", CHECKPASSWORD, null, null));
    users.add(new TestMemberEncrypted("Doublette", CHECKPASSWORD, null, null));
    users.add(new TestMemberEncrypted("Doublette", CHECKPASSWORD2, null, null));
    users.add(new TestMemberEncrypted("Doublette", CHECKPASSWORD2, null, null));

    users.add(new TestMemberEncrypted("tim", CHECKPASSWORD3, Arrays.asList("morris_dancer", "superadmin", "developer"),
        Arrays.asList("commit_code", "merge_pr", "do_actual_work", "bang_sticks")));
    return users;
  }

}
