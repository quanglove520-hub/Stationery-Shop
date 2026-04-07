const API_BASE_URL = 'http://localhost:8080/api/v1';

// Các Element DOM
const productGrid = document.getElementById('productGrid');
const emptyState = document.getElementById('emptyState');
const loadingState = document.getElementById('loadingState');
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const productModal = document.getElementById('productModal');
const closeModal = document.getElementById('closeModal');
const modalBody = document.getElementById('modalBody');

// Biến toàn cục để giả lập trạng thái cho phiên bản MOCK
window.MOCK_DATA_ADDED = true;
window.MOCK_DATA_CLEARED = false;

// Render dữ liệu Product Grid
function renderProducts(products) {
    productGrid.innerHTML = ''; // Clear danh sách cũ
    
    products.forEach(product => {
        const card = document.createElement('div');
        card.className = 'card';
        card.onclick = () => openProductDetail(product);
        
        const priceFmt = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(product.price);

        card.innerHTML = `
            <img src="${product.imageUrl}" alt="${product.name}" class="card-img">
            <span class="card-category">${product.category}</span>
            <div class="card-title">${product.name}</div>
            <div class="card-footer">
                <span class="card-price">${priceFmt}</span>
                <button class="buy-btn">Chi tiết</button>
            </div>
        `;
        productGrid.appendChild(card);
    });
}

// Yêu cầu (Request) fetch Products từ API Server
async function fetchProducts(search = '') {
    showState('loading');
    
    try {
        const url = search ? `${API_BASE_URL}/products?search=${encodeURIComponent(search)}` : `${API_BASE_URL}/products`;
        const response = await fetch(url);
        
        if (!response.ok) throw new Error('API failed');

        const data = await response.json();
        const products = data.content;

        // Xử lý Business Logic: Yêu cầu hiển thị NO DATA
        if (products.length === 0) {
            showState('empty');
        } else {
            renderProducts(products);
            showState('data');
        }
    } catch (error) {
        console.error("Lỗi gọi API hoặc máy chủ chưa sẵn sàng:", error);
        const mockProducts = [
            { id:1, name: "Bút bi Thiên Long", category: "Pen", price: 5000, stock: 1000, description: "Bút bi xanh chính hãng Thiên Long TL-027. Đầu bi 0.5mm nét thanh, mực ra đều trơn tru, không ứ đọng. Thân vỏ nhựa trong suốt cường lực, thiết kế tinh tế tiện dụng cho việc học tập và ký tá tài liệu văn phòng.", imageUrl: "https://vanphong-pham.com/wp-content/uploads/2018/12/but-bi-thien-long-TL-027.jpg" },
            { id:2, name: "Sổ tay da cao cấp", category: "Notebook", price: 150000, stock: 50, description: "Sổ tay bìa da thật cao cấp, thiết kế mộc mạc đường chỉ may tay tinh xảo. Có dây chun cài sổ chắc chắn. Lõi giấy màu kem chống lóa chuẩn châu Âu, định lượng 100gsm không bị thấm mực, thích hợp cho doanh nhân.", imageUrl: "https://www.xuongsanxuatsoda.com/wp-content/uploads/2025/06/so-tay-bia-da-that-xuongsanxuatsoda.com_.jpg" },
            { id:3, name: "Tẩy Gôm Trắng Pentel", category: "Eraser", price: 12000, stock: 200, description: "Gôm tẩy Pentel HI-POLYMER siêu sạch, siêu mềm. Tẩy dẻo không làm rách hay sờn bề mặt giấy, không để lại nhiều mạt cao su. An toàn không chứa chất độc hại, thân thiện môi trường dành cho học sinh.", imageUrl: "https://tse4.mm.bing.net/th/id/OIP.2XVHpnU7StA1XLaB5Ni3gQHaHa?pid=Api&P=0&h=180" },
            { id:4, name: "Balo sinh viên đa năng", category: "Bag", price: 350000, stock: 10, description: "Balo thiết kế unisex năng động. Vải trượt nước Oxford cao cấp chống mưa nhẹ. Có ngăn vách lót chống sốc đựng vừa Laptop 15.6 inch. Khóa kéo mượt mà, phân bổ nhiều ngăn nhỏ tiện dụng cho việc đựng tài liệu và đồ dùng học tập.", imageUrl: "https://tse2.mm.bing.net/th/id/OIP.yCt8azYLsyKFz4Wf6S9wEAHaHa?pid=Api&P=0&h=180" },
            { id:5, name: "Hộp bút màu HB", category: "Pencil", price: 45000, stock: 120, description: "Set hộp chì màu chuyên dụng tô phác họa nghệ thuật, vỏ gỗ chống gãy lõi. Chất chì tiêu chuẩn HB, màu tô êm ái, bám giấy tốt giúp tô mảng rộng không bị sần. Mang lại các phối màu tự nhiên rực rỡ.", imageUrl: "https://tse4.mm.bing.net/th/id/OIP.d3O1T8r7xf2m3VtIWiU3KwHaHa?pid=Api&P=0&h=180" },
            { id:6, name: "Kéo thủ công Deli", category: "Accessories", price: 25000, stock: 80, description: "Kéo cắt thủ công Deli, lưỡi kéo bằng thép không gỉ nguyên khối sắc bén. Cán cầm nhựa đúc bọc cao su chống trơn trượt cầm rất êm tay. Sản phẩm phù hợp cho việc cắt giấy, decan, băng keo mà không dính lưỡi.", imageUrl: "https://tse2.mm.bing.net/th/id/OIP.4Tbbhn8hXgP_faYzs-PVzAHaHa?pid=Api&P=0&h=180" },
            { id:7, name: "Thước 20cm nhôm", category: "Ruler", price: 15000, stock: 300, description: "Thước kẻ độ dài 20cm từ nhôm định hình hợp kim siêu nhẹ nhưng cực kì bền, không độc hại gỉ sét. Các vạch phân chia cm/mm được khắc chìm tinh tế chính xác, mực chống xước trọn đời không lo phai vạch.", imageUrl: "https://tse3.mm.bing.net/th/id/OIP.I4E-dZQ6tnloNTR3sRqK4QHaHa?pid=Api&P=0&h=180" },
            { id:8, name: "Đinh ghim kẹp giấy", category: "Accessories", price: 35000, stock: 150, description: "Hộp đồ dùng tổng hợp đinh ghim bảng và kẹp giấy. Sử dụng chất liệu kim loại bọc lớp nhựa bóng đa sắc giúp tổng hợp các trang tài liệu lại thành mảng, tránh thất lạc. Ghim có đầu cắm bảo vệ an toàn.", imageUrl: "https://tse3.mm.bing.net/th/id/OIP.5yarAQS4DrxGOefGPrEM3wHaHa?pid=Api&P=0&h=180" },
            { id:9, name: "Hộp lưu trữ Desktop", category: "Organizer", price: 120000, stock: 30, description: "Hộp kệ mini sắp xếp đồ lưu trữ đa năng cho Desktop. Thiết kế tầng kệ xếp chéo chia hộc giúp phân loại từ bút, gôm, kẹp cạp, phone. Setup góc văn phòng chuẩn phong cách tối giản gọn gàng, tăng cảm hứng làm việc.", imageUrl: "https://tse1.mm.bing.net/th/id/OIP.Oc34zpLTh_YjKsCyluPdWQHaHa?pid=Api&P=0&h=180" },
            { id:10, name: "Sổ tay ghi chú lò xo", category: "Notebook", price: 25000, stock: 400, description: "Sổ caro gáy lò xo xoắn ốc kép dày dặn và cực kỳ chắc tay. Cho phép lật mở 360 độ hoặc xé trang giấy ghi nháp mà hoàn toàn không ảnh hưởng gáy sổ cuốn. Lưới kẻ caro nhẹ dịu phù hợp với môn tư duy toán học lập trình.", imageUrl: "https://tse4.mm.bing.net/th/id/OIP.7t3kAcBf9JDnD2SZ9KwUqgHaHa?pid=Api&P=0&h=180" }
        ];
        
        // Giả lập trạng thái từ Biến toàn cục (để nút Thêm/Xóa có tác dụng)
        if (window.MOCK_DATA_CLEARED) {
            showState('empty');
        } else {
            renderProducts(window.MOCK_DATA_ADDED ? mockProducts : []);
            showState(window.MOCK_DATA_ADDED ? 'data' : 'empty');
        }
    }
}

