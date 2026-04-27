const API_BASE_URL = 'http://localhost:4567/api';

// Các Element DOM
const productGrid = document.getElementById('productGrid');
const emptyState = document.getElementById('emptyState');
const loadingState = document.getElementById('loadingState');
const infiniteScrollTrigger = document.getElementById('infiniteScrollTrigger');
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const productModal = document.getElementById('productModal');
const closeModal = document.getElementById('closeModal');
const modalBody = document.getElementById('modalBody');
const toast = document.getElementById('toast');



// State biến toàn cục
let currentPage = 0;
let currentSearch = '';
let currentCategory = 'all';
let currentSort = '';
let isFetching = false;
let hasMoreData = true;
let allLoadedProducts = []; // Lưu lại để dùng cho Related products

let wishlist = JSON.parse(localStorage.getItem('wishlist')) || [];

// Khởi tạo ứng dụng
window.onload = () => {
    fetchProducts(true);
    setupIntersectionObserver();
};

/* =========================================
   Wishlist & Toast Logic
========================================= */
function showToast(message) {
    toast.textContent = message;
    toast.classList.add('show');
    toast.classList.remove('hidden');
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

function toggleWishlist(e, productId) {
    e.stopPropagation(); // Ngăn sự kiện click lan ra thẻ card
    const index = wishlist.indexOf(productId);
    
    if (index === -1) {
        wishlist.push(productId);
        showToast("Đã thêm vào yêu thích ❤");
    } else {
        wishlist.splice(index, 1);
        showToast("Đã bỏ khỏi yêu thích 🤍");
    }
    
    localStorage.setItem('wishlist', JSON.stringify(wishlist));
    
    // Tìm button và toggle class
    const btn = e.currentTarget;
    btn.classList.toggle('active');
}

/* =========================================
   Product Grid & Fetch Logic
========================================= */
function formatPrice(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function renderProducts(products, append = false) {
    if (!append) productGrid.innerHTML = '';
    
    products.forEach(product => {
        const isWished = wishlist.includes(product.id);
        const card = document.createElement('div');
        card.className = 'card';
        card.onclick = () => openProductDetail(product);
        
        const priceFmt = formatPrice(product.price);

        card.innerHTML = `
            <img src="${product.imageUrl}" alt="${product.name}" class="card-img" onerror="this.src='https://placehold.co/400x300?text=No+Image'">
            <button class="wishlist-btn ${isWished ? 'active' : ''}" onclick="toggleWishlist(event, ${product.id})">
                <span class="material-icons-outlined">favorite</span>
            </button>
            <span class="card-category">${product.categoryId}</span>
            <div class="card-title">${product.name}</div>
            <div class="card-footer">
                <span class="card-price">${priceFmt}</span>
                <button class="buy-btn">Chi tiết</button>
            </div>
        `;
        productGrid.appendChild(card);
    });
}

// Hàm Fetch có hỗ trợ phân trang
async function fetchProducts(isFirstLoad = false, search = '') {
    if (isFetching || (!hasMoreData && !isFirstLoad)) return;
    
    if (isFirstLoad) {
        currentPage = 0;
        currentSearch = search;
        hasMoreData = true;
        allLoadedProducts = [];
        showState('loading');
    } else {
        infiniteScrollTrigger.classList.remove('hidden');
    }

    isFetching = true;
    
    try {
        const url = new URL(`${API_BASE_URL}/products`);
        url.searchParams.append('page', currentPage);
        url.searchParams.append('size', 12); // Lấy 12 item / trang mỗi lần lướt
        if (currentSearch) url.searchParams.append('search', currentSearch);
        if (currentCategory && currentCategory !== 'all') url.searchParams.append('categoryId', currentCategory);
        if (currentSort) url.searchParams.append('sort', currentSort);

        const response = await fetch(url);
        if (!response.ok) throw new Error('API failed');

        const data = await response.json();
        const products = data.content;
        
        allLoadedProducts = isFirstLoad ? products : [...allLoadedProducts, ...products];

        if (products.length < 12) {
            hasMoreData = false; // Đã hết layout
            infiniteScrollTrigger.classList.add('hidden');
        }

        if (isFirstLoad && products.length === 0) {
            showState('empty');
        } else {
            renderProducts(products, !isFirstLoad); // Thêm nối cào cuối lưới
            showState('data');
            currentPage++;
        }
    } catch (error) {
        console.error("Fetch lỗi:", error);
        // Fallback Mock Data nếu máy chủ bị tắt
        hasMoreData = false;
        infiniteScrollTrigger.classList.add('hidden');
        if(isFirstLoad) {
            const mock = [
                { id:1, name: "Bút bi Thiên Long", category: "Pen", price: 5000, stock: 1000, description: "Bút bi xanh nét thanh, mực ra đều trơn tru.", imageUrl: "https://tse2.mm.bing.net/th/id/OIP.UWI6PnnPDKETAxhUtQCm0gHaHa?pid=Api&P=0&h=180" },
                { id:2, name: "Sổ tay da cao cấp", category: "Notebook", price: 150000, stock: 50, description: "Sổ bìa da thật cao cấp, thiết kế mộc mạc.", imageUrl: "https://tse2.mm.bing.net/th/id/OIP.aYdCziJV_Y3hlhyypSvbtwHaHa?pid=Api&P=0&h=180" },
                { id:3, name: "Tẩy Gôm Trắng Pentel", category: "Eraser", price: 12000, stock: 200, description: "Gôm tẩy êm ái, siêu sạch.", imageUrl: "https://tse4.mm.bing.net/th/id/OIP.2XVHpnU7StA1XLaB5Ni3gQHaHa?pid=Api&P=0&h=180" },
                { id:4, name: "Balo sinh viên đa năng", category: "Bag", price: 350000, stock: 10, description: "Balo thiết kế unisex năng động chống thấm.", imageUrl: "https://tse2.mm.bing.net/th/id/OIP.CxVhY1HQft1QUUtxWEorOAHaHa?pid=Api&P=0&h=180" },
                { id:5, name: "Hộp bút màu HB", category: "Pencil", price: 45000, stock: 120, description: "Set 12 màu nghệ thuật, vỏ gỗ chống gãy lõi.", imageUrl: "https://tse3.mm.bing.net/th/id/OIP.oOZAsSifRH-Si0v2ru2RYAHaHa?pid=Api&P=0&h=180" },
                { id:6, name: "Kéo thủ công Deli", category: "Accessories", price: 25000, stock: 80, description: "Lưỡi kéo sắc bén bọc đệm êm.", imageUrl: "https://tse2.mm.bing.net/th/id/OIP.l4QQVz8-Fl1eqTevKCQd2wHaHa?pid=Api&P=0&h=180" }
            ];
            allLoadedProducts = mock;
            renderProducts(mock, false);
            showState('data');
        }
    } finally {
        isFetching = false;
        if (isFirstLoad && !hasMoreData) infiniteScrollTrigger.classList.add('hidden');
        else if (isFirstLoad && hasMoreData) infiniteScrollTrigger.classList.remove('hidden');
    }
}

// Infinite Scroll Observer Trigger
function setupIntersectionObserver() {
    const observer = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && !isFetching && hasMoreData) {
            fetchProducts(false, currentSearch);
        }
    }, { rootMargin: '100px' });
    
    observer.observe(infiniteScrollTrigger);
}

