AUTH
POST   /api/auth/register       → Register new user
POST   /api/auth/login          → Login, returns JWT
POST   /api/auth/verify-email   → Verify OTP

PRODUCTS (Public)
GET    /api/products            → Get all (with filters: ?category=&search=&minPrice=&maxPrice=)
GET    /api/products/{id}       → Get single product

PRODUCTS (Admin only)
POST   /api/admin/products      → Add product
PUT    /api/admin/products/{id} → Update product
DELETE /api/admin/products/{id} → Delete product

CART (Authenticated)
GET    /api/cart                → Get my cart
POST   /api/cart/add            → Add item to cart
PUT    /api/cart/update         → Update quantity
DELETE /api/cart/remove/{id}    → Remove item

ORDERS (Authenticated)
POST   /api/orders              → Place order from cart
GET    /api/orders/my           → My order history
GET    /api/orders/{id}         → Single order detail

ORDERS (Admin)
GET    /api/admin/orders        → All orders
PUT    /api/admin/orders/{id}/status → Update status