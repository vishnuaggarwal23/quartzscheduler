"use strict";

var Login = function () {
    var handleLogin = function () {
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
                    type: "post",
                    contentType: "application/json",
                    data: JSON.stringify({
                        username: $('input#username').val(),
                        password: $('input#password').val()
                    }),
                    complete: function (response) {
                        if (response && response.responseJSON) {
                            var data = $.parseJSON(response.responseJSON.data);
                            if (response.responseJSON.responseCode === 202) {
                                if (data.path) {
                                    // $('form.redirect-login-form').attr('action', data.path).submit();
                                    window.location.href = data.path;
                                }
                            } else {
                                alert(response.responseJSON.message);
                            }
                        }
                    }
                })
            }
        })
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