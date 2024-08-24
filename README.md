# http-request-converter-to-curl

`http-request-converter-to-curl` — это небольшая Kotlin-библиотека, которая предоставляет метод для преобразования входящего HTTP-запроса в формат cURL с возможностью кэширования запроса.

## Особенности

- Преобразование HTTP-запросов в формат cURL.
- Кэширование входящего запроса для дальнейшего использования.

## Установка
Проще всего скачать [архив](https://github.com/Valentina810/http-request-converter-to-curl/releases/tag/1.0.0) в ваш проект в папку libs, распаковать его и подключить так:
```
<dependency>
    <groupId>com.github.valentina810</groupId>
    <artifactId>http-request-converter-to-curl</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/http-request-converter-to-curl-1.0.0.jar</systemPath>
</dependency>
```
или
```
dependencies {
    implementation("com.github.valentina810:http-request-converter-to-curl:1.0.0") {
                artifact {
                    file = file("libs/http-request-converter-to-curl-1.0.0.jar")
                }
    }
}
```

### Можно добавлять как зависимость, но 
нужно будет настроить [учетные данные для доступа к GitHub Packages](https://github.com/Valentina810/how-to/blob/main/how-to-configure-connection-of-a-library-published-in-github-packages.md)   
Для maven:
```
<dependency>
    <groupId>com.github.valentina810</groupId>
    <artifactId>http-request-converter-to-curl</artifactId>
    <version>1.0.0</version>
</dependency>
```

Для gradle:

```
dependencies {
    implementation("com.github.valentina810:http-request-converter-to-curl:1.0.0")
}
```
### Использование
Выходными данными основного метода класса Converter
```kotlin
import javax.servlet.http.HttpServletRequest

class Converter {

    fun convert(request: HttpServletRequest): Data {
        val cachedRequest = CachedBodyHttpServletRequest(request)
        return Data(cachedRequest, cachedRequest.getCurl())
    }
}
```
является объект класса Data
```kotlin
data class Data(
val cachedBodyHttpServletRequest: CachedBodyHttpServletRequest?, // кэшированный запрос
val curl: String                                                 // запрос в curl-формате в строковом представлении   
)
```
Ниже приведён пример использования библиотеки:
```java
public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
    if (servletRequest instanceof HttpServletRequest request && servletResponse instanceof HttpServletResponse response) {
        Data convert = new Converter().convert(request);
        log.info("Входящий запрос {}", convert.getCurl());
        processRequest.accept(request, response);
        if (!response.isCommitted()) {
            try {
                filterChain.doFilter(convert.getCachedBodyHttpServletRequest(), response);
            } catch (IOException | ServletException e) {
                LOG_ERROR.accept(e.getMessage());
            }
        } else log.info("Запрос не был обработан по причине отсутствия заголовка User-Agent с требуемым значением");
    }
}
```

### Зависимости
Библиотека зависит от стандартных библиотек Kotlin и Java(21).

### Лицензия
Этот проект лицензирован на условиях лицензии MIT. Вы можете свободно использовать, распространять и модифицировать код.
Лицензия MIT

Авторские права (c) 2024 г. Валентина Колесникова

Разрешение настоящим предоставляется бесплатно любому лицу, получившему копию.
данного программного обеспечения и связанных с ним файлов документации («Программное обеспечение») для решения
в Программном обеспечении без ограничений, включая, помимо прочего, права
использовать, копировать, изменять, объединять, публиковать, распространять, сублицензировать и/или продавать
копий Программного обеспечения и разрешать лицам, которым Программное обеспечение
предоставлено для этого при соблюдении следующих условий:

ПРОГРАММНОЕ ОБЕСПЕЧЕНИЕ ПРЕДОСТАВЛЯЕТСЯ «КАК ЕСТЬ», БЕЗ КАКИХ-ЛИБО ГАРАНТИЙ, ЯВНЫХ ИЛИ
ПОДРАЗУМЕВАЕМЫЕ, ВКЛЮЧАЯ, НО НЕ ОГРАНИЧИВАЯСЬ, ГАРАНТИЯМИ ТОВАРНОЙ ПРИГОДНОСТИ,
ПРИГОДНОСТЬ ДЛЯ ОПРЕДЕЛЕННОЙ ЦЕЛИ И НЕНАРУШЕНИЕ ПРАВ. НИ В КОЕМ СЛУЧАЕ НЕ ДОЛЖНО
АВТОРЫ ИЛИ ОБЛАДАТЕЛИ АВТОРСКИХ ПРАВ НЕСУТ ОТВЕТСТВЕННОСТЬ ЗА ЛЮБЫЕ ПРЕТЕНЗИИ, УБЫТКИ ИЛИ ДРУГИЕ
ОТВЕТСТВЕННОСТЬ ПО ДОГОВОРУ, ПРАВИЛАМ ИЛИ ДРУГИМ ОБРАЗУ, ВЫТЕКАЮЩАЯ ИЗ:
ВНЕ ИЛИ В СВЯЗИ С ПРОГРАММНЫМ ОБЕСПЕЧЕНИЕМ ИЛИ ИСПОЛЬЗОВАНИЕМ ИЛИ ДРУГИМИ ДЕЛАМИ В
ПРОГРАММНОЕ ОБЕСПЕЧЕНИЕ.

###  Контакты
Если у вас есть вопросы или предложения, вы можете связаться с автором по адресу: valentinavasileva34@gmail.com

