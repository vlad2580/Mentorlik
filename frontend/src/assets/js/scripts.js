// Global scripts for the application

// DOM Ready handler
document.addEventListener('DOMContentLoaded', function() {
    console.log('Application initialized');
    
    // Initialize toggles for donation method selection if present
    initDonationMethodToggle();
});

// Initialize donation method selection
function initDonationMethodToggle() {
    const methodRadios = document.querySelectorAll('input[name="donation-method"]');
    const donationForm = document.querySelector('.donation-form');
    
    if (!methodRadios.length || !donationForm) return;
    
    methodRadios.forEach(radio => {
        radio.addEventListener('change', function() {
            donationForm.classList.remove('hidden');
        });
    });
}

// Generic form validation
function validateForm(formElement) {
    let isValid = true;
    const requiredFields = formElement.querySelectorAll('[required]');
    
    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            field.classList.add('invalid');
            isValid = false;
        } else {
            field.classList.remove('invalid');
        }
    });
    
    return isValid;
} 