const API_BASE_URL = '/api';

// DOM Elements
const productGrid = document.getElementById('productGrid');
const emptyState = document.getElementById('emptyState');
const loadingState = document.getElementById('loadingState');
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const productModal = document.getElementById('productModal');
const closeModal = document.getElementById('closeModal');
const modalBody = document.getElementById('modalBody');
const cartDrawer = document.getElementById('cartDrawer');
const cartItemsContainer = document.getElementById('cartItems');
const cartCount = document.getElementById('cartCount');
const cartTotal = document.getElementById('cartTotal');

// State
let cart = [];
let wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
let currentUser = null;
let currentCategory = 'all';
let currentSort = 'default';
let currentSearch = '';
let allLoadedProducts = [];

// --- User Management ---
function initUser() {
    const userJson = localStorage.getItem('user');
    if (userJson) {
        currentUser = JSON.parse(userJson);
        renderUserArea();
    }
}

function renderUserArea() {
    const userArea = document.getElementById('userArea');
    if (currentUser) {
        userArea.innerHTML = `
            <div class="user-info">
                <span class="user-name">Chào, ${currentUser.username}</span>
                <button onclick="logout()" class="logout-btn">Đăng xuất</button>
                ${currentUser.role === 'ROLE_ADMIN' ? '<a href="admin.html" class="admin-link">Quản trị</a>' : ''}
            </div>
        `;
    } else {
        userArea.innerHTML = `<a href="login.html" class="nav-link"><span class="material-icons-outlined">login</span> Đăng Nhập</a>`;
    }
}

function logout() {
    localStorage.removeItem('user');
    currentUser = null;
    location.reload();
}

// --- Product Loading & Rendering ---

async function fetchProducts(reset = true, search = '') {
    if (reset) {
        productGrid.innerHTML = '';
        allLoadedProducts = [];
    }
    showState('loading');
    
    try {
        let url = `${API_BASE_URL}/products`;
        if (search) url += `?search=${encodeURIComponent(search)}`;
        
        const response = await fetch(url);
        if (!response.ok) throw new Error('API Error');
        const data = await response.json();
        let products = data.content;

        // Filtering
        if (currentCategory !== 'all') {
            products = products.filter(p => p.categoryId === currentCategory);
        }

        // Sorting
        if (currentSort === 'price-asc') products.sort((a,b) => a.price - b.price);
        if (currentSort === 'price-desc') products.sort((a,b) => b.price - a.price);

        allLoadedProducts = products;

        if (products.length === 0) {
            showState('empty');
        } else {
            renderProducts(products);
            showState('data');
        }
    } catch (error) {
        console.error("Fetch error:", error);
        showState('empty');
    }
}

function renderProducts(products) {
    products.forEach(product => {
        const isWishlisted = wishlist.includes(product.id);
        const card = document.createElement('div');
        card.className = 'card';
        const priceFmt = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(product.price);

        card.innerHTML = `
            <div class="wishlist-btn ${isWishlisted ? 'active' : ''}" onclick="event.stopPropagation(); toggleWishlist('${product.id}')">
                <span class="material-icons-outlined">favorite</span>
            </div>
            <img src="${product.imageUrl}" alt="${product.name}" class="card-img" onclick="openProductDetail(${JSON.stringify(product).replace(/"/g, '&quot;')})">
            <span class="card-category">${product.categoryId}</span>
            <div class="card-title" onclick="openProductDetail(${JSON.stringify(product).replace(/"/g, '&quot;')})">${product.name}</div>
            <div class="card-footer">
                <span class="card-price">${priceFmt}</span>
                <button class="buy-btn" onclick='addToCart(${JSON.stringify(product).replace(/"/g, '&quot;')})'>Thêm</button>
            </div>
        `;
        productGrid.appendChild(card);
    });
}

function showState(state) {
    loadingState.classList.add('hidden');
    emptyState.classList.add('hidden');
    productGrid.classList.add('hidden');
    if (state === 'loading') loadingState.classList.remove('hidden');
    if (state === 'empty') emptyState.classList.remove('hidden');
    if (state === 'data') productGrid.classList.remove('hidden');
}

// --- Product Details & Reviews ---

async function openProductDetail(product) {
    const priceFmt = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(product.price);
    
    modalBody.innerHTML = `
        <div style="display:flex; flex-direction:column; gap:2rem; width:100%">
            <div style="display:flex; gap:2rem;">
                <img src="${product.imageUrl}" alt="${product.name}" style="width:50%; border-radius:12px; object-fit:cover;">
                <div class="modal-info">
                    <span class="card-category">${product.categoryId}</span>
                    <h2>${product.name}</h2>
                    <h1 style="color: var(--primary-color); font-size: 2.5rem; margin: 0.5rem 0;">${priceFmt}</h1>
                    <span class="stock-badge">Còn trong kho: ${product.stock}</span>
                    <p style="color: var(--text-muted); line-height: 1.6; margin-top: 1rem;">${product.description}</p>
                    <button class="btn-add-cart" onclick='addToCart(${JSON.stringify(product).replace(/"/g, '&quot;')})'>
                        <span class="material-icons-outlined">add_shopping_cart</span> Thêm vào giỏ hàng
                    </button>
                </div>
            </div>
            <div style="border-top:1px solid #333; padding-top:1.5rem;">
                <h3 style="margin-bottom:1rem;">Đánh giá sản phẩm</h3>
                <div id="reviewsList" style="display:flex; flex-direction:column; gap:1rem;">Đang tải đánh giá...</div>
            </div>
        </div>
    `;
    productModal.classList.add('active');
    
    // Lazy fetch reviews
    fetchReviews(product.id);
}

