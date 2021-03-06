/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.guacamole.auth.jdbc.user;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import org.apache.guacamole.auth.jdbc.base.ObjectModel;

/**
 * Object representation of a Guacamole user, as represented in the database.
 *
 * @author Michael Jumper
 */
public class UserModel extends ObjectModel {

    /**
     * The SHA-256 hash of the password and salt.
     */
    private byte[] passwordHash;

    /**
     * The 32-byte random binary password salt that was appended to the
     * password prior to hashing.
     */
    private byte[] passwordSalt;

    /**
     * The time this user's password was last reset.
     */
    private Timestamp passwordDate;

    /**
     * Whether the user account is disabled. Disabled accounts exist and can
     * be modified, but cannot be used.
     */
    private boolean disabled;

    /**
     * Whether the user's password is expired. If a user's password is expired,
     * it must be changed immediately upon login, and the account cannot be
     * used until this occurs.
     */
    private boolean expired;

    /**
     * The time each day after which this user account may be used, stored in
     * local time according to the value of timeZone.
     */
    private Time accessWindowStart;

    /**
     * The time each day after which this user account may NOT be used, stored
     * in local time according to the value of timeZone.
     */
    private Time accessWindowEnd;

    /**
     * The day after which this account becomes valid and usable. Account
     * validity begins at midnight of this day. Time information within the
     * Date object is ignored.
     */
    private Date validFrom;

    /**
     * The day after which this account can no longer be used. Account validity
     * ends at midnight of the day following this day. Time information within
     * the Date object is ignored.
     */
    private Date validUntil;

    /**
     * The ID of the time zone used for all time comparisons for this user.
     * Both accessWindowStart and accessWindowEnd values will use this time
     * zone, as will checks for whether account validity dates have passed. If
     * unset, the server's local time zone is used.
     */
    private String timeZone;

    /**
     * Creates a new, empty user.
     */
    public UserModel() {
    }

    /**
     * Returns the hash of this user's password and password salt. This may be
     * null if the user was not retrieved from the database, and setPassword()
     * has not yet been called.
     *
     * @return
     *     The hash of this user's password and password salt.
     */
    public byte[] getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the hash of this user's password and password salt. This is
     * normally only set upon retrieval from the database, or through a call
     * to the higher-level setPassword() function.
     *
     * @param passwordHash
     *     The hash of this user's password and password salt.
     */
    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns the random salt that was used when generating this user's
     * password hash. This may be null if the user was not retrieved from the
     * database, and setPassword() has not yet been called.
     *
     * @return
     *     The random salt that was used when generating this user's password
     *     hash.
     */
    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * Sets the random salt that was used when generating this user's password
     * hash. This is normally only set upon retrieval from the database, or
     * through a call to the higher-level setPassword() function.
     *
     * @param passwordSalt
     *     The random salt used when generating this user's password hash.
     */
    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    /**
     * Returns the date that this user's password was last set/reset. This
     * value is required to be manually updated whenever the user's password is
     * changed; it will not be automatically updated by the database.
     *
     * @return
     *     The date that this user's password was last set/reset.
     */
    public Timestamp getPasswordDate() {
        return passwordDate;
    }

    /**
     * Sets the date that this user's password was last set/reset. This
     * value is required to be manually updated whenever the user's password is
     * changed; it will not be automatically updated by the database.
     *
     * @param passwordDate
     *     The date that this user's password was last set/reset.
     */
    public void setPasswordDate(Timestamp passwordDate) {
        this.passwordDate = passwordDate;
    }

