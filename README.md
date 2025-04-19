# drools-spring-boot-3-sample
The Spring Boot 3.x Drools app. 


Using the drools rules starter



## Debug

You can run app from your IDE, using gradle and connect with remote debuger (e.g. on default 5005 pot)"

```
./gradlew bootRun --debug-jvm
```

In both cases it would load `drl` files from the file system.

If you need to test how it would work from the `jar` (e.g. when you deploy it to a cloud) you can run it 
from jar and connect with remote debugger:


```
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar build/libs/drools-spring-boot-3-sample-0.0.1-SNAPSHOT.jar
```