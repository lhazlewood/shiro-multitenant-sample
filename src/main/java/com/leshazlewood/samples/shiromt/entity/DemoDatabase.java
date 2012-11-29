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
package com.leshazlewood.samples.shiromt.entity;

import com.leshazlewood.samples.shiromt.tenant.Tenant;
import com.leshazlewood.samples.shiromt.user.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simulate a full JPA/Hibernate/NoSQL data solution for demo purposes only (simplifies demo greatly).
 */
public class DemoDatabase {

    private static final DemoDatabase INSTANCE = new DemoDatabase();

    private final Map<String, Tenant> TENANTS;

    private final Map<Long, Map<String,User>> TENANT_USERS;

    private DemoDatabase() {
        TENANTS = createTenants();
        TENANT_USERS = createUsers();
    }

    private Map<String,Tenant> createTenants() {
        Map<String,Tenant> tenants = new ConcurrentHashMap<String, Tenant>();

        Tenant system = new Tenant(1L, Tenant.SYSTEM_TENANT_NAME, Tenant.SYSTEM_TENANT_NAME_KEY, null);
        tenants.put(Tenant.SYSTEM_TENANT_NAME_KEY, system);

        Tenant a = new Tenant(2L, "Customer A", "customera", system);
        tenants.put("customera", a);

        Tenant b = new Tenant(3L, "Customer B", "customerb", system);
        tenants.put("customerb", b);

        return tenants;
    }

    private Map<Long, Map<String,User>> createUsers() {

        Map<Long, Map<String,User>> tenantUsers = new ConcurrentHashMap<Long, Map<String, User>>();

        Tenant tenant = TENANTS.get(Tenant.SYSTEM_TENANT_NAME_KEY);
        Map<String,User> users = new ConcurrentHashMap<String, User>();
        users.put("root", new User("root", "secret", tenant));
        tenantUsers.put(1L, users);

        tenant = TENANTS.get("customera");
        users = new ConcurrentHashMap<String, User>();
        users.put("jsmith", new User("jsmith", "jsmith", tenant));
        users.put("lonestarr", new User("lonestarr", "vespa", tenant));
        tenantUsers.put(2L, users);

        tenant = TENANTS.get("customerb");
        users = new ConcurrentHashMap<String, User>();
        users.put("jsmith", new User("jsmith", "jsmith", tenant));
        users.put("presidentskroob", new User("presidentskroob", "12345", tenant));
        users.put("darkhelmet", new User("darkhelmet", "ludicrousspeed", tenant));
        tenantUsers.put(3L, users);

        return tenantUsers;
    }

    public Map<String,Tenant> getTenants() {
        return TENANTS;
    }

    public Map<Long,Map<String,User>> getTenantUsers() {
        return TENANT_USERS;
    }

    public static DemoDatabase getInstance() {
        return INSTANCE;
    }
}
