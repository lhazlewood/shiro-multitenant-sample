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

/**
 * {@link MutableTenantSource} that stores and retrieves the tenant from a {@link ThreadLocal}.
 *
 * @author Les Hazlewood
 * @since 0.1
 */
public class ThreadLocalTenantSource implements MutableTenantSource {

    private static final ThreadLocal<Tenant> TENANT = new TenantInheritableThreadLocal();

    @Override
    public Tenant getTenant() {
        return TENANT.get();
    }

    @Override
    public void setTenant(Tenant tenant) {
        if (tenant == null) {
            TENANT.remove();
        } else {
            TENANT.set(tenant);
        }
    }

    private static class TenantInheritableThreadLocal extends InheritableThreadLocal<Tenant> {
        @Override
        protected Tenant childValue(Tenant tenant) {
            if (tenant == null) {
                return null;
            }
            Tenant clone = (Tenant) tenant.clone();
            clone.setEntityId(tenant.getEntityId());
            clone.setEntityVersion(tenant.getEntityVersion());
            return clone;
        }
    }
}

