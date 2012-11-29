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
 * {@link MutableTenantSource} implementation that uses a delegate instance but will return the default
 * system tenant in case the delegate does not reference a tenant.
 *
 * @author Les Hazlewood
 * @since 0.4
 */
public class SystemFallbackTenantSource implements MutableTenantSource {

    private MutableTenantSource delegate;
    private TenantManager tenantManager;

    @Override
    public void setTenant(Tenant tenant) {
        delegate.setTenant(tenant);
    }

    @Override
    public Tenant getTenant() {
        Tenant tenant = delegate.getTenant();
        if (tenant == null) {
            tenant = tenantManager.getSystemTenant();
        }
        return tenant;
    }

    public void setDelegate(MutableTenantSource delegate) {
        this.delegate = delegate;
    }

    public void setTenantManager(TenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }
}
