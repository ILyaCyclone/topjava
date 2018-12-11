let context, form;
// const humanDateRegExp = new RegExp("(\\d{2})\\.(\\d{2})\\.(\\d{4}) (\\d{2}:\\d{2})");
const humanDateRegExp = new RegExp("(\\d{4})\\-(\\d{2})\\-(\\d{2}) (\\d{2}:\\d{2})");
const isoDateRegExp = new RegExp("(\\d{4})\\-(\\d{2})\\-(\\d{2})T(\\d{2}:\\d{2}):\\d{2}");

function makeEditable(ctx) {
    context = ctx;
    form = $('#detailsForm');
    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});

    // init datepicker
    $.datetimepicker.setLocale('ru');
    $(".datetimepicker").each(function () {
        let pickertype = $(this).attr("data-pickertype");
        let options = null;
        switch (pickertype) {
            case "date":
                options = {format: "Y-m-d", timepicker: false};
                break;
            case "time":
                options = {format: "H:i", datepicker: false};
                break;
            default:
                options = {format: "Y-m-d H:i"};
        }
        $(this).datetimepicker(options);
    });
}

function add() {
    $("#modalTitle").html(i18n["addTitle"]);
    form.find(":input").val("");
    $("#editRow").modal();
}

function updateRow(id) {
    $("#modalTitle").html(i18n["editTitle"]);
    $.get(context.ajaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            let humanDate = isoDateToHumanDate(value);
            if (humanDate != null) value = humanDate;
            form.find("input[name='" + key + "']").val(value);
        });
        $('#editRow').modal();
    });
}

function deleteRow(id) {
    $.ajax({
        url: context.ajaxUrl + id,
        type: "DELETE"
    }).done(function () {
        context.updateTable();
        successNoty("common.deleted");
    });
}

function updateTableByData(data) {
    context.datatableApi.clear().rows.add(data).draw();
}

function save() {
    // let data = form.serialize();

    let dataArray = form.serializeArray();
    let parameters = [];
    // // convert date from human format to ISO date
    for (let i = 0; i < dataArray.length; i++) {
        let dataItem = dataArray[i];

        let isoDate = humanDateToIsoDate(dataItem.value);
        if (isoDate != null) dataItem.value = isoDate;
        parameters.push(dataItem.name + "=" + encodeURI(dataItem.value));
    }

    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: parameters.join("&")
    }).done(function () {
        $("#editRow").modal("hide");
        context.updateTable();
        successNoty("common.saved");
    });
}

let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(key) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + i18n[key],
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;" + i18n["common.errorStatus"] + ": " + jqXHR.status + (jqXHR.responseJSON ? "<br>" + jqXHR.responseJSON : ""),
        type: "error",
        layout: "bottomRight"
    }).show();
}

function renderEditBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='updateRow(" + row.id + ");'><span class='fa fa-pencil'></span></a>";
    }
}

function renderDeleteBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='deleteRow(" + row.id + ");'><span class='fa fa-remove'></span></a>";
    }
}

function humanDateToIsoDate(humanDateStr) {
    let dateParts = humanDateRegExp.exec(humanDateStr);
    if (dateParts == null) {
        return null;
    } else {
        let yyyy = dateParts[1];
        let MM = dateParts[2];
        let dd = dateParts[3];
        let hhmm = dateParts[4];
        return yyyy + "-" + MM + "-" + dd + "T" + hhmm + ":00";
    }
}

function isoDateToHumanDate(isoDateStr) {
    let dateParts = isoDateRegExp.exec(isoDateStr);
    if (dateParts == null) {
        return null;
    } else {
        let yyyy = dateParts[1];
        let MM = dateParts[2];
        let dd = dateParts[3];
        let hhmm = dateParts[4];
        return yyyy + "-" + MM + "-" + dd + " " + hhmm;
    }
}