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
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@code Tenant} represents a unique customer and/or organization within the software.  See this
 * <a href="http://en.wikipedia.org/wiki/Multitenancy">Multitenancy WikiPedia article</a> for more on
 * multi-tenant systems.
 *
 * @author Les Hazlewood
 * @since 0.1
 */
@AttributeOverride(name = "entityId", column = @Column(name = "tenant_id", nullable = false))
@Cacheable
@Entity
@Table(name = "tenants", uniqueConstraints = {
        @UniqueConstraint(name = "tenants_name_uq", columnNames = "name"),
        @UniqueConstraint(name = "tenants_uid_uq", columnNames = "uid")
})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tenant extends PersistentEntity {

    public static final String SYSTEM_TENANT_NAME = "System Tenant";
    public static final String SYSTEM_TENANT_NAME_KEY = "system";
    public static final int MAX_NAME_KEY_LENGTH = 63; //subdomains can't be longer than 63 chars

    @Column(name = "is_allowed_subtenants", nullable = false)
    private boolean allowedSubtenants;

    @OneToMany(targetEntity = Tenant.class, cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Tenant> children;

    @Column(name = "name", length = DEFAULT_VARCHAR_LENGTH, nullable = false, unique = true)
    private String name; //publicly recognizable name, e.g. the client company name

    @Column(name = "name_key", length = MAX_NAME_KEY_LENGTH, nullable = false, unique = true) //subdomains can't be longer than 63 chars
    private String nameKey; //human-friendly public identifier, likely to be used as a subdomain

    @ForeignKey(name = "tenants_parent_tenant_fk")
    @JoinColumn(name = "parent_id", nullable = true)
    @ManyToOne(targetEntity = Tenant.class, fetch = FetchType.LAZY)
    private Tenant parent;

    public Tenant() {
        children = new HashSet<Tenant>();
    }

    public Tenant(String name) {
        this.name = name;
    }

    //for demo needs only
    public Tenant(long entityId, String name, String nameKey, Tenant parent) {
        super.setEntityId(entityId);
        this.name = name;
        this.nameKey = nameKey;
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        return getNameKey().hashCode();
    }

    @Override
    public StringBuilder toStringBuilder() {
        return new StringBuilder(getNameKey()).append(" (").append(getEntityId()).append(")");
    }

    @Override
    protected boolean onEquals(PersistentEntity e) {
        if (e instanceof Tenant) {
            Tenant t = (Tenant) e;
            return getNameKey().equals(t.getNameKey());
        }
        return false;
    }

    public Set<Tenant> getChildren() {
        return children;
    }

    public void setChildren(Set<Tenant> children) {
        this.children = children;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public Tenant getParent() {
        return parent;
    }

    public void setParent(Tenant parent) {
        this.parent = parent;
    }

    public boolean isAllowedSubtenants() {
        return allowedSubtenants;
    }

    public void setAllowedSubtenants(boolean allowedSubtenants) {
        this.allowedSubtenants = allowedSubtenants;
    }

    public boolean isRootTenant() {
        return getParent() == null && SYSTEM_TENANT_NAME_KEY.equals(getNameKey());
    }
}

