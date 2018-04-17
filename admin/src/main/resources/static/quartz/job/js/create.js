"use strict";

$.validator.addMethod('customTriggerStartNowRequired', function (value, element, options) {
    if ($(element).is(':checked')) {
        return true;
    }
    return !$('input#triggerStartTime').hasClass('hidden');
});

$.validator.addMethod('customRepeatForeverRequired', function (value, element, options) {
    if ($(element).is(':checked')) {
        return true;
    }
    return !$('th.repeat-count-column').hasClass('hidden') && !$('td.repeat-count-column').parent().hasClass('hidden');
});

$.validator.addMethod('customTriggerStartTimeRequired', function (value, element, options) {
    return !$('input#triggerStartNow').is(':checked') && $(element).val().length > 0 && !$(element).hasClass('hidden');
});

$.validator.addMethod('customTriggerRepeatCountRequired', function (value, element, options) {
    return !$('input#repeatForever').is(':checked') && $(element).val().length > 0 && !$('th.repeat-count-column').hasClass('hidden') && !$('td.repeat-count-column').hasClass('hidden');
});

$.validator.addMethod('uniqueJobKey', function (value, element, options) {
    var requestJson = {
        keyName: $(element).val(),
        groupName: $($(element).data('groupNameSelector')).val()
    };

    $.ajax({
        url: $(element).data('uniqueKeyValidation'),
        type: "post",
        async: false,
        data: JSON.stringify(requestJson),
        contentType: "application/json",
        complete: function (response) {
            if (response && response.responseText) {
                var responseJson = $.parseJSON(response.responseText);
                if (responseJson.data) {
                    return true;
                }
            }
        }
    });
    return false;
});

$(document).ready(function () {
    $('form#createApiJobForm input').keypress(function (e) {
        if (e.which === 13) {
            preventEvent(e);
            if ($('form#createApiJobForm').validate().form()) {
                $('.form#createApiJobForm').submit();
            }
            return false;
        }
    });

    $(document).on('click', 'button[name=addRequestHeaderRow]', function () {
        var $parentRow = $('button[name=addRequestHeaderRow]').closest('tr.request-header-row');
        var currentRowNumber = $parentRow.data('rowNumber');
        var nextRowNumber = currentRowNumber + 1;
        var nextHeaderKeyRowId = "requestHeaderKey_" + nextRowNumber;
        var nextHeaderValueRowId = "requestHeaderValue_" + nextRowNumber;
        var nextRemoveHeaderKeyValueRowId = "removeRequestHeaderRow_" + nextRowNumber;
        var newRowHtml = "<tr data-row-number='" + nextRowNumber + "' class=\"request-header-row\">\n" +
            "            <td>\n" +
            "                <input type=\"text\" id='" + nextHeaderKeyRowId + "'\n" +
            "                       name=\"requestHeaderKey\"\n" +
            "                       class=\"form-control request-header-key\"\n" +
            "                       placeholder=\"Request Header Key\"/>\n" +
            "            </td>\n" +
            "            <td>\n" +
            "                <input type=\"text\" id='" + nextHeaderValueRowId + "'\n" +
            "                       name=\"requestHeaderValue\"\n" +
            "                       class=\"form-control request-header-value\"\n" +
            "                       placeholder=\"Request Header Value\"/>\n" +
            "            </td>\n" +
            "            <td>\n" +
            "                <span>\n" +
            "                    <button type=\"button\" id=\"addRequestHeaderRow\" " +
            "                           data-previous-row-number='" + currentRowNumber + "'" +
            "                           name=\"addRequestHeaderRow\" " +
            "                           class=\"btn btn-sm green default\">Add</button>\n" +
            "                </span>\n" +
            "                <span>\n" +
            "                    <button type=\"button\" id='" + nextRemoveHeaderKeyValueRowId + "' " +
            "                           data-previous-row-number='" + currentRowNumber + "'" +
            "                           name=\"removeRequestHeaderRow\" " +
            "                           class=\"btn btn-sm red default\">Remove</button>\n" +
            "                </span>\n" +
            "            </td>\n" +
            "        </tr>";
        $('button[name=addRequestHeaderRow]').closest('tbody').append(newRowHtml);
        $(this).remove();
    });

    $(document).on('click', 'button[name=removeRequestHeaderRow]', function () {
        var $currentRow = $(this).closest('tr.request-header-row');
        var $previousRow = $currentRow.prev()[0];
        var $nextRow = $currentRow.next()[0];
        var deleteRow = false;

        var isCurrentRowLast = $($currentRow).is(':last-child');
        var isCurrentRowFirst = $($currentRow).is(':first-child');
        var isPreviousRowFirst = $previousRow !== undefined || $previousRow !== null ? $($previousRow).is(':first-child') : null;

        if (isCurrentRowFirst && !isCurrentRowLast && ($nextRow === null || $nextRow === undefined) && ($previousRow === null || $previousRow === undefined)) {
            deleteRow = false;
        }
        if (isCurrentRowLast) {
            if (($previousRow !== null || $previousRow !== undefined) && isPreviousRowFirst !== null) {
                var $lastTd = $($previousRow).find('td').eq(2);
                var addButtonHtml = "<span>" +
                    "                   <button type=\"button\" id=\"addRequestHeaderRow\"" +
                    "                       data-previous-row-number=\"\"" +
                    "                       name=\"addRequestHeaderRow\"" +
                    "                       class=\"btn btn-sm green default\">Add" +
                    "                   </button>" +
                    "               </span>";
                if (isPreviousRowFirst === true) {
                    $lastTd.append(addButtonHtml);
                    deleteRow = true;
                } else if (isPreviousRowFirst === false) {
                    $lastTd.prepend(addButtonHtml);
                    deleteRow = true;
                } else {
                    deleteRow = false;
                }
            } else {
                deleteRow = false;
            }
        }
        if (!isCurrentRowFirst && !isCurrentRowLast && ($nextRow !== null || $nextRow !== undefined) && ($previousRow !== null || $previousRow !== undefined)) {
            deleteRow = true
        }
        if (deleteRow === true) {
            $currentRow.remove();
        }
    });

    handleCreateUnScheduledJob();
    // handleCreateScheduledJob();
});

