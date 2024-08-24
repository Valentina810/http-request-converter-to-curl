# Library for converting HTTP requests to Curl

`http-request-converter-to-curl` is a small Kotlin library that provides a method for converting incoming HTTP requests into cURL format with the ability to cache the request.


### Purpose
- Conversion of HTTP requests into cURL format.
- Caching the incoming request for future use.

### Installation
The easiest way is to download the [archive](https://github.com/Valentina810/http-request-converter-to-curl/releases/tag/1.0.0) into the libs folder of your project, extract it, and include it like this:
```
<dependency>
    <groupId>com.github.valentina810</groupId>
    <artifactId>http-request-converter-to-curl</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/libs/http-request-converter-to-curl-1.0.0.jar</systemPath>
</dependency>
```
or
```
dependencies {
    implementation("com.github.valentina810:http-request-converter-to-curl:1.0.0") {
                artifact {
                    file = file("libs/http-request-converter-to-curl-1.0.0.jar")
                }
    }
}
```

### You can add it as a dependency, but
you'll need to [configure credentials for access to GitHub Packages](https://github.com/Valentina810/how-to/blob/main/library-published-in-github-packages/how-to-configure-connection-of-a-library-published-in-github-packages.md).

maven:
```
<dependency>
    <groupId>com.github.valentina810</groupId>
    <artifactId>http-request-converter-to-curl</artifactId>
    <version>1.0.0</version>
</dependency>
```

gradle:

```
dependencies {
    implementation("com.github.valentina810:http-request-converter-to-curl:1.0.0")
}
```

### Usage
The output of the main method of the Converter class:
```kotlin
import javax.servlet.http.HttpServletRequest

class Converter {

    fun convert(request: HttpServletRequest): Data {
        val cachedRequest = CachedBodyHttpServletRequest(request)
        return Data(cachedRequest, cachedRequest.getCurl())
    }
}
```
is an object of the Data class:
```kotlin
data class Data(
val cachedBodyHttpServletRequest: CachedBodyHttpServletRequest?, // cached request
val curl: String                                                 // request in curl format in string representation   
)
```
Below is an example of using the library:
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

### Dependencies
The library depends on the standard Kotlin and Java (21) libraries.

### License
MIT License

Copyright (c) 2024 Valentina Kolesnikova

Permission is hereby granted, free of charge, to any person obtaining a copy  
of this software and associated documentation files (the "Software"), to deal  
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
copies of the Software, and to permit persons to whom the Software is  
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  
SOFTWARE.

### Contacts
If you have any questions or suggestions, you can contact the author at: valentinavasileva34@gmail.com.

---

# Библиотека для конвертации HTTP-запросов в Curl

`http-request-converter-to-curl` — это небольшая Kotlin-библиотека, которая предоставляет метод для преобразования входящего HTTP-запроса в формат cURL с возможностью кэширования запроса.

## Возможности

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
    <systemPath>${project.basedir}/libs/http-request-converter-to-curl-1.0.0.jar</systemPath>
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
нужно будет настроить [учетные данные для доступа к GitHub Packages](https://github.com/Valentina810/how-to/blob/main/library-published-in-github-packages/how-to-configure-connection-of-a-library-published-in-github-packages.md)   
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
является объект класса Data:
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

---
