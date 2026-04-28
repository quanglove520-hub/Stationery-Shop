const API_BASE_URL = 'http://localhost:4567/api';

// CÃ¡c Element DOM
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



// State biáº¿n toÃ n cá»¥c
let currentPage = 0;
let currentSearch = '';
let currentCategory = 'all';
let currentSort = '';
let isFetching = false;
let hasMoreData = true;
let allLoadedProducts = []; // LÆ°u láº¡i Ä‘á»ƒ dÃ¹ng cho Related products

let wishlist = [];
try {
    const stored = JSON.parse(localStorage.getItem('wishlist'));
    if (Array.isArray(stored)) {
        wishlist = stored;
    }
} catch (e) { console.error('Lá»—i parse wishlist:', e); }

wishlist = wishlist.filter(item => typeof item === 'object' && item !== null); // Clear old format
localStorage.setItem('wishlist', JSON.stringify(wishlist));

// Dropdown UI elements
const wishlistDropdown = document.getElementById('wishlistDropdown');
const wishlistItemsContainer = document.getElementById('wishlistItems');
const wishlistBadge = document.getElementById('wishlistBadge');

// Khá»Ÿi táº¡o á»©ng dá»¥ng
window.onload = () => {
    fetchProducts(true);
    setupIntersectionObserver();
    updateWishlistUI();
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

function toggleWishlistPanel() {
    wishlistDropdown.classList.toggle('hidden');
    setTimeout(() => {
        wishlistDropdown.classList.toggle('active');
    }, 10);
}

// Click outside to close dropdown
document.addEventListener('click', (e) => {
    if (!e.target.closest('nav') && wishlistDropdown.classList.contains('active')) {
        wishlistDropdown.classList.remove('active');
        setTimeout(() => wishlistDropdown.classList.add('hidden'), 300);
    }
});

function toggleWishlist(btn, productId) {
    try {
        let product = allLoadedProducts.find(p => p.id === productId);
        if (!product) product = { id: productId, name: 'Sáº£n pháº©m ' + productId, price: 0, imageUrl: '' };
        
        const index = wishlist.findIndex(item => item && item.id === productId);
        
        if (index === -1) {
            wishlist.push({ 
                id: product.id, name: product.name, price: product.price || 0, 
                imageUrl: product.imageUrl || '', categoryId: product.categoryId || '', 
                stock: product.stock || 0, description: product.description || '' 
            });
            showToast("ÄÃ£ thÃªm vÃ o yÃªu thÃ­ch â¤");
        } else {
            wishlist.splice(index, 1);
            showToast("ÄÃ£ bá» khá»i yÃªu thÃ­ch ðŸ¤");
        }
        
        localStorage.setItem('wishlist', JSON.stringify(wishlist));
        
        // TÃ¬m button vÃ  toggle class
        if(btn && btn.classList && btn.classList.contains('wishlist-btn')) {
            btn.classList.toggle('active');
        } else {
            // If removed from dropdown, update the grid button if exists
            const allBtns = document.querySelectorAll('.wishlist-btn');
            allBtns.forEach(b => {
                const onclickAttr = b.getAttribute('onclick');
                if (onclickAttr && onclickAttr.includes(productId)) {
                    b.classList.remove('active');
                }
            });
        }
        updateWishlistUI();
    } catch (e) {
        alert("Lá»—i toggleWishlist: " + e.message);
    }
}

function updateWishlistUI() {
    try {
        if(wishlistBadge) wishlistBadge.textContent = wishlist.length;
        
        if(!wishlistItemsContainer) return;
        wishlistItemsContainer.innerHTML = '';
        
        if (wishlist.length === 0) {
            wishlistItemsContainer.innerHTML = '<p style="color: var(--text-muted); text-align: center; font-size: 0.9rem; padding: 1rem 0;">Báº¡n chÆ°a cÃ³ sáº£n pháº©m nÃ o trong danh sÃ¡ch yÃªu thÃ­ch.</p>';
            return;
        }
        
        wishlist.forEach(item => {
            if (!item) return;
            const priceFmt = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(item.price || 0);
            const div = document.createElement('div');
            div.className = 'wishlist-item-small';
            div.onclick = () => {
                const fullProd = allLoadedProducts.find(p => p.id === item.id) || item;
                openProductDetail(fullProd);
                wishlistDropdown.classList.remove('active');
                setTimeout(() => wishlistDropdown.classList.add('hidden'), 300);
            };
            div.innerHTML = `
                <img src="${item.imageUrl || ''}" onerror="this.src='https://placehold.co/40x40?text=No+Img'">
                <div class="wishlist-item-small-info">
                    <span>${item.name || 'Unknown'}</span>
                    <span class="price">${priceFmt}</span>
                </div>
                <button class="remove-wishlist-btn">
                    <span class="material-icons-outlined" style="font-size: 1.2rem;">delete</span>
                </button>
            `;
            const rmvBtn = div.querySelector('.remove-wishlist-btn');
            rmvBtn.onclick = (e) => {
                e.stopPropagation();
                toggleWishlist(rmvBtn, item.id);
            };
            wishlistItemsContainer.appendChild(div);
        });
    } catch (e) {
        console.error("Lá»—i updateWishlistUI:", e);
    }
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
        const isWished = wishlist.some(item => item.id === product.id);
        const card = document.createElement('div');
        card.className = 'card';
        card.onclick = () => openProductDetail(product);
        
        const priceFmt = formatPrice(product.price);

        card.innerHTML = `
            <img src="${product.imageUrl}" alt="${product.name}" class="card-img" onerror="this.src='https://placehold.co/400x300?text=No+Image'">
            <button class="wishlist-btn ${isWished ? 'active' : ''}">
                <span class="material-icons-outlined">favorite</span>
            </button>
            <span class="card-category">${product.categoryId}</span>
            <div class="card-title">${product.name}</div>
            <div class="card-footer">
                <span class="card-price">${priceFmt}</span>
                <div style="display:flex; gap:0.5rem;">
                    <button class="buy-btn" onclick="event.stopPropagation(); addToCart('${encodeURIComponent(JSON.stringify(product))}')"><span class="material-icons-outlined" style="font-size:1.2rem">add_shopping_cart</span></button>
                    <button class="buy-btn" onclick="openProductDetail(product)">Chi tiáº¿t</button>
                </div>
            </div>
        `;
        
        const wishBtn = card.querySelector('.wishlist-btn');
        wishBtn.onclick = (e) => {
            e.stopPropagation();
            toggleWishlist(wishBtn, product.id);
        };
        
        productGrid.appendChild(card);
    });
}

