[//]: # (# ProductManagementSystem)

[//]: # (PROJE İÇİN DOCKER'DA kod run edilmeli.)

[//]: # (## RabbitMQ Docker)

[//]: # (    docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672 -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=root rabbitmq:3-management)

[//]: # (1&#41; postgres pgAdmin indirilmiş olmalı, sonrasında AuthServiceDB ve ProductServiceDB Databaseleri oluşturulmalı.)

[//]: # ()
[//]: # (2&#41; Modüllerde öncelikle ConfigServerGit, sonra ApiGateway sonrasında AuthService, ProductService ve MailService Mikroservisleri ayağa kaldırılmalı.)

[//]: # ()
[//]: # (3&#41; Proje çalıştırılıp tablolar oluşturulunca register işleminde volkangenel@hotmail.com emailli bir kişi oluşturulmalı;)

[//]: # (çünkü update işlemini gerçekleştirerek ADMIN_ROLE tanımlama yetkisi o emaile sahip kişide. Sonrasında ProductService'te)

[//]: # (yetkiye göre istek atılabilir, diğer türlü ADMIN_ROLE'e sahip bir kişi olmayacağı için ürün ekleme yapılamayacaktır.)

-- KURULUM GEREKTİRMEDEN İŞLEM YAPMAK İÇİN;
1) Kayıt işlemi gerçek bir mail adresi ile yapılmalı
2) Admin yetkisi verilmeden ürün kayıt işlemleri gibi işlemler yapılamamaktadır. Bu yüzden talep edilmeli

3) AuthService için methodlar buradan görüntülenebilir http://34.163.6.240:9595/swagger-ui/index.html#/
4) ProductService için methodlar buradan görüntülenebilir http://34.155.67.112:9596/swagger-ui/index.html#/
5) ProductMicroService için User'ın şifresi ve emaili ile Login methodu çalıştırılarak token alınıp bu tokenla Swagger veya PostMan üzerinden istek atılarak başlanabilir.

6) postgre EXTERNAL-IP=34.163.221.50, port=11111, password=root
7) postgres pgAdmin indirilmiş olmalı, sonrasında AuthServiceDB ve ProductServiceDB Databaseleri oluşturulmalı.

8) docker build --platform linux/amd64 --build-arg JAR_FILE=ConfigServerGit/build/libs/ConfigServerGit-v.0.1.jar -t volkangenel/product-management-configservergit:v.0.1 . 
9) docker build --platform linux/amd64 --build-arg JAR_FILE=AuthService/build/libs/AuthService-v.0.1.jar -t volkangenel/product-management-authservice:v.0.1 .
10) docker build --platform linux/amd64 --build-arg JAR_FILE=ProductService/build/libs/ProductService-v.0.1.jar -t volkangenel/product-management-productservice:v.0.1 .
11) docker build --platform linux/amd64 --build-arg JAR_FILE=MailService/build/libs/MailService-v.0.1.jar -t volkangenel/product-management-mailservice:v.0.1 .
12) docker build --platform linux/amd64 --build-arg JAR_FILE=Gateway/build/libs/Gateway-v.0.1.jar -t volkangenel/product-management-gateway:v.0.1 .
13) ApiGateway için http://34.155.185.73/