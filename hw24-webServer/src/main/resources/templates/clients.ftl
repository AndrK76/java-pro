<html xmlns="http://www.w3.org/1999/xhtml" lang="ru">
<head>
    <link rel="stylesheet" href="css/default.css">
    <title>Клиенты</title>
</head>

<body>

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
</body>
</html>
