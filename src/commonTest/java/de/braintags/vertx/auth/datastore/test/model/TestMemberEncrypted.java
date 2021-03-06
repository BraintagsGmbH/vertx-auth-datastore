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
package de.braintags.vertx.auth.datastore.test.model;

import java.util.List;

import de.braintags.vertx.auth.datastore.IAuthenticatable;
import de.braintags.vertx.jomnigate.annotation.Entity;
import de.braintags.vertx.jomnigate.annotation.field.Encoder;
import de.braintags.vertx.jomnigate.annotation.field.Id;

/**
 *
 *
 * @author Michael Remme
 *
 */
@Entity
public class TestMemberEncrypted implements IAuthenticatable {
  @Id
  public String id;
  private List<String> permissions;
  private List<String> roles;
  @Encoder(name = "StandardEncoder")
  private String password;
  private String email;

  public TestMemberEncrypted() {

  }

  public TestMemberEncrypted(final String username, final String password, final List<String> roles,
      final List<String> permissions) {
    this.email = username;
    this.password = password;
    this.roles = roles;
    this.permissions = permissions;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.braintags.vertx.auth.datastore.IAuthenticatable#getEmail()
   */
  @Override
  public String getEmail() {
    return email;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.braintags.vertx.auth.datastore.IAuthenticatable#setEmail(java.lang.String)
   */
  @Override
  public void setEmail(final String email) {
    this.email = email;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.braintags.vertx.auth.datastore.IAuthenticatable#getPassword()
   */
  @Override
  public String getPassword() {
    return password;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.braintags.vertx.auth.datastore.IAuthenticatable#setPassword(java.lang.String)
   */
  @Override
  public void setPassword(final String password) {
    this.password = password;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.braintags.vertx.auth.datastore.IAuthenticatable#getRoles()
   */
  @Override
  public List<String> getRoles() {
    return roles;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.braintags.vertx.auth.datastore.IAuthenticatable#setRoles(java.util.List)
   */
  @Override
  public void setRoles(final List<String> roles) {
    this.roles = roles;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.braintags.vertx.auth.datastore.IAuthenticatable#getPermissions()
   */
  @Override
  public List<String> getPermissions() {
    return permissions;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.braintags.vertx.auth.datastore.IAuthenticatable#setPermissions(java.util.List)
   */
  @Override
  public void setPermissions(final List<String> permissions) {
    this.permissions = permissions;
  }

}
