window.onload = redirectHome;



function redirectHome() {
	redirectUrlPath(`/home`);
}

function redirectLogin() {
	redirectUrlPath(`/login`);
}

function redirectAbout() {
	redirectUrlPath(`/about`);
}

function redirectAdmin() {
	redirectUrlPath(`/admin/`);
}

function redirectProductDetails(productId) {
	redirectUrlPath(`/admin/productDetails/${productId}`);
}

function deleteProduct(productId) {
	if (confirm("Are you sure you want to delete this product?")) {
		axios.get("http://localhost:8080/admin/deleteProduct/" + productId)
			.then(() => {
				redirectAdminProductList();
			})


			.catch(() => {
				alert(`Failed!`);
			})
	}
};

function redirectAddProduct() {
	redirectUrlPath(`/admin/createProduct`);
}


function redirectAdminProductList() {
	axios.get(`/admin/allProducts`)
		.then(response => {
			var contentDiv = document.getElementById(`axiosLoadedContent`);
			contentDiv.innerHTML = response.data;

			var scripts = contentDiv.querySelectorAll(`script`);
			scripts.forEach((oldScript) => {
				var newScript = document.createElement(`script`);
				newScript.type = `text/javascript`;
				if (oldScript.src) {
					newScript.src = oldScript.src;
				} else {
					newScript.textContent = oldScript.textContent;
				}
				document.body.appendChild(newScript);
				oldScript.parentNode.removeChild(oldScript);
			});
		})
		.catch(error => {
			console.error(`Error loading login page:`, error);
		});
}

function redirectAllProducts() {
	axios.get(`/products/allProducts`)
		.then(response => {
			var contentDiv = document.getElementById(`axiosLoadedContent`);
			contentDiv.innerHTML = response.data;

			var scripts = contentDiv.querySelectorAll(`script`);
			scripts.forEach((oldScript) => {
				var newScript = document.createElement(`script`);
				newScript.type = `text/javascript`;
				if (oldScript.src) {
					newScript.src = oldScript.src;
				} else {
					newScript.textContent = oldScript.textContent;
				}
				document.body.appendChild(newScript);
				oldScript.parentNode.removeChild(oldScript);
			});
		})
		.catch(error => {
			console.error(`Error loading login page:`, error);
		});
}

function redirectAllProductsByCategory(category) {
	axios.get(`/products/allProductsByCategory/${category}`)
		.then(response => {
			var contentDiv = document.getElementById(`axiosLoadedContent`);
			contentDiv.innerHTML = response.data;

			var scripts = contentDiv.querySelectorAll(`script`);
			scripts.forEach((oldScript) => {
				var newScript = document.createElement(`script`);
				newScript.type = `text/javascript`;
				if (oldScript.src) {
					newScript.src = oldScript.src;
				} else {
					newScript.textContent = oldScript.textContent;
				}
				document.body.appendChild(newScript);
				oldScript.parentNode.removeChild(oldScript);
			});
		})
		.catch(error => {
			console.error(`Error loading login page:`, error);
		});
}

function redirectAllCustomers() {
	axios.get(`/admin/allCustomers`)
		.then(response => {
			var contentDiv = document.getElementById(`axiosLoadedContent`);
			contentDiv.innerHTML = response.data;

			var scripts = contentDiv.querySelectorAll(`script`);
			scripts.forEach((oldScript) => {
				var newScript = document.createElement(`script`);
				newScript.type = `text/javascript`;
				if (oldScript.src) {
					newScript.src = oldScript.src;
				} else {
					newScript.textContent = oldScript.textContent;
				}
				document.body.appendChild(newScript);
				oldScript.parentNode.removeChild(oldScript);
			});
		})
		.catch(error => {
			console.error(`Error loading login page:`, error);
		});
}

function redirectAllOrders() {
	axios.get(`/admin/allOrders`)
		.then(response => {
			var contentDiv = document.getElementById(`axiosLoadedContent`);
			contentDiv.innerHTML = response.data;

			var scripts = contentDiv.querySelectorAll(`script`);
			scripts.forEach((oldScript) => {
				var newScript = document.createElement(`script`);
				newScript.type = `text/javascript`;
				if (oldScript.src) {
					newScript.src = oldScript.src;
				} else {
					newScript.textContent = oldScript.textContent;
				}
				document.body.appendChild(newScript);
				oldScript.parentNode.removeChild(oldScript);
			});
		})
		.catch(error => {
			console.error(`Error loading login page:`, error);
		});
}

function redirectTodaysOrders() {
	axios.get(`/admin/allOrdersToday`)
		.then(response => {
			var contentDiv = document.getElementById(`axiosLoadedContent`);
			contentDiv.innerHTML = response.data;

			var scripts = contentDiv.querySelectorAll(`script`);
			scripts.forEach((oldScript) => {
				var newScript = document.createElement(`script`);
				newScript.type = `text/javascript`;
				if (oldScript.src) {
					newScript.src = oldScript.src;
				} else {
					newScript.textContent = oldScript.textContent;
				}
				document.body.appendChild(newScript);
				oldScript.parentNode.removeChild(oldScript);
			});
		})
		.catch(error => {
			console.error(`Error loading login page:`, error);
		});
}




