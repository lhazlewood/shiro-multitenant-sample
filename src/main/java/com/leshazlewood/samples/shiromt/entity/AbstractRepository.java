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
import com.leshazlewood.samples.shiromt.tenant.TenantSource;

/**
 * Abstract base Repository (aka DAO) for persistent entity CRUD operations.
 */
public class AbstractRepository {

    private TenantSource tenantSource; //need to know tenant when performing queries

    public TenantSource getTenantSource() {
        return tenantSource;
    }

    public void setTenantSource(TenantSource tenantSource) {
        this.tenantSource = tenantSource;
    }

    protected Tenant getCurrentTenant() {
        return getTenantSource().getTenant();
    }

    protected Long getCurrentTenantId() {
        return getTenantSource().getTenant().getEntityId();
    }
}
