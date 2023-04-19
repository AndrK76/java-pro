<html xmlns="http://www.w3.org/1999/xhtml" lang="ru">
<head>
    <link rel="stylesheet" href="css/default.css">
    <title>Клиенты</title>
</head>

<body>

<a href="/">Главная</a>&nbsp;&nbsp;
Вариант без API&nbsp;&nbsp;
<a href="${page2_address}">Вариант с API</a>
<table class="data-table">
    <thead>
    <tr>
        <th colspan="3">Все клиенты</th>
        <td style="text-align: right">Всего: ${clients?size}</td>
    </tr>
    <tr>
        <th style="width: 50px">Id</th>
        <th style="width: 150px">Имя</th>
        <th style="width: 100px">Адрес</th>
        <th style="width: 100px">Телефон</th>
    </tr>
    </thead>
    <tbody>
    <#list clients as client>
        <tr>
            <td>${client.id}</td>
            <td>${client.name}</td>
            <td>
                <#if client.address?has_content>
                    ${client.address.street}
                <#else>
                    &nbsp;
                </#if>
            </td>
            <td>
                <#if client.phones?has_content && (client.phones?size > 0)>
                    ${client.phones?first.number}
                <#else>
                    &nbsp;
                </#if>
            </td>
        </tr>
    </#list>
    </tbody>
</table>
<hr/>
<div class="form-new">
    <h3>Новый клиент</h3>
    <form action="${page_address}" method="post">
        <label for="client_name" class="input-label">Имя:</label>
        <input type="text" id="client_name" name="name" class="input-item" placeholder="Имя клиента"/>
        <#if err_msg?has_content>
            <span class="error-item">${err_msg}</span>
        </#if>
        <br/><br/>
        <label for="client_address" class="input-label">Адрес:</label>
        <input type="text" id="client_address" name="address" class="input-item"
               placeholder="Улица" value="${address}"/>
        <br/><br/>
        <label for="client_phone" class="input-label">Телефон:</label>
        <input type="text" id="client_phone" name="phone" class="input-item"
               placeholder="Номер" value="${phone}"/>
        <br/><br/>
        <input type="submit" value="Добавить"/>
    </form>
</div>
</body>
</html>
