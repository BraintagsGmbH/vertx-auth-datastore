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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import de.braintags.vertx.auth.datastore.HashSaltStyle;
import de.braintags.vertx.auth.datastore.IHashStrategy;
import io.vertx.core.VertxException;
import io.vertx.ext.auth.User;

/**
 * Implementation of HashStrategy which is using SHA-512 as crypt
 * 
 * @author Michael Remme
 */

public class DefaultHashStrategy implements IHashStrategy {
  private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

  private HashSaltStyle saltStyle;
  // Used only if SaltStyle#External is used
  private String externalSalt;

  /**
   * 
   */
  public DefaultHashStrategy() {
    saltStyle = HashSaltStyle.COLUMN;
  }

  /**
   * This method is called, if the strategy shall be {@link io.vertx.ext.auth.mongo.HashSaltStyle#EXTERNAL}
   * 
   * @param externalSalt
   *          the external salt to be used
   */
  public DefaultHashStrategy(String externalSalt) {
    saltStyle = HashSaltStyle.EXTERNAL;
    this.externalSalt = externalSalt;
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.ext.auth.mongo.HashStrategy#computeHash(java.lang.String, io.vertx.ext.auth.User)
   */
  @Override
  public String computeHash(String password, User user) {
    switch (saltStyle) {
    case NO_SALT:
      return password;
    case COLUMN:
    case EXTERNAL:
      String salt = getSalt(user);
      return computeHash(password, salt, "SHA-512");
    default:
      throw new UnsupportedOperationException("Not existing, saltstyle " + saltStyle);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.ext.auth.mongo.HashStrategy#getStoredPwd(io.vertx.ext.auth.User)
   */
  @Override
  public String getStoredPwd(User user) {
    return ((MongoUser) user).getPassword();
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.ext.auth.mongo.HashStrategy#getSalt(io.vertx.ext.auth.User)
   */
  @Override
  public String getSalt(User user) {
    switch (saltStyle) {
    case NO_SALT:
      return null;
    case COLUMN:
      return ((MongoUser) user).getSalt();
    case EXTERNAL:
      return externalSalt;
    default:
      throw new UnsupportedOperationException("Not existing, saltstyle " + saltStyle);
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.ext.auth.mongo.HashStrategy#setSaltStyle(io.vertx.ext.auth.mongo.HashStrategy.SaltStyle)
   */
  @Override
  public void setSaltStyle(HashSaltStyle saltStyle) {
    this.saltStyle = saltStyle;
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.ext.auth.mongo.HashStrategy#getSaltStyle()
   */
  @Override
  public HashSaltStyle getSaltStyle() {
    return saltStyle;
  }

  private String computeHash(String password, String salt, String algo) {
    try {
      MessageDigest md = MessageDigest.getInstance(algo);
      String concat = (salt == null ? "" : salt) + password;
      byte[] bHash = md.digest(concat.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(bHash);
    } catch (NoSuchAlgorithmException e) {
      throw new VertxException(e);
    }
  }

  /**
   * Generate a salt
   * 
   * @return the generated salt
   */
  public static String generateSalt() {
    final Random r = new SecureRandom();
    byte[] salt = new byte[32];
    r.nextBytes(salt);
    return bytesToHex(salt);
  }

  private static String bytesToHex(byte[] bytes) {
    char[] chars = new char[bytes.length * 2];
    for (int i = 0; i < bytes.length; i++) {
      int x = 0xFF & bytes[i];
      chars[i * 2] = HEX_CHARS[x >>> 4];
      chars[1 + i * 2] = HEX_CHARS[0x0F & x];
    }
    return new String(chars);
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.ext.auth.mongo.HashStrategy#setExternalSalt(java.lang.String)
   */
  @Override
  public void setExternalSalt(String externalSalt) {
    this.externalSalt = externalSalt;
  }

}
