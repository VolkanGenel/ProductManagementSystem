# ProductManagementSystem
PROJE İÇİN DOCKER'DA kod run edilmeli.
## RabbitMQ Docker
    docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672 -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=root rabbitmq:3-management
2) postgres pgAdmin indirilmiş olmalı, sonrasında AuthServiceDB ve ProductServiceDB Databaseleri oluşturulmalı.

3) Modüllerde öncelikle ConfigServerGit, sonra ApiGateway sonrasında AuthService, ProductService ve MailService Mikroservisleri ayağa kaldırılmalı.

4) Proje çalıştırılıp tablolar oluşturulunca register işleminde volkangenel@hotmail.com emailli bir kişi oluşturulmalı;
çünkü update işlemini gerçekleştirerek ADMIN_ROLE tanımlama yetkisi o emaile sahip kişide. Sonrasında ProductService'te
yetkiye göre istek atılabilir, diğer türlü ADMIN_ROLE'e sahip bir kişi olmayacağı için ürün ekleme yapılamayacaktır.

5)  AuthService için methodlar buradan görüntülenebilir http://localhost:9595/swagger-ui/index.html#/
    ProductService için methodlar buradan görüntülenebilir http://localhost:9596/swagger-ui/index.html#/

6) ProductMicroService için oluşturduğumuz User'ın şifresi ve emaili ile Login methodu çalıştırılarak token alınıp bu tokenla Swagger veya PostMan üzerinden istek atılarak başlanabilir.

----

7) postgre EXTERNAL-IP=34.163.221.50, port=22222, password=root
8) postgres pgAdmin indirilmiş olmalı, sonrasında AuthServiceDB ve ProductServiceDB Databaseleri oluşturulmalı.
9) 
