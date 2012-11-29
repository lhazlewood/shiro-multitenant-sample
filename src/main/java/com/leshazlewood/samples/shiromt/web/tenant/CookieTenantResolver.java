package com.leshazlewood.samples.shiromt.web.tenant;

import com.leshazlewood.samples.shiromt.tenant.Tenant;
import com.leshazlewood.samples.shiromt.tenant.TenantManager;
import org.apache.shiro.web.servlet.Cookie;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Resolves a tenant based on a previously set tenant cookie that stores the tenant nameKey.
 * @since 0.16
 */
public class CookieTenantResolver implements TenantResolver {

    private TenantManager tenantManager;

    private Cookie tenantNameKeyCookie;

    @Override
    public Tenant resolveTenant(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String nameKey = tenantNameKeyCookie.readValue(httpRequest, httpResponse);
        return tenantManager.findByNameKey(nameKey);
    }

    public void setTenantManager(TenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }

    public void setTenantNameKeyCookie(Cookie tenantNameKeyCookie) {
        this.tenantNameKeyCookie = tenantNameKeyCookie;
    }
}