// HÃ m Fetch cÃ³ há»— trá»£ phÃ¢n trang
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
        url.searchParams.append('size', 12); // Láº¥y 12 item / trang má»—i láº§n lÆ°á»›t
        if (currentSearch) url.searchParams.append('search', currentSearch);
        if (currentCategory && currentCategory !== 'all') url.searchParams.append('categoryId', currentCategory);
        if (currentSort) url.searchParams.append('sort', currentSort);

        const response = await fetch(url);
        if (!response.ok) throw new Error('API failed');

        const data = await response.json();
        const products = data.content;
        
        allLoadedProducts = isFirstLoad ? products : [...allLoadedProducts, ...products];

        if (products.length < 12) {
            hasMoreData = false; // ÄÃ£ háº¿t layout
            infiniteScrollTrigger.classList.add('hidden');
        }

        if (isFirstLoad && products.length === 0) {
            showState('empty');
        } else {
            renderProducts(products, !isFirstLoad); // ThÃªm ná»‘i cÃ o cuá»‘i lÆ°á»›i
            showState('data');
            currentPage++;
        }
    } catch (error) {
        console.error("Fetch lá»—i:", error);
        // Fallback Mock Data náº¿u mÃ¡y chá»§ bá»‹ táº¯t
        hasMoreData = false;
        infiniteScrollTrigger.classList.add('hidden');
        if(isFirstLoad) {
            const mock = [
                { id:1, name: "BÃºt bi ThiÃªn Long", category: "Pen", price: 5000, stock: 1000, description: "BÃºt bi xanh nÃ©t thanh, má»±c ra Ä‘á»u trÆ¡n tru.", imageUrl: "https://tse2.mm.bing.net/th/id/OIP.UWI6PnnPDKETAxhUtQCm0gHaHa?pid=Api&P=0&h=180" },
                { id:2, name: "Sá»• tay da cao cáº¥p", category: "Notebook", price: 150000, stock: 50, description: "Sá»• bÃ¬a da tháº­t cao cáº¥p, thiáº¿t káº¿ má»™c máº¡c.", imageUrl: "https://tse2.mm.bing.net/th/id/OIP.aYdCziJV_Y3hlhyypSvbtwHaHa?pid=Api&P=0&h=180" },
                { id:3, name: "Táº©y GÃ´m Tráº¯ng Pentel", category: "Eraser", price: 12000, stock: 200, description: "GÃ´m táº©y Ãªm Ã¡i, siÃªu sáº¡ch.", imageUrl: "https://tse4.mm.bing.net/th/id/OIP.2XVHpnU7StA1XLaB5Ni3gQHaHa?pid=Api&P=0&h=180" },
                { id:4, name: "Balo sinh viÃªn Ä‘a nÄƒng", category: "Bag", price: 350000, stock: 10, description: "Balo thiáº¿t káº¿ unisex nÄƒng Ä‘á»™ng chá»‘ng tháº¥m.", imageUrl: "https://tse2.mm.bing.net/th/id/OIP.CxVhY1HQft1QUUtxWEorOAHaHa?pid=Api&P=0&h=180" },
                { id:5, name: "Há»™p bÃºt mÃ u HB", category: "Pencil", price: 45000, stock: 120, description: "Set 12 mÃ u nghá»‡ thuáº­t, vá» gá»— chá»‘ng gÃ£y lÃµi.", imageUrl: "https://tse3.mm.bing.net/th/id/OIP.oOZAsSifRH-Si0v2ru2RYAHaHa?pid=Api&P=0&h=180" },
                { id:6, name: "KÃ©o thá»§ cÃ´ng Deli", category: "Accessories", price: 25000, stock: 80, description: "LÆ°á»¡i kÃ©o sáº¯c bÃ©n bá»c Ä‘á»‡m Ãªm.", imageUrl: "https://tse2.mm.bing.net/th/id/OIP.l4QQVz8-Fl1eqTevKCQd2wHaHa?pid=Api&P=0&h=180" }
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
    
    // TÃ¬m cÃ¡c sáº£n pháº©m khÃ¡c (Related Products)
    let related = allLoadedProducts.filter(p => p.id !== product.id && p.category === product.categoryId).slice(0, 3);
    if(related.length === 0) related = allLoadedProducts.filter(p => p.id !== product.id).slice(0, 3); // Láº¥y Ä‘ai náº¿u ko cÃ¹ng category

    // Render HTML Advance (Ban Ä‘áº§u Ä‘á»ƒ loading pháº§n comments)
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
            <span class="stock-badge">CÃ²n trong kho: ${product.stock} items</span>
            
            <p style="color: var(--text-muted); line-height: 1.6; margin-top: 1rem; flex:1;">
                ${product.description}
            </p>

            <!-- Khá»‘i ÄÃ¡nh GiÃ¡ vÃ  Form LiÃªn Quan Má»›i -->
            <div class="advanced-sections">
                <div class="reviews-section">
                    <div style="display:flex; justify-content: space-between; align-items: center;">
                        <h3 class="reviews-title">ÄÃ¡nh giÃ¡ ná»•i báº­t</h3>
                        <div id="averageRating" style="color: #fbbf24; font-weight: bold;"></div>
                    </div>
                    <div id="reviewsContainer" style="margin-top: 0.5rem; max-height: 200px; overflow-y: auto;">
                        <span style="color: #aaa; font-size: 0.9rem;">Äang táº£i Ä‘Ã¡nh giÃ¡...</span>
                    </div>

                    <!-- Review Form -->
                    <div style="margin-top: 0.5rem; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 0.5rem;">
                        <h4 style="margin-bottom: 0.2rem; font-size: 0.85rem;">Viáº¿t Ä‘Ã¡nh giÃ¡ cá»§a báº¡n</h4>
                        <input type="text" id="reviewAuthor" placeholder="TÃªn cá»§a báº¡n" style="width:100%; padding: 0.5rem; border-radius:4px; margin-bottom:0.5rem; background:rgba(255,255,255,0.05); color:white; border:1px solid var(--border)">
                        <select id="reviewRating" style="width:100%; padding: 0.5rem; border-radius:4px; margin-bottom:0.5rem; background:rgba(15,23,42,0.9); color:white; border:1px solid var(--border)">
                            <option value="5">5 Sao - Tuyá»‡t vá»i</option>
                            <option value="4">4 Sao - Ráº¥t tá»‘t</option>
                            <option value="3">3 Sao - BÃ¬nh thÆ°á»ng</option>
                            <option value="2">2 Sao - KÃ©m</option>
                            <option value="1">1 Sao - Tá»‡</option>
                        </select>
                        <textarea id="reviewComment" placeholder="Nháº­n xÃ©t chi tiáº¿t..." style="width:100%; padding: 0.5rem; border-radius:4px; margin-bottom:0.4rem; background:rgba(255,255,255,0.05); color:white; border:1px solid var(--border); min-height: 45px"></textarea>
                        <button onclick="submitReview('${product.id}')" style="background:var(--primary); color:white; border:none; padding:0.5rem 1rem; border-radius:4px; cursor:pointer; width:100%">Gá»­i Ä‘Ã¡nh giÃ¡</button>
                    </div>
                </div>
                
                <div class="related-products">
                    <h3 class="reviews-title" style="margin-top: 1rem;">CÃ³ thá»ƒ báº¡n cÅ©ng thÃ­ch</h3>
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

        if (!response.ok) throw new Error("API Lá»—i");
        const reviews = await response.json();

        if (reviews.length === 0) {
            reviewsContainer.innerHTML = `<p style="font-size:0.85rem; color: #aaa;">ChÆ°a cÃ³ Ä‘Ã¡nh giÃ¡ nÃ o cho sáº£n pháº©m. Trá»Ÿ thÃ nh ngÆ°á»i Ä‘áº§u tiÃªn review!</p>`;
            averageRating.innerHTML = "";
        } else {
            let totalRating = 0;
            let htmlStr = "";
            reviews.forEach(r => {
                totalRating += r.rating;
                const stars = "â˜…".repeat(r.rating) + "â˜†".repeat(5 - r.rating);
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
            
            averageRating.innerHTML = `${avg}/5 â˜…`;
            reviewsContainer.innerHTML = htmlStr;
        }
    } catch (e) {
        console.error("Lá»—i fetch reviews:", e);
        const reviewsContainer = document.getElementById('reviewsContainer');
        if(reviewsContainer) {
            reviewsContainer.innerHTML = `<p style="font-size:0.85rem; color: #ef4444;">KhÃ´ng thá»ƒ táº£i Ä‘Ã¡nh giÃ¡ lÃºc nÃ y.</p>`;
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
        showToast("Vui lÃ²ng nháº­p tÃªn vÃ  ná»™i dung Ä‘Ã¡nh giÃ¡!");
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

        if (!response.ok) throw new Error("Lá»—i khi gá»­i Ä‘Ã¡nh giÃ¡");
        showToast("Cáº£m Æ¡n báº¡n Ä‘Ã£ Ä‘Ã¡nh giÃ¡!");
        
        // Refresh product detail automatically by reopening modal
        const prod = allLoadedProducts.find(p => p.id === productId);
        if (prod) openProductDetail(prod);
    } catch (e) {
        console.error("Lá»—i submit review:", e);
        showToast("KhÃ´ng thá»ƒ gá»­i Ä‘Ã¡nh giÃ¡ lÃºc nÃ y.");
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

  
document.addEventListener('DOMContentLoaded', function() { var btn = document.getElementById('closeModal'); if(btn) { btn.addEventListener('click', function() { var mod = document.getElementById('productModal'); mod.classList.remove('active'); setTimeout(function(){ mod.classList.add('hidden'); }, 300); }); } }); 

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
        area.innerHTML = '<span class="nav-btn" style="color:var(--text);margin-right:10px;">Xin chÃ o, ' + currentUser.fullName + '</span><button class="nav-btn" onclick="logout()" style="cursor:pointer; border:1px solid rgba(255,255,255,0.2); background:rgba(255,255,255,0.1); border-radius:4px; padding:0.3rem 0.6rem; color:white;">ÄÄƒng xuáº¥t</button>';
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
    showToast('ÄÃ£ thÃªm vÃ o giá» hÃ ng!');
}

function updateCartUI() {
    document.getElementById('cartCount').textContent = cart.reduce((s, i) => s + i.quantity, 0);
    const itemsContainer = document.getElementById('cartItems');
    if(cart.length === 0) {
        itemsContainer.innerHTML = '<p class="empty-msg">Giá» hÃ ng Ä‘ang trá»‘ng</p>';
        document.getElementById('cartTotal').textContent = '0 Ä‘';
        return;
    }
    itemsContainer.innerHTML = cart.map(i => '<div style="display:flex;justify-content:space-between;margin-bottom:10px;"><span style="color:white">' + i.name + ' x' + i.quantity + '</span><span style="color:var(--primary); font-weight:bold;">' + formatPrice(i.price * i.quantity) + '</span></div>').join('');
    const total = cart.reduce((s, i) => s + (i.price * i.quantity), 0);
    document.getElementById('cartTotal').textContent = formatPrice(total);
}

function checkout() {
    if (cart.length === 0) return showToast('Giá» hÃ ng trá»‘ng!');
    if (!currentUser) {
        showToast('Vui lÃ²ng Ä‘Äƒng nháº­p!');
        setTimeout(() => window.location.href = 'login.html', 1500);
        return;
    }
    showToast('Äang xá»­ lÃ½...');
    fetch('/api/order/checkout', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            userId: currentUser.id,
            items: cart.map(i => ({ productId: i.id, quantity: i.quantity, price: i.price }))
        })
    }).then(r => r.json()).then(res => {
        if(res.error) {
            showToast('Lá»—i: ' + res.error);
        } else {
            showToast('Äáº·t hÃ ng thÃ nh cÃ´ng!');
            cart = [];
            updateCartUI();
            toggleCart();
        }
    }).catch(e => showToast('Lá»—i máº¡ng'));
}

document.addEventListener('DOMContentLoaded', initUser);
