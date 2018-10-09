

document.querySelectorAll(".meals .delete").forEach(function(button) {
    var mealId = button.closest("tr").getAttribute("data-id");
    button.addEventListener("click", function() {
        onDelete(mealId);
    });
});

function onDelete(mealId) {
    console.log("onDelete id = "+mealId);
    if(confirm("Delete record?")) {
        document.forms.delete.id.value = mealId;
        document.forms.delete.submit();
    }
}