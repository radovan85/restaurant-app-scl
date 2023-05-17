window.onload = redirectHome;

function redirectLogin() {
	$("#ajaxLoadedContent").load("/login");

}

function redirectHome() {
	$("#ajaxLoadedContent").load("/home");
}

function redirectRegister() {
	$("#ajaxLoadedContent").load("/userRegistration");

}

function redirectAllProducts() {
	$("#ajaxLoadedContent").load("/products/allProducts");
}

function redirectAddProduct() {
	$("#ajaxLoadedContent").load("/admin/createProduct");
}

function redirectAllProductsByCategory(category) {
	$("#ajaxLoadedContent").load("/products/allProductsByCategory/" + category);
}

function redirectAdmin() {
	$("#ajaxLoadedContent").load("/admin/");
}

function redirectAdminProductList() {
	$("#ajaxLoadedContent").load("/admin/allProducts");
}

function redirectUpdateProduct(productId) {
	$("#ajaxLoadedContent").load("/admin/updateProduct/" + productId);
}

function redirectProductDetails(productId) {
	$("#ajaxLoadedContent").load("/admin/productDetails/" + productId);
}

function redirectItemForm(productId) {
	$("#ajaxLoadedContent").load("/cart/addToCart/" + productId);
}

function redirectItemAdded() {
	$("#ajaxLoadedContent").load("/cart/itemAddCompleted");
}

function redirectCart() {
	$("#ajaxLoadedContent").load("/cart/getCart");
}

function redirectOrderCompleted() {
	$("#ajaxLoadedContent").load("/orders/orderCompleted");
}

function redirectAccountDetails() {
	$("#ajaxLoadedContent").load("/accountInfo");
}

function redirectUpdateAddress(addressId) {
	$("#ajaxLoadedContent").load("/addresses/updateAddress/" + addressId);
}

function redirectAllCustomers() {
	$("#ajaxLoadedContent").load("/admin/allCustomers");
}

function redirectCustomerDetails(customerId) {
	$("#ajaxLoadedContent").load("/admin/customerDetails/" + customerId);
}

function redirectAllOrders() {
	$("#ajaxLoadedContent").load("/admin/allOrders");
}

function redirectOrderDetails(orderId) {
	$("#ajaxLoadedContent").load("/admin/getOrder/" + orderId);
}

function redirectTodaysOrders() {
	$("#ajaxLoadedContent").load("/admin/allOrdersToday");
}

function redirectAbout() {
	$("#ajaxLoadedContent").load("/about");
}


function confirmLoginPass() {
	$.ajax({
		url : "http://localhost:8080/loginPassConfirm",
		type : "POST"
	})
	.done(function(){
		window.location.href = "/";
	})
	.fail(function(){
		$("#ajaxLoadedContent").load("/loginErrorPage");
	})
}

function redirectLogout() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/loggedout"
	})
	.done(function(){
		window.location.href = "/";
	})
	.fail(function(){
		alert("Logout error!");
	})
}

function deleteProduct(productId) {
	if (confirm("Are you sure you want to delete this product?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteProduct/" + productId
		})
		.done(function(){
			redirectAdminProductList();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};


function eraseItem(cartId, itemId) {
	if (confirm("Remove this item from cart?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/cart/deleteItem/" + cartId + "/"
					+ itemId
		})
		.done(function(){
			redirectCart();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
}

function eraseAllItems(cartId) {
	if (confirm("Are you sure you want to clear your cart?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/cart/deleteAllItems/" + cartId
		})
		.done(function(){
			redirectCart();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
}

function redirectOrderConfirmation(cartId) {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/orders/confirmOrder/" + cartId
	})
	.done(function(){
		$("#ajaxLoadedContent").load("orders/confirmOrder/" + cartId);
	})
	.fail(function(){
		$("#ajaxLoadedContent").load("/cart/invalidCart");
	})
}

function executeOrder() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/orders/processOrder"
	})
	.done(function(){
		redirectOrderCompleted();
	})
	.fail(function(){
		alert("Failed!");
	})
}


function deleteOrder(orderId) {
	if (confirm("Remove this order?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteOrder/" + orderId
		})
		.done(function(){
			redirectAllOrders();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteCustomer(customerId) {
	if (confirm("Are you sure you want to remove this customer?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteCustomer/" + customerId
		})
		.done(function(){
			redirectAllCustomers();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};


