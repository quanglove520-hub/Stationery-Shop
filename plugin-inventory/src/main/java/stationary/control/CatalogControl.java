package stationary.control;

import stationary.entity.Category;
import stationary.entity.Product;
import stationary.store.InMemoryStore;

import java.util.List;
import java.util.stream.Collectors;

public class CatalogControl {
    public List<Category> getCategories() {
        return InMemoryStore.getInstance().getCategories();
    }

    public List<Product> getAllProducts() {
        return InMemoryStore.getInstance().getProducts();
    }

    public List<Product> getProductsByCategory(String categoryId) {
        return InMemoryStore.getInstance().getProducts().stream()
                .filter(p -> p.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public Product getProductById(String id) {
        return InMemoryStore.getInstance().getProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static class PageResponse<T> {
        public List<T> content;
        public int totalElements;
        public int totalPages;
        public int size;
        public int number;
        public PageResponse(List<T> content, int totalElements, int size, int number) {
            this.content = content;
            this.totalElements = totalElements;
            this.size = size;
            this.number = number;
            this.totalPages = (int) Math.ceil((double) totalElements / size);
        }
    }

    public PageResponse<Product> getAllProducts(String categoryId, String search, String sortType, int page, int size) {
        List<Product> all = InMemoryStore.getInstance().getProducts();
        
        // Filter by category
        if (categoryId != null && !categoryId.trim().isEmpty() && !categoryId.equals("all")) {
            all = all.stream().filter(p -> p.getCategoryId().equals(categoryId)).collect(Collectors.toList());
        }

        // Filter by search
        if (search != null && !search.trim().isEmpty()) {
            final String s = search.toLowerCase();
            all = all.stream().filter(p -> p.getName().toLowerCase().contains(s)).collect(Collectors.toList());
        }
        
        // Sort
        if (sortType != null && !sortType.trim().isEmpty()) {
            if (sortType.equals("price_asc")) {
                all = all.stream().sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice())).collect(Collectors.toList());
            } else if (sortType.equals("price_desc")) {
                all = all.stream().sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice())).collect(Collectors.toList());
            }
        }

        int totalElements = all.size();
        int fromIndex = Math.min(page * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<Product> content = all.subList(fromIndex, toIndex);
        return new PageResponse<>(content, totalElements, size, page);
    }

    public void clearProducts() {
        InMemoryStore.getInstance().clearProducts();
    }

