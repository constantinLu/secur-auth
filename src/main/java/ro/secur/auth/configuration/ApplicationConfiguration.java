package ro.secur.auth.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ro.secur.auth.exceptions.ResponseFactory;

import java.util.Properties;

@Configuration
public class ApplicationConfiguration {

    private final EmailConfiguration emailConfiguration;

    public ApplicationConfiguration(EmailConfiguration emailConfiguration) {
        this.emailConfiguration = emailConfiguration;
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ResponseFactory getResponseFactory() {
        return new ResponseFactory();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfiguration.host);
        mailSender.setPort(587);

        mailSender.setUsername(emailConfiguration.emailAddress);
        mailSender.setPassword(emailConfiguration.emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
