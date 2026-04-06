document.addEventListener('DOMContentLoaded', () => {
    loadCategories();
    updateCartBadge();
});

const API = {
    getCategories: () => fetch('/api/categories').then(r => r.json()),
    getProducts: (categoryId) => fetch(`/api/products?categoryId=${categoryId}`).then(r => r.json()),
    getCart: () => fetch('/api/cart').then(r => r.json()),
    addToCart: (productId, qty) => fetch('/api/cart/add', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({productId, quantity: qty})
    }).then(r => r.json()),
    checkout: () => fetch('/api/cart/checkout', {method: 'POST'}).then(r => r.json()),
    getProductDetails: (id) => fetch(`/api/products/${id}`).then(r => r.json())
};

let currentCategory = null;

async function loadCategories() {
    const categories = await API.getCategories();
    const list = document.getElementById('categoryList');
    list.innerHTML = '';
    
    categories.forEach(cat => {
        const div = document.createElement('div');
        div.className = 'cat-item';
        div.innerHTML = `<span class="cat-icon">${cat.iconPath}</span> ${cat.name}`;
        div.onclick = () => {
            document.querySelectorAll('.cat-item').forEach(e => e.classList.remove('active'));
            div.classList.add('active');
            currentCategory = cat;
            document.getElementById('contentTitle').innerText = 'Danh mục: ' + cat.name;
            loadProducts(cat.id);
        };
        list.appendChild(div);
    });
}

async function loadProducts(categoryId) {
    const products = await API.getProducts(categoryId);
    const grid = document.getElementById('productGrid');
    grid.innerHTML = '';
    
    if (products.length === 0) {
        grid.innerHTML = '<div style="grid-column: 1 / -1; text-align: center; color: var(--text-muted);">Chưa có sản phẩm nào thuộc danh mục này</div>';
        return;
    }
    
    products.forEach(p => {
        const card = document.createElement('div');
        card.className = 'product-card';
        card.innerHTML = `
            <div class="product-icon">📦</div>
            <div class="product-name">${p.name}</div>
            <div class="product-price">${p.price.toLocaleString()} đ</div>
            <div class="product-stock">Tồn kho: ${p.stock}</div>
            <button class="add-cart-btn" onclick="addCart('${p.id}', '${p.name}')">Thêm vào 🛒</button>
        `;
        grid.appendChild(card);
    });
}

async function addCart(productId, productName) {
    await API.addToCart(productId, 1);
    showToast(`Đã thêm ${productName} vào giỏ!`);
    updateCartBadge();
}

async function updateCartBadge() {
    const cartData = await API.getCart();
    let totalItems = 0;
    cartData.items.forEach(i => totalItems += i.quantity);
    document.getElementById('cartBadge').innerText = totalItems;
}

// Sidebar Cart
async function openCart() {
    document.getElementById('cartOverlay').classList.add('show');
    document.getElementById('cartPanel').classList.add('open');
    
    const cartData = await API.getCart();
    const itemsContainer = document.getElementById('cartItems');
    itemsContainer.innerHTML = '';
    
    document.getElementById('cartTotal').innerText = cartData.total.toLocaleString() + ' đ';
    
    if (cartData.items.length === 0) {
        itemsContainer.innerHTML = '<div style="text-align:center; color: #6b7280; padding-top: 50px;">Giỏ hàng của bạn đang trống</div>';
        return;
    }
    
    for (let item of cartData.items) {
        const prod = await API.getProductDetails(item.productId);
        if(prod.error) continue; // skipped 404
        
        const row = document.createElement('div');
        row.className = 'cart-item-row';
        row.innerHTML = `
            <div class="cart-item-info">
                <span class="cart-item-name">${prod.name}</span>
                <span class="cart-item-qty">SL: ${item.quantity}</span>
            </div>
            <span class="cart-item-price">${(prod.price * item.quantity).toLocaleString()} đ</span>
        `;
        itemsContainer.appendChild(row);
    }
}

function closeCart() {
    document.getElementById('cartOverlay').classList.remove('show');
    document.getElementById('cartPanel').classList.remove('open');
}

async function checkout() {
    await API.checkout();
    closeCart();
    updateCartBadge();
    showToast('Thanh toán thành công! 👋');
}

let toastTimeout;
function showToast(msg) {
    const toast = document.getElementById('toast');
    toast.innerText = msg;
    toast.classList.add('show');
    clearTimeout(toastTimeout);
    toastTimeout = setTimeout(() => toast.classList.remove('show'), 3000);
}
