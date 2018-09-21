"use strict";

const HTTP_RESPONSE_CODE_ENUM = Object.freeze({
    INFORMATIONAL: 1,
    SUCCESSFUL: 2,
    REDIRECTION: 3,
    CLIENT_ERROR: 4,
    SERVER_ERROR: 5
});

const APPLICATION_JSON = "application/json";
const JSON_DATA_TYPE = "json";
const HTTP_GET = "GET";
const HTTP_POST = "POST";

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

function getCookie(cookieName) {
    return Cookies.get(cookieName);
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

function compareStringWithBoolean(string, boolean) {
    return (string === boolean ? 'true' : 'false') || (string === boolean ? "true" : "false") || (string === boolean)
}

function trimText() {
    $("input[type=text],textarea").each(function (index, object) {
        $(object).val($.trim($(object).val()).trim())
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
        let $element = $('.message-text-' + value + ':first', '.messageContainer');
        let message = $.trim($element.text());
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

function getResponseCodeValue(responseCode) {
    return parseInt(parseInt(responseCode) / 100);
}

function is2xxResponseCode(responseCode) {
    return HTTP_RESPONSE_CODE_ENUM.SUCCESSFUL === getResponseCodeValue(responseCode);
}

function createGroupNameFieldText(groupJson) {
    return groupJson.firstName.trim().toString() + " " + groupJson.lastName.trim().toString() + " : " + groupJson.id;
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