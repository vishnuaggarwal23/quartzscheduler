"use strict";

toastr.options = {
    "closeButton": true,
    "debug": false,
    "newestOnTop": true,
    "progressBar": true,
    "positionClass": "toast-top-full-width",
    "preventDuplicates": false,
    "onclick": null,
    "showDuration": "500",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
};

function log() {
    if (console) {
        console.log.apply(console, arguments);
    }
}

function removeCookie(cookieName) {
    Cookies.remove(cookieName, {path: '/', expires: 0});
}

function showSuccessMessage(message, timeout) {
    message && toastr.success(message, "", {timeout: timeout || 3500});
    changeToasterPositionFromTop($('.toast-top-full-width'));
}

function showErrorMessage(message, timeout) {
    message && toastr.error(message, "", {timeOut: timeout || 6000});
    changeToasterPositionFromTop($('.toast-top-full-width'));
}

function showWarningMessage(message, timeout) {
    message && toastr.warning(message, "", {timeOut: timeout || 3500});
    changeToasterPositionFromTop($('.toast-top-full-width'));
}

function showInfoMessage(message, timeout) {
    message && toastr.info(message, "", {timeOut: timeout || 3500});
    changeToasterPositionFromTop($('.toast-top-full-width'));
}

function showStatus(status, message) {
    status ? showSuccessMessage(message) : showErrorMessage(message);
}

function preventEvent(event) {
    event.preventDefault();
}

function reloadDataTable(table) {
    table.ajax.reload();
}

function compareStringWithBoolean(string) {
    return (string === 'true')
}

function trimText() {
    $("input[type=text],textarea").each(function (index, object) {
        var text = $.trim($(object).val());
        $(object).val(text.trim())
    });
}

function changeToasterPositionFromTop(toastrElement) {
    if (toastrElement.length > 0) {
        toastrElement.css('top', '5%');
    }
}

function bootBoxConfirmDialog(message, callback) {
    bootbox.confirm({
        animate: true,
        backdrop: true,
        closeButton: false,
        onEscape: false,
        buttons: {
            confirm: {
                label: 'Yes',
                className: 'btn-success'
            },
            cancel: {
                label: 'No',
                className: 'btn-danger'
            }
        },
        message: message,
        callback: callback
    });
}

function showFlashMessagesOnPageLoad() {
    $(['info', 'success', 'error', 'warning']).each(function (index, value) {
        var $element = $('.message-text-' + value + ':first', '.messageContainer');
        var message = $.trim($element.text());
        if ($element.length > 0 && message.length > 0) {
            switch (value) {
                case 'info':
                    showInfoMessage(message);
                    break;
                case 'success':
                    showSuccessMessage(message);
                    break;
                case 'error':
                    showErrorMessage(message);
                    break;
                case 'warning':
                    showWarningMessage(message);
                    break;
            }
            $element.empty();
        }
    });
}

$(document).ready(function () {
    // showFlashMessagesOnPageLoad();

    $(document).ajaxStart(function () {
        App.blockUI({
            message: "Please Wait!",
            boxed: true
        })
    });

    $(document).ajaxStop(function () {
        App.unblockUI();
    });
});