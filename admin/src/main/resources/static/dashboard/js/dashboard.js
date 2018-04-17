var Dashboard = function () {
    return {
        init: function () {
        }
    };

}();

if (App.isAngularJsApp() === false) {
    $(document).ready(function () {
        Dashboard.init();
    });
}