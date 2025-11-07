package com.shakalinux.biblia.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String textHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            String template = """
                <html>
                <body style="font-family: 'Georgia', serif; background-color: #fff8e1; color: #4e342e; padding: 30px;">

                    <div style="text-align: center;">
                        <img src="https://i.imgur.com/tI5VZVb.png"
                             alt="Caminho da Fé"
                             style="max-width: 160px; margin-bottom: 20px;">
                        <h2 style="color: #795548;">Caminho da Fé</h2>
                        <hr style="border: none; height: 2px; background-color: #ffb300; width: 70%; margin: 10px auto;">
                    </div>

                    <div style="margin: 25px auto; max-width: 600px; background: #fffdf5; border-radius: 10px; padding: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                        {{CONTENT}}
                    </div>

                    <div style="margin-top: 25px; text-align: center; color: #6d4c41; font-style: italic;">
                        <p>"O Senhor é o meu pastor, nada me faltará."</p>
                        <small>— Salmos 23:1</small>
                    </div>

                    <div style="margin-top: 30px; text-align: center; font-size: 13px; color: #8d6e63;">
                        <p>© 2025 Caminho da Fé • Que a paz esteja contigo 🙏</p>
                    </div>

                </body>
                </html>
                """;


            String htmlContent = template.replace("{{CONTENT}}",
                textHtml.replace("\n", "<br>"));

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendVerificationEmail(String to, String username, String code) {

        String textoHtml = """
            Olá, <strong>%s</strong>!<br><br>
            Seja bem-vindo(a) ao <strong>Caminho da Fé</strong> 🌿<br>
            Seu código de verificação é:<br>
            <div style='font-size: 22px; font-weight: bold; color: #d84315; margin: 10px 0;'>%s</div>
            Use-o para confirmar sua conta e começar sua jornada espiritual conosco.<br><br>
            Que a luz do Senhor guie o seu caminho!
            """.formatted(username, code);

        sendEmail(to, "Código de Verificação — Caminho da Fé", textoHtml);
    }

    public void sendResetPasswordEmail(String to, String username, String code) {
        String textoHtml = """
            Olá, <strong>%s</strong>!<br><br>
            Recebemos um pedido para redefinir sua senha no <strong>Caminho da Fé</strong>.<br><br>
            Seu código de redefinição é:<br>
            <div style='font-size: 22px; font-weight: bold; color: #d84315; margin: 10px 0;'>%s</div>
            Use este código para definir uma nova senha e continuar sua jornada de fé com segurança.<br><br>
            Se não foi você quem solicitou, apenas ignore este e-mail.<br><br>
            Que Deus te abençoe e te guarde 🙏
            """.formatted(username, code);

        sendEmail(to, "Redefinição de Senha — Caminho da Fé", textoHtml);
    }
}
