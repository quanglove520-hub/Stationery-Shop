
// --- TEAM LEADER LOGIC ---
let cart = [];
let currentUser = null;

function initUser() {
    const userData = localStorage.getItem('user');
    if (userData) {
        currentUser = JSON.parse(userData);
        renderUserArea();
    }
}

function renderUserArea() {
    const area = document.getElementById('userArea');
    if (currentUser) {
        area.innerHTML = '<span class="nav-btn" style="color:var(--text);margin-right:10px;">Xin chào, ' + currentUser.fullName + '</span><button class="nav-btn" onclick="logout()" style="cursor:pointer;">Đăng xuất</button>';
    }
}

function logout() {
    localStorage.removeItem('user');
    currentUser = null;
    window.location.reload();
}

function toggleCart() {
    const drawer = document.getElementById('cartDrawer');
    if (drawer.classList.contains('hidden')) {
        drawer.classList.remove('hidden');
        drawer.style.transform = 'translateX(0)';
    } else {
        drawer.style.transform = 'translateX(100%)';
        setTimeout(() => drawer.classList.add('hidden'), 300);
    }
}

function addToCart(productStr) {
    const product = JSON.parse(decodeURIComponent(productStr));
    const existing = cart.find(i => i.id === product.id);
    if (existing) existing.quantity++;
    else cart.push({...product, quantity: 1});
    updateCartUI();
    showToast('Đã thêm vào giỏ hàng!');
}

function updateCartUI() {
    document.getElementById('cartCount').textContent = cart.reduce((s, i) => s + i.quantity, 0);
    const itemsContainer = document.getElementById('cartItems');
    if(cart.length === 0) {
        itemsContainer.innerHTML = '<p class="empty-msg">Giỏ hàng đang trống</p>';
        document.getElementById('cartTotal').textContent = '0 đ';
        return;
    }
    itemsContainer.innerHTML = cart.map(i => '<div style="display:flex;justify-content:space-between;margin-bottom:10px;"><span style="color:white">' + i.name + ' x' + i.quantity + '</span><span style="color:white">' + (i.price * i.quantity) + 'đ</span></div>').join('');
    const total = cart.reduce((s, i) => s + (i.price * i.quantity), 0);
    document.getElementById('cartTotal').textContent = total + ' đ';
}

function checkout() {
    if (cart.length === 0) return showToast('Giỏ hàng trống!');
    if (!currentUser) {
        showToast('Vui lòng đăng nhập!');
        setTimeout(() => window.location.href = 'login.html', 1500);
        return;
    }
    showToast('Đang xử lý...');
    fetch('/api/order/checkout', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            userId: currentUser.id,
            items: cart.map(i => ({ productId: i.id, quantity: i.quantity, price: i.price }))
        })
    }).then(r => r.json()).then(res => {
        if(res.error) {
            showToast('Lỗi: ' + res.error);
        } else {
            showToast('Đặt hàng thành công!');
            cart = [];
            updateCartUI();
            toggleCart();
        }
    }).catch(e => showToast('Lỗi mạng'));
}

document.addEventListener('DOMContentLoaded', initUser);
