"use strict";

let Login = function () {
    let handleLogin = function () {
        $('form.login-form').validate({
            rules: {
                username: {
                    required: true
                },
                password: {
                    required: true
                }
            },
            message: {
                username: {
                    required: "Username is missing."
                },
                password: {
                    required: "Password is missing."
                }
            },
            submitHandler: function (form) {
                $.ajax({
                    url: $('form.login-form').data('userLoginUri'),
                    type: HTTP_POST,
                    contentType: APPLICATION_JSON,
                    async: false,
                    cache: false,
                    data: JSON.stringify({
                        username: $('input#username').val(),
                        password: $('input#password').val()
                    }),
                    complete: function (response) {
                        let goForError = true;
                        if (response && is2xxResponseCode(response.status) && response.responseJSON && response.responseJSON.path) {
                            window.location.href = response.responseJSON.path.trim().toString();
                            goForError = false;
                        }
                        if (goForError) {
                            showErrorMessage(response.responseJSON.error.message)
                        }
                    }
                })
            }
        });
        deleteFromLocalStorage(CURRENT_LOGGED_IN_USER_STORAGE_KEY + getCookie(CURRENT_LOGGED_IN_USER_ID))
    };

    return {
        init: function () {
            handleLogin();
        }
    };
}();

$(document).ready(function () {
    Login.init();
});