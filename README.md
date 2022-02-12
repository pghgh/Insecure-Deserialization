# Insecure-Deserialization

This repository contains two simple examples of exploiting and preventing insecure deserialization, a web application security risk included in a ranking made by OWASP in 2017: [OWASP Top Ten 2017 - A8:2017-Insecure Deserialization](https://owasp.org/www-project-top-ten/2017/A8_2017-Insecure_Deserialization). The examples were implemented for the course [Information Security, University of Vienna, Summer Semester 2021](https://ufind.univie.ac.at/en/course.html?lv=051061&semester=2021S).

The scenario (inspired by the situation presented in the 8th entry of the mentioned Top 10) is the following: A PHP forum uses PHP object serialization in order to save a “super” cookie, which contains the user's state, including his role (normal user/admin). The attacker can modify the serialized object, so that he'll give himself the administrator role. In order to prevent this from happening, hash codes or digital signatures can be used. Java was used as programming language because it offers support for these said protection mechanisms.

## How to run the examples 

The instructions on how to run the code can be found in the classes titled "MainClass" (for each of the two proposed solutions).