    /**
     * Returns whether the user has been disabled. Disabled users are not
     * allowed to login. Although their account data exists, all login attempts
     * will fail as if the account does not exist.
     *
     * @return
     *     true if the account is disabled, false otherwise.
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Sets whether the user is disabled. Disabled users are not allowed to
     * login. Although their account data exists, all login attempts will fail
     * as if the account does not exist.
     *
     * @param disabled
     *     true if the account should be disabled, false otherwise.
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Returns whether the user's password has expired. If a user's password is
     * expired, it must be immediately changed upon login. A user account with
     * an expired password cannot be used until the password has been changed.
     *
     * @return
     *     true if the user's password has expired, false otherwise.
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * Sets whether the user's password is expired. If a user's password is
     * expired, it must be immediately changed upon login. A user account with
     * an expired password cannot be used until the password has been changed.
     *
     * @param expired
     *     true to expire the user's password, false otherwise.
     */
    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    /**
     * Returns the time each day after which this user account may be used. The
     * time returned will be local time according to the time zone set with
     * setTimeZone().
     *
     * @return
     *     The time each day after which this user account may be used, or null
     *     if this restriction does not apply.
     */
    public Time getAccessWindowStart() {
        return accessWindowStart;
    }

    /**
     * Sets the time each day after which this user account may be used. The
     * time given must be in local time according to the time zone set with
     * setTimeZone().
     *
     * @param accessWindowStart
     *     The time each day after which this user account may be used, or null
     *     if this restriction does not apply.
     */
    public void setAccessWindowStart(Time accessWindowStart) {
        this.accessWindowStart = accessWindowStart;
    }

    /**
     * Returns the time each day after which this user account may NOT be used.
     * The time returned will be local time according to the time zone set with
     * setTimeZone().
     *
     * @return
     *     The time each day after which this user account may NOT be used, or
     *     null if this restriction does not apply.
     */
    public Time getAccessWindowEnd() {
        return accessWindowEnd;
    }

    /**
     * Sets the time each day after which this user account may NOT be used.
     * The time given must be in local time according to the time zone set with
     * setTimeZone().
     *
     * @param accessWindowEnd
     *     The time each day after which this user account may NOT be used, or
     *     null if this restriction does not apply.
     */
    public void setAccessWindowEnd(Time accessWindowEnd) {
        this.accessWindowEnd = accessWindowEnd;
    }

    /**
     * Returns the day after which this account becomes valid and usable.
     * Account validity begins at midnight of this day. Any time information
     * within the returned Date object must be ignored.
     *
     * @return
     *     The day after which this account becomes valid and usable, or null
     *     if this restriction does not apply.
     */
    public Date getValidFrom() {
        return validFrom;
    }

    /**
     * Sets the day after which this account becomes valid and usable. Account
     * validity begins at midnight of this day. Any time information within
     * the provided Date object will be ignored.
     *
     * @param validFrom
     *     The day after which this account becomes valid and usable, or null
     *     if this restriction does not apply.
     */
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    /**
     * Returns the day after which this account can no longer be used. Account
     * validity ends at midnight of the day following this day. Any time
     * information within the returned Date object must be ignored.
     *
     * @return
     *     The day after which this account can no longer be used, or null if
     *     this restriction does not apply.
     */
    public Date getValidUntil() {
        return validUntil;
    }

    /**
     * Sets the day after which this account can no longer be used. Account
     * validity ends at midnight of the day following this day. Any time
     * information within the provided Date object will be ignored.
     *
     * @param validUntil
     *     The day after which this account can no longer be used, or null if
     *     this restriction does not apply.
     */
    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    /**
     * Returns the Java ID of the time zone to be used for all time comparisons
     * for this user. This ID should correspond to a value returned by
     * TimeZone.getAvailableIDs(). If unset or invalid, the server's local time
     * zone must be used.
     *
     * @return
     *     The ID of the time zone to be used for all time comparisons, which
     *     should correspond to a value returned by TimeZone.getAvailableIDs().
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the Java ID of the time zone to be used for all time comparisons
     * for this user. This ID should correspond to a value returned by
     * TimeZone.getAvailableIDs(). If unset or invalid, the server's local time
     * zone will be used.
     *
     * @param timeZone
     *     The ID of the time zone to be used for all time comparisons, which
     *     should correspond to a value returned by TimeZone.getAvailableIDs().
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

}
