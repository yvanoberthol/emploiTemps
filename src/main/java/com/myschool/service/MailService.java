package com.myschool.service;

import com.myschool.domain.User;
import com.myschool.dto.UserDto;
import com.myschool.repository.UserRepository;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    private static final String TRANSACTION = "transaction";
    private static final String BASE_URL = "baseUrl";

    /*
    @Autowired
    private JHipsterProperties jHipsterProperties;
    */

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private UserRepository userRepository;

    @Async
    public void sendEmailFromNoreplyAddress(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        javaMailSender.setUsername("noreply@myschool.com");
        javaMailSender.setPassword("myschoolnr1");

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            //message.setFrom("noreply@myschool.com");
            message.setFrom(new InternetAddress("noreply@myschool.com", "Myschool"));
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendEmailFromContactAddress(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        javaMailSender.setUsername("contact@sprinted.net");
        javaMailSender.setPassword("Oiseau+2018");

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(new InternetAddress("contact@sprinted.net", "Famille Ndeng-Nsiah"));
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(UserDto user, String baseUrl) {

        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());

        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);

        sendEmailFromNoreplyAddress(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreationEmail(UserDto user, String baseUrl) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmailFromNoreplyAddress(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(UserDto user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmailFromContactAddress(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCredentialsMail(User user, String mail, String baseUrl) {
        log.debug("Sending credentials to '{}'", mail);
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.creation.title", null, locale);
        sendEmailFromContactAddress(mail, subject, content, false, true);
    }

    @Async
    public void sendNewRegistrationEmail(UserDto userDto) {
        log.debug("Sending credentials to '{}'", "t.teufak@sprinted.net");
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(USER, userDto);
        String content = templateEngine.process("newRegistrationEmail", context);
        String subject = messageSource.getMessage("email.newRegistration.title", null, locale);
        sendEmailFromContactAddress("t.teufak@sprinted.net", subject, content, false, true);
    }
}
