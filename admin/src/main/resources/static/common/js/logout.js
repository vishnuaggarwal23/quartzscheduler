$(document).ready(function () {
    $(document).on('click', 'a#logout', function (e) {
        preventEvent(e);

        $.ajax({
            url: $(this).data('logoutUri'),
            type: HTTP_POST,
            contentType: APPLICATION_JSON,
            dataType: JSON_DATA_TYPE,
            complete: function (response) {
                let goForError = true;
                if (response && is2xxResponseCode(response.status) && response.responseJSON && response.responseJSON.path) {
                    if (isObjectTrue(response.responseJSON.logout)) {
                        goForError = false;
                        removeCookie(X_AUTH_TOKEN);
                        removeCookie(CURRENT_LOGGED_IN_USER_ID);
                        deleteFromLocalStorage(CURRENT_LOGGED_IN_USER_STORAGE_KEY + getCookie(CURRENT_LOGGED_IN_USER_ID));
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