function confirmLoginPass() {
	axios.post(`http://localhost:8080/loginPassConfirm`)
		.then(function(response) {
			window.location.href = `/`;
		})
		.catch(function(error) {
			axios.get(`/loginErrorPage`)
				.then(function(response) {
					document.getElementById(`axiosLoadedContent`).innerHTML = response.data;
				})
				.catch(function(error) {
					console.error(`Error loading the error page content.`, error);
				});
		});
}


function login() {
	var formData = new FormData(document.getElementById("loginForm"));
	var serializedFormData = new URLSearchParams(formData).toString();

	document.cookie = "JSESSIONID=" + Math.random() + "; SameSite=None; Secure";

	axios.post('http://localhost:8080/login', serializedFormData, {
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded'
		},
		withCredentials: true
	})
		.then(function(response) {
			confirmLoginPass();
		})
		.catch(function(error) {
			alert("Failed!");
		});
}





function redirectLogout() {
	axios.post('http://localhost:8080/loggedout')
		.then(function() {
			window.location.href = "/";
		})
		.catch(function() {
			alert("Logout error!");
		});
}

function addProduct() {
	if (validateProduct()) {
		var formData = new FormData(document.getElementById("productAddForm"));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post('http://localhost:8080/admin/createProduct', serializedFormData)
			.then(function(response) {
				redirectAdminProductList();
			})
			.catch(function(error) {
				alert("Failed!");
			});
	}
}

function redirectUpdateProduct(productId) {
	redirectUrlPath(`/admin/updateProduct/${productId}`);
}

function redirectRegister() {
	redirectUrlPath(`/userRegistration`);
}


function register() {
	if (validateRegForm()) {
		var formData = new FormData(document.getElementById("registrationForm"));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/saveUser`, serializedFormData)
			.then(() => {
				redirectRegistrationComplete();
			})

			.catch((error) => {
				if (error.response.status === 409) {
					redirectRegistrationFailed();
				} else {
					alert(`Failed!`);
				}

			})
	}
}

function addCartItem() {
	if (validateItem()) {
		var formData = new FormData(document.getElementById(`itemForm`));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/cart/addToCart`, serializedFormData)
			.then(() => {
				redirectItemAdded();
			})

			.catch(() => {
				alert(`Failed`);
			})
	}
}

function updateAddress() {
	if (validateAddress()) {
		var formData = new FormData(document.getElementById(`addressForm`));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/addresses/createAddress`, serializedFormData)
			.then(() => {
				redirectAccountDetails();
			})

			.catch(() => {
				alert(`Failed`);
			})
	}
}

function redirectRegistrationComplete() {
	redirectUrlPath(`/registerComplete`);
}

function redirectRegistrationFailed() {
	redirectUrlPath(`/registerFail`);
}

function redirectItemForm(productId) {
	redirectUrlPath(`/cart/addToCart/${productId}`);
}

function redirectItemAdded() {
	redirectUrlPath(`/cart/itemAddCompleted`);
}

function redirectCart() {
	redirectUrlPath(`/cart/getCart`);
}

function eraseItem(itemId) {
	if (confirm(`Remove this item from cart?`)) {
		axios.get(`http://localhost:8080/cart/deleteItem/${itemId}`)
			.then(() => {
				redirectCart();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}

function eraseAllItems(cartId) {
	if (confirm("Are you sure you want to clear your cart?")) {
		axios.get(`http://localhost:8080/cart/deleteAllItems/${cartId}`)
			.then(() => {
				redirectCart();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}



function redirectOrderConfirmation(cartId) {
	axios.get(`http://localhost:8080/orders/confirmOrder/${cartId}`)
		.then((response) => {
			document.getElementById(`axiosLoadedContent`).innerHTML = response.data;
		})

		.catch((error) => {
			if (error.response.status === 422) {
				redirectInvalidCart();
			} else {
				alert(`Failed`);
			}
		})
}

function redirectInvalidCart() {
	redirectUrlPath(`/cart/invalidCart`);
}

function redirectOrderCompleted() {
	redirectUrlPath(`/orders/orderCompleted`);
}

function executeOrder() {
	axios.post(`http://localhost:8080/orders/processOrder`)

		.then(() => {
			redirectOrderCompleted();
		})

		.catch(() => {
			alert(`Failed!`);
		})
}

function redirectAccountDetails() {
	redirectUrlPath(`/accountInfo`);
}

function redirectUpdateAddress(addressId) {
	redirectUrlPath(`/addresses/updateAddress/${addressId}`);
}

function redirectCustomerDetails(customerId){
	redirectUrlPath(`/admin/customerDetails/${customerId}`);
}

function deleteCustomer(customerId){
	if (confirm("Are you sure you want to remove this customer?")) {
		axios.get(`http://localhost:8080/admin/deleteCustomer/${customerId}`)
		.then(() => {
			redirectAllCustomers();
		})
		
		.catch(() => {
			alert(`Failed!`);
		})
	}
}

function redirectOrderDetails(orderId){
	redirectUrlPath(`/admin/getOrder/${orderId}`);
}

function deleteOrder(orderId){
	if (confirm("Remove this order?")) {
		axios.get(`http://localhost:8080/admin/deleteOrder/${orderId}`)
		.then(() => {
			redirectAllOrders();
		})
		
		.catch(() => {
			alert(`Failed!`);
		})
	}
}



function redirectUrlPath(path) {
	axios.get(path)
		.then(response => {
			document.getElementById(`axiosLoadedContent`).innerHTML = response.data;
		})
		.catch(error => {
			console.error(`Error loading home page:`, error);
		});
}




















