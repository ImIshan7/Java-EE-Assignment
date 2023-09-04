var customersArray=[];
getAllCustomer();

function getAllCustomer() {
    /*<!--send ajax request to the customer servlet using jQuery-->*/
    $("#tblCustomer").empty();
    $.ajax({
       // url: "http://localhost:8082/pos/pages/customer?option=GetAll",
        url: "http://localhost:8085/DBCP_Web_exploded/customer?option=GetAll",
        method: "GET",
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin"
        },

        success: function (cus) {
            generateCustomerId();
            /*<!--when the response received catch it and set it to the table-->*/
            customersArray = cus.data;
            for (let i = 0; i < cus.length; i++) {
                let id = cus[i].id;
                let name = cus[i].name;
                let address = cus[i].address;
                let contact = cus[i].contact;
                let row = `<tr><td>${id}</td><td>${name}</td><td>${address}</td><td>${contact}</td></tr>`;
                $("#tblCustomer").append(row);

            }
        }
    });
}

function generateCustomerId() {
    $("#txtCustomerID").val("C0-001");
    $.ajax({
/*
        url: "http://localhost:8082/pos/pages/customer?option=GetIds",

*/
        url: "http://localhost:8085/DBCP_Web_exploded/customer?option=GetIds",

        method: "GET",
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin"
        },

        success: function (resp) {
            $("#txtCustomerID").val("C0-00" + resp);
        }
    });

}
searchCustomer();

var search = false;
function searchCustomer() {
    // let id = $("#txtCustomerID").val();
    $.ajax({
        url: "http://localhost:8082/pos/pages/customer?option=search&cusID=" + $("#txtCustomerID").val(),
        method: "GET",
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin"
        },
        success: function (resp) {
            if (resp.id==$("#txtCustomerID").val());
            search=true;
            $("#txtCustomerName").val(resp.name);
            $("#txtCustomerAddress").val(resp.address);
            $("#txtCustomerSalary").val(resp.contact);
        },
        error: function (error) {
            //  alert(error.responseJSON.message);
            search=false;
            alert(error.responseJSON.message)
            //console.log(error);
        }
    });
}

function clearData(){
    //$("#txtCustomerID").val("");
    $("#txtCustomerName").val("");
    $("#txtCustomerAddress").val("");
    $("#txtCustomerSalary").val("");

}

$("#btn-clear1").click(function (){
    clearData();
});



$("#btnGetAll").click(function () {
    generateCustomerId();
});
/*<!--bind a event on btn save btn-->*/

$("#btnCustomer").click(function () {
    let formData = $("#customerFrom").serialize();
    console.log(formData)
    $.ajax({
    //    url: "http://localhost:8082/pos/pages/customer",
        url: "http://localhost:8085/DBCP_Web_exploded/customer",
        method: "POST",
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin",
            data: formData,
        },
        success: function (resp) {
            getAllCustomer();
            clearData();
            alert(resp.message);
        },
        error: function (error) {
            //   alert("An error occurred while communicating with the server.");
            alert(error.responseJSON.message);

        }
    });


});



$("#btnUpdate").click(function () {

    /* alert("D");*/

    let id = $("#txtCustomerID").val();
    let name = $("#txtCustomerName").val();
    let address = $("#txtCustomerAddress").val();
    let contacts = $("#txtCustomerSalary").val();
    let cuData = {
        cusID: id,
        cusName: name,
        cusAddress: address,
        contact: contacts
    };

    $.ajax({
        url: "http://localhost:8082/pos/pages/customer",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(cuData),
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin"
        },

        success: function (resp) {
            getAllCustomer();
            alert(resp.message)
            console.log(resp);

        },
        error: function (error) {
            //   alert("An error occurred while communicating with the server.");
            //  alert(error.responseJSON.message);
            alert(error.responseJSON.message)
            console.log(error);
        }

    });
});

$("#btnCusDelete").click(function () {

    $.ajax({
        url: "http://localhost:8082/pos/pages/customer?cusID=" + $("#txtCustomerID").val(),
        method: "DELETE",
        //data:formData,
        dataType: "json",
        headers: {
            Auth: "user=admin,pass=admin"
        },

        success: function (resp) {
            getAllCustomer();
            alert(resp.message);

        },
        error: function (error) {
            //   alert("An error occurred while communicating with the server.");
            //  alert(error.responseJSON.message);
            alert(error.responseJSON.message)
            console.log(error);
        }
    });
});





$("#tblCustomer").on('click', function (event) {


    let tr = event.target.closest("tr");

    let id=tr.cells[0].textContent;
    let name=tr.cells[1].textContent;
    let address=tr.cells[2].textContent;
    let contact=tr.cells[3].textContent;

    // Call the updated setFiledSet function to populate the form fields
    setFiledSet(id, name, address, contact);
    console.log(id, name, address, contact);
});



// Modify the setFiledSet function to populate the form fields
function setFiledSet(id, name, address, contact) {
    $("#txtCustomerID").val(id);
    $("#txtCustomerName").val(name);
    $("#txtCustomerAddress").val(address);
    $("#txtCustomerSalary").val(contact);
    // Re-bind the click event after updating the fields
}