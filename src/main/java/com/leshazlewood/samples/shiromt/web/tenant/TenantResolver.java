package com.leshazlewood.samples.shiromt.web.tenant;

import com.leshazlewood.samples.shiromt.tenant.Tenant;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Interface representing the ability to acquire the tenant corresponding to the user/subject currently interacting
 * with the system.
 *
 * @since 0.1
 */
public interface TenantResolver {

    /**
     * Returns the {@link Tenant} of the user/subject interacting with the system
     * based on the specified request/response pair or {@code null} to indicate no tenant could be found.
     *
     * @param request  the inbound ServletRequest
     * @param response the outbound ServletResponse
     * @return the {@link Tenant} of the user/subject interacting with the system
     *         based on the specified request/response pair or {@code null} to indicate no tenant could be found.
     */
    Tenant resolveTenant(ServletRequest request, ServletResponse response);

}
