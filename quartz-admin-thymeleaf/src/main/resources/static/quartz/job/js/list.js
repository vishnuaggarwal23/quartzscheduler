"use strict";

$(document).ready(function () {
    $('table#listJobs').DataTable({
        processing: true,
        serverSide: false,
        filter: true,
        responsive: true,
        autoWidth: true,
        ajax: {
            url: $('input#listJobsUri').val().trim(),
            type: HTTP_GET
        },
        columns: [
            {
                data: "details.key"
            }, {
                data: "details.group"
            }, {
                data: "executorClass"
            }, {
                orderable: false,
                searchable: false,
                data: null,
                defaultContent: null
            }
        ],
        columnDefs: [
            {
                targets: [0],
                whiteSpace: "nowrap",
                render: function (data, type, row, meta) {
                    return data;
                }
            }, {
                targets: [1],
                whiteSpace: "nowrap",
                render: function (data, type, row, meta) {
                    return data.firstName.trim().toString() + " " + data.lastName.trim().toString() + " : " + data.id;
                }
            }, {
                targets: [2],
                whiteSpace: "nowrap",
                render: function (data, type, row, meta) {
                    return data;
                }
            }, {
                targets: [3],
                whiteSpace: "nowrap",
                render: function (data, type, row, meta) {
                    return "<div class=\"actions\">\n" +
                        "       <div class=\"btn-group pull-right\">\n" +
                        "           <button class=\"btn green btn-group btn-group-devided btn-outline dropdown-toggle\" data-toggle=\"dropdown\">\n" +
                        "               <i class=\"fa fa-angle-down\"></i>\n" +
                        "           </button>\n" +
                        "           <ul class=\"dropdown-menu pull-right\">\n" +
                        "               <li>\n" +
                        "                   <a href=\"javascript:;\"><i></i> View </a>\n" +
                        "               </li>\n" +
                        "               <li>\n" +
                        "                   <a href=\"javascript:;\"><i></i> Delete </a>\n" +
                        "               </li>\n" +
                        "               <li>\n" +
                        "                   <a href=\"javascript:;\"><i></i> Update </a>\n" +
                        "               </li>\n" +
                        "               <li>\n" +
                        "                   <a href=\"javascript:;\"><i></i> Create Trigger </a>\n" +
                        "               </li>\n" +
                        "               <li>\n" +
                        "                   <a href=\"javascript:;\"><i></i> List Triggers </a>\n" +
                        "               </li>\n" +
                        "           </ul>\n" +
                        "       </div>\n" +
                        "   </div>"
                }
            }
        ],
        fnRowCallback: function (row, data, displayIndex) {
            $(row).addClass("gradeX");
        }
    });
});