    public void seedProducts() {
        InMemoryStore store = InMemoryStore.getInstance();
        store.clearProducts(); // Clear default dummy data
        store.addProduct(new Product("P1", "Pen", "Bút bi Thiên Long TL-027", "Bút bi quốc dân vỏ nhựa trong, đầu bi 0.5mm nét thanh. Mực ra đều trơn tru, không xước giấy hay bị nhoè mực khi viết nhanh. Rất thích hợp cho học sinh và nhân viên văn phòng.", 5000.0, 1000, "https://tse2.mm.bing.net/th/id/OIP.UWI6PnnPDKETAxhUtQCm0gHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P2", "Notebook", "Sổ tay da cao cấp bìa còng", "Sổ tay bìa da thật cao cấp, thiết kế đường may tinh xảo. Khổ giấy A5 100gsm chống thấm mực tuyệt đối, thích hợp làm quà tặng hoặc ghi chép Bullet Journal.", 150000.0, 50, "https://tse2.mm.bing.net/th/id/OIP.aYdCziJV_Y3hlhyypSvbtwHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P3", "Eraser", "Tẩy Gôm Trắng Pentel", "Gôm tẩy Pentel siêu sạch, không rách giấy. Kết cấu cao su đặc biệt cuốn bụi tẩy siêu mượt, loại bỏ nét chì 2B-4B dễ dàng mà không để lại vệt đen.", 12000.0, 200, "https://tse4.mm.bing.net/th/id/OIP.2XVHpnU7StA1XLaB5Ni3gQHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P4", "Bag", "Balo sinh viên đa năng Oxford", "Balo thiết kế unisex năng động bằng vải Oxford chống thấm nước chuyên sâu. Đựng vừa laptop 15.6 inch với lưng đệm thoáng khí chống vẹo cột sống.", 350000.0, 40, "https://tse2.mm.bing.net/th/id/OIP.CxVhY1HQft1QUUtxWEorOAHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P5", "Pencil", "Hộp bút màu Colokit Thiên Long", "Set hộp bút chì màu 12 màu cơ bản, ngòi chì siêu mềm mịn giúp tán màu dễ dàng trên giấy mĩ thuật. Lõi gỗ ép đặc hạn chế gãy khi chuốt.", 45000.0, 120, "https://tse3.mm.bing.net/th/id/OIP.oOZAsSifRH-Si0v2ru2RYAHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P6", "Accessories", "Kéo thủ công bọc nhựa Deli", "Kéo mũi nhọn thép không gỉ sáng bóng, cán cầm bọc lớp đệm silicone êm ái chống phồng rộp tay khi cắt giấy cacton hay vật liệu dày.", 25000.0, 80, "https://tse2.mm.bing.net/th/id/OIP.l4QQVz8-Fl1eqTevKCQd2wHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P7", "Ruler", "Thước 20cm nhôm kĩ thuật", "Thước kẻ vỏ nhôm nguyên khối siêu cứng, không bị mẻ cạnh gãy mép. Vạch chia mm khắc laser chính xác, không phai mờ sau nhiều năm sử dụng.", 15000.0, 300, "https://tse2.mm.bing.net/th/id/OIP.PUUhaHwk-_WybGkb51Yt4gHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P8", "Accessories", "Đinh ghim kẹp giấy vỏ tam giác", "Hộp 100 đinh ghim bảng nút bọc nhựa nhiều màu tươi sáng thiết kế công thái học dễ thao tác. Mũi kim thép mạ niken cắm sâu chống oxi hóa.", 35000.0, 150, "https://tse4.mm.bing.net/th/id/OIP.F2liBwFcfFrGo3BTo_fxdgHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P9", "Organizer", "Hộp đựng bút để bàn kim loại", "Kệ vuông lưới thép đan chéo mang đậm chất hiện đại. Thiết kế thoáng đãng, chống đọng bụi bẩn, sơn tĩnh điện màu đen mờ chống gỉ sét tuyệt đối.", 40000.0, 60, "https://tse1.mm.bing.net/th/id/OIP.Tc2mgXzVGRXiAPgWFYRgcAHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P10", "Notebook", "Sổ tay lò xo caro A5", "Sổ gáy lò xo kẻ caro 100 trang, chất giấy kem chống lóa 70gsm siêu mượt. Vạch caro caro 5mm cực phù hợp học toán học hoặc sketchnote tư duy.", 25000.0, 400, "https://tse3.mm.bing.net/th/id/OIP.jNNO8xkLe5oXREoiK-d1JAHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P11", "Pen", "Dạ quang Stabilo Boss Neon", "Bút đánh dấu mực dạ quang công nghệ Anti-Dry-Out (Không khô mực trong 4 tiếng bỏ nắp). Highlighter bám màu lâu phai, không lem ướt giấy nháp mỏng.", 21000.0, 200, "https://tse2.mm.bing.net/th/id/OIP.XbmskjyzYRmrIMTfakChcwHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P12", "Accessories", "Máy tính Casio fx-580VN X", "Máy tính tiêu chuẩn vàng cho kì thi THPTQG chính hãng Casio với 521 tính năng vượt trội. Hiển thị độ phân giải cao đa biểu thức siêu nhanh.", 680000.0, 30, "https://tse1.mm.bing.net/th/id/OIP.G16KkBtCcN6xfoMiLGnvYwHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P13", "Accessories", "Giấy in A4 Double A 70gsm", "Ram 500 tờ A4 nhập khẩu trực tiếp. Độ trắng sáng tuyệt đối, độ dày dặn vượt trội chống kẹt giấy trong thân máy photo. Lý tưởng cho hồ sơ tối quan trọng.", 75000.0, 500, "https://tse2.mm.bing.net/th/id/OIP.RdVzuHSjZm0XmwFc7GoWRQHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P14", "Pencil", "Ngòi chì kim Pentel 0.5", "1 hộp 40 ruột chì tinh chế từ carbon siêu bền. Đậm ngang chuẩn 2B, mảnh 0.5mm nét đanh, chống gãy khúc trong thân bút kim.", 18000.0, 350, "https://tse1.mm.bing.net/th/id/OIP.6kaPMw5v_O4rOOQwoDjhiQHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P15", "Accessories", "Màu nước nén Superior 24 Màu", "Bộ màu sinh viên mĩ thuật đi kèm cọ nước waterbrush. Lên màu loang cực tốt, kết cấu trong trẻo khô nhanh, dải màu từ pastel tới rực rỡ pha trộn vô tận.", 185000.0, 80, "https://tse4.mm.bing.net/th/id/OIP.AzlotM_VJKmXYaP_R81FZgHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P16", "Organizer", "Bìa Clear Book 20 Lá Deli", "Sổ bìa nhựa đục hàn chắc 20 lá fillet mỏng. Đựng vừa chuẩn vặn A4, giữ phẳng phiu hồ sơ, chống ẩm ướt tuyệt đối.", 30000.0, 120, "https://tse2.mm.bing.net/th/id/OIP.vIHhoz39gLx3Ymst5k3iygHaJM?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P17", "Accessories", "Kẹp bướm 25mm SLECH", "Hộp 12 cái kẹp bướm làm bằng thép cường lực lò xo không gião. Kẹp chặt tới 50 tờ mà không làm rách hay in hằn sâu mép giấy.", 15000.0, 300, "https://tse3.mm.bing.net/th/id/OIP.0dXtECby4_WMoJCkcTQM_AHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P18", "Accessories", "Chuốt chì hình ngôi nhà", "Siêu phẩm gọt chì quay tay tự động hút dăm gỗ vào khoang bụng gọn gàng. Có cần khóa mũi chì tránh chuốt quá nhọn gây gãy.", 50000.0, 90, "https://tse3.mm.bing.net/th/id/OIP.cOZlygVeutpKHN_Jz0lyFQHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P19", "Pen", "Bút mực Gel nét chuẩn Muji 0.38", "Mực gel lỏng trượt êm ru, vô cùng đậm sắc. Thiết kế nhựa nhám trong Minimalist mang tính thẩm mỹ cao chuẩn Nhật Bản.", 25000.0, 800, "https://tse3.mm.bing.net/th/id/OIP.X8v6hbJkR5Yay7LQfdNbPgHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P20", "Ruler", "Bộ eke học sinh nhôm vạch xanh", "Bộ 4 món bằng nhựa Mica siêu cứng phủ màng bảo vệ vạch in. Thước đứng góc đo chuẩn xác góc vuông và sin cos.", 42000.0, 120, "https://tse4.mm.bing.net/th/id/OIP.XdXVCoDPirDs5AeaqDOCCQHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P21", "Accessories", "Bảng mica trắng kèm viền nhôm", "Bảng nháp từ tính bóp nhôm chịu lực ở các góc. Mặt mica bám mực bút lông, chỉ 1 giẻ lau trôi tuột không để bóng mờ lem nhem.", 45000.0, 150, "https://tse4.mm.bing.net/th/id/OIP.r3DnGCuje_uj4I3Tk8XccQHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P22", "Ruler", "Compa Deli xám kỹ thuật", "Bằng kim loại đúc, đính kèm ngòi chì và hộp nhựa. Bản lề xiết ốc chắc nịch giúp xoay đường bao tròn trơn khít không bị trượt bán kính.", 35000.0, 85, "https://tse3.mm.bing.net/th/id/OIP.oSK5r2AGWB9O17Fyxeh_WwHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P23", "Organizer", "Bìa còng 7cm Kingjim", "Bìa siêu dày lõi bìa carton phủ PP. Còng O-ring phủ kẽm cường lực kẹp kẹp hàng nghìn trang cực chuẩn dùng lập hồ sơ kho dữ liệu cuối năm.", 45000.0, 200, "https://tse1.mm.bing.net/th/id/OIP.7HULx5TQZ_8hPOHnULiT8wHaHH?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P24", "Notebook", "Flashcard từ vựng kèm ghim khuyên", "Dập khuyên tròn inox tháo lắp tiện lợi. Giấy dày dặn viết bút dạ không thấm, nhỏ gọn bỏ túi để ôn tập ngoại ngữ trên bất kì chuyến xe bus nào.", 15000.0, 400, "https://tse4.mm.bing.net/th/id/OIP.SPEv3WZa2HTqGCQGO-TL4gHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P25", "Accessories", "Băng keo trong màng co 5cm", "Băng dính bản lớn màng OPP tráng keo Acrylic dẻo dai. Kéo giãn chịu lực nặng dùng niêm phong đóng gói hàng hóa đi ngoại tỉnh.", 12000.0, 500, "https://tse4.mm.bing.net/th/id/OIP.BH_qkXZMJUQ_KcNk1PXrswHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P26", "Accessories", "Dao rọc giấy đệm cao su SDI", "Lưỡi thép SK5 vát nghiêng 30 độ sắt lẹm. Cán ốp cao su TPR thiết kế công thái học và ngàm auto-lock chốt chặn trượt.", 16000.0, 280, "https://tse1.mm.bing.net/th/id/OIP.lH6u_155qfoODWFv4c9D6gHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P27", "Accessories", "Giấy note Post-it 3M 3x3", "Dập nổi từ thương hiệu 3M vĩ đại. Lớp keo dán dính đi dính lại trên tường màn hình nhưng không hề cấu vết nham nhở. Viết ăn mực mượt mà.", 18000.0, 600, "https://tse1.mm.bing.net/th/id/OIP.PLTsY_MDpj2HxCR9Aj5F-gHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P28", "Organizer", "Kệ hồ sơ Mica 3 tầng bẹt", "Kệ chia ngăn đựng giấy loại lớn A4. Lắp ráp nhanh theo module, chất nhựa đúc bóng loáng mang lại góc làm việc chuyên nghiệp gọn gàng tối đa.", 210000.0, 40, "https://tse2.mm.bing.net/th/id/OIP.FIpIfquAsQ2jTgwjARvzZAHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P29", "Bag", "Balo chống gù tiểu học Hami", "Balo cho trẻ ôm vừa lưng form hộp bọc da PU siêu nhẹ. Quai 3D phân tán trọng lực và có dạ quang phát sáng ban đêm an toàn qua đường.", 420000.0, 55, "https://tse3.mm.bing.net/th/id/OIP.9a9jDLz_Yfy6-KPB1LJ0vAHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("P30", "Organizer", "Túi My Clear Bag A4 nút gài", "Túi nhựa trong nút bấm tiện dụng. Bảo vệ mọi giấy tờ cá nhân, biên lai hóa đơn khỏi trời mưa gió ẩm ướt hay rơi rớt. Nhựa dẻo không gãy viền.", 3500.0, 1500, "https://tse4.mm.bing.net/th/id/OIP.rC8-SHcnprz1WgyNrBYF9QHaHa?pid=Api&P=0&h=180"));
    }
}
