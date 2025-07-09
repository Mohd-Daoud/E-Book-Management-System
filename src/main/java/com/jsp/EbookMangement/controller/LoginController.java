package com.jsp.EbookMangement.controller;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jsp.EbookMangement.entity.User;
import com.jsp.EbookMangement.service.UserService;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@SuppressWarnings("serial")
@WebServlet(value = "/login")
public class LoginController extends HttpServlet {

	UserService service = new UserService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();

		try {
			String email = req.getParameter("email");
			String password = req.getParameter("password");

			if ("admin@gmail.com".equals(email) && "admin".equals(password)) {
				User user = new User();
				user.setName("Admin");

				session.setAttribute("userobj", user);
				resp.sendRedirect("admin/home.jsp");
			} else {
				User user = service.userLoginDaoByEmailDService(email, password);

				if (user.getEmail() != null && user.getPassword() != null) {
					session.setAttribute("userobj", user);

					// Email Details
					String to = email;
					String subject = "Welcome to Online Book Store!";
					String messageText = "Hi " + user.getName() + ",\n\n"
							+ "Welcome to our online book store! Explore, buy, and enjoy thousands of books.\n\n"
							+ "Regards,\nOnline Book Store Team";

					// Secure SMTP credentials using Environment Variables
					String host = "smtp.gmail.com";
					final String username = System.getenv("SMTP_USER");
					final String userpassword = System.getenv("SMTP_PASS");

					Properties props = new Properties();
					props.put("mail.smtp.host", host);
					props.put("mail.smtp.port", "587");
					props.put("mail.smtp.auth", "true");
					props.put("mail.smtp.starttls.enable", "true");

					Session session1 = Session.getInstance(props, new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, userpassword);
						}
					});

					try {
						MimeMessage message = new MimeMessage(session1);
						message.setFrom(new InternetAddress(username));
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
						message.setSubject(subject);
						message.setText(messageText);
						Transport.send(message);
					} catch (MessagingException e) {
						e.printStackTrace();
					}

					resp.sendRedirect("index.jsp");
				} else {
					session.setAttribute("faildMsg", "Please Enter Valid Email & Password");
					resp.sendRedirect("login.jsp");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
