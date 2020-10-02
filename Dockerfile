FROM openjdk:8

 

WORKDIR /usrapp/bin

 

ENV PORT 6000

 

COPY SeverFachada/target/classes /usrapp/bin/classes
COPY SeverFachada/target/dependency /usrapp/bin/dependency
COPY SeverFachada/keystores /usrapp/bin/keystores
COPY CalculadoraTrigonometrica/keystores /usrapp/bin/CalculadoraTrigonometrica/keystores
 

CMD ["java","-cp","./classes:./dependency/*","edu.eci.arem.App"]