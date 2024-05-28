function validateAddress() {

	var address = document.getElementById("address").value;
	var city = document.getElementById("city").value;
	var postcode = document.getElementById("postcode").value;

	var addressError = document.getElementById("addressError");
	var cityError = document.getElementById("cityError");
	var postcodeError = document.getElementById("postcodeError");


	var returnValue = true;

	if (address === "" || address.length > 75) {
		addressError.style.visibility = "visible";
		returnValue = false;
	} else {
		addressError.style.visibility = "hidden";
	};

	if (city === "" || city.length > 40) {
		cityError.style.visibility = "visible";
		returnValue = false;
	} else {
		cityError.style.visibility = "hidden";
	};


	if (postcode.length < 5 || postcode.length > 10) {
		postcodeError.style.visibility = "visible";
		returnValue = false;
	} else {
		postcodeError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateProduct() {
	var productName = document.getElementById("productName").value;
	var description = document.getElementById("description").value;
	var category = document.getElementById("category").value;
	var productPrice = document.getElementById("productPrice").value;
	var imageUrl = document.getElementById("imageUrl").value;

	var productNameError = document.getElementById("productNameError");
	var descriptionError = document.getElementById("descriptionError");
	var categoryError = document.getElementById("categoryError");
	var productPriceError = document.getElementById("productPriceError");
	var imageUrlError = document.getElementById("imageUrlError");

	var productPriceNum = Number(productPrice);
	var returnValue = true;

	if (productName === "" || productName.length > 40) {
		productNameError.style.visibility = "visible";
		returnValue = false;
	} else {
		productNameError.style.visibility = "hidden";
	}


	if (description === "" || description.length > 90) {
		descriptionError.style.visibility = "visible";
		returnValue = false;
	} else {
		descriptionError.style.visibility = "hidden";
	}


	if (category === "") {
		categoryError.style.visibility = "visible";
		returnValue = false;
	} else {
		categoryError.style.visibility = "hidden";
	}


	if (productPrice === "" || productPriceNum <= 0) {
		productPriceError.style.visibility = "visible";
		returnValue = false;
	} else {
		productPriceError.style.visibility = "hidden";
	}

	if (imageUrl === "" || imageUrl.length > 255) {
		imageUrlError.style.visibility = "visible";
		returnValue = false;
	} else {
		imageUrlError.style.visibility = "hidden";
	}


	return returnValue;
};

function validateRegForm() {

	var firstName = document.getElementById("firstName").value;
	var lastName = document.getElementById("lastName").value;
	var email = document.getElementById("email").value;
	var password = document.getElementById("password").value;
	var confirmpass = document.getElementById("confirmpass").value;
	var address = document.getElementById("address").value;
	var city = document.getElementById("city").value;
	var postcode = document.getElementById("postcode").value;
	var customerPhone = document.getElementById("customerPhone").value;

	var firstNameError = document.getElementById("firstNameError");
	var lastNameError = document.getElementById("lastNameError");
	var emailError = document.getElementById("emailError");
	var passwordError = document.getElementById("passwordError");
	var addressError = document.getElementById("addressError");
	var cityError = document.getElementById("cityError");
	var postcodeError = document.getElementById("postcodeError");
	var customerPhoneError = document.getElementById("customerPhoneError");



	var returnValue = true;
	var regEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;

	if (firstName === "" || firstName.length > 30) {
		firstNameError.style.visibility = "visible";
		returnValue = false;
	} else {
		firstNameError.style.visibility = "hidden";
	}


	if (lastName === "" || lastName.length > 30) {
		lastNameError.style.visibility = "visible";
		returnValue = false;
	} else {
		lastNameError.style.visibility = "hidden";
	}


	if (email === "" || !regEmail.test(email) || email.length > 50) {
		emailError.style.visibility = "visible";
		returnValue = false;
	} else {
		emailError.style.visibility = "hidden";
	}


	if (password.length < 6 || password.length > 30) {
		passwordError.style.visibility = "visible";
		returnValue = false;
	} else {
		passwordError.style.visibility = "hidden";
	}

	if (address === "" || address.length > 75) {
		addressError.style.visibility = "visible";
		returnValue = false;
	} else {
		addressError.style.visibility = "hidden";
	};

	if (city === "" || city.length > 40) {
		cityError.style.visibility = "visible";
		returnValue = false;
	} else {
		cityError.style.visibility = "hidden";
	};


	if (postcode.length < 5 || postcode.length > 10) {
		postcodeError.style.visibility = "visible";
		returnValue = false;
	} else {
		postcodeError.style.visibility = "hidden";
	}

	if (customerPhone.length < 9 || customerPhone.length > 15) {
		customerPhoneError.style.visibility = "visible";
		returnValue = false;
	} else {
		customerPhoneError.style.visibility = "hidden";
	}

	if (password != confirmpass) {
		returnValue = false;
		alert(`Password not match!`);
	}

	return returnValue;
};

function ValidatePassword() {
	var password = document.getElementById("password").value;
	var confirmpass = document.getElementById("confirmpass").value;
	if (password != confirmpass) {
		alert("Password does Not Match.");
		return false;
	}
	return true;
};

function validateNumber(event) {
	if (!/^[\d.]$/.test(event.key) && !['Backspace', 'ArrowLeft', 'ArrowRight', 'Tab'].includes(event.key)) {
		event.preventDefault();
	}
}




function validateItem() {
	var quantity = document.getElementById("quantity").value;
	var hotnessLevel = document.getElementById("hotnessLevel").value;
	var quantityNum = Number(quantity);

	var quantityError = document.getElementById("quantityError");
	var hotnessLevelError = document.getElementById("hotnessLevelError");


	var returnValue = true;

	if (quantity === "" || quantityNum < 1 || quantityNum > 50) {
		quantityError.style.visibility = "visible";
		returnValue = false;
	} else {
		quantityError.style.visibility = "hidden";
	}

	if (hotnessLevel === "") {
		hotnessLevelError.style.visibility = "visible";
		returnValue = false;
	} else {
		hotnessLevelError.style.visibility = "hidden";
	}

	return returnValue;
}