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

import com.leshazlewood.samples.shiromt.entity.PersistentEntity;

/**
 * Wrapper {@link MutableTenantSource} implementation that guarantees that a call to the internal delegate
 * {@link TenantSource#getTenant() getTenant()} call always returns a non-null
 * value.  If a non-null value is discovered, an {@link IllegalStateException} is thrown.
 *
 * @since 0.1
 */
public class RequiredTenantSource implements MutableTenantSource {

    private MutableTenantSource delegate;

    @Override
    public void setTenant(Tenant tenant) {
        this.delegate.setTenant(tenant);
    }

    @Override
    public Tenant getTenant() {
        Tenant tenant = this.delegate.getTenant();
        if (tenant == null) {
            throw new IllegalStateException("A tenant must always be accessible.");
        }
        if (tenant.getEntityId() <= PersistentEntity.DEFAULT_ENTITY_ID) {
            throw new IllegalStateException("A persistent (already-saved) tenant must always be accessible.");
        }
        return tenant;
    }

    public void setDelegate(MutableTenantSource delegate) {
        this.delegate = delegate;
    }
}
