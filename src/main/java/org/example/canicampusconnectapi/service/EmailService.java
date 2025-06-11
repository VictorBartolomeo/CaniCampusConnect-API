
package org.example.canicampusconnectapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.canicampusconnectapi.dao.EmailValidationTokenDao;
import org.example.canicampusconnectapi.common.exception.EmailConstraintRequests; // ⭐ Votre chemin d'exception
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Year;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TokenService tokenService;
    private final EmailValidationTokenDao tokenRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${APP_NAME:CaniCampusConnect}")
    private String appName;

    private static final int MAX_EMAILS_PER_HOUR = 3;

    public EmailService(JavaMailSender mailSender,
                        TokenService tokenService,
                        EmailValidationTokenDao tokenRepository) {
        this.mailSender = mailSender;
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
    }

    /**
     * ⭐ SIMPLIFIÉ - Envoie un email de validation avec token sécurisé
     */
    public void sendEmailValidationToken(String to) {
        // Vérifier la limitation
        if (!canSendEmail(to)) {
            throw new EmailConstraintRequests("Trop de demandes d'email envoyées. Maximum " + MAX_EMAILS_PER_HOUR + " par heure.");
        }

        try {
            // ⭐ CORRIGÉ - Générer un token avec 1 seul paramètre
            String token = tokenService.generateValidationToken(to);

            // Créer le lien de validation
            String validationLink = buildValidationLink(token, to);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String username = extractUsername(to);
            int currentYear = Year.now().getValue();

            helper.setTo(to);
            helper.setSubject("🐕 Validation de votre compte " + appName + " 🐕");
            helper.setFrom("noreply@" + appName.toLowerCase() + ".com");

            String htmlContent = createSecureEmailTemplate(username, validationLink, currentYear);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de validation", e);
        }
    }

    /**
     * Vérifie si on peut envoyer un email (limitation anti-spam)
     */
    private boolean canSendEmail(String email) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long recentCount = tokenRepository.countRecentTokensByEmail(email, oneHourAgo);
        return recentCount < MAX_EMAILS_PER_HOUR;
    }

    /**
     * Construit le lien de validation sécurisé
     */
    private String buildValidationLink(String token, String email) {
        try {
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
            return String.format("%s/validate-email?token=%s&email=%s",
                    baseUrl, encodedToken, encodedEmail);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du lien de validation", e);
        }
    }

    /**
     * Crée le template HTML sécurisé
     */
    private String createSecureEmailTemplate(String username, String validationLink, int year) {
        return String.format("""
            <!DOCTYPE html>
            <html lang="fr">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Validation de compte - %s</title>
            </head>
            <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
                         line-height: 1.6; margin: 0; padding: 0; background-color: #f4f4f4;">
                
                <table width="100%%" cellpadding="0" cellspacing="0" style="max-width: 600px; 
                       margin: 0 auto; background-color: white; border-radius: 10px; 
                       box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                    
                    <!-- Header -->
                    <tr>
                        <td style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); 
                                   padding: 30px; text-align: center; border-radius: 10px 10px 0 0;">
                            <h1 style="color: white; margin: 0; font-size: 28px;">🐕 %s 🐕</h1>
                            <p style="color: #f0f0f0; margin: 10px 0 0 0; font-size: 16px;">
                                Votre plateforme canine de confiance
                            </p>
                        </td>
                    </tr>
                    
                    <!-- Body -->
                    <tr>
                        <td style="padding: 40px 30px;">
                            <h2 style="color: #333; margin-top: 0;">Bonjour %s ! 👋</h2>
                            
                            <p style="color: #555; font-size: 16px;">
                                Merci de vous être inscrit sur <strong>%s</strong> ! 
                                Nous sommes ravis de vous accueillir dans notre communauté canine.
                            </p>
                            
                            <p style="color: #555; font-size: 16px;">
                                Pour activer votre compte et commencer à utiliser tous nos services, 
                                veuillez cliquer sur le bouton ci-dessous :
                            </p>
                            
                            <!-- CTA Button -->
                            <div style="text-align: center; margin: 35px 0;">
                                <a href="%s" 
                                   style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); 
                                          color: white; padding: 15px 40px; text-decoration: none; 
                                          border-radius: 50px; font-weight: bold; font-size: 16px;
                                          display: inline-block; box-shadow: 0 4px 15px rgba(102,126,234,0.3);">
                                    ✅ VALIDER MON COMPTE
                                </a>
                            </div>
                            
                            <!-- Security Info -->
                            <div style="background-color: #fff3cd; border: 1px solid #ffeaa7; 
                                        border-radius: 8px; padding: 20px; margin: 30px 0;">
                                <h3 style="color: #856404; margin-top: 0; font-size: 18px;">
                                    ⚠️ Informations importantes
                                </h3>
                                <ul style="color: #856404; margin-bottom: 0; padding-left: 20px;">
                                    <li>Ce lien expire dans <strong>24 heures</strong></li>
                                    <li>Il ne peut être utilisé qu'<strong>une seule fois</strong></li>
                                    <li>Si vous n'avez pas créé ce compte, ignorez cet email</li>
                                    <li>Ne partagez jamais ce lien avec personne</li>
                                </ul>
                            </div>
                            
                            <!-- Alternative Link -->
                            <p style="color: #777; font-size: 14px; text-align: center;">
                                Si le bouton ne fonctionne pas, copiez ce lien :<br>
                                <a href="%s" style="color: #667eea; word-break: break-all;">%s</a>
                            </p>
                        </td>
                    </tr>
                    
                    <!-- Footer -->
                    <tr>
                        <td style="background-color: #f8f9fa; padding: 20px 30px; 
                                   border-radius: 0 0 10px 10px; text-align: center;">
                            <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                © %d %s - Tous droits réservés<br>
                                Cet email a été envoyé automatiquement, merci de ne pas y répondre.
                            </p>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """,
                appName, appName, username, appName, validationLink,
                validationLink, validationLink, year, appName);
    }

    /**
     * Extrait le nom d'utilisateur de l'email de manière sécurisée
     */
    private String extractUsername(String email) {
        if (email == null || !email.contains("@")) {
            return "Utilisateur";
        }

        String username = email.split("@")[0];
        // Sécurisation contre les injections et caractères spéciaux
        username = username.replaceAll("[^a-zA-Z0-9._-]", "");

        if (username.isEmpty()) {
            return "Utilisateur";
        }

        // Capitalisation
        return Character.toUpperCase(username.charAt(0)) +
                username.substring(1).toLowerCase();
    }
}