async function fetchReviews(productId) {
    try {
        const res = await fetch(`${API_BASE_URL}/ecommerce/products/${productId}/reviews`);
        if (!res.ok) throw new Error();
        const reviews = await res.json();
        const list = document.getElementById('reviewsList');
        if (reviews.length === 0) {
            list.innerHTML = '<p style="color:#888; font-style:italic;">Chưa có đánh giá nào.</p>';
        } else {
            list.innerHTML = reviews.map(r => `
                <div style="background:rgba(255,255,255,0.05); padding:1rem; border-radius:8px;">
                    <div style="color:#facc15; margin-bottom:0.3rem;">${"★".repeat(r.rating)}${"☆".repeat(5-r.rating)}</div>
                    <p style="font-size:0.9rem;">${r.comment}</p>
                    <small style="color:#666;">- ${r.author}</small>
                </div>
            `).join('');
        }
    } catch (e) {
        document.getElementById('reviewsList').innerHTML = 'Không thể tải đánh giá.';
    }
}

// --- Cart & Discount Logic ---

function toggleCart() {
    cartDrawer.classList.toggle('active');
}

function addToCart(product) {
    const existing = cart.find(item => item.id === product.id);
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ ...product, quantity: 1 });
    }
    updateCartUI();
    showToast(`Đã thêm ${product.name} vào giỏ!`);
    if (!cartDrawer.classList.contains('active')) toggleCart();
}

let currentDiscount = 0;

function applyDiscount() {
    const code = document.getElementById('discountInput').value.trim();
    if (code === 'SALE10') {
        currentDiscount = 0.1;
        document.getElementById('discountMsg').innerText = "Đã áp dụng mã SALE10 (Giảm 10%)";
        document.getElementById('discountMsg').style.display = "block";
        updateCartUI();
    } else {
        alert("Mã giảm giá không hợp lệ");
    }
}

function updateCartUI() {
    cartCount.innerText = cart.reduce((sum, item) => sum + item.quantity, 0);
    if (cart.length === 0) {
        cartItemsContainer.innerHTML = '<p class="empty-msg">Giỏ hàng đang trống</p>';
        cartTotal.innerText = '0 đ';
        return;
    }

    cartItemsContainer.innerHTML = cart.map(item => {
        const priceFmt = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(item.price);
        return `
            <div class="cart-item">
                <img src="${item.imageUrl}" alt="${item.name}">
                <div class="cart-item-info">
                    <h4>${item.name}</h4>
                    <span>${priceFmt} x ${item.quantity}</span>
                </div>
                <span class="material-icons-outlined remove-item" onclick="removeFromCart(${item.id})">delete</span>
            </div>
        `;
    }).join('') + `
        <div class="discount-section">
            <input type="text" id="discountInput" placeholder="Nhập mã giảm giá (SALE10)...">
            <button class="discount-btn" onclick="applyDiscount()">Áp dụng</button>
        </div>
        <div id="discountMsg" class="discount-msg"></div>
    `;

    const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const finalTotal = subtotal * (1 - currentDiscount);
    cartTotal.innerText = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(finalTotal);
}

async function checkout() {
    if (!currentUser) {
        alert("Bạn cần Đăng nhập để thực hiện thanh toán!");
        window.location.href = 'login.html';
        return;
    }
    if (cart.length === 0) return;

    const orderData = {
        customerEmail: currentUser.email,
        totalAmount: cart.reduce((sum, item) => sum + (item.price * item.quantity), 0) * (1 - currentDiscount),
        items: cart.map(item => ({
            productId: item.id.toString(),
            productName: item.name,
            quantity: item.quantity,
            price: item.price
        }))
    };

    try {
        const response = await fetch('/api/checkout', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(orderData)
        });
        const result = await response.json();
        if (result.status === 'success') {
            alert("Thanh toán thành công!\nMã đơn: " + result.orderId);
            cart = [];
            currentDiscount = 0;
            updateCartUI();
            toggleCart();
        }
    } catch (e) { alert("Lỗi thanh toán."); }
}

// --- Wishlist & Utils ---

function toggleWishlist(id) {
    if (wishlist.includes(id)) {
        wishlist = wishlist.filter(w => w !== id);
        showToast("Đã xóa khỏi danh sách yêu thích");
    } else {
        wishlist.push(id);
        showToast("Đã thêm vào danh sách yêu thích!");
    }
    localStorage.setItem('wishlist', JSON.stringify(wishlist));
    fetchProducts(true, currentSearch);
}

function showToast(msg) {
    const toast = document.getElementById('toast');
    toast.innerText = msg;
    toast.classList.add('show');
    setTimeout(() => toast.classList.remove('show'), 3000);
}

// --- Init & Listeners ---

searchBtn.onclick = () => { currentSearch = searchInput.value; fetchProducts(true, currentSearch); };
searchInput.onkeyup = (e) => { if (e.key === 'Enter') { currentSearch = searchInput.value; fetchProducts(true, currentSearch); } };

document.querySelectorAll('.cat-tab').forEach(tab => {
    tab.onclick = (e) => {
        document.querySelectorAll('.cat-tab').forEach(t => t.classList.remove('active'));
        e.target.classList.add('active');
        currentCategory = e.target.getAttribute('data-id');
        fetchProducts();
    };
});

document.getElementById('sortSelect').onchange = (e) => {
    currentSort = e.target.value;
    fetchProducts();
};

window.onload = () => {
    initUser();
    fetchProducts();
};
