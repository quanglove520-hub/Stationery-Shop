package com.stationery.shop.controller;

import com.stationery.shop.entity.Product;
import com.stationery.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        Page<Product> products = productService.getAllProducts(search, PageRequest.of(page, size));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/seed")
    public ResponseEntity<String> seedData() {
        productService.saveProduct(Product.builder().name("Bút bi Thiên Long").description("Bút bi xanh chính hãng Thiên Long TL-027. Đầu bi 0.5mm nét thanh, mực ra đều trơn tru, không ứ đọng. Thân vỏ nhựa trong suốt cường lực, thiết kế tinh tế tiện dụng cho việc học tập và ký tá tài liệu văn phòng.").price(new java.math.BigDecimal("5000")).stock(1000).category("Pen").imageUrl("https://vanphong-pham.com/wp-content/uploads/2018/12/but-bi-thien-long-TL-027.jpg").build());
        productService.saveProduct(Product.builder().name("Sổ tay da cao cấp").description("Sổ tay bìa da thật cao cấp, thiết kế mộc mạc đường chỉ may tay tinh xảo. Có dây chun cài sổ chắc chắn. Lõi giấy màu kem chống lóa chuẩn châu Âu, định lượng 100gsm không bị thấm mực, thích hợp cho doanh nhân.").price(new java.math.BigDecimal("150000")).stock(50).category("Notebook").imageUrl("https://www.xuongsanxuatsoda.com/wp-content/uploads/2025/06/so-tay-bia-da-that-xuongsanxuatsoda.com_.jpg").build());
        productService.saveProduct(Product.builder().name("Tẩy Gôm Trắng Pentel").description("Gôm tẩy Pentel HI-POLYMER siêu sạch, siêu mềm. Tẩy dẻo không làm rách hay sờn bề mặt giấy, không để lại nhiều mạt cao su. An toàn không chứa chất độc hại, thân thiện môi trường dành cho học sinh.").price(new java.math.BigDecimal("12000")).stock(200).category("Eraser").imageUrl("https://tse4.mm.bing.net/th/id/OIP.2XVHpnU7StA1XLaB5Ni3gQHaHa?pid=Api&P=0&h=180").build());
        productService.saveProduct(Product.builder().name("Balo sinh viên đa năng").description("Balo thiết kế unisex năng động. Vải trượt nước Oxford cao cấp chống mưa nhẹ. Có ngăn vách lót chống sốc đựng vừa Laptop 15.6 inch. Khóa kéo mượt mà, phân bổ nhiều ngăn nhỏ tiện dụng cho việc đựng tài liệu và đồ dùng học tập.").price(new java.math.BigDecimal("350000")).stock(10).category("Bag").imageUrl("https://tse2.mm.bing.net/th/id/OIP.yCt8azYLsyKFz4Wf6S9wEAHaHa?pid=Api&P=0&h=180").build());
        productService.saveProduct(Product.builder().name("Hộp bút màu HB").description("Set hộp chì màu chuyên dụng tô phác họa nghệ thuật, vỏ gỗ chống gãy lõi. Chất chì tiêu chuẩn HB, màu tô êm ái, bám giấy tốt giúp tô mảng rộng không bị sần. Mang lại các phối màu tự nhiên rực rỡ.").price(new java.math.BigDecimal("45000")).stock(120).category("Pencil").imageUrl("https://tse4.mm.bing.net/th/id/OIP.d3O1T8r7xf2m3VtIWiU3KwHaHa?pid=Api&P=0&h=180").build());
        productService.saveProduct(Product.builder().name("Kéo thủ công Deli").description("Kéo cắt thủ công Deli, lưỡi kéo bằng thép không gỉ nguyên khối sắc bén. Cán cầm nhựa đúc bọc cao su chống trơn trượt cầm rất êm tay. Sản phẩm phù hợp cho việc cắt giấy, decan, băng keo mà không dính lưỡi.").price(new java.math.BigDecimal("25000")).stock(80).category("Accessories").imageUrl("https://tse2.mm.bing.net/th/id/OIP.4Tbbhn8hXgP_faYzs-PVzAHaHa?pid=Api&P=0&h=180").build());
        productService.saveProduct(Product.builder().name("Thước 20cm nhôm").description("Thước kẻ độ dài 20cm từ nhôm định hình hợp kim siêu nhẹ nhưng cực kì bền, không độc hại gỉ sét. Các vạch phân chia cm/mm được khắc chìm tinh tế chính xác, mực chống xước trọn đời không lo phai vạch.").price(new java.math.BigDecimal("15000")).stock(300).category("Ruler").imageUrl("https://tse3.mm.bing.net/th/id/OIP.I4E-dZQ6tnloNTR3sRqK4QHaHa?pid=Api&P=0&h=180").build());
        productService.saveProduct(Product.builder().name("Đinh ghim kẹp giấy").description("Hộp đồ dùng tổng hợp đinh ghim bảng và kẹp giấy. Sử dụng chất liệu kim loại bọc lớp nhựa bóng đa sắc giúp tổng hợp các trang tài liệu lại thành mảng, tránh thất lạc. Ghim có đầu cắm bảo vệ an toàn.").price(new java.math.BigDecimal("35000")).stock(150).category("Accessories").imageUrl("https://tse3.mm.bing.net/th/id/OIP.5yarAQS4DrxGOefGPrEM3wHaHa?pid=Api&P=0&h=180").build());
        productService.saveProduct(Product.builder().name("Hộp lưu trữ Desktop").description("Hộp kệ mini sắp xếp đồ lưu trữ đa năng cho Desktop. Thiết kế tầng kệ xếp chéo chia hộc giúp phân loại từ bút, gôm, kẹp cạp, phone. Setup góc văn phòng chuẩn phong cách tối giản gọn gàng, tăng cảm hứng làm việc.").price(new java.math.BigDecimal("120000")).stock(30).category("Organizer").imageUrl("https://tse1.mm.bing.net/th/id/OIP.Oc34zpLTh_YjKsCyluPdWQHaHa?pid=Api&P=0&h=180").build());
        productService.saveProduct(Product.builder().name("Sổ tay ghi chú lò xo").description("Sổ caro gáy lò xo xoắn ốc kép dày dặn và cực kỳ chắc tay. Cho phép lật mở 360 độ hoặc xé trang giấy ghi nháp mà hoàn toàn không ảnh hưởng gáy sổ cuốn. Lưới kẻ caro nhẹ dịu phù hợp với môn tư duy toán học lập trình.").price(new java.math.BigDecimal("25000")).stock(400).category("Notebook").imageUrl("https://tse4.mm.bing.net/th/id/OIP.7t3kAcBf9JDnD2SZ9KwUqgHaHa?pid=Api&P=0&h=180").build());
        return ResponseEntity.ok("Seeded Data");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearData() {
        productService.deleteAll();
        return ResponseEntity.ok("Cleared Data. You can test NO DATA scenario.");
    }
}
