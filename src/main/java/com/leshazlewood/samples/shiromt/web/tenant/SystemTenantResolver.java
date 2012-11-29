package com.leshazlewood.samples.shiromt.web.tenant;

import com.leshazlewood.samples.shiromt.tenant.Tenant;
import com.leshazlewood.samples.shiromt.tenant.TenantManager;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * {@link TenantResolver} that will ignore the request/response pair and always resolve to the installation's
 * single (system) tenant.  Typically used for single-tenant installations and in testing.  Production-quality
 * implementations will probably resolve a tenant based on subdomain or other request-specific properties.
 *
 * @author Les Hazlewood
 * @since 0.1
 */
public class SystemTenantResolver implements TenantResolver {

    private TenantManager tenantManager;

    @Override
    public Tenant resolveTenant(ServletRequest request, ServletResponse response) {
        return tenantManager.getSystemTenant();
    }

    public void setTenantManager(TenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }
}
