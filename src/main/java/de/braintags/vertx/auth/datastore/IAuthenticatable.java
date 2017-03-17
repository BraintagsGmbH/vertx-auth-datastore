/*
 * #%L
 * netrelay
 * %%
 * Copyright (C) 2015 Braintags GmbH
 * %%
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * #L%
 */
package de.braintags.vertx.auth.datastore;

import java.util.List;

import de.braintags.vertx.jomnigate.dataaccess.query.IIndexedField;
import de.braintags.vertx.jomnigate.dataaccess.query.impl.IndexedField;

/**
 * The interface defines the base fields for an instance, who is able to authenticate inside the current
 * system
 * 
 * @author Michael Remme
 * 
 */
public interface IAuthenticatable {
  /**
   * The name of the property, which is used to specify a current user
   */
  public static final String CURRENT_USER_PROPERTY = "currentUser";

  public static final IIndexedField EMAIL = new IndexedField("email");

  /**
   * The email to be used for login and registration
   * 
   * @return
   */
  String getEmail();

  /**
   * The email to be used for login and registration
   * 
   * @param email
   */
  void setEmail(String email);

  /**
   * The password to be used for login and registration
   * 
   * @return
   */
  String getPassword();

  /**
   * The password to be used for login and registration
   * 
   * @param password
   */
  void setPassword(String password);

  /**
   * Defines the roles of a member, like admin, users etc.
   * 
   * @return the roles
   */
  List<String> getRoles();

  /**
   * Defines the roles of a member, like admin, users etc.
   * 
   * @param roles
   *          the roles to set
   */
  void setRoles(List<String> roles);

  /**
   * Defines the permissions of a user ( other than roles ) like the access right to a printer for instance
   * 
   * @return
   */
  List<String> getPermissions();

  /**
   * Defines the permissions of a user ( other than roles ) like the access right to a printer for instance
   * 
   * @param permissions
   */
  void setPermissions(List<String> permissions);

}
