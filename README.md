# Java Security Unit Tests
A web application that contains several unit tests for the purpose of Java security

## Overview
The purpose of this web app is to test the following vulnerabilities in Java (click to view their respective code):
- [XML External Enitity (XXE) Injection](./src/main/java/com/aspectsecurity/unittestsweb/xxetestcases)
   - A summary of these tests can be found in the [OWASP XXE Prevention Cheat Sheet](https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#Java)
- [XPath Query Language Injection](./src/main/java/com/aspectsecurity/unittestsweb/xpathtestcases)
- [XQuery Query Language Injection](./src/main/java/com/aspectsecurity/unittestsweb/xquerytestcases)

The code can be analyzed by static code tools or deployed as a web application and analyzed via dynamic tools. The underlying Java code can also be used as examples for how to make Java code safe/unsafe. You can also find detailed comments in the code with each test explaining why it is safe/unsafe.

## Installation
Please see the [SETUP_INSTRUCTIONS.txt](./SETUP_INSTRUCTIONS.txt) file for information on deploying the web app.

## License
```
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
