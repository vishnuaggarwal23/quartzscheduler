var ComponentsDateTimePickers = function () {

    var handleDatetimePicker = function () {

        if (!jQuery().datetimepicker) {
            return;
        }

        $('input.date-time-picker').datetimepicker({
            viewMode: 'years',
            stepping: 1,
            useCurrent: true,
            collapse: true,
            locale: moment.locale(),
            useStrict: true,
            calendarWeeks: true,
            showTodayButton: true,
            showClear: true,
            showClose: true
        });
    };

    return {
        init: function () {
            handleDatetimePicker();
        }
    };

}();

if (App.isAngularJsApp() === false) {
    jQuery(document).ready(function () {
        ComponentsDateTimePickers.init();
    });
}