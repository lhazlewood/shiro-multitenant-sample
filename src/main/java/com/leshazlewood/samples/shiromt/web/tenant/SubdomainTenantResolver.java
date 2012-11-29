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

import com.leshazlewood.samples.shiromt.tenant.Tenant;
import com.leshazlewood.samples.shiromt.tenant.TenantManager;
import org.apache.shiro.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @since 0.9
 */
public class SubdomainTenantResolver implements TenantResolver {

    private String baseDomain;
    private TenantManager tenantManager;

    @Override
    public Tenant resolveTenant(ServletRequest request, ServletResponse response) {
        String key = resolveTenantKey(request);

        Tenant tenant = null;
        if (StringUtils.hasLength(key)) {
            tenant = tenantManager.findByNameKey(key);
        }
        if (tenant == null) {
            tenant = tenantManager.getSystemTenant();
        }

        return tenant;
    }

    protected String resolveTenantKey(ServletRequest request) {
        String hostHeader = request.getServerName();
        if (hostHeader != null) {
            hostHeader = hostHeader.toLowerCase();
        }
        String base = this.baseDomain;
        String key = null;

        if (base == null && StringUtils.hasLength(hostHeader)) {
            //infer the base from the host header:
            assert hostHeader != null;
            String[] tokens = hostHeader.split("\\.");
            if (tokens.length > 2) {
                StringBuilder sb  = new StringBuilder();
                int subcount = tokens.length - 2;
                for (int i = 0; i < subcount; i++) {
                    if (i > 0) {
                        sb.append(".");
                    }
                    sb.append(tokens[i]);
                }
                key = sb.toString();
            }
        }

        if (key == null && StringUtils.hasLength(base) && StringUtils.hasLength(hostHeader)) {
            assert hostHeader != null;
            int index = hostHeader.lastIndexOf(base);
            if (index >= 0) {
                key = hostHeader.substring(0, index);
            }
        }
        return key;
    }

    public void setBaseDomain(String baseDomain) {
        //performance optimization - add the '.' prefix here in case there isn't one
        //this way, we don't need to do the checking during the resolveTenant invocations
        //This is a minor adjustment, but if servicing thousands of requests, this will help over over time
        String base = StringUtils.clean(baseDomain);
        if (base != null) {
            base = base.toLowerCase();
        }
        if (base != null && !base.startsWith(".")) {
            base = "." + base;
        }
        this.baseDomain = base;
    }

    public void setTenantManager(TenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }
}
