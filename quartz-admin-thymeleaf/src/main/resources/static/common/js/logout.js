$(document).ready(function () {
    $(document).on('click', 'a#logout', function (e) {
        preventEvent(e);

        $.ajax({
            url: $(this).data('logoutUri'),
            contentType: APPLICATION_JSON,
            dataType: JSON_DATA_TYPE,
            complete: function (response) {
                let goForError = true;
                if (response && is2xxResponseCode(response.status) && response.responseJSON && response.responseJSON.path) {
                    if (compareVariableWithBoolean(response.responseJSON.logout, true)) {
                        goForError = false;
                        removeCookie(X_AUTH_TOKEN);
                        window.location.href = response.responseJSON.path.trim().toString();
                    }
                }
                if (goForError) {
                    showErrorMessage("Unable to logout the user.")
                }
            }
        })
    });
});