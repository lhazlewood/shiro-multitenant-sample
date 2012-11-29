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
package com.leshazlewood.samples.shiromt.web.tenant;

import com.leshazlewood.samples.shiromt.tenant.MutableTenantSource;
import com.leshazlewood.samples.shiromt.tenant.Tenant;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Servlet {@code Filter} that ensures a tenant is associated and then removed before and after request
 * processing, respectively.
 *
 * @since 0.1
 */
public class TenantFilter extends AdviceFilter {

    private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);

    private TenantResolver tenantResolver;
    private MutableTenantSource tenantSource;

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        tenantSource.setTenant(null);
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Tenant tenant = tenantResolver.resolveTenant(request, response);
        if (tenant == null) {
            throw new IllegalStateException("TenantResolver must resolve a non-null tenant.  The system tenant " +
                "should be returned as the default if a request-specific tenant could not be resolved.");
        }

        log.trace("Resolved tenant {}", tenant);

        tenantSource.setTenant(tenant);
        return true;
    }

    public void setTenantResolver(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    public void setTenantSource(MutableTenantSource tenantSource) {
        this.tenantSource = tenantSource;
    }
}
