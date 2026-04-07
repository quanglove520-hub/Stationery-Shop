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

    public PageResponse<Product> getAllProducts(String search, int page, int size) {
        List<Product> all = InMemoryStore.getInstance().getProducts();
        if (search != null && !search.trim().isEmpty()) {
            final String s = search.toLowerCase();
            all = all.stream().filter(p -> p.getName().toLowerCase().contains(s)).collect(Collectors.toList());
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
        store.addProduct(new Product("S1", "Pen", "Bút bi Thiên Long", "Bút bi xanh chính hãng Thiên Long TL-027. Đầu bi 0.5mm nét thanh, mực ra đều trơn tru.", 5000.0, 1000, "https://vanphong-pham.com/wp-content/uploads/2018/12/but-bi-thien-long-TL-027.jpg"));
        store.addProduct(new Product("S2", "Notebook", "Sổ tay da cao cấp", "Sổ tay bìa da thật cao cấp, thiết kế mộc mạc đường chỉ may tay tinh xảo. Có dây chun cài sổ chắc chắn.", 150000.0, 50, "https://www.xuongsanxuatsoda.com/wp-content/uploads/2025/06/so-tay-bia-da-that-xuongsanxuatsoda.com_.jpg"));
        store.addProduct(new Product("S3", "Eraser", "Tẩy Gôm Trắng Pentel", "Gôm tẩy Pentel HI-POLYMER siêu sạch, siêu mềm. Tẩy dẻo không làm rách.", 12000.0, 200, "https://tse4.mm.bing.net/th/id/OIP.2XVHpnU7StA1XLaB5Ni3gQHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("S4", "Bag", "Balo sinh viên đa năng", "Balo thiết kế unisex năng động. Vải trượt nước Oxford cao cấp chống mưa nhẹ.", 350000.0, 10, "https://tse2.mm.bing.net/th/id/OIP.yCt8azYLsyKFz4Wf6S9wEAHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("S5", "Pencil", "Hộp bút màu HB", "Set hộp chì màu chuyên dụng tô phác họa nghệ thuật, vỏ gỗ chống gãy lõi.", 45000.0, 120, "https://tse4.mm.bing.net/th/id/OIP.d3O1T8r7xf2m3VtIWiU3KwHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("S6", "Accessories", "Kéo thủ công Deli", "Kéo cắt thủ công Deli, lưỡi kéo bằng thép không gỉ nguyên khối sắc bén.", 25000.0, 80, "https://tse2.mm.bing.net/th/id/OIP.4Tbbhn8hXgP_faYzs-PVzAHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("S7", "Ruler", "Thước 20cm nhôm", "Thước kẻ độ dài 20cm từ nhôm định hình hợp kim siêu nhẹ nhưng cực kì bền.", 15000.0, 300, "https://tse3.mm.bing.net/th/id/OIP.I4E-dZQ6tnloNTR3sRqK4QHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("S8", "Accessories", "Đinh ghim kẹp giấy", "Hộp đồ dùng tổng hợp đinh ghim bảng và kẹp giấy. Sử dụng chất liệu kim loại bọc lớp nhựa bóng đa sắc.", 35000.0, 150, "https://tse3.mm.bing.net/th/id/OIP.5yarAQS4DrxGOefGPrEM3wHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("S9", "Organizer", "Hộp lưu trữ Desktop", "Hộp kệ mini sắp xếp đồ lưu trữ đa năng cho Desktop.", 120000.0, 30, "https://tse1.mm.bing.net/th/id/OIP.Oc34zpLTh_YjKsCyluPdWQHaHa?pid=Api&P=0&h=180"));
        store.addProduct(new Product("S10", "Notebook", "Sổ tay ghi chú lò xo", "Sổ caro gáy lò xo xoắn ốc kép dày dặn và cực kỳ chắc tay. Cho phép lật mở 360 độ hoặc xé trang giấy ghi nháp mà hoàn toàn không ảnh hưởng gáy sổ cuốn.", 25000.0, 400, "https://tse4.mm.bing.net/th/id/OIP.7t3kAcBf9JDnD2SZ9KwUqgHaHa?pid=Api&P=0&h=180"));
    }
}
