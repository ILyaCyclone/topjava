const ajaxUrl = "ajax/admin/users/";
let datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();

    enableCheckboxEvent();
});

function enableCheckboxEvent() {
    $("#datatable .enable").change(function () {
        let enabled = this.checked;
        let userId = $(this).closest("tr").attr("data-id");
        let params = {enabled: enabled};
        $.ajax({
            url: ajaxUrl + userId + "/enable",
            data: params,
            type: "POST"
        }).done(function () {
            updateTable();
            successNoty("Updated");
        });
    });
}