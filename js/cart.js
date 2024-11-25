// Razorpay Options for Payment Integration
const razorpayOptions = {
    key: "CYQyLn45gM8JHIogWDMO0By6", // Razorpay Key ID
    amount: 50000, // Amount in paisa (50000 = 500 INR)
    currency: "INR",
    name: "Your Company Name", // Replace with your company's name
    description: "Purchase Description", // Brief description
    image: "https://yourdomain.com/logo.png", // Optional company logo
    handler: function (response) {
        alert("Payment Successful! Payment ID: " + response.razorpay_payment_id);
        // Send the payment ID to your server if needed for further processing
    },
    prefill: {
        name: "Customer Name", // Optional customer name
        email: "customer@example.com", // Optional customer email
        contact: "9876543210" // Optional customer contact
    },
    theme: {
        color: "#3399cc" // Theme color
    }
};

// Load Cart Items from LocalStorage and Display Them
function loadCartItems() {
    const savedCartItems = JSON.parse(localStorage.getItem("cartItems")) || [];
    const cartItemsContainer = document.getElementById("cart-items-container");
    let totalPrice = 0; // Initialize total price

    cartItemsContainer.innerHTML = ''; // Clear previous items

    savedCartItems.forEach((item, index) => {
        totalPrice += item.price; // Add item price to total

        const cartItem = document.createElement("div");
        cartItem.classList.add("cart-item");

        cartItem.innerHTML = `
            <div class="cart-item-content d-flex">
                <img src="${item.image}" alt="${item.name}" class="cart-item-image">
                <div class="cart-item-details">
                    <p class="product-name">${item.name}</p>
                    <p class="product-description">${item.description}</p>
                    <p class="product-price">$${item.price.toFixed(2)}</p>
                </div>
            </div>
            <button class="btn btn-danger remove-item" onclick="removeItem(${index})">Remove</button>
        `;

        cartItemsContainer.appendChild(cartItem);
    });

    // Update total price
    document.querySelector(".total-price").textContent = `Total: $${totalPrice.toFixed(2)}`;
}

// Remove an Item from the Cart
function removeItem(index) {
    const savedCartItems = JSON.parse(localStorage.getItem("cartItems")) || [];
    savedCartItems.splice(index, 1); // Remove the item at the specified index
    localStorage.setItem("cartItems", JSON.stringify(savedCartItems)); // Update LocalStorage
    loadCartItems(); // Reload the cart items to reflect the removal
}

// Attach Razorpay Payment Handler
document.getElementById("proceedToPayButton").onclick = function () {
    const rzp = new Razorpay(razorpayOptions);
    rzp.open();
};

// Toggle Booking Section
document.getElementById("checkoutButton").addEventListener("click", function () {
    // Hide the cart section
    document.querySelector(".container.mt-5").style.display = "none";

    // Show the booking section
    document.getElementById("booking1").style.display = "block";
});

// Initial Cart Loading
if (window.location.pathname.includes("book.php")) {
    loadCartItems();
}

// Subscribe to Newsletter
document.getElementById("newsletter_submit").addEventListener("click", function (e) {
    e.preventDefault(); // Prevent form submission
    const email = document.getElementById("newsletter_email").value;
    if (email) {
        alert(`Subscribed successfully with email: ${email}`);
    } else {
        alert("Please enter a valid email.");
    }
});
