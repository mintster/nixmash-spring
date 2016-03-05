/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nixmash.springdata.mvc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.repository.UserRepository;

public class SocialSignInAdapter implements SignInAdapter {

	@Autowired
	UserRepository userRepository;
	
	@Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
       User user = userRepository.findByUsername(localUserId);
		SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(user,user.getPassword(), user.getAuthorities()));
        return null;
    }

}
