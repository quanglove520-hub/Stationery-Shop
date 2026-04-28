package stationary.order.service;

import stationary.entity.Order;
import stationary.entity.OrderLineItem;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {
    
    // Giả lập cấu hình (Bạn có thể điền thông tin thật tại đây)
    private final String username = "your-email@gmail.com";
    private final String password = "your-app-password";

    public void sendOrderConfirmation(Order order) {
        String recipient = order.getCustomerEmail();
        String subject = "Xác nhận đơn hàng Stationary #" + order.getId();
        
        StringBuilder content = new StringBuilder();
        content.append("<h2>Cảm ơn bạn đã mua hàng tại Stationary!</h2>");
        content.append("<p>Mã đơn hàng: <b>").append(order.getId()).append("</b></p>");
        content.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
        content.append("<tr><th>Sản phẩm</th><th>Số lượng</th><th>Giá</th></tr>");
        
        for (OrderLineItem item : order.getItems()) {
            content.append("<tr>")
                   .append("<td>").append(item.getProductName()).append("</td>")
                   .append("<td>").append(item.getQuantity()).append("</td>")
                   .append("<td>").append(String.format("%,.0f", item.getPrice())).append(" đ</td>")
                   .append("</tr>");
        }
        
        content.append("</table>");
        content.append("<h3>Tổng cộng: ").append(String.format("%,.0f", order.getTotalAmount())).append(" đ</h3>");
        content.append("<p>Đơn hàng sẽ sớm được giao tới bạn.</p>");

        System.out.println("----------------------------------------------");
        System.out.println("[EMAIL SERVICE] Đang gửi mail tới: " + recipient);
        System.out.println("[EMAIL SERVICE] Tiêu đề: " + subject);
        System.out.println("[EMAIL SERVICE] Nội dung:\n" + content.toString().replaceAll("<[^>]*>", " "));
        System.out.println("----------------------------------------------");

        // Logic gửi mail thực tế (Uncomment khi có cấu hình SMTP)
        /*
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setContent(content.toString(), "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("[EMAIL SERVICE] Gửi Mail thành công!");
        } catch (MessagingException e) {
            System.err.println("[EMAIL SERVICE] Lỗi gửi mail: " + e.getMessage());
        }
        */
    }
}
