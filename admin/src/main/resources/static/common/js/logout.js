$(document).ready(function () {
    $(document).on('click', 'a#logout', function (e) {
        preventEvent(e);

        $.ajax({
            url: $(this).data('logoutUri'),
            contentType: APPLICATION_JSON,
            dataType: JSON_DATA_TYPE,
            complete: function (response) {
                if (response && response.responseJSON) {
                    if (response.responseJSON.logout === 'true' || response.responseJSON.logout === true) {
                        removeCookie('X-AUTH-TOKEN');
                        window.location.href = response.responseJSON.path.trim().toString();
                    } else {
                        showErrorMessage("Unable to logout the user.")
                    }
                }
            }
        })
    });
});