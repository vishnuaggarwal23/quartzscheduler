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
    let isValid = false;
    $.ajax({
        url: $(element).data('customUniqueKeyValidationUri'),
        type: HTTP_GET,
        async: false,
        data: {
            keyName: $(element).val()
        },
        contentType: APPLICATION_JSON,
        dataType: JSON_DATA_TYPE,
        complete: function (response) {
            isValid = response && is2xxResponseCode(response.status) && response.responseJSON ? !response.responseJSON.valid : false;
        }
    });
    return isValid;
});

let formSubmitHandler = function (form) {
    trimText();

    let createJobUri = $(form).data('createJobUri');
    let requestHeaders = [];
    $.each($('tr.request-header-row'), function (index, value) {
        let $headerKey = $(value).find('input.request-header-key');
        let $headerValue = $(value).find('input.request-header-value');
        if ($headerKey.length > 0 && $headerValue.length > 0) {
            if ($($headerKey).val().trim().length > 0 && $($headerValue).val().trim().length > 0) {
                requestHeaders.push({
                    key: $($headerKey).val().trim(),
                    value: $($headerValue).val().trim()
                })
            }
        }
    });

    let apiJobDataCO = {
        executorClass: $('select#jobExecutorClass').find(':selected').val(),
        requestType: $('select#requestType').find(':selected').val(),
        requestUrl: $('input#requestUrl').val().trim()
    };

    if (requestHeaders.length > 0) {
        apiJobDataCO["requestHeaders"] = requestHeaders;
    }

    if ($('input#jobScheduled').is(':checked')) {
        let triggerCO = {
            details: {
                key: $('input#triggerKeyName').val().trim(),
                description: $('textarea#triggerDescription').val().trim()
            }
        };

        if ($('input#triggerEndTime').val().trim().length > 0) {
            triggerCO["endTime"] = $('input#triggerEndTime').data("DateTimePicker").date();
        }

        if ($('input#triggerStartNow').is(':checked')) {
            triggerCO["startNow"] = true;
        } else {
            triggerCO["startNow"] = false;
            triggerCO["startTime"] = $('#input#triggerStartTime').data("DateTimePicker").date()
        }

        if ($('select#scheduleType').find(':selected').val() === "SIMPLE") {
            let repeatInterval = {
                repeatValue: $('input#repeatValue').val()
            };

            if ($('input#repeatForever').is(':checked')) {
                repeatInterval["repeatForever"] = true;
            } else {
                repeatInterval["repeatForever"] = false;
                repeatInterval["repeatCount"] = $('input#repeatCount').val()
            }

            apiJobDataCO["simpleJobScheduler"] = {
                trigger: triggerCO,
                repeatType: $('select#repeatType').find(':selected').val(),
                repeatInterval: repeatInterval
            };
            createJobUri = $(form).data('createNewSimpleApiJobUri');
        } else if ($('select#scheduleType').find(':selected').val() === "CRON") {
            apiJobDataCO["cronJobScheduler"] = {
                trigger: triggerCO,
                second: $('input#cronExpressionSecond').val(),
                minute: $('input#cronExpressionMinute').val(),
                hour: $('input#cronExpressionHour').val(),
                dayOfMonth: $('input#cronExpressionDayOfMonth').val(),
                month: $('input#cronExpressionMonth').val(),
                dayOfWeek: $('input#cronExpressionDayOfWeek').val(),
                year: $('input#cronExpressionYear').val()
            };
            createJobUri = $(form).data('createNewCronApiJobUri');
        }
    }

    $.ajax({
        url: createJobUri,
        type: HTTP_POST,
        contentType: APPLICATION_JSON,
        dataType: JSON_DATA_TYPE,
        data: JSON.stringify({
            job: {
                details: {
                    key: $('input#jobKeyName').val().trim(),
                    description: $('textarea#jobDescription').val().trim()
                },
                durability: $('input#jobDurability').is(':checked'),
                recover: $('input#jobRecovery').is(':checked'),
                type: $('input#jobType').val(),
                scheduled: $('input#jobScheduled').is(':checked')
            },
            scheduleType: $('select#scheduleType').find(':selected').val() ? $('select#scheduleType').find(':selected').val() : null,
            apiJobData: apiJobDataCO
        }),
        complete: function (response) {
            let goForError = true;
            if (response && is2xxResponseCode(response.status) && response.responseJSON) {
                if ($('input#jobScheduled').is(':checked')) {
                    showSuccessMessage("Job is successfully created and scheduled at " + response.responseJSON.jobDetails.jobScheduledDate);
                } else {
                    showSuccessMessage("Job is successfully created");
                }
                goForError = false;
            }
            if (goForError) {
                showErrorMessage("Unable to create the job" + response.responseJSON.error.message);
            }
        }
    });
};

$(document).ready(function () {
    let userJson = JSON.parse($('input#currentLoggedInUserJson').val()).user;

    $('input#jobGroupName.job-group-name').val(userJson.firstName.trim().toString() + " " + userJson.lastName.trim().toString() + " : " + userJson.id);
    $('input#triggerGroupName').val(userJson.firstName.trim().toString() + " " + userJson.lastName.trim().toString() + " : " + userJson.id);

    $(document).on('change', 'select#jobType.job-type', function () {
        $(this).closest('body').find('div#job-type-fragment').load($(this).find(":selected").data('getFragmentUri'));
    });

    $(document).on('click', 'input#jobScheduled.job-scheduled', function () {
        $(this).closest('body').find('div#trigger-form-fragment').load($(this).data(this.checked ? 'triggerNewFormFragmentUri' : 'triggerNoneFormFragmentUri'));
    });

    $(document).on('click', 'button[name=addRequestHeaderRow].api-job-type', function () {
        let $parentRow = $(this).closest('tr.request-header-row.api-job-type');
        let currentRowNumber = $parentRow.data('rowNumber');
        let nextRowNumber = currentRowNumber + 1;
        let nextHeaderKeyRowId = "requestHeaderKey_" + nextRowNumber;
        let nextHeaderValueRowId = "requestHeaderValue_" + nextRowNumber;
        let nextHeaderKeyRowReference = "input#" + nextHeaderKeyRowId;
        let nextHeaderValueRowReference = "input#" + nextHeaderValueRowId;
        let nextRemoveHeaderKeyValueRowId = "removeRequestHeaderRow_" + nextRowNumber;
        let newRowHtml = "<tr data-row-number='" + nextRowNumber + "' class=\"request-header-row api-job-type\">\n" +
            "            <td>\n" +
            "                <input type=\"text\" id='" + nextHeaderKeyRowId + "'\n" +
            "                       name=\"requestHeaderKey\"\n" +
            "                       class=\"form-control request-header-key api-job-type\"\n" +
            "                       data-rule-customRequestHeadersRequired=\"true\"\n" +
            "                       data-msg-customRequestHeadersRequired=\"Request Header Key is missing.\"\n" +
            "                       data-associated-header-value-id='" + nextHeaderValueRowReference + "'\n" +
            "                       placeholder=\"Request Header Key\"/>\n" +
            "            </td>\n" +
            "            <td>\n" +
            "                <input type=\"text\" id='" + nextHeaderValueRowId + "'\n" +
            "                       name=\"requestHeaderValue\"\n" +
            "                       class=\"form-control request-header-value api-job-type\"\n" +
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
            "                           class=\"btn btn-sm green default api-job-type\">Add</button>\n" +
            "                </span>\n" +
            "                <span>\n" +
            "                    <button type=\"button\" id='" + nextRemoveHeaderKeyValueRowId + "' " +
            "                           data-previous-row-number='" + currentRowNumber + "'" +
            "                           name=\"removeRequestHeaderRow\" " +
            "                           class=\"btn btn-sm red default api-job-type\">Remove</button>\n" +
            "                </span>\n" +
            "            </td>\n" +
            "        </tr>";
        $(this).closest('tbody').append(newRowHtml);
        $(this).remove();
    });

    $(document).on('click', 'button[name=removeRequestHeaderRow].api-job-type', function () {
        let $currentRow = $(this).closest('tr.request-header-row.api-job-type');
        let $previousRow = $currentRow.prev()[0];
        let $nextRow = $currentRow.next()[0];
        let deleteRow = false;

        let isCurrentRowLast = $($currentRow).is(':last-child');
        let isCurrentRowFirst = $($currentRow).is(':first-child');
        let isPreviousRowFirst = $previousRow !== undefined || $previousRow !== null ? $($previousRow).is(':first-child') : null;

        if (isCurrentRowFirst && !isCurrentRowLast && ($nextRow === null || $nextRow === undefined) && ($previousRow === null || $previousRow === undefined)) {
            deleteRow = false;
        }
        if (isCurrentRowLast) {
            if (($previousRow !== null || $previousRow !== undefined) && isPreviousRowFirst !== null) {
                let $lastTd = $($previousRow).find('td').eq(2);
                let addButtonHtml = "<span>" +
                    "                   <button type=\"button\" id=\"addRequestHeaderRow\"" +
                    "                       data-previous-row-number=\"\"" +
                    "                       name=\"addRequestHeaderRow\"" +
                    "                       class=\"btn btn-sm green default api-job-type\">Add" +
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

    $(document).on('keypress', 'form#create-job-form', function (e) {
        if (e.which === 13) {
            if ($(this).validate().form()) {
                $(this).submit();
            }
            return false;
        }
    });

    $('form#create-job-form').validate({
        submitHandler: function (form) {
            // formSubmitHandler(form);
            let createJobUri = $(form).data('createJobUri');
            let details = {
                key: $(form).find('input[type=text]#jobKeyName.job-key-name').val().toString(),
                description: $(form).find('textarea#jobDescription.job-description').val().toString()
            };
            let durability = $(form).find('input[type=checkbox]#jobDurability.job-durability').is(':checked');
            let recover = $(form).find('input[type=checkbox]#jobRecovery.job-recovery').is(':checked');
            let replace = false;
            let type = $(form).find('select#jobType.job-type').find(":selected").val().toString();
            let scheduled = $(form).find('input[type=checkbox]#jobScheduled.job-scheduled').is(':checked');
            let executorClass = $(form).find('select#jobExecutorClass.job-executor-class').find(':selected').val().toString();

            $.ajax({
                url: createJobUri,
                type: HTTP_POST,
                contentType: APPLICATION_JSON,
                dataType: JSON_DATA_TYPE,
                data: JSON.stringify({
                    job: {
                        details: details,
                        durability: durability,
                        recover: recover,
                        replace: replace,
                        type: type,
                        scheduled: scheduled,
                        executorClass: executorClass
                    }
                }),
                complete: function (response) {
                    let goForError = true;
                    if (response && is2xxResponseCode(response.status) && response.responseJSON) {
                        showSuccessMessage(scheduled ? "Job is successfully created and scheduled at " + response.responseJSON.jobDetails.jobScheduledDate : "Job is successfully created");
                        goForError = false;
                    }
                    if (goForError) {
                        showErrorMessage("Unable to create the job" + response.responseJSON.error.message);
                    }
                }
            });
        }
    });
});

const toggleTriggerStartTimeBasedOnTriggerStartNow = function (triggerStartNowElement, triggerStartTimeRowElement) {
    if (triggerStartNowElement.checked) {
        $(triggerStartTimeRowElement).addClass('hidden');
    } else {
        $(triggerStartTimeRowElement).removeClass('hidden');
    }
    $(triggerStartTimeRowElement).find('input[type=text]').val('');
};

const toggleRepeatCountBasedOnRepeatForever = function (repeatForeverElement, repeatCountThElement, repeatCountTdElement) {
    if (repeatForeverElement.checked) {
        $(repeatCountThElement).addClass('hidden');
        $(repeatCountTdElement).addClass('hidden');
    } else {
        $(repeatCountThElement).removeClass('hidden');
        $(repeatCountTdElement).removeClass('hidden');
    }
    $(repeatCountTdElement).find('input[type=text]').val('');
};

const toggleTriggerFormVisibilityBasedOnJobScheduled = function (jobScheduledElement, triggerFormElement) {
    if (jobScheduledElement.checked) {
        $(triggerFormElement).removeClass('hidden');
    } else {
        $(triggerFormElement).addClass('hidden');
    }
};

const toggleSchedulerTypeVisibilityBasedOnType = function (schedulerTypeElement, simpleSchedulerTypeElement, cronSchedulerTypeElement) {
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

const changeRequestTypeBasedOnExecutorClass = function (executorClassElement, requestTypeElement) {
    $(requestTypeElement).val($($(executorClassElement).find(':selected')).data('associatedHttpMethod'));
};