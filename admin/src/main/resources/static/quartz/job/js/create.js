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

/*$.validator.addMethod('customAllowedValues', function (value, element, options) {
    if ($(element).hasClass('cron-seconds') || $(element).hasClass('cron-minutes')) {
        return (parseInt(value) >= 0 && parseInt(value) <= 59;
    }
    if ($(element).hasClass('cron-hours')) {
        return parseInt(value) >= 0 && parseInt(value) <= 23;
    }
    if ($(element).hasClass('cron-day-of-month')) {
        return parseInt(value) >= 1 && parseInt(value) <= 31;
    }
    if ($(element).hasClass('cron-month')) {
        var months = Array.apply(0, Array(12)).map(function (_, i) {
            return moment().month(i).format('MMM').toUpperCase()
        });
        return (parseInt(value) >= 1 && parseInt(value) <= 12) || $.inArray(value, months) !== -1;
    }
    if ($(element).hasClass('cron-day-of-week')) {
        var days = Array.apply(0, Array(7)).map(function (_, i) {
            return moment(i, 'e').format('ddd').toUpperCase();
        });
        return (parseInt(value) >= 1 && parseInt(value) <= 7) || $.inArray(value, days) !== -1;
    }
    if ($(element).hasClass('cron-years')) {
        return value.trim().length === 0 || parseInt(value) >= 1970 && parseInt(value) <= 2099;
    }
});

$.validator.addMethod('customAllowedSpecialCharacters', function (value, element, options) {
    if ($(element).hasClass('cron-seconds') || $(element).hasClass('cron-minutes') || $(element).hasClass('cron-hours') || $(element).hasClass('cron-month')) {
        return $.inArray(value, [',', '-', '*', '/']) !== -1
    }
    if ($(element).hasClass('cron-day-of-month')) {
        return $.inArray(value, [',', '-', '*', '/', '?', 'L', 'W']) !== -1
    }
    if ($(element).hasClass('cron-day-of-week')) {
        return $.inArray(value, [',', '-', '*', '/', '?', 'L', '#']) !== -1
    }
    if ($(element).hasClass('cron-years')) {
        return value.trim().length === 0 || $.inArray(value, [',', '-', '*', '/']) !== -1;
    }
});*/

$.validator.addMethod('customTriggerStartTimeRequired', function (value, element, options) {
    return !$('input#triggerStartNow').is(':checked') && $(element).val().trim().length > 0 && !$(element).hasClass('hidden');
});

$.validator.addMethod('customTriggerRepeatCountRequired', function (value, element, options) {
    return !$('input#repeatForever').is(':checked') && $(element).val().trim().length > 0 && !$('th.repeat-count-column').hasClass('hidden') && !$('td.repeat-count-column').hasClass('hidden');
});

$.validator.addMethod('customRequestHeadersRequired', function (value, element, options) {
    if ($(element).hasClass('request-header-key')) {
        if ($(element).val().trim().length === 0) {
            return $($(element).data('associatedHeaderValueId')).val().trim().length === 0;
        } else {
            if ($($(element).data('associatedHeaderValueId')).val().trim().length === 0) {
                $('form#createApiJobForm').validate().element($(element).data('associatedHeaderValueId'));
            }
            return true;
        }
    }
    if ($(element).hasClass('request-header-value')) {
        if ($(element).val().trim().length === 0) {
            return $($(element).data('associatedHeaderKeyId')).val().trim().length === 0;
        } else {
            if ($($(element).data('associatedHeaderKeyId')).val().trim().length === 0) {
                $('form#createApiJobForm').validate().element($(element).data('associatedHeaderKeyId'));
            }
            return true;
        }
    }
});

$.validator.addMethod('customUniqueKey', function (value, element, options) {
    var isValid = false;
    $.ajax({
        url: $(element).data('customUniqueKeyValidation'),
        type: "get",
        async: false,
        data: {
            keyName: $(element).val()
        },
        contentType: "application/json",
        dataType: "json",
        complete: function (response) {
            if (response && response.responseText) {
                var responseJson = $.parseJSON(response.responseText);
                if (responseJson.data) {
                    isValid = true;
                }
            }
        }
    });
    return isValid;
});

$(document).ready(function () {
    $('form#createApiJobForm input').keypress(function (e) {
        if (e.which === 13) {
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
        var nextHeaderKeyRowReference = "input#" + nextHeaderKeyRowId;
        var nextHeaderValueRowReference = "input#" + nextHeaderValueRowId;
        var nextRemoveHeaderKeyValueRowId = "removeRequestHeaderRow_" + nextRowNumber;
        var newRowHtml = "<tr data-row-number='" + nextRowNumber + "' class=\"request-header-row\">\n" +
            "            <td>\n" +
            "                <input type=\"text\" id='" + nextHeaderKeyRowId + "'\n" +
            "                       name=\"requestHeaderKey\"\n" +
            "                       class=\"form-control request-header-key\"\n" +
            "                       data-rule-customRequestHeadersRequired=\"true\"\n" +
            "                       data-msg-customRequestHeadersRequired=\"Request Header Key is missing.\"\n" +
            "                       data-associated-header-value-id='" + nextHeaderValueRowReference + "'\n" +
            "                       placeholder=\"Request Header Key\"/>\n" +
            "            </td>\n" +
            "            <td>\n" +
            "                <input type=\"text\" id='" + nextHeaderValueRowId + "'\n" +
            "                       name=\"requestHeaderValue\"\n" +
            "                       class=\"form-control request-header-value\"\n" +
            "                       data-rule-customRequestHeadersRequired=\"true\"\n" +
            "                       data-msg-customRequestHeadersRequired=\"Request Header Value is missing.\"\n" +
            "                       data-associated-header-key-id='" + nextHeaderKeyRowReference + "'\n" +
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

    handleCreateJob();
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

var handleCreateJob = function () {
    $('form#createApiJobForm').validate({
        submitHandler: function (form) {
            formSubmitHandler(form);
        }
    });
};

var formSubmitHandler = function (form) {
    trimText();

    var quartzDTO = {};

    var jobCO = {
        keyName: $('input#jobKeyName').val().trim(),
        description: $('textarea#jobDescription').val().trim(),
        durability: $('input#jobDurability').is(':checked'),
        recover: $('input#jobRecovery').is(':checked'),
        type: $('input#jobType').val(),
        scheduled: $('input#jobScheduled').is(':checked')
    };

    var requestHeaders = [];
    $.each($('tr.request-header-row'), function (index, value) {
        var $headerKey = $(value).find('input.request-header-key');
        var $headerValue = $(value).find('input.request-header-value');
        if ($headerKey.length > 0 && $headerValue.length > 0) {
            if ($($headerKey).val().trim().length > 0 && $($headerValue).val().trim().length > 0) {
                requestHeaders.push({
                    key: $($headerKey).val().trim(),
                    value: $($headerValue).val().trim()
                })
            }
        }
    });

    var apiJobDataCO = {
        executorClass: $('select#jobExecutorClass').find(':selected').val(),
        requestType: $('select#requestType').find(':selected').val(),
        requestUrl: $('input#requestUrl').val().trim()
    };

    if (requestHeaders.length > 0) {
        apiJobDataCO["requestHeaders"] = requestHeaders;
    }

    if ($('input#jobScheduled').is(':checked')) {
        var triggerCO = {
            keyName: $('input#triggerKeyName').val(),
            triggerDescription: $('input#triggerDescription').val()
        };

        if ($('input#triggerEndTime').val().trim().length > 0) {
            triggerCO["endTime"] = $('input#triggerEndTime').data("DateTimePicker").date();
        }

        if ($('input#triggerStartNow').is(':checked')) {
            triggerCO["startNow"] = true;
        } else {
            triggerCO["startTime"] = $('#input#triggerStartTime').data("DateTimePicker").date()
        }

        quartzDTO["scheduleType"] = $('select#scheduleType').find(':selected').val();
        if ($('select#scheduleType').find(':selected').val() === "SIMPLE") {
            var repeatInterval = {
                repeatValue: $('input#repeatValue').val()
            };

            if ($('input#repeatForever').is(':checked')) {
                repeatInterval["repeatForever"] = true;
            } else {
                repeatInterval["repeatCount"] = $('input#repeatCount').val()
            }

            apiJobDataCO["simpleJobScheduler"] = {
                trigger: triggerCO,
                repeatType: $('select#repeatType').find(':selected').val(),
                repeatInterval: repeatInterval
            };
        }
        if ($('select#scheduleType').find(':selected').val() === "CRON") {
            apiJobDataCO["cronJobScheduler"] = {
                trigger: triggerCO,
                second: $('input#cronExpressionSecond').val(),
                minute: $('input#cronExpressionMinute').val(),
                hour: $('input#cronExpressionHour').val(),
                dayOfMonth: $('input#cronExpressionDayOfMonth').val(),
                month: $('input#cronExpressionMonth').val(),
                dayOfWeek: $('input#cronExpressionDayOfWeek').val(),
                year: $('input#cronExpressionYear').val()
            }
        }
    }

    quartzDTO["job"] = jobCO;
    quartzDTO["apiJobData"] = apiJobDataCO;

    $.ajax({
        url: $(form).data('createNewJobUri'),
        type: "post",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(quartzDTO),
        complete: function (response) {
            if (response && response.responseText) {
                var responseJson = $.parseJSON(response.responseText);
                responseJson.responseCode in [200, 201, 202, 203, 204] ? showSuccessMessage(responseJson.message) : showErrorMessage(responseJson.message);
            }
        }
    });
};