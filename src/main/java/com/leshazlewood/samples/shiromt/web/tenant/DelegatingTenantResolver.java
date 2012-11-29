package com.leshazlewood.samples.shiromt.web.tenant;

import com.leshazlewood.samples.shiromt.tenant.Tenant;
import com.leshazlewood.samples.shiromt.tenant.TenantManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * @since 0.16
 */
public class DelegatingTenantResolver implements TenantResolver {
    
    private static final Logger log = LoggerFactory.getLogger(DelegatingTenantResolver.class);
    
    private List<TenantResolver> tenantResolvers;

    private TenantManager tenantManager;
    
    public DelegatingTenantResolver() {
        this.tenantResolvers = Collections.emptyList();
    }

    @Override
    public Tenant resolveTenant(ServletRequest request, ServletResponse response) {
        Tenant tenant = null;

        for (TenantResolver resolver : this.tenantResolvers) {
            tenant = resolver.resolveTenant(request, response);
            if (tenant != null) {
                break;
            }
        }
        
        if (tenant == null) {
            log.debug("Unable to resolve tenant from current request.  Defaulting to the system tenant.");
            tenant = tenantManager.getSystemTenant();
        }

        return tenant;
    }

    public void setTenantResolvers(List<TenantResolver> tenantResolvers) {
        this.tenantResolvers = tenantResolvers;
    }

    public void setTenantManager(TenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }
}
