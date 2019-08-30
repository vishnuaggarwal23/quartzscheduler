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
const X_AUTH_TOKEN = "X-AUTH-TOKEN";
const CURRENT_LOGGED_IN_USER_ID = "CURRENT_LOGGED_IN_USER_ID";
const CURRENT_LOGGED_IN_USER_STORAGE_KEY = "currentLoggedInUser_";

toastr.options = {
    closeButton: true,
    debug: false,
    newestOnTop: true,
    progressBar: true,
    positionClass: "toast-top-full-width",
    preventDuplicates: false,
    onclick: null,
    showDuration: "500",
    hideDuration: "1000",
    timeOut: "5000",
    extendedTimeOut: "1000",
    showEasing: "swing",
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut"
};

let log = function () {
    if (console) {
        console.log.apply(console, arguments);
    }
};

let removeCookie = function (cookieName) {
    Cookies.remove(cookieName, {path: '/', expires: 0});
};

let getCookie = function (cookieName) {
    return Cookies.get(cookieName);
};

let showSuccessMessage = function (message, timeout) {
    message && toastr.success(message, "", {timeout: timeout || 3500});
    changeToasterPositionFromTop($('.toast-top-full-width'));
};

let showErrorMessage = function (message, timeout) {
    message && toastr.error(message, "", {timeOut: timeout || 6000});
    changeToasterPositionFromTop($('.toast-top-full-width'));
};

let showWarningMessage = function (message, timeout) {
    message && toastr.warning(message, "", {timeOut: timeout || 3500});
    changeToasterPositionFromTop($('.toast-top-full-width'));
};

let showInfoMessage = function (message, timeout) {
    message && toastr.info(message, "", {timeOut: timeout || 3500});
    changeToasterPositionFromTop($('.toast-top-full-width'));
};

let showStatus = function (status, message) {
    status ? showSuccessMessage(message) : showErrorMessage(message);
};

let preventEvent = function (event) {
    event.preventDefault();
};

let reloadDataTable = function (table) {
    table.ajax.reload();
};

let isObjectTrue = function (object) {
    return object === true || object === 'true' || object === "true";
};

let isObjectFalse = function (object) {
    return object === false || object === 'false' || object === "false";
};

let trimText = function () {
    $("input[type=text],textarea").each(function (index, object) {
        $(object).val($.trim($(object).val()).trim())
    });
};

let changeToasterPositionFromTop = function (toastrElement) {
    if (toastrElement.length > 0) {
        toastrElement.css('top', '5%');
    }
};

let bootBoxConfirmDialog = function (message, callback) {
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
};

let showFlashMessagesOnPageLoad = function () {
    $([$('input[type=hidden]#info_logType').val().trim().toString(), $('input[type=hidden]#success_logType').val().trim().toString(), $('input[type=hidden]#error_logType').val().trim().toString(), $('input[type=hidden]#warning_logType').val().trim().toString()]).each(function (index, value) {
        let $element = $('.message-text-' + value + ':first', '.messageContainer');
        let message = $.trim($element.text());
        if ($element.length > 0 && message.length > 0) {
            if (value === $('input[type=hidden]#info_logType').val().trim().toString()) {
                showInfoMessage(message);
            } else if (value === $('input[type=hidden]#success_logType').val().trim().toString()) {
                showSuccessMessage(message);
            } else if (value === $('input[type=hidden]#error_logType').val().trim().toString()) {
                showErrorMessage(message);
            } else if (value === $('input[type=hidden]#warning_logType').val().trim().toString()) {
                showWarningMessage(message);
            }
            $element.empty();
        }
    });
};

let getResponseCodeValue = function (responseCode) {
    return parseInt(parseInt(responseCode) / 100);
};

let is2xxResponseCode = function (responseCode) {
    return HTTP_RESPONSE_CODE_ENUM.SUCCESSFUL === getResponseCodeValue(responseCode);
};

let createGroupNameFieldText = function (groupJson) {
    return groupJson.firstName.trim().toString() + " " + groupJson.lastName.trim().toString() + " : " + groupJson.id;
};

let trimValue = function(value) {
    return value.trim().toString();
};

let saveInLocalStorage = function(key, value) {
    if(localStorage) {
        localStorage.setItem(key, value);
    }
};

let getFromLocalStorage = function(key) {
    return localStorage ? localStorage.getItem(key) : null;
};

let deleteFromLocalStorage = function(key) {
    if(localStorage) {
        localStorage.removeItem(key);
    }
};

let deleteAllFromLocalStorage = function() {
    if(localStorage) {
        localStorage.clear();
    }
};

$(document).ready(function () {
    // showFlashMessagesOnPageLoad();
    let $inputIsUserLoggedIn = $('input[type=hidden]#isUserLoggedIn');
    let continueToApiCall = false;
    if(isObjectTrue(trimValue($inputIsUserLoggedIn.val()))) {
        let currentLoggedInUserId = getCookie(CURRENT_LOGGED_IN_USER_ID);
        if(currentLoggedInUserId === undefined || currentLoggedInUserId === null || currentLoggedInUserId.length === 0) {
            continueToApiCall = true;
        } else {
            let loggedInUserJsonInLocalStorage = getFromLocalStorage(CURRENT_LOGGED_IN_USER_STORAGE_KEY + currentLoggedInUserId);
            if(loggedInUserJsonInLocalStorage === undefined || loggedInUserJsonInLocalStorage === null || loggedInUserJsonInLocalStorage.length === 0) {
                continueToApiCall = true;
            } else {
                $('input[type=hidden]#currentLoggedInUserJson').val(JSON.stringify(JSON.parse(loggedInUserJsonInLocalStorage)));
            }
        }
    }
    if(continueToApiCall) {
        $.ajax({
            url: trimValue($('input[type=hidden][name=currentLoggedInUserApi]').val()),
            contentType: APPLICATION_JSON,
            dataType: JSON_DATA_TYPE,
            cache: true,
            type: HTTP_GET,
            async: false,
            complete: function (response) {
                if (response && response.responseJSON && is2xxResponseCode(response.status)) {
                    let userId = response.responseJSON.user.id;
                    saveInLocalStorage(CURRENT_LOGGED_IN_USER_STORAGE_KEY + userId.toString(), response.responseText);
                    $('input#currentLoggedInUserJson').val(response.responseText);
                }
            }
        })
    }

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