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
package com.leshazlewood.samples.shiromt.realm;

import com.leshazlewood.samples.shiromt.user.User;
import com.leshazlewood.samples.shiromt.user.UserRepository;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @since 0.1
 */
public class UserRealm extends AuthorizingRealm {

    private UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;  //authorization not part of this demo
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //This is a tenant-specific lookup, but the Realm has no 'knowledge' of tenants!  This is hidden by the
        //UserRepository implementation and use of the TenantSource concept.
        User user = userRepository.findByUsername(String.valueOf(token.getPrincipal()));

        if (user == null) { //no user by that username in the current tenant:
            return null;
        }

        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
