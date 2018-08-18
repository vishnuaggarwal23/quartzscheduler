$(document).ready(function () {
    $(document).on('click', 'a#logout', function (e) {
        preventEvent(e);

        $.ajax({
            url: $(this).data('logoutUri'),
            contentType: "application/json",
            dataType: "json",
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