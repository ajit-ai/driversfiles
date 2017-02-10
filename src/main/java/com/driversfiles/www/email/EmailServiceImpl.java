package com.driversfiles.www.email;

import com.driversfiles.www.template.TemplateException;
import com.driversfiles.www.template.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Service("emailService")
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private TemplateService templateService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	@Qualifier("mailRecipient")
	private String recipient;

	@Autowired
	@Qualifier("mailFrom")
	private String from;

	@Override
	public void send(EmailType type, Locale locale, Map<String, Object> arguments) throws EmailException {
		send(recipient, type, locale, arguments);
	}

	@Override
	public void send(String to, EmailType type, Locale locale, Map<String, Object> arguments) throws EmailException {
		try {
			Subject subject = new Subject();
			arguments.put("subject", subject);
			String msg = templateService.process(type.toString().toLowerCase(), locale, arguments);
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(to);
			mail.setFrom(from);
			mail.setSubject(subject.getText());
			mail.setText(msg);
			mailSender.send(mail);
		} catch (TemplateException x) {
			throw new EmailException("Error sending email to [" + to + "] of type [" + type
					+ "] in locale [" + locale + "]: " + x.getMessage(), x);
		} catch (MailException x) {
			throw new EmailException("Error sending email to [" + to + "] of type [" + type
					+ "] in locale [" + locale + "]: " + x.getMessage(), x);
		}
	}

	@Override
	public void sendAsync(EmailType type, Locale locale, Map<String, Object> arguments) {
		sendAsync(recipient, type, locale, arguments);
	}

	@Override
	public void sendAsync(final String to, final EmailType type, final Locale locale, final Map<String, Object> arguments) {
		Runnable r = new Runnable() {
			public void run() {
				try {
					send(to, type, locale, arguments);
				} catch (EmailException x) {
					log.error("Error sending email: " + x.getMessage(), x);
				}
			}
		};
		new Thread(r).start();
	}

	public class Subject {

		private String text;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}
}