var toggleTriggerStartTimeBasedOnTriggerStartNow = function (triggerStartNowElement, triggerStartTimeRowElement) {
    if (triggerStartNowElement.checked) {
        $(triggerStartTimeRowElement).addClass('hidden');
    } else {
        $(triggerStartTimeRowElement).removeClass('hidden');
    }
    $(triggerStartTimeRowElement).find('input[type=text]').val('');
};

var toggleRepeatCountBasedOnRepeatForever = function (repeatForeverElement, repeatCountThElement, repeatCountTdElement) {
    if (repeatForeverElement.checked) {
        $(repeatCountThElement).addClass('hidden');
        $(repeatCountTdElement).addClass('hidden');
    } else {
        $(repeatCountThElement).removeClass('hidden');
        $(repeatCountTdElement).removeClass('hidden');
    }
    $(repeatCountTdElement).find('input[type=text]').val('');
};

var toggleTriggerFormVisibilityBasedOnJobScheduled = function (jobScheduledElement, triggerFormElement) {
    if (jobScheduledElement.checked) {
        $(triggerFormElement).removeClass('hidden');
    } else {
        $(triggerFormElement).addClass('hidden');
    }
};

var toggleSchedulerTypeVisibilityBasedOnType = function (schedulerTypeElement, simpleSchedulerTypeElement, cronSchedulerTypeElement) {
    if ($(schedulerTypeElement).val() === "SIMPLE") {
        $(simpleSchedulerTypeElement).removeClass('hidden');
        $(cronSchedulerTypeElement).addClass('hidden');
    }
    if ($(schedulerTypeElement).val() === "CRON") {
        $(simpleSchedulerTypeElement).addClass('hidden');
        $(cronSchedulerTypeElement).removeClass('hidden');
    }
    if ($(schedulerTypeElement).val() === "" || $(schedulerTypeElement).val() === null || $(schedulerTypeElement) === undefined) {
        $(simpleSchedulerTypeElement).addClass('hidden');
        $(cronSchedulerTypeElement).addClass('hidden');
    }
};

var changeRequestTypeBasedOnExecutorClass = function (executorClassElement, requestTypeElement) {
    $(requestTypeElement).val($($(executorClassElement).find(':selected')).data('associatedHttpMethod'));

};

var handleCreateScheduledJob = function () {
    handleCreateUnScheduledJob();
};

var handleCreateUnScheduledJob = function () {
    $('form#createApiJobForm').validate({
        submitHandler: function (form) {
            formSubmitHandler(form, false);
        }
    });
};

var formSubmitHandler = function (form, isScheduledJob) {
    var jobCO = {
        keyName: $('input#jobKeyName').val().trim(),
        description: $('textarea#jobDescription').val().trim(),
        durability: $('input#jobDurability').is(':checked'),
        recover: $('input#jobRecovery').is(':checked'),
        type: $('input#jobType').val(),
        scheduled: $('input#jobScheduled').is(':checked')
    };

    var apiJobDataCO = {
        executorClass: $('select#jobExecutorClass').val(),
        requestType: $('select#requestType').val(),
        requestUrl: $('input#requestUrl').val().trim()
    };

    if (isScheduledJob) {

    }

    var quartzDTO = {
        job: jobCO,
        apiJobData: apiJobDataCO
    };

    $.ajax({
        url: $(form).data('createNewJobUri'),
        type: "post",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(quartzDTO),
        complete: function (response) {
            if (response && response.responseText) {
                var responseJson = $.parseJSON(response.responseText);
                responseJson.responseCode === 200 ? showSuccessMessage(responseJson.message) : showErrorMessage(responseJson.message);
            }
        }
    });
};