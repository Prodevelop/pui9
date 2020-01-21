package es.prodevelop.pui9.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.components.PuiApplicationContext;

/**
 * Utility class to send emails. This class is a singleton, and it's configured
 * using a file named "pui_email.properties" that should exist in the resources
 * folder of the application
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiMailSender {

	private final Log logger = LogFactory.getLog(this.getClass());

	private static final EmailAttachment[] NO_ATTACHMENTS = null;
	private static final String NO_TO = null;
	private static final String NO_CC = null;
	private static final String NO_BCC = null;
	private static final String EMAIL_SEPARATOR = ";";
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

	private static PuiMailSender singleton;

	public static PuiMailSender getSingleton() {
		if (singleton == null) {
			singleton = new PuiMailSender();
		}

		return singleton;
	}

	private IPuiVariableService variableService;

	private String from;

	private Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
	private JavaMailSenderImpl mailSender;
	private VelocityEngine engine;

	/**
	 * The properties set in the file should be the following:
	 * <ul>
	 * <li><b>pui.mail.from</b>: the email used in the From tag</li>
	 * <li><b>pui.mail.smtp.host</b>: the SMTP server</li>
	 * <li><b>pui.mail.smtp.port</b>: the port of the SMTP server (by default
	 * 587)</li>
	 * <li><b>pui.mail.smtp.user</b>: the user to authenticate in the SMTP
	 * server</li>
	 * <li><b>pui.mail.smtp.pass</b>: the password of the SMTP server user</li>
	 * <li><b>pui.mail.smtp.default.encoding</b>: the encoding to be used in the
	 * email (by default UTF8</li>
	 * <li><b>pui.mail.smtp.auth</b>: if authentication is needed (by default
	 * true)</li>
	 * <li><b>pui.mail.smtp.starttls.enable</b>: if TLS o SSL connection is needed
	 * (by default true)</li>
	 * </ul>
	 */
	private PuiMailSender() {
		variableService = PuiApplicationContext.getInstance().getBean(IPuiVariableService.class);

		mailSender = new JavaMailSenderImpl();
		engine = new VelocityEngine();
		engine.addProperty("runtime.log", FileUtils.getTempDirectory().toString() + File.separator + "velocity.log");
		engine.init();
	}

	public void configureSender() {
		if (variableService == null) {
			return;
		}

		mailSender = new JavaMailSenderImpl();

		String smtpHost = variableService.getVariable(PuiVariableValues.MAIL_SMTP_HOST.name());
		if (IPuiVariable.VARIABLE_WITH_NO_VALUE.equals(smtpHost)) {
			smtpHost = null;
		}

		Integer smtpPort = variableService.getVariable(Integer.class, PuiVariableValues.MAIL_SMTP_PORT.name());

		String smtpUser = variableService.getVariable(PuiVariableValues.MAIL_SMTP_USER.name());
		if (IPuiVariable.VARIABLE_WITH_NO_VALUE.equals(smtpUser)) {
			smtpUser = null;
		}

		String smtpPass = variableService.getVariable(PuiVariableValues.MAIL_SMTP_PASS.name());
		if (IPuiVariable.VARIABLE_WITH_NO_VALUE.equals(smtpPass)) {
			smtpPass = null;
		}

		Boolean smtpAuth = variableService.getVariable(Boolean.class, PuiVariableValues.MAIL_SMTP_AUTH.name());
		if (smtpAuth == null) {
			smtpAuth = false;
		}

		Boolean smtpStarttlsEnable = variableService.getVariable(Boolean.class,
				PuiVariableValues.MAIL_SMTP_STARTTLS_ENABLE.name());
		if (smtpStarttlsEnable == null) {
			smtpStarttlsEnable = false;
		}

		from = variableService.getVariable(PuiVariableValues.MAIL_FROM.name());

		mailSender.setHost(smtpHost);
		mailSender.setPort(smtpPort);
		mailSender.setUsername(smtpUser);
		mailSender.setPassword(smtpPass);
		mailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());
		mailSender.getJavaMailProperties().put("mail.smtp.auth", smtpAuth);
		mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", smtpStarttlsEnable);
	}

	/**
	 * Change the "from" email with the one provided
	 * 
	 * @param from The new "from" email
	 */
	public void setFrom(String from) {
		if (StringUtils.isEmpty(from)) {
			throw new IllegalArgumentException("Missing from email address");
		}
		this.from = from;
	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param from        The sender email
	 * @param to          The target email. You can use multiple emails, separating
	 *                    them using ";"
	 * @param bcc         The blind carbon copy. You can use multiple emails,
	 *                    separating them using ";"
	 * @param cc          The carbon copy. You can use multiple emails, separating
	 *                    them using ";"
	 * @param subject     The subject of the email
	 * @param text        The content of the email
	 * @param textHTML    The content of the email as HTML
	 * @param isHtml      If the content of the "text" parameter is HTML
	 * @param attachments An array of attachments
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(String from, String to, String bcc, String cc, String subject, String text, String textHTML,
			boolean isHtml, EmailAttachment... attachments) throws Exception {
		singleton.configureSender();
		if (StringUtils.isEmpty(from)) {
			from = this.from;
		}

		if (StringUtils.isEmpty(from)) {
			throw new IllegalArgumentException("Missing from email address");
		}
		if (StringUtils.isEmpty(to) && StringUtils.isEmpty(bcc) && StringUtils.isEmpty(cc)) {
			throw new IllegalArgumentException("Missing receiver email address");
		}

		// normalizar las direcciones de correo
		to = normalizeAndValidateEmails(to);
		bcc = normalizeAndValidateEmails(bcc);
		cc = normalizeAndValidateEmails(cc);

		// En caso de que from tenga mas de una direccion se obtiene la primera
		from = normalizeAndValidateEmails(from);
		from = from.split(EMAIL_SEPARATOR)[0];

		logger.info("Sending email from '" + from + "' to '" + to + "' (bcc: '" + bcc + "' cc: '" + cc + "') [through '"
				+ mailSender.getHost() + ":" + mailSender.getPort() + "', username: '" + mailSender.getUsername()
				+ "']");

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			// from
			helper.setFrom(from);

			// to
			String[] arrayTo = to.split(EMAIL_SEPARATOR);
			for (int i = 0; i < arrayTo.length; i++) {
				String destinatario = arrayTo[i].trim();
				if (StringUtils.hasText(destinatario)) {
					if (i == 0) {
						helper.setTo(destinatario);
					} else {
						helper.addTo(destinatario);
					}
				}
			}

			// bcc
			if (bcc != null) {
				String[] arrayBcc = bcc.split(EMAIL_SEPARATOR);
				for (int i = 0; i < arrayBcc.length; i++) {
					String lStrBcc = arrayBcc[i].trim();
					if (StringUtils.hasText(lStrBcc)) {
						if (i == 0) {
							helper.setBcc(lStrBcc);
						} else {
							helper.addBcc(lStrBcc);
						}
					}
				}
			}

			// cc
			if (cc != null) {
				String[] arrayCc = cc.split(EMAIL_SEPARATOR);
				for (int i = 0; i < arrayCc.length; i++) {
					String lStrCc = arrayCc[i].trim();
					if (StringUtils.hasText(lStrCc)) {
						if (i == 0) {
							helper.setCc(lStrCc);
						} else {
							helper.addCc(lStrCc);
						}
					}
				}
			}

			// subject
			helper.setSubject(subject);

			// content
			if (StringUtils.hasText(textHTML)) {
				helper.setText(text, textHTML);
			} else {
				helper.setText(text, isHtml);
			}

			// attachments
			if (attachments != null) {
				for (EmailAttachment attachment : attachments) {
					InputStreamSource byteArrayResource = new ByteArrayResource(attachment.getContent());
					if (StringUtils.hasText(attachment.getContentType())) {
						helper.addAttachment(attachment.getFilename(), byteArrayResource, attachment.getContentType());
					} else {
						helper.addAttachment(attachment.getFilename(), byteArrayResource);
					}
				}
			}
		} catch (MessagingException e) {
			logger.error("Error adding attachment to the message", e);
			throw e;
		}

		try {
			this.mailSender.send(message);
		} catch (MailException e) {
			logger.error("Error sending the message", e);
			throw e;
		}

		logger.info("The email was sent correctly");
	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param from        The sender email
	 * @param to          The target email. You can use multiple emails, separating
	 *                    them using ";"
	 * @param bcc         The blind carbon copy. You can use multiple emails,
	 *                    separating them using ";"
	 * @param cc          The carbon copy. You can use multiple emails, separating
	 *                    them using ";"
	 * @param subject     The subject of the email
	 * @param text        The content of the email
	 * @param attachments An array of attachments
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(String from, String to, String bcc, String cc, String subject, String text,
			EmailAttachment... attachments) throws Exception {
		send(from, to, bcc, cc, subject, text, false, attachments);
	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param from        The sender email
	 * @param to          The target email. You can use multiple emails, separating
	 *                    them using ";"
	 * @param bcc         The blind carbon copy. You can use multiple emails,
	 *                    separating them using ";"
	 * @param cc          The carbon copy. You can use multiple emails, separating
	 *                    them using ";"
	 * @param subject     The subject of the email
	 * @param text        The content of the email
	 * @param isHtml      If the content of the "text" parameter is HTML
	 * @param attachments An array of attachments
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(String from, String to, String bcc, String cc, String subject, String text, boolean isHtml,
			EmailAttachment... attachments) throws Exception {
		send(from, to, bcc, cc, subject, text, null, isHtml, attachments);
	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param to      The target email. You can use multiple emails, separating them
	 *                using ";"
	 * @param subject The subject of the email
	 * @param text    The content of the email
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(String to, String subject, String text) throws Exception {
		send(from, to, NO_BCC, NO_CC, subject, text, NO_ATTACHMENTS);
	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param to      The target email. You can use multiple emails, separating them
	 *                using ";"
	 * @param subject The subject of the email
	 * @param text    The content of the email
	 * @param isHtml  If the content of the "text" parameter is HTML
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(String to, String subject, String text, boolean isHtml) throws Exception {
		send(from, to, NO_BCC, NO_CC, subject, text, isHtml, NO_ATTACHMENTS);
	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param to      The target email. You can use multiple emails, separating them
	 *                using ";"
	 * @param subject The subject of the email
	 * @param text    The content of the email
	 * @param isHtml  If the content of the "text" parameter is HTML
	 * @param isBcc   If true, use BCC instead of TO; if false, use TO instead of
	 *                BCC
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(String to, String subject, String text, boolean isHtml, boolean isBcc) throws Exception {
		if (isBcc) {
			send(from, NO_TO, to, NO_CC, subject, text, isHtml, NO_ATTACHMENTS);
		} else {
			send(from, to, NO_BCC, NO_CC, subject, text, isHtml, NO_ATTACHMENTS);
		}

	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param to          The target email. You can use multiple emails, separating
	 *                    them using ";"
	 * @param subject     The subject of the email
	 * @param text        The content of the email
	 * @param attachments An array of attachments
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(String to, String subject, String text, EmailAttachment... attachments) throws Exception {
		send(from, to, NO_BCC, NO_CC, subject, text, attachments);
	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param to          The target email. You can use multiple emails, separating
	 *                    them using ";"
	 * @param subject     The subject of the email
	 * @param text        The content of the email
	 * @param isHtml      If the content of the "text" parameter is HTML
	 * @param attachments An array of attachments
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(String to, String subject, String text, boolean isHtml, EmailAttachment... attachments)
			throws Exception {
		send(from, to, NO_BCC, NO_CC, subject, text, isHtml, attachments);
	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param from    The sender email
	 * @param to      The target email. You can use multiple emails, separating them
	 *                using ";"
	 * @param subject The subject of the email
	 * @param text    The content of the email
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(String from, String to, String subject, String text) throws Exception {
		send(from, to, NO_BCC, NO_CC, subject, text, NO_ATTACHMENTS);
	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param from        The sender email
	 * @param to          The target email. You can use multiple emails, separating
	 *                    them using ";"
	 * @param subject     The subject of the email
	 * @param text        The content of the email
	 * @param isHtml      If the content of the "text" parameter is HTML
	 * @param attachments An array of attachments
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(String from, String to, String subject, String text, boolean isHtml) throws Exception {
		send(from, to, NO_BCC, NO_CC, subject, text, isHtml, NO_ATTACHMENTS);
	}

	/**
	 * Compile the given template (as path) with the given parameters using Velocity
	 * 
	 * @param templatePath The path of the template
	 * @param parameters   The map with the parameters for velocity
	 * @return The content compiled
	 */
	public String compileTemplateFromClasspath(String templatePath, Map<String, Object> parameters) {
		try {
			String template = IOUtils.toString(
					Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath),
					StandardCharsets.UTF_8);
			return compileTemplate(template, parameters);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Compile the given template with the given parameters using Velocity
	 * 
	 * @param templateThe path of the template
	 * @param parameters  The map with the parameters for velocity
	 * @return The content compiled
	 */
	public String compileTemplate(String template, Map<String, Object> parameters) {
		try {
			VelocityContext context = new VelocityContext();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}

			InputStreamReader isr = new InputStreamReader(IOUtils.toInputStream(template, StandardCharsets.UTF_8));
			StringWriter writer = new StringWriter();
			boolean processed = engine.evaluate(context, writer, "PUI_MAIL", isr);

			return processed ? writer.toString() : null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get an attachment from the local disk with the given full path
	 * 
	 * @param fullPath The full path of the file
	 * @return The attachment
	 * @throws IOException If the file doesn't exist
	 */
	public EmailAttachment getFileFromLocal(String fullPath) throws IOException {
		File file = new File(fullPath);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}

		FileInputStream fis = new FileInputStream(file);
		return getFile(fis, file.getName());
	}

	/**
	 * Get an attachment from the classpath (as path)
	 * 
	 * @param filePath The path of the file
	 * @return The attachment
	 * @throws IOException If the file doesn't exist
	 */
	public EmailAttachment getFileFromClasspath(String filePath) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
		if (is == null) {
			throw new FileNotFoundException();
		}

		return getFile(is, filePath);
	}

	/**
	 * Get the file attachment from an input stream
	 * 
	 * @param is       The input stream of the file
	 * @param filePath The path of the file
	 * @return The attachment
	 * @throws IOException If any error occurs while reading the input stream
	 */
	private EmailAttachment getFile(InputStream is, String filePath) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, Charset.defaultCharset());

		EmailAttachment fileDto = new EmailAttachment();
		fileDto.setFilename(filePath);
		fileDto.setContentType(URLConnection.guessContentTypeFromStream(is));
		fileDto.setContent(writer.toString().getBytes());

		is.close();
		writer.close();

		return fileDto;
	}

	/**
	 * Validate the email address returning an String with this addresses separated
	 * by a semicolon ';'. Wrong emails are removed
	 * 
	 * @param emails The emails as String
	 * @return The same email list but with the emails validated
	 */
	private String normalizeAndValidateEmails(String emails) {
		if (!StringUtils.hasText(emails)) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		// validamos direcciones
		for (String email : emails.split(EMAIL_SEPARATOR)) {
			email = email.trim();
			if (emailPattern.matcher(email).matches()) {
				sb.append(email + EMAIL_SEPARATOR);
			} else {
				logger.warn("The email [" + email + "] is not valid, it will not be included in the list");
			}
		}

		return sb.toString();
	}

}
