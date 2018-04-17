$(document).ready(function () {
    $(document).on('click', 'a#logout', function (e) {
        preventEvent(e);

        $.ajax({
            url: $(this).data('logoutUri'),
            contentType: "application/json",
            dataType: "json",
            complete: function (response) {
                if (response && response.responseText) {
                    var responseJson = $.parseJSON(response.responseText);
                    if (responseJson.status !== 401) {
                        removeCookie('X-AUTH-TOKEN');
                    }
                    window.location.href = responseJson.path;
                }
            }
        })
    });
});