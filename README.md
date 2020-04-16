# Human Resource Management System

## Project Introduction

Spring Boot Actual project（Human Resource Management System），Separate front-end development，Spring Boot + Vue 。

- project address：[https://github.com/cxy35/hr](https://github.com/cxy35/hr)

## Project Technology Stack

### Front-end technology stack

1. Vue
2. ElementUI
3. axios
4. vue-router
5. Vuex
6. WebSocket
7. vue-cli4
8. ...

### Backend technology stack

1. Spring Boot
2. Spring Security
3. MyBatis
4. MySQL
5. Redis
6. ActiveMQ/RabbitMQ
7. Spring Cache
8. WebSocket
9. POI
10. Mail service
11. ...

## Project renderings

log in

![](https://oscimg.oschina.net/oscnet/up-f1bb1a9dcf69435da89054fd98a91bf0f6c.png)

Home

![](https://oscimg.oschina.net/oscnet/up-bf28b202417cb2040b8a9ed1854788a423a.png)

Employee list

![](https://oscimg.oschina.net/oscnet/up-05405d2c04606aaff686d277f9f16ba7b4b.png)

New employee

![](https://oscimg.oschina.net/oscnet/up-98df569bad73ce4ef4fe32b9b606a7e21ef.png)

Role management

![](https://oscimg.oschina.net/oscnet/up-4e9c51fe334994c728a0e4d4e8f0e6c9963.png)

User Management

![](https://oscimg.oschina.net/oscnet/up-9958c4977b05d2ef529413c0ebd5a9376ce.png)

Online chat

![](https://oscimg.oschina.net/oscnet/up-73929a7a0a07d7fed372401fb08c989fd0e.png)

## Project deployment and secondary development

1. clone Code to local `hr` table of Contents： `git clone https://github.com/cxy35/hr.git hr` 。
2. New locally `hr` database，Execute database initialization script `hr/hr.sql` 。
3. use IntelliJ IDEA or others IDE Import `hr` Backend engineering（selected hr Directory）。If it is not secondary development，You can skip this step。
4. modify `hr-web` Under engineering `application.properties` Database in configuration file、ActiveMQ/RabbitMQ、Redis Wait for configuration，Then pass IDE start up or Maven Start the backend service after packaging，default `8081` port（see `application.properties` Configuration file）。
5. modify `hr-mail` Under engineering `application.properties` Mail service in configuration file、ActiveMQ/RabbitMQ Wait for configuration，Then pass IDE start up or Maven Start the mail service after packaging，default `8082` port（see `application.properties` Configuration file）。If you don't want to send email，You can skip this step。
6. use WebStorm or others IDE Import `hr-vue` Front-end engineering（selected hr-vue table of Contents）。If it is not secondary development，You can skip this step。
7. in `hr-vue` Used in the root directory `npm install` Command to install front-end dependencies（Need to install first `node` surroundings，Baidu），Such as：vue、element-ui、axios Wait，See `hr-vue/README.md` 。
8. in `hr-vue` Used in the root directory `npm run serve` Command to start the front-end service，default `8080` port（see `vue.config.js` Configuration file）。
9. access [http://127.0.0.1:8080](http://127.0.0.1:8080) ，use admin/123 log in。

## Project R &amp; D History

- front end，create hr-vue engineering
- front end，log in page
- rear end，create hr engineering
- rear end，Login interface
- Front end，Login function joint debugging
- front end，Home（Includes left navigation menu）
- rear end，Menu interface
- front end，Pages corresponding to all menus（The content temporarily contains only the menu name）
- Front end，Menu function joint debugging
- rear end，Unified increase of access control for all requested addresses
- System Management-Basic information settings
- System Management-Basic information settings-Position management
- System Management-Basic information settings-Job title management
- System Management-Basic information settings-Role management
- System Management-Basic information settings-Department management
- System Management-User Management
- Employee information-basic information
- New employees automatically send onboarding notification emails
- Payroll management-Payroll set management
- Payroll management-Employee account set management
- Online chat
- Other business modules have not been implemented...


---

- [Spring Boot Tutorial collection](https://mp.weixin.qq.com/s/9vOiAxHFnfJnRwSlTfAHwg)
- [Spring Boot Tutorial collection sample code](https://github.com/cxy35/spring-boot-samples)
- [Spring Boot Actual project（Human Resource Management System）](https://github.com/cxy35/hr)


---

Scan the code to follow WeChat public account **programmer35** ，Get the latest technology dry goods，Chat #Programmer's35，35Programmer# 。Independent site：[https://cxy35.com](https://cxy35.com)

![](https://oscimg.oschina.net/oscnet/up-285838b9c516db5bb1ba760f292f2346078.JPEG)
