getAllItems();
var itemArray=[];
function getAllItems() {
    $("#tblItem").empty();

    $.ajax({
/*
        url: "http://localhost:8082/pos/pages/item?option=getAll",
*/
        url: "http://localhost:8085/DBCP_Web_exploded/item?option=getAll",
        method: "GET",
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin"
        },
        success: function (data) {
            itemArray=data.data;
            generateNewIdItem();
            for (let i = 0; i < data.length; i++) {
                let code = data[i].code;
                let description = data[i].description;
                let qty = data[i].qty;
                let unitPrice = data[i].unitPrice;
                let tr = `<tr><td>${code}</td><td>${description}</td><td>${unitPrice}</td><td>${qty}</td></tr>`;
                $("#tblItem").append(tr);
            }
        },
    });
    /*});*/
}





function generateNewIdItem(){
    $("#itemCode").val("I0-001");

    $.ajax({
       // url: "http://localhost:8082/pos/pages/item?option=getNewId",

        url: "http://localhost:8085/DBCP_Web_exploded/item?option=getNewId",

        method: "GET",
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin"
        },

        success: function (resp) {
            var tempId=0

            if (tempId <= 9) {
                $("#itemCode").val("I0-00"+resp);
            } else if (tempId <= 99) {
                $("#itemCode").val("I0-0"+resp);
            } else if (tempId <= 999) {
                $("#itemCode").val("I0-"+resp);
            }
        },
        error: function (ob, statusText, error) {
        }

    });
}


$("#btnItemGetAll").click(function (){

});

function clearAll(){

    $("#itemName").val("");
    $("#itemQty").val("");
    $("#itemPrice").val("");
}

$("#btn-clear").click(function () {
    clearAll();
});


$("#btnItem").click(function () {
    let formData1 = $("#ItemFromData").serialize();

    $.ajax({
       // url: "http://localhost:8082/pos/pages/item",
        url: "http://localhost:8085/DBCP_Web_exploded/item",

        method: "POST",
        data: formData1,
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin"
        },

        success: function (data) {
            getAllItems();
            clearAll();
            alert(data.message);
        },
        error: function (data) {
            alert(data.responseJSON.message);
        }

    });
});


$("#btnItemUpdate").click(function () {

    let code = $("#itemCode").val();
    let description = $("#itemName").val();
    let qty = $("#itemQty").val();
    let unitPrice = $("#itemPrice").val();

    let itemData = {
        code: code,
        description: description,
        qty: qty,
        unitPrice: unitPrice
    };

    $.ajax({
       // url: "http://localhost:8082/pos/pages/item",
        url: "http://localhost:8085/DBCP_Web_exploded/item",

        method: "PUT",
        contentType: "application/json",
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin"
        },
        data: JSON.stringify(itemData),

        success: function (data) {
            getAllItems();
            alert(data.message);
        },
        error: function (data) {
            alert(data.responseJSON.message);
        }
    });
});




$("#btnItemDelete").click(function () {

    $.ajax({
        url: "http://localhost:8085/DBCP_Web_exploded/item?code=" + $("#itemCode").val(),
        method: "DELETE",
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin"
        },


        success: function (data) {
            getAllItems();
            alert(data.message);

        },
        error: function (data) {
            alert(data.responseJSON.message)
            console.log(data);
        }
    });
});





$("#tblItem").click(function () {

    let tr = event.target.closest("tr");

    let code=tr.cells[0].textContent;
    let description=tr.cells[1].textContent;
    let qty=tr.cells[2].textContent;
    let unitPrice=tr.cells[3].textContent;

    $("#itemCode").val(code);
    $("#itemName").val(description);
    $("#itemQty").val(qty);
    $("#itemPrice").val(unitPrice);
});