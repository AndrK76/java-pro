<html xmlns="http://www.w3.org/1999/xhtml" lang="ru">

<head>
    <script>
        const apiUrl = "${api_address}";
    </script>

    <link rel="stylesheet" href="/css/default.css">
    <script src="/js/jquery.min.js"></script>
    <title>Клиенты (API + AJAX)</title>
</head>

<body>
<a href="/">Главная</a>&nbsp;&nbsp;
<a href="${page_address}">Вариант без API</a>
&nbsp;&nbsp;Вариант с API

<table class="data-table" id="listTable">
    <thead>
    <tr>
        <th colspan="3">Все клиенты</th>
        <td style="text-align: right" id="countCell">Всего: 0</td>
    </tr>
    <tr>
        <th style="width: 50px">Id</th>
        <th style="width: 150px">Имя</th>
        <th style="width: 100px">Адрес</th>
        <th style="width: 100px">Телефон</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>

<hr />

<div class="form-new">
    <h3>Новый клиент</h3>
    <form>
        <label for="client_name" class="input-label">Имя:</label>
        <input type="text" id="client_name" name="name" class="input-item" placeholder="Имя клиента" />
        <span class="error-item" id="error-item"></span>
        <br /><br />
        <label for="client_address" class="input-label">Адрес:</label>
        <input type="text" id="client_address" name="address" class="input-item" placeholder="Улица" />
        <br /><br />
        <label for="client_phone" class="input-label">Телефон:</label>
        <input type="text" id="client_phone" name="phone" class="input-item" placeholder="Номер" />
        <br /><br />
        <input type="button" value="Добавить" id="addButton" />
    </form>
</div>

<script src="/js/client.js"></script>

</script>

</body>

</html>