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
package com.leshazlewood.samples.shiromt.web;

import com.leshazlewood.samples.shiromt.tenant.MutableTenantSource;
import com.leshazlewood.samples.shiromt.tenant.Tenant;
import com.leshazlewood.samples.shiromt.tenant.TenantManager;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Ensures that a login attempt targets the appropriate tenant.
 * @since 0.1
 */
public class TenantFormAuthenticationFilter extends FormAuthenticationFilter {

    private MutableTenantSource tenantSource;
    private TenantManager tenantManager;

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

        Tenant currentTenant = tenantSource.getTenant();

        //we only need to target a tenant during login if (and only if) we haven't already resolved a tenant, either
        //by looking at an existing identified Subject (in which case they should always be tied to the tenant they
        // originally logged into) or, if the Subject is anonymous, by looking at a subdomain name for example.

        //We know a tenant has already been resolved if the current tenant is _not_ the root/system tenant:
        if (!currentTenant.isRootTenant()) {
            return super.executeLogin(request, response);
        }

        //default/system tenant, let's see if the login is targeting a particular tenant:

        String nameKey = request.getParameter("tenantNameKey"); //nameKey aka subdomain in most apps

        if (StringUtils.hasText(nameKey)) {

            Tenant specified = tenantManager.findByNameKey(nameKey);

            if (specified != null) { //login targeted at a specific tenant:

                try {
                    tenantSource.setTenant(specified); //set the targeted tenant for downstream components
                    return super.executeLogin(request, response);
                } finally {
                    //always restore the current/original tenant:
                    tenantSource.setTenant(currentTenant);
                }
            }
        }

        return super.executeLogin(request, response);
    }

    public void setTenantSource(MutableTenantSource tenantSource) {
        this.tenantSource = tenantSource;
    }

    public void setTenantManager(TenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }
}
