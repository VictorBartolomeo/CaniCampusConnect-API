package org.example.canicampusconnectapi.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    //    public void sendEmailValidationToken(String to, String token) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("Validation de votre compte PineApple Store");
//        message.setText("Merci de cliquer sur le lien suivant " +
//                "pour valider votre compte : http://localhost:8080/validate?token=" + token);
//        mailSender.send(message);
//    }
    public void sendEmailValidationToken(String to, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Extraction du nom d'utilisateur à partir de l'email
            String username = to.split("@")[0];

            // Première lettre en majuscule pour un meilleur rendu
            username = username.substring(0, 1).toUpperCase() + username.substring(1);

            // Année courante
            int currentYear = Year.now().getValue();

            helper.setTo(to);
            helper.setSubject("🍍 Validation de votre compte PineApple Store 🍍");

            // Création du contenu HTML avec le token injecté manuellement
            String htmlContent = "";

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de validation", e);
        }
    }


}