function showState(state) {
    loadingState.classList.add('hidden');
    emptyState.classList.add('hidden');
    if (state === 'loading') loadingState.classList.remove('hidden');
    if (state === 'empty') emptyState.classList.remove('hidden');
    if (state === 'data') productGrid.classList.remove('hidden');
}

searchBtn.onclick = () => fetchProducts(true, searchInput.value);
searchInput.onkeyup = (e) => { if (e.key === 'Enter') fetchProducts(true, searchInput.value) };

/* =========================================
   Product Detail Advanced Modal Logic
========================================= */
async function openProductDetail(product) {
    const priceFmt = formatPrice(product.price);
    const prodJson = encodeURIComponent(JSON.stringify(product));
    
    // Tìm các sản phẩm khác (Related Products)
    let related = allLoadedProducts.filter(p => p.id !== product.id && p.category === product.categoryId).slice(0, 3);
    if(related.length === 0) related = allLoadedProducts.filter(p => p.id !== product.id).slice(0, 3); // Lấy đai nếu ko cùng category

    // Render HTML Advance (Ban đầu để loading phần comments)
    modalBody.innerHTML = `
        <div class="image-gallery">
            <img src="${product.imageUrl}" class="main-img" onerror="this.src='https://placehold.co/400x300?text=No+Image'">
            <div class="thumbnails">
                <img src="${product.imageUrl}" onerror="this.src='https://placehold.co/100x100?text=1'">
                <img src="https://tse3.mm.bing.net/th/id/OIP.UWI6PnnPDKETAxhUtQCm0gHaHa?pid=Api&P=0&h=100" >
                <img src="https://tse2.mm.bing.net/th/id/OIP.CxVhY1HQft1QUUtxWEorOAHaHa?pid=Api&P=0&h=100">
            </div>
        </div>
        
        <div class="modal-info">
            <div style="display:flex; justify-content: space-between; align-items: start;">
                <div>
                    <span class="card-category">${product.categoryId}</span>
                    <h2 style="font-size: 2rem; margin-top: 0.5rem">${product.name}</h2>
                </div>
            </div>
            
            <h1 style="color: var(--primary); font-size: 2.5rem;">${priceFmt}</h1>
            <span class="stock-badge">Còn trong kho: ${product.stock} items</span>
            
            <p style="color: var(--text-muted); line-height: 1.6; margin-top: 1rem; flex:1;">
                ${product.description}
            </p>

            <!-- Khối Đánh Giá và Form Liên Quan Mới -->
            <div class="advanced-sections">
                <div class="reviews-section">
                    <div style="display:flex; justify-content: space-between; align-items: center;">
                        <h3 class="reviews-title">Đánh giá nổi bật</h3>
                        <div id="averageRating" style="color: #fbbf24; font-weight: bold;"></div>
                    </div>
                    <div id="reviewsContainer" style="margin-top: 0.5rem; max-height: 200px; overflow-y: auto;">
                        <span style="color: #aaa; font-size: 0.9rem;">Đang tải đánh giá...</span>
                    </div>

                    <!-- Review Form -->
                    <div style="margin-top: 0.5rem; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 0.5rem;">
                        <h4 style="margin-bottom: 0.2rem; font-size: 0.85rem;">Viết đánh giá của bạn</h4>
                        <input type="text" id="reviewAuthor" placeholder="Tên của bạn" style="width:100%; padding: 0.5rem; border-radius:4px; margin-bottom:0.5rem; background:rgba(255,255,255,0.05); color:white; border:1px solid var(--border)">
                        <select id="reviewRating" style="width:100%; padding: 0.5rem; border-radius:4px; margin-bottom:0.5rem; background:rgba(15,23,42,0.9); color:white; border:1px solid var(--border)">
                            <option value="5">5 Sao - Tuyệt vời</option>
                            <option value="4">4 Sao - Rất tốt</option>
                            <option value="3">3 Sao - Bình thường</option>
                            <option value="2">2 Sao - Kém</option>
                            <option value="1">1 Sao - Tệ</option>
                        </select>
                        <textarea id="reviewComment" placeholder="Nhận xét chi tiết..." style="width:100%; padding: 0.5rem; border-radius:4px; margin-bottom:0.4rem; background:rgba(255,255,255,0.05); color:white; border:1px solid var(--border); min-height: 45px"></textarea>
                        <button onclick="submitReview('${product.id}')" style="background:var(--primary); color:white; border:none; padding:0.5rem 1rem; border-radius:4px; cursor:pointer; width:100%">Gửi đánh giá</button>
                    </div>
                </div>
                
                <div class="related-products">
                    <h3 class="reviews-title" style="margin-top: 1rem;">Có thể bạn cũng thích</h3>
                    <div style="display:grid; grid-template-columns: repeat(3, 1fr); gap:0.5rem;">
                        ${related.map(r => `
                            <div style="background:rgba(255,255,255,0.05); border-radius:8px; padding:0.5rem; text-align:center; cursor:pointer; border:1px solid rgba(255,255,255,0.05); transition: 0.2s;" onclick="openProductDetail({ id:'${r.id}', name:'${r.name.replace(/'/g, "\\'")}', categoryId:'${r.categoryId}', price:${r.price}, stock:${r.stock}, description:'${r.description.replace(/'/g, "\\'")}', imageUrl:'${r.imageUrl}' })" onmouseover="this.style.borderColor='var(--primary)'" onmouseout="this.style.borderColor='rgba(255,255,255,0.05)'">
                                <img src="${r.imageUrl}" style="width:100%; height:80px; object-fit:cover; border-radius:4px;" onerror="this.src='https://placehold.co/100x100?text=No+Img'">
                                <div style="font-size:0.75rem; margin-top:0.3rem; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">${r.name}</div>
                                <div style="font-size:0.75rem; color:var(--primary); font-weight:bold;">${formatPrice(r.price)}</div>
                            </div>
                        `).join('')}
                    </div>
                </div>
            </div>
        </div>
    `;
    productModal.classList.add('active');
    productModal.classList.remove('hidden');

    // Fetch reviews from API
    try {
        const response = await fetch(`${API_BASE_URL}/ecommerce/products/${product.id}/reviews`);
        const reviewsContainer = document.getElementById('reviewsContainer');
        const averageRating = document.getElementById('averageRating');

        if (!response.ok) throw new Error("API Lỗi");
        const reviews = await response.json();

        if (reviews.length === 0) {
            reviewsContainer.innerHTML = `<p style="font-size:0.85rem; color: #aaa;">Chưa có đánh giá nào cho sản phẩm. Trở thành người đầu tiên review!</p>`;
            averageRating.innerHTML = "";
        } else {
            let totalRating = 0;
            let htmlStr = "";
            reviews.forEach(r => {
                totalRating += r.rating;
                const stars = "★".repeat(r.rating) + "☆".repeat(5 - r.rating);
                htmlStr += `
                    <div class="review-item" style="margin-bottom: 0.75rem; padding-bottom: 0.5rem; border-bottom: 1px solid rgba(255,255,255,0.05);">
                        <div class="stars" style="color: #fbbf24;">${stars}</div>
                        <p style="font-size:0.85rem; margin: 0.2rem 0;">"${r.comment}"</p>
                        <span style="font-size:0.7rem; color: #888;">- ${r.author}</span>
                    </div>
                `;
            });
            let avg = (totalRating / reviews.length).toFixed(1);
            if (avg.endsWith('.0')) avg = avg.slice(0, -2);
            
            averageRating.innerHTML = `${avg}/5 ★`;
            reviewsContainer.innerHTML = htmlStr;
        }
    } catch (e) {
        console.error("Lỗi fetch reviews:", e);
        const reviewsContainer = document.getElementById('reviewsContainer');
        if(reviewsContainer) {
            reviewsContainer.innerHTML = `<p style="font-size:0.85rem; color: #ef4444;">Không thể tải đánh giá lúc này.</p>`;
        }
    }
}