// Chuyển đổi trạng thái giao diện UI
function showState(state) {
    loadingState.classList.add('hidden');
    emptyState.classList.add('hidden');
    productGrid.classList.add('hidden');

    if (state === 'loading') loadingState.classList.remove('hidden');
    if (state === 'empty') emptyState.classList.remove('hidden');
    if (state === 'data') productGrid.classList.remove('hidden');
}

// Lắng nghe sự kiện người dùng
searchBtn.onclick = () => fetchProducts(searchInput.value);
searchInput.onkeyup = (e) => { if (e.key === 'Enter') fetchProducts(searchInput.value) };

// Logic hiển thị Product Detail HTML
function openProductDetail(product) {
    const priceFmt = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(product.price);
    
    modalBody.innerHTML = `
        <img src="${product.imageUrl}" alt="${product.name}">
        <div class="modal-info">
            <span class="card-category">${product.category}</span>
            <h2>${product.name}</h2>
            <h1 style="color: var(--primary); font-size: 2.5rem;">${priceFmt}</h1>
            <span class="stock-badge">Còn trong kho: ${product.stock} items</span>
            <p style="color: var(--text-muted); line-height: 1.6; margin-top: 1rem;">${product.description}</p>
        </div>
    `;
    productModal.classList.add('active'); // Hiện popup xem chi tiết
}

closeModal.onclick = () => productModal.classList.remove('active');



// Init Khởi động Web khi tải xong
window.onload = () => {
    fetchProducts();
};
