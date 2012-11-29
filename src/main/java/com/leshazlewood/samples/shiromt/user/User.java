/*
 * Copyright (c) 2012 Les Hazlewood
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leshazlewood.samples.shiromt.user;

import com.leshazlewood.samples.shiromt.entity.PersistentEntity;
import com.leshazlewood.samples.shiromt.tenant.Tenant;

/**
 * Simple User class.
 */
public class User extends PersistentEntity {

    private String username; //in this demo, usernames are unique per tenant, not globally.

    private String password;

    private Tenant tenant; //users belong to a tenant.

    public User(){}

    public User(String username, String password, Tenant tenant) {
        this.username = username;
        this.password = password;
        this.tenant = tenant;
    }

    @Override
    public int hashCode() {
        int result = getTenant().hashCode();
        result = 31 * result + this.username.hashCode();
        return result;
    }

    @Override
    protected boolean onEquals(PersistentEntity e) {
        if (e instanceof User) {
            User u = (User)e;
            //equality is based on both tenant + username (since usernames are unique per tenant only):
            return getTenant().equals(u.getTenant()) && getUsername().equals(u.getUsername());
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