closeModal.onclick = () => {
    productModal.classList.remove('active');
    setTimeout(() => productModal.classList.add('hidden'), 300);
};

/* =========================================
   Submit Review
========================================= */
async function submitReview(productId) {
    const author = document.getElementById('reviewAuthor').value.trim();
    const rating = document.getElementById('reviewRating').value;
    const comment = document.getElementById('reviewComment').value.trim();

    if (!author || !comment) {
        showToast("Vui lòng nhập tên và nội dung đánh giá!");
        return;
    }

    try {
        const formData = new URLSearchParams();
        formData.append('author', author);
        formData.append('rating', rating);
        formData.append('comment', comment);

        const response = await fetch(`${API_BASE_URL}/ecommerce/products/${productId}/reviews`, {
            method: 'POST',
            body: formData
        });

        if (!response.ok) throw new Error("Lỗi khi gửi đánh giá");
        showToast("Cảm ơn bạn đã đánh giá!");
        
        // Refresh product detail automatically by reopening modal
        const prod = allLoadedProducts.find(p => p.id === productId);
        if (prod) openProductDetail(prod);
    } catch (e) {
        console.error("Lỗi submit review:", e);
        showToast("Không thể gửi đánh giá lúc này.");
    }
}

/* =========================================
   Filter & Sort Event Listeners
========================================= */
const categoryTabs = document.querySelectorAll('.cat-tab');
if (categoryTabs) {
    categoryTabs.forEach(tab => {
        tab.addEventListener('click', (e) => {
            categoryTabs.forEach(t => t.classList.remove('active'));
            e.currentTarget.classList.add('active');
            currentCategory = e.currentTarget.getAttribute('data-id');
            fetchProducts(true, currentSearch);
        });
    });
}

const sortSelect = document.getElementById('sortSelect');
if (sortSelect) {
    sortSelect.addEventListener('change', (e) => {
        currentSort = e.target.value;
        fetchProducts(true, currentSearch);
    });
}

