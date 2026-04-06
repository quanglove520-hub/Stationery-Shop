document.addEventListener('DOMContentLoaded', () => {
    loadCategories();
    loadProducts();
});

const API = {
    getCategories: () => fetch('/api/categories').then(r => r.json()),
    getProducts: () => fetch('/api/products').then(r => r.json()), // Mock for admin: get all requires adjusting API or loop over cat. For now, our backend /api/products returns all if no categoryId is passed? Wait, /api/products requires categoryId. Let me double check... Yes it did. I'll need to fetch all categories, then fetch products for each. Or just write a /api/admin/products endpoint. 
    // It's easier if I fetch categories, then call /api/products?categoryId= for each, and combine.
    addCategory: (data) => fetch('/api/admin/categories', {method:'POST', body: JSON.stringify(data)}).then(r => r.json()),
    updateCategory: (id, data) => fetch(`/api/admin/categories/${id}`, {method:'PUT', body: JSON.stringify(data)}).then(r => r.json()),
    deleteCategory: (id) => fetch(`/api/admin/categories/${id}`, {method:'DELETE'}).then(r => r.json()),
    
    addProduct: (data) => fetch('/api/admin/products', {method:'POST', body: JSON.stringify(data)}).then(r => r.json()),
    updateProduct: (id, data) => fetch(`/api/admin/products/${id}`, {method:'PUT', body: JSON.stringify(data)}).then(r => r.json()),
    deleteProduct: (id) => fetch(`/api/admin/products/${id}`, {method:'DELETE'}).then(r => r.json()),
};

// State
let allCategories = [];
let allProducts = [];
let isEditMode = false;
let editTargetId = null;

function showTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(e => e.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(e => e.classList.remove('active'));
    document.getElementById(tabName + '-tab').classList.add('active');
    event.target.classList.add('active');
}

async function loadCategories() {
    allCategories = await API.getCategories();
    const tbody = document.querySelector('#catTable tbody');
    tbody.innerHTML = '';
    
    // update select box in Product Modal
    const pCatId = document.getElementById('prodCatId');
    pCatId.innerHTML = '';
    
    allCategories.forEach(c => {
        tbody.innerHTML += `
            <tr>
                <td>${c.id}</td>
                <td>${c.name}</td>
                <td>${c.iconPath}</td>
                <td>
                    <button class="btn-edit" onclick="openCatEdit('${c.id}')">Sửa</button>
                    <button class="btn-danger" onclick="deleteCategory('${c.id}')">Xóa</button>
                </td>
            </tr>
        `;
        pCatId.innerHTML += `<option value="${c.id}">${c.name}</option>`;
    });
}

// Chữa cháy việc chưa có /api/admin/products getAll -> Fetch bằng nhiều request
async function loadProducts() {
    if(allCategories.length === 0) await loadCategories();
    
    const tbody = document.querySelector('#prodTable tbody');
    tbody.innerHTML = '';
    allProducts = [];
    
    for(let c of allCategories) {
        let ps = await fetch(`/api/products?categoryId=${c.id}`).then(r => r.json());
        allProducts = allProducts.concat(ps);
    }
    
    allProducts.forEach(p => {
        tbody.innerHTML += `
            <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td>${p.categoryId}</td>
                <td>${p.price.toLocaleString()} đ</td>
                <td>${p.stock}</td>
                <td>
                    <button class="btn-edit" onclick="openProdEdit('${p.id}')">Sửa</button>
                    <button class="btn-danger" onclick="deleteProduct('${p.id}')">Xóa</button>
                </td>
            </tr>
        `;
    });
}

// ====== MODALS ======

function closeModals() {
    document.getElementById('catModal').classList.remove('show');
    document.getElementById('prodModal').classList.remove('show');
}

function openCatModal() {
    isEditMode = false; editTargetId = null;
    document.getElementById('catModalTitle').innerText = 'Thêm Mới Danh Mục';
    document.getElementById('catId').value = '';
    document.getElementById('catId').disabled = false;
    document.getElementById('catName').value = '';
    document.getElementById('catIcon').value = '';
    document.getElementById('catModal').classList.add('show');
}

function openCatEdit(id) {
    isEditMode = true; editTargetId = id;
    let c = allCategories.find(x => x.id === id);
    document.getElementById('catModalTitle').innerText = 'Sửa Danh Mục';
    document.getElementById('catId').value = c.id;
    document.getElementById('catId').disabled = true; // ko cho sửa ID
    document.getElementById('catName').value = c.name;
    document.getElementById('catIcon').value = c.iconPath;
    document.getElementById('catModal').classList.add('show');
}

function openProdModal() {
    isEditMode = false; editTargetId = null;
    document.getElementById('prodModalTitle').innerText = 'Thêm Mới Sản Phẩm';
    document.getElementById('prodId').value = '';
    document.getElementById('prodId').disabled = false;
    document.getElementById('prodName').value = '';
    document.getElementById('prodPrice').value = '';
    document.getElementById('prodStock').value = '';
    document.getElementById('prodModal').classList.add('show');
}

function openProdEdit(id) {
    isEditMode = true; editTargetId = id;
    let p = allProducts.find(x => x.id === id);
    document.getElementById('prodModalTitle').innerText = 'Sửa Sản Phẩm';
    document.getElementById('prodId').value = p.id;
    document.getElementById('prodId').disabled = true;
    document.getElementById('prodCatId').value = p.categoryId;
    document.getElementById('prodName').value = p.name;
    document.getElementById('prodPrice').value = p.price;
    document.getElementById('prodStock').value = p.stock;
    document.getElementById('prodModal').classList.add('show');
}

// ====== ACTIONS ======

async function saveCategory() {
    let data = {
        id: document.getElementById('catId').value,
        name: document.getElementById('catName').value,
        iconPath: document.getElementById('catIcon').value
    };
    
    if(!isEditMode) {
        await API.addCategory(data);
    } else {
        await API.updateCategory(editTargetId, data);
    }
    closeModals();
    await loadCategories();
    await loadProducts(); // update names
}

async function deleteCategory(id) {
    if(confirm("Bạn có chắc chắn muốn xóa danh mục này?")) {
        let res = await API.deleteCategory(id);
        if(res.error) {
            alert("Lỗi Backend: " + res.error); // Hiển thị Exception theo luật cũ
        } else {
            await loadCategories();
        }
    }
}

async function saveProduct() {
    let data = {
        id: document.getElementById('prodId').value,
        categoryId: document.getElementById('prodCatId').value,
        name: document.getElementById('prodName').value,
        price: parseFloat(document.getElementById('prodPrice').value),
        stock: parseInt(document.getElementById('prodStock').value)
    };
    
    if(!isEditMode) {
        await API.addProduct(data);
    } else {
        await API.updateProduct(editTargetId, data);
    }
    closeModals();
    await loadProducts();
}

async function deleteProduct(id) {
    if(confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
        let res = await API.deleteProduct(id);
        if(res.error) {
            alert("Lỗi Backend: " + res.error); // Hiển thị Exception theo luật cũ
        } else {
            await loadProducts();
        }
    }
}
