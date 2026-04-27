package com.stationery.shop;

import com.stationery.shop.entity.Product;
import com.stationery.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        // --- 1 to 10 ---
        Product p1 = productService.saveProduct(Product.builder().name("Bút bi Thiên Long TL-027").description("Bút bi quốc dân vỏ nhựa trong, đầu bi 0.5mm nét thanh.").price(new BigDecimal("5000")).stock(1000).category("Pen").imageUrl("https://tse2.mm.bing.net/th/id/OIP.UWI6PnnPDKETAxhUtQCm0gHaHa?pid=Api&P=0&h=180").build());
        Product p2 = productService.saveProduct(Product.builder().name("Sổ tay da cao cấp bìa còng").description("Sổ tay bìa da thật cao cấp, thiết kế đường may tinh xảo.").price(new BigDecimal("150000")).stock(50).category("Notebook").imageUrl("https://tse2.mm.bing.net/th/id/OIP.aYdCziJV_Y3hlhyypSvbtwHaHa?pid=Api&P=0&h=180").build());
        Product p3 = productService.saveProduct(Product.builder().name("Tẩy Gôm Trắng Pentel").description("Gôm tẩy Pentel siêu sạch, không rách giấy.").price(new BigDecimal("12000")).stock(200).category("Eraser").imageUrl("https://tse4.mm.bing.net/th/id/OIP.2XVHpnU7StA1XLaB5Ni3gQHaHa?pid=Api&P=0&h=180").build());
        Product p4 = productService.saveProduct(Product.builder().name("Balo sinh viên đa năng Oxford").description("Balo thiết kế trẻ trung đựng vừa laptop 15.6 inch.").price(new BigDecimal("350000")).stock(40).category("Bag").imageUrl("https://tse2.mm.bing.net/th/id/OIP.CxVhY1HQft1QUUtxWEorOAHaHa?pid=Api&P=0&h=180").build());
        Product p5 = productService.saveProduct(Product.builder().name("Hộp bút màu Colokit Thiên Long").description("Set hộp bút chì màu 12 màu cơ bản.").price(new BigDecimal("45000")).stock(120).category("Pencil").imageUrl("https://tse3.mm.bing.net/th/id/OIP.oOZAsSifRH-Si0v2ru2RYAHaHa?pid=Api&P=0&h=180").build());
        Product p6 = productService.saveProduct(Product.builder().name("Kéo thủ công bọc nhựa Deli").description("Kéo mũi nhọn thép không gỉ.").price(new BigDecimal("25000")).stock(80).category("Accessories").imageUrl("https://tse2.mm.bing.net/th/id/OIP.l4QQVz8-Fl1eqTevKCQd2wHaHa?pid=Api&P=0&h=180").build());
        Product p7 = productService.saveProduct(Product.builder().name("Thước 20cm nhôm kĩ thuật").description("Thước kẻ vỏ nhôm nguyên khối, số chia laser.").price(new BigDecimal("15000")).stock(300).category("Ruler").imageUrl("https://tse2.mm.bing.net/th/id/OIP.PUUhaHwk-_WybGkb51Yt4gHaHa?pid=Api&P=0&h=180").build());
        Product p8 = productService.saveProduct(Product.builder().name("Đinh ghim kẹp giấy vỏ tam giác").description("Đinh ghim bảng nhiều màu hộp nhựa tổng hợp.").price(new BigDecimal("35000")).stock(150).category("Accessories").imageUrl("https://tse4.mm.bing.net/th/id/OIP.F2liBwFcfFrGo3BTo_fxdgHaHa?pid=Api&P=0&h=180").build());
        Product p9 = productService.saveProduct(Product.builder().name("Hộp đựng bút để bàn kim loại").description("Kệ bút vuông lưới thép.").price(new BigDecimal("40000")).stock(60).category("Organizer").imageUrl("https://tse1.mm.bing.net/th/id/OIP.Tc2mgXzVGRXiAPgWFYRgcAHaHa?pid=Api&P=0&h=180").build());
        Product p10 = productService.saveProduct(Product.builder().name("Sổ tay lò xo caro A5").description("Sổ gáy lò xo kẻ caro 100 trang.").price(new BigDecimal("25000")).stock(400).category("Notebook").imageUrl("https://tse3.mm.bing.net/th/id/OIP.jNNO8xkLe5oXREoiK-d1JAHaHa?pid=Api&P=0&h=180").build());

        // --- 11 to 20 ---
        Product p11 = productService.saveProduct(Product.builder().name("Dạ quang Stabilo Boss Neon").description("Bút đánh dấu mực sáng, không thấm qua giấy nháp.").price(new BigDecimal("21000")).stock(200).category("Pen").imageUrl("https://tse2.mm.bing.net/th/id/OIP.XbmskjyzYRmrIMTfakChcwHaHa?pid=Api&P=0&h=180").build());
        Product p12 = productService.saveProduct(Product.builder().name("Máy tính Casio fx-580VN X").description("Máy tính chuẩn kì thi THPTQG chính hãng Casio.").price(new BigDecimal("680000")).stock(30).category("Accessories").imageUrl("https://tse1.mm.bing.net/th/id/OIP.G16KkBtCcN6xfoMiLGnvYwHaHa?pid=Api&P=0&h=180").build());
        Product p13 = productService.saveProduct(Product.builder().name("Giấy in A4 Double A 70gsm").description("1 ram giấy A4 chuẩn độ trắng sáng, mịn màng.").price(new BigDecimal("75000")).stock(500).category("Accessories").imageUrl("https://tse2.mm.bing.net/th/id/OIP.RdVzuHSjZm0XmwFc7GoWRQHaHa?pid=Api&P=0&h=180").build());
        Product p14 = productService.saveProduct(Product.builder().name("Ngòi chì kim Pentel 0.5").description("Ruột chì vỏ hộp đen mảnh tiêu chuẩn 2B HB.").price(new BigDecimal("18000")).stock(350).category("Pencil").imageUrl("https://tse1.mm.bing.net/th/id/OIP.6kaPMw5v_O4rOOQwoDjhiQHaHa?pid=Api&P=0&h=180").build());
        Product p15 = productService.saveProduct(Product.builder().name("Màu nước nén Superior 24 Màu").description("Tặng kèm cọ nước, màu loang cực tốt.").price(new BigDecimal("185000")).stock(80).category("Accessories").imageUrl("https://tse4.mm.bing.net/th/id/OIP.AzlotM_VJKmXYaP_R81FZgHaHa?pid=Api&P=0&h=180").build());
        Product p16 = productService.saveProduct(Product.builder().name("Bìa Clear Book 20 Lá Deli").description("Bìa nhựa đục 20 lá mỏng chứa tài liệu chống nước.").price(new BigDecimal("30000")).stock(120).category("Organizer").imageUrl("https://tse2.mm.bing.net/th/id/OIP.vIHhoz39gLx3Ymst5k3iygHaJM?pid=Api&P=0&h=180").build());
        Product p17 = productService.saveProduct(Product.builder().name("Kẹp bướm 25mm SLECH").description("Hộp 12 cái kẹp bướm thép đen cường lực kẹp 50 tờ.").price(new BigDecimal("15000")).stock(300).category("Accessories").imageUrl("https://tse3.mm.bing.net/th/id/OIP.0dXtECby4_WMoJCkcTQM_AHaHa?pid=Api&P=0&h=180").build());
        Product p18 = productService.saveProduct(Product.builder().name("Chuốt chì hình ngôi nhà").description("Gọt chì quay tay tự động hút dăm gỗ gọn gàng.").price(new BigDecimal("50000")).stock(90).category("Accessories").imageUrl("https://tse3.mm.bing.net/th/id/OIP.cOZlygVeutpKHN_Jz0lyFQHaHa?pid=Api&P=0&h=180").build());
        Product p19 = productService.saveProduct(Product.builder().name("Bút mực Gel nét chuẩn Muji 0.38").description("Mực gel mịn màng, vỏ nhựa trơn tuột Minimalist.").price(new BigDecimal("25000")).stock(800).category("Pen").imageUrl("https://tse3.mm.bing.net/th/id/OIP.X8v6hbJkR5Yay7LQfdNbPgHaHa?pid=Api&P=0&h=180").build());
        Product p20 = productService.saveProduct(Product.builder().name("Bộ eke học sinh nhôm vạch xanh").description("Gồm 2 eke, 1 thước thẳng, 1 đo độ.").price(new BigDecimal("42000")).stock(120).category("Ruler").imageUrl("https://tse4.mm.bing.net/th/id/OIP.XdXVCoDPirDs5AeaqDOCCQHaHa?pid=Api&P=0&h=180").build());

        // --- 21 to 30 ---
        Product p21 = productService.saveProduct(Product.builder().name("Bảng mica trắng kèm viền nhôm").description("Bảng học sinh lau xóa không lem mực.").price(new BigDecimal("45000")).stock(150).category("Accessories").imageUrl("https://tse4.mm.bing.net/th/id/OIP.r3DnGCuje_uj4I3Tk8XccQHaHa?pid=Api&P=0&h=180").build());
        Product p22 = productService.saveProduct(Product.builder().name("Compa Deli xám kỹ thuật").description("Bằng kim loại đúc, đính kèm ngòi chì và hộp nhựa bảo vệ.").price(new BigDecimal("35000")).stock(85).category("Ruler").imageUrl("https://tse3.mm.bing.net/th/id/OIP.oSK5r2AGWB9O17Fyxeh_WwHaHa?pid=Api&P=0&h=180").build());
        Product p23 = productService.saveProduct(Product.builder().name("Bìa còng 7cm Kingjim").description("Bìa còng cứng cáp nhập khẩu Nhật Bản lưu trữ lượng lớn hóa đơn.").price(new BigDecimal("45000")).stock(200).category("Organizer").imageUrl("https://tse1.mm.bing.net/th/id/OIP.7HULx5TQZ_8hPOHnULiT8wHaHH?pid=Api&P=0&h=180").build());
        Product p24 = productService.saveProduct(Product.builder().name("Flashcard học từ vựng xâu khuyên").description("Sỏ lỗ dập sẵn kèm khoen inox học mọi lúc mọi nơi.").price(new BigDecimal("15000")).stock(400).category("Notebook").imageUrl("https://tse4.mm.bing.net/th/id/OIP.SPEv3WZa2HTqGCQGO-TL4gHaHa?pid=Api&P=0&h=180").build());
        Product p25 = productService.saveProduct(Product.builder().name("Băng keo trong màng co 5cm").description("Cuộn băng dán thùng siêu dai chống thấm.").price(new BigDecimal("12000")).stock(500).category("Accessories").imageUrl("https://tse4.mm.bing.net/th/id/OIP.BH_qkXZMJUQ_KcNk1PXrswHaHa?pid=Api&P=0&h=180").build());
        Product p26 = productService.saveProduct(Product.builder().name("Dao rọc giấy đệm cao su SDI").description("Cán trượt tự động khóa auto-lock an toàn lổm chổm.").price(new BigDecimal("16000")).stock(280).category("Accessories").imageUrl("https://tse1.mm.bing.net/th/id/OIP.lH6u_155qfoODWFv4c9D6gHaHa?pid=Api&P=0&h=180").build());
        Product p27 = productService.saveProduct(Product.builder().name("Giấy note Post-it 3M 3x3").description("Tập giấy nhớ dán màu vàng huỳnh quang siêu dính.").price(new BigDecimal("18000")).stock(600).category("Accessories").imageUrl("https://tse1.mm.bing.net/th/id/OIP.PLTsY_MDpj2HxCR9Aj5F-gHaHa?pid=Api&P=0&h=180").build());
        Product p28 = productService.saveProduct(Product.builder().name("Kệ hồ sơ Mica 3 tầng bẹt").description("Tầng lắp ráp để bàn làm việc chắc chắn.").price(new BigDecimal("210000")).stock(40).category("Organizer").imageUrl("https://tse2.mm.bing.net/th/id/OIP.FIpIfquAsQ2jTgwjARvzZAHaHa?pid=Api&P=0&h=180").build());
        Product p29 = productService.saveProduct(Product.builder().name("Balo chống gù tiểu học Hami").description("Trọng lượng nhẹ ôm lưng tản lực bảo vệ cột sống.").price(new BigDecimal("420000")).stock(55).category("Bag").imageUrl("https://tse3.mm.bing.net/th/id/OIP.9a9jDLz_Yfy6-KPB1LJ0vAHaHa?pid=Api&P=0&h=180").build());
        Product p30 = productService.saveProduct(Product.builder().name("Túi My Clear Bag A4 nút gài").description("Túi khuy bấm mỏng trong suốt bảo mật.").price(new BigDecimal("3500")).stock(1500).category("Organizer").imageUrl("https://tse4.mm.bing.net/th/id/OIP.rC8-SHcnprz1WgyNrBYF9QHaHa?pid=Api&P=0&h=180").build());
    }
}
