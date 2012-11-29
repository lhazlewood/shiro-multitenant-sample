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

import com.leshazlewood.samples.shiromt.entity.AbstractRepository;
import com.leshazlewood.samples.shiromt.entity.DemoDatabase;

import java.util.Map;

/**
 * Simulates what would probably be a JPA or Hibernate (or NoSQL-based) DAO object in a real project.
 *
 * @since 0.1
 */
public class DemoUserRepository extends AbstractRepository implements UserRepository {

    private DemoDatabase database = DemoDatabase.getInstance();

    @Override
    public User findByUsername(String username) {
        //Notice how we're using the tenantId property from the parent class (that in turn uses a TenantSource):
        Map<String,User> users = database.getTenantUsers().get(getCurrentTenantId()); //get users for the current tenant
        return users.get(username); //get the user within the tenant
    }
}
