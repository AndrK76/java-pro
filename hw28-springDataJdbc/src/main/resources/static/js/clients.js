function addRow(row) {
    $('#listTable  > tbody').append('<tr>'
        + '<td>' + row.id + '</td>'
        + '<td>' + row.name + '</td>'
        + '<td>' + (row.address == null ? '' : row.address.street) + '</td>'
        + '<td>' + (row.phones == null || row.phones.length == 0 ? '' : row.phones[0].number) + '</td>'
        + '</tr>');
    //console.log(row.id + ' : ' + row.name);
}

function setRowCount() {
    const rowCount = $('#listTable > tbody > tr').length;
    $('#totalItem').text("Всего: " + rowCount);
    //console.log('row count: ' + rowCount);
}

function getAndCheckName() {
    let name = $('#inputName').val();
    //console.log('name: value="' + name + '" length=' + name.length);
    if (name.length == 0) {
        $('#nameError').text('Имя клиента обязательно для ввода');
        return '';
    } else {
        $('#nameError').text('');
    }
    return name;
}

function createClientObj(name) {
    let client = {};
    client["name"] = name;
    let addrText = $('#inputAddress').val();
    let phoneNum = $('#inputPhone').val();
    if (addrText.length > 0) {
        let address = {};
        address["street"] = addrText;
        client["address"] = address;
    }
    if (phoneNum.length > 0) {
        let phones = [];
        let phone = {};
        phone["number"] = phoneNum;
        phones.push(phone);
        client["phones"] = phones;
    }
    return client;
}

$.getJSON(apiUrl, function (data) {
    $('#listTable  > tbody').empty();
    $.each(data, function (key, val) {
        addRow(val);
    });
    setRowCount();
});

$.ajaxSetup({ contentType: "application/json; charset=utf-8" });

$('#addClientButton').click(function () {
    //console.log('addClient click');
    let name = getAndCheckName();
    if (name.length == 0) return;
    //console.log('name valid');
    let client = createClientObj(name);
    //console.log('client='+ JSON.stringify(client));

    $.post(apiUrl, JSON.stringify(client),
        function (data, status, xhr) {
            xhr.setRequestHeader("Content-type", "application/json");
            //console.log('status: ' + status);
            //console.log('data: ' + JSON.stringify(data));
            addRow(data);
            $('#inputName').val('');
            $('#inputAddress').val('');
            $('#inputPhone').val('');
            setRowCount();
        })
        .fail(function (data, status) {
            //console.log('status: ' + status);
            //console.log('error: ' + data.status + ' ' + data.statusText);
            alert('status: ' + status + '\n' + 'error: ' + data.status + ' ' + data.statusText);
        });
});
