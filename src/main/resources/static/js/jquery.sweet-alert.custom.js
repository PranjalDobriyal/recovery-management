!function($) {
    "use strict";

    var SweetAlert = function() {};

    // Initialize SweetAlert examples
    SweetAlert.prototype.init = function() {

        // Custom Success Alert (Bootstrap styled)
        this.showSuccess = function(message) {
            Swal.fire({
                icon: 'success',
                title: 'Success',
                text: message || "Operation completed successfully!",
                showConfirmButton: false,
                timer: 3000, // Auto-close after 3 seconds
                customClass: {
                    popup: 'alert alert-success rounded-0', // Bootstrap success alert
                },
                toast: true, // Makes it a toast notification
                position: 'top-end', // Position the toast at top-end
                showClass: {
                    popup: 'swal2-fade swal2-show'
                },
                hideClass: {
                    popup: 'swal2-fade swal2-hide'
                },
                queue: true // Queue alerts to prevent overlap
            });
        };

        // Custom Error Alert (Bootstrap styled)
        this.showError = function(message) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: message || "Something went wrong!",
                showConfirmButton: false,
                timer: 3000, // Auto-close after 3 seconds
                customClass: {
                    popup: 'alert alert-danger rounded-0', // Bootstrap danger alert
                },
                toast: true, // Makes it a toast notification
                position: 'top-end', // Position the toast at top-end
                showClass: {
                    popup: 'swal2-fade swal2-show'
                },
                hideClass: {
                    popup: 'swal2-fade swal2-hide'
                },
                queue: true // Queue alerts to prevent overlap
            });
        };

    };

    // Initialize the custom alert system
    $.SweetAlert = new SweetAlert, $.SweetAlert.Constructor = SweetAlert;

}(window.jQuery),

// Initializing SweetAlert
function($) {
    "use strict";
    $.SweetAlert.init();
}(window.jQuery);