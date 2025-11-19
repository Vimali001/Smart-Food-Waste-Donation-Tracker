// API Base URL
const API_BASE_URL = '/api';

// Helper function for API calls
async function apiCall(endpoint, method = 'GET', body = null, requiresAuth = false) {
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include' // Important for session cookies
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// User API
const userAPI = {
    register: (userData) => apiCall('/users/register', 'POST', userData),
    login: (credentials) => apiCall('/users/login', 'POST', credentials),
    logout: () => apiCall('/users/logout', 'POST'),
    getCurrent: () => apiCall('/users/current'),
    getById: (id) => apiCall(`/users/${id}`)
};

// Food Item API
const foodAPI = {
    getAll: () => apiCall('/food-items'),
    getActive: () => apiCall('/food-items/active'),
    getExpiringSoon: (days = 2) => apiCall(`/food-items/expiring-soon?days=${days}`),
    add: (foodItem) => apiCall('/food-items', 'POST', foodItem),
    update: (id, foodItem) => apiCall(`/food-items/${id}`, 'PUT', foodItem),
    delete: (id) => apiCall(`/food-items/${id}`, 'DELETE'),
    getExpiryAlert: (id) => apiCall(`/food-items/${id}/alert`)
};

// Donation API
const donationAPI = {
    create: (foodId) => apiCall('/donations', 'POST', { foodId }),
    getUserDonations: () => apiCall('/donations/user'),
    getNGODonations: () => apiCall('/donations/ngo'),
    getPending: () => apiCall('/donations/pending'),
    getNearby: (pincode) => apiCall(`/donations/nearby?pincode=${pincode}`),
    accept: (donationId) => apiCall(`/donations/${donationId}/accept`, 'POST', {}),
    updateStatus: (donationId, status) => apiCall(`/donations/${donationId}/status`, 'PUT', { status }),
    getById: (id) => apiCall(`/donations/${id}`)
};

// NGO API
const ngoAPI = {
    register: (ngoData) => apiCall('/ngos/register', 'POST', ngoData),
    login: (credentials) => apiCall('/ngos/login', 'POST', credentials),
    logout: () => apiCall('/ngos/logout', 'POST'),
    getAll: () => apiCall('/ngos'),
    getApproved: () => apiCall('/ngos/approved'),
    getNearby: (pincode) => apiCall(`/ngos/nearby?pincode=${pincode}`),
    getById: (id) => apiCall(`/ngos/${id}`)
};

// Recipe API
const recipeAPI = {
    getAll: () => apiCall('/recipes'),
    getByCategory: (category) => apiCall(`/recipes/category/${category}`),
    suggest: () => apiCall('/recipes/suggest'),
    add: (recipe) => apiCall('/recipes', 'POST', recipe)
};

// Admin API
const adminAPI = {
    login: (credentials) => apiCall('/admin/login', 'POST', credentials),
    logout: () => apiCall('/admin/logout', 'POST'),
    getStatistics: () => apiCall('/admin/statistics'),
    getAllUsers: () => apiCall('/admin/users'),
    getAllNGOs: () => apiCall('/admin/ngos'),
    getPendingNGOs: () => apiCall('/admin/ngos/pending'),
    approveNGO: (id) => apiCall(`/admin/ngos/${id}/approve`, 'PUT'),
    rejectNGO: (id) => apiCall(`/admin/ngos/${id}/reject`, 'PUT'),
    deleteUser: (id) => apiCall(`/admin/users/${id}`, 'DELETE')
};

// Utility functions
function showAlert(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;
    
    const container = document.querySelector('.container') || document.body;
    container.insertBefore(alertDiv, container.firstChild);
    
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
}

function getDaysUntilExpiry(expiryDate) {
    if (!expiryDate) return null;
    const today = new Date();
    const expiry = new Date(expiryDate);
    const diffTime = expiry - today;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays;
}

function checkAuth() {
    // Check if user is logged in
    return apiCall('/users/current')
        .then(data => {
            if (data.id) {
                return true;
            }
            return false;
        })
        .catch(() => false);
}

function redirectIfNotAuth() {
    checkAuth().then(isAuth => {
        if (!isAuth) {
            window.location.href = '/login.html';
        }
    });
}

// Store auth info
function setAuthInfo(user) {
    localStorage.setItem('currentUser', JSON.stringify(user));
}

function getAuthInfo() {
    const userStr = localStorage.getItem('currentUser');
    return userStr ? JSON.parse(userStr) : null;
}

function clearAuthInfo() {
    localStorage.removeItem('currentUser');
}

