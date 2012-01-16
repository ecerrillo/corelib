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

package eu.europeana.corelib.web.email.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.web.email.EmailBuilder;
import eu.europeana.corelib.web.email.model.EmailConfig;
import eu.europeana.corelib.web.exception.EmailServiceException;

/**
 * @author Willem-Jan Boogerd <www.eledge.net/contact>
 */
public class EmailBuilderImpl implements EmailBuilder {

	private static final String TEMPLATE_NAME_AFFIX_TEXT = ".txt.vm";
	private static final String TEMPLATE_NAME_AFFIX_HTML = ".html.vm";

	private String emailTo;
	
	private String emailFrom;
	
	private String subject;

	private Map<String, Object> model;

	@Resource
	private VelocityEngine engine;

	@Resource(name = "corelib_web_emailConfigs")
	Map<String, EmailConfig> configs;

	EmailConfig config;

	@Override
	public void prepare(MimeMessage mimeMessage) throws Exception {

		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
		
		message.setTo(emailTo);
		message.setFrom(emailFrom);
		message.setSubject(subject);

		String text = VelocityEngineUtils.mergeTemplateIntoString(engine, config.getTemplate()
				+ TEMPLATE_NAME_AFFIX_TEXT, model);
		
		String html = VelocityEngineUtils.mergeTemplateIntoString(engine, config.getTemplate()
				+ TEMPLATE_NAME_AFFIX_HTML, model);
		
		BodyPart textPart = new MimeBodyPart();
		textPart.setText(text);
		
		BodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(html, "text/html");
		
		message.getMimeMultipart().addBodyPart(textPart);
		message.getMimeMultipart().addBodyPart(htmlPart);
		
	}

	@Override
	public void setModel(Map<String, Object> model) {
		this.model = model;
	}
	
	@Override
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	
	@Override
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Override
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	@Override
	public void setTemplate(String template) throws EmailServiceException {
		if (configs.containsKey(template)) {
			this.config = configs.get(template);
			this.emailTo = config.getEmailTo();
			this.emailFrom = config.getEmailFrom();
			this.subject = config.getSubject();
		} else {
			throw new EmailServiceException(ProblemType.INVALIDARGUMENTS);
		}
	}

}