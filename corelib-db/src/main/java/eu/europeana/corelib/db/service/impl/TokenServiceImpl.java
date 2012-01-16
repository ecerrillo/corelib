/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they 
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "License");
 * you may not use this work except in compliance with the
 * License.
 * You may obtain a copy of the License at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */

package eu.europeana.corelib.db.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import eu.europeana.corelib.db.entity.TokenImpl;
import eu.europeana.corelib.db.exception.DatabaseException;
import eu.europeana.corelib.db.service.TokenService;
import eu.europeana.corelib.db.service.abstracts.AbstractServiceImpl;
import eu.europeana.corelib.definitions.db.entity.Token;
import eu.europeana.corelib.definitions.exception.ProblemType;

/**
 * @author Willem-Jan Boogerd <www.eledge.net/contact>
 * 
 * @see eu.europeana.corelib.db.service.TokenService
 */
@Transactional
public class TokenServiceImpl extends AbstractServiceImpl<Token> implements
		TokenService {

	/**
	 * Overriding the findByID() method to handle expiration
	 */
	@Override
	public Token findByID(Serializable id) {
		Token token;
		try {
			token = super.findByID(id);
		} catch (DatabaseException e) {
			return null;
		}
		if (token != null) {
			long age = System.currentTimeMillis() - token.getCreated().getTime();
	        if (age <= MAX_TOKEN_AGE) {
	        	return token;
	        }
	        dao.delete(token);
		}
		return null;
	}
	
	@Override
	public Token create(String email) throws DatabaseException {
		if (StringUtils.isBlank(email)) {
			throw new DatabaseException(ProblemType.INVALIDARGUMENTS);
		}
		TokenImpl token = new TokenImpl();
		token.setCreated(new Date());
		token.setEmail(email);
		token.setToken(createRandomToken());
		return dao.insert(token);
	}
	
	@Override
	public String createRandomToken() {
		String token = UUID.randomUUID().toString();
		return StringUtils.remove(token, "-");
	}

}