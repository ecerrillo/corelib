/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package eu.europeana.corelib.definitions.users;

/**
 * Enumerates the roles
 */

public enum Role {
	ROLE_USER, // general portal user, no dashboard access
	ROLE_CONTENT_TESTER, // a user of the special sandbox version of the dashboard
	ROLE_TRANSLATOR, // translation tab only
	ROLE_EDITOR, // carousel + proposed search terms
	ROLE_ADMINISTRATOR, // all rights, except record editor
	ROLE_GOD // all rights
}
