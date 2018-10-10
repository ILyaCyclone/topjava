var _saveForm = document.forms.save;
var _creatButton = document.querySelector(".meals__create");

// ==================== create action ====================
_creatButton.addEventListener("click", function() {
    onCreate();
});


function onCreate() {
    console.log("onCreate");

    clearSaveForm();
    _creatButton.style.display = 'none';
    document.querySelector('.meals__form-label.meals__form-label_create').style.display = 'block';
    document.querySelector('.meals__form').style.display = 'inline-block';
}



// ==================== edit action ====================
document.querySelectorAll(".meals__table-edit").forEach(function(button) {
    var mealId = button.closest("tr").getAttribute("data-id");
    button.addEventListener("click", function() {
        onEdit(mealId);
    });
});

function onEdit(mealId) {
    console.log("onEdit id = "+mealId);

    var row = document.querySelector(".meals__table tr[data-id=\""+mealId+"\"]");

    _saveForm.id.value = mealId;
    _saveForm.datetime.value = row.querySelector(".meals__table-datetime").getAttribute("datetime");
    _saveForm.description.value = row.querySelector(".meals__table-description").innerText;
    _saveForm.calories.value = row.querySelector(".meals__table-calories").innerText;

    _creatButton.style.display = 'none';
    document.querySelectorAll('.meals__form-label').forEach(function(el) {el.style.display = 'none';});
    document.querySelector('.meals__form-label.meals__form-label_edit').style.display = 'block';
    document.querySelector('.meals__form').style.display = 'inline-block';
}






// ==================== end of delete action ====================


// ==================== delete action ====================
document.querySelectorAll(".meals__table-delete").forEach(function(button) {
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
// ==================== end of delete action ====================



// ==================== cancel form ====================

document.querySelector(".meals__form-cancel").addEventListener("click", function() {
    _creatButton.style.display = 'block';
    hideSaveForm();
    clearSaveForm();
});
function hideSaveForm() {
    document.querySelector('.meals__form').style.display = 'none';
    document.querySelectorAll('.meals__form-label').forEach(function(el) {el.style.display = 'none';});
}
function clearSaveForm() {
    _saveForm.id.value = null;
    _saveForm.datetime.value = null;
    _saveForm.description.value = null;
    _saveForm.calories.value = null;
}

// ==================== end of cancel form ====================