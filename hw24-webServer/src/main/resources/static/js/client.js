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
    $('#countCell').text("Всего: " + rowCount);
    //console.log('row count: ' + rowCount);
}

$.getJSON(apiUrl, function (data) {
    $.each(data, function (key, val) {
        addRow(val);
    });
    setRowCount();
});

$('#addButton').click(function () {
    let name = $('#client_name').val();
    //console.log('name: value="' + name + '" length=' + name.length);
    if (name.length == 0) {
        $('#error-item').text('Имя клиента обязательно для ввода');
        return;
    } else {
        $('#error-item').text('');
    }
    let addrText = $('#client_address').val();
    //console.log('address: value="' + addrText + '" length=' + addrText.length);
    let phoneNum = $('#client_phone').val();
    //console.log('phone: value="' + phoneNum + '" length=' + phoneNum.length);
    let client = {};
    client["name"] = name;
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
        client["phones2"] = phones;
    }
    //console.log("send:" + JSON.stringify(client));
    $.post(apiUrl, JSON.stringify(client),
        function (data, status) {
            //console.log('status: ' + status);
            //console.log('data: ' + JSON.stringify(data));
            addRow(data);
            setRowCount();
            $('#client_name').val('');
            $('#client_address').val('');
            $('#client_phone').val('');
        })
        .fail(function (data, status) {
            //console.log('status: ' + status);
            //console.log('error: ' + data.status + ' ' + data.statusText);
            alert('status: ' + status + '\n' + 'error: ' + data.status + ' ' + data.statusText);
        });
});