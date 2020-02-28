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
    return !$('input#triggerStartNow').is(':checked') && $(element).val().trim().length > 0 && !$(element).hasClass('hidden');
});

$.validator.addMethod('customTriggerRepeatCountRequired', function (value, element, options) {
    return !$('input#repeatForever').is(':checked') && $(element).val().trim().length > 0 && !$('th.repeat-count-column').hasClass('hidden') && !$('td.repeat-count-column').hasClass('hidden');
});

$.validator.addMethod('customUniqueKey', function (value, element, options) {
    let isValid = false;
    $.ajax({
        url: $(element).data('customUniqueKeyValidation'),
        type: HTTP_GET,
        async: false,
        data: {
            keyName: $(element).val()
        },
        contentType: APPLICATION_JSON,
        dataType: JSON_DATA_TYPE,
        complete: function (response) {
            if (response && is2xxResponseCode(response.status) && response.responseJSON) {
                isValid = response.responseJSON.valid;
            }
        }
    });
    return isValid;
});

let formSubmitHandler = function (form) {
    trimText();

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

    let repeatInterval = {
        repeatValue: $('input#repeatValue').val()
    };
    if ($('input#repeatForever').is(':checked')) {
        repeatInterval["repeatForever"] = true;
    } else {
        repeatInterval["repeatForever"] = false;
        repeatInterval["repeatCount"] = $('input#repeatCount').val()
    }

    $.ajax({
        url: $(form).data('createNewSimpleTriggerUri').trim(),
        type: HTTP_POST,
        contentType: APPLICATION_JSON,
        dataType: JSON_DATA_TYPE,
        data: JSON.stringify({
            job: {
                details: {
                    key: $('input#jobKeyName').val().trim()
                }
            },
            scheduleType: $('input#scheduleType').val().trim(),
            apiJobData: {
                simpleJobScheduler: {
                    trigger: triggerCO,
                    repeatType: $('select#repeatType').find(':selected').val(),
                    repeatInterval: repeatInterval
                }
            }
        }),
        complete: function (response) {
            let goForError = true;
            if (response) {
                if (is2xxResponseCode(response.status) && response.responseJSON && response.responseJSON.triggerDetails) {
                    showSuccessMessage("Simple Trigger is successfully created");
                    goForError = false;
                }
            }
            if (goForError) {
                showErrorMessage("Unable to create the trigger" + response.responseJSON.error.message);
            }
        }
    });
};
const handleCreateSimpleTrigger = function () {
    $('form#createSimpleTriggerForm').validate({
        submitHandler: function (form) {
            formSubmitHandler(form);
        }
    });
};
$(document).ready(function () {
    let userJson = JSON.parse($('input#currentLoggedInUserJson').val()).user;

    $('input#triggerGroupName').val(userJson.firstName.trim().toString() + " " + userJson.lastName.trim().toString() + " : " + userJson.id);

    $('form#createSimpleTriggerForm input').keypress(function (e) {
        if (e.which === 13) {
            if ($('form#createSimpleTriggerForm').validate().form()) {
                $('form#createSimpleTriggerForm').submit();
            } else {
                return false;
            }
        }
    });
    handleCreateSimpleTrigger();
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


let callAutocompleteAndPopulate = function (element) {
    let jsonResponse = null;
    $.ajax({
        url: $(element).data('jobKeyNameAutoComplete'),
        data: {
            searchText: $(element).val().trim()
        },
        type: HTTP_GET,
        contentType: APPLICATION_JSON,
        dataType: JSON_DATA_TYPE,
        async: false,
        cache: true,
        complete: function (response) {
            if (response && is2xxResponseCode(response.status) && response.responseJSON && response.responseJSON.jobDetails.length > 0) {
                jsonResponse = response.responseJSON.jobDetails;
                $(element).typeahead({
                    source: function (query, process) {
                        let keys = [];
                        $.each(jsonResponse, function (index, value) {
                            keys.push(value.key);
                        });
                        process(keys);
                    },
                    matcher: function (item) {
                        if (item.toLowerCase().indexOf(this.query.trim().toLowerCase()) !== -1) {
                            return true;
                        }
                    },
                    sorter: function (items) {
                        return items.sort();
                    },
                    updater: function (item) {
                        return item;
                    },
                    items: 'all',
                    minLength: 0,
                    fitToElement: true,
                    afterSelect: function (item) {
                        $.each(jsonResponse, function (index, value) {
                            if (value.key === item) {
                                $('input#jobGroupName').val(createGroupNameFieldText(value.group));
                                $('textarea#jobDescription').val(value.description);
                            }
                        });
                    }
                });
            } else {
                $('input#jobGroupName').val('');
                $('textarea#jobDescription').val('');
                jsonResponse = null;
            }
        }
    });

    if (jsonResponse !== null) {
        $(element).typeahead({
            source: function (query, process) {
                let keys = [];
                $.each(jsonResponse, function (index, value) {
                    keys.push(value.key);
                });
                process(keys);
            },
            matcher: function (item) {
                if (item.toLowerCase().indexOf(this.query.trim().toLowerCase()) !== -1) {
                    return true;
                }
            },
            sorter: function (items) {
                return items.sort();
            },
            updater: function (item) {
                return item;
            },
            items: 'all',
            minLength: 0,
            fitToElement: true,
            afterSelect: function (item) {
                $.each(jsonResponse, function (index, value) {
                    if (value.key === item) {
                        $('input#jobGroupName').val(createGroupNameFieldText(value.group));
                        $('textarea#jobDescription').val(value.description);
                    }
                });
            }
        });
    }
};