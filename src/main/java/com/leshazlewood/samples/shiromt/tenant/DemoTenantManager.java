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
package com.leshazlewood.samples.shiromt.tenant;

import com.leshazlewood.samples.shiromt.entity.DemoDatabase;

/**
 * Demo TenantManager implementation.  Does not use a data source for simplicity.  In reality, this would invoke
 * a TenantDAO that uses Hibernate or JPA to interact with a database or NoSQL data store.
 */
public class DemoTenantManager implements TenantManager {

    private DemoDatabase database = DemoDatabase.getInstance();

    @Override
    public Tenant findByNameKey(String nameKey) {
        //simulate data store lookup
        return database.getTenants().get(nameKey);
    }

    @Override
    public Tenant getSystemTenant() {
        return database.getTenants().get(Tenant.SYSTEM_TENANT_NAME_KEY);
    }
}
