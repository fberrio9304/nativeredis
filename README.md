# SPRING NATIVE

###Primero debemos conocer un poco de Spring Native

## Información general: 
El proyecto Spring Native usa varias tecnologías para proporcionar el rendimiento de aplicaciones nativas a los desarrolladores.

Para comprender completamente a Spring Native, es útil comprender algunas de estas tecnologías de componentes, qué es lo que nos habilitan y cómo funcionan juntas.

## Compilación AOT: 
Cuando los desarrolladores ejecutan javac normalmente en el momento de la compilación, nuestro código fuente .java se compila en archivos .class escritos en código de bytes. Solo la máquina virtual Java debe interpretar este código de bytes, por lo que la JVM deberá interpretarlo en otras máquinas para poder ejecutar nuestro código.

Este proceso es lo que nos brinda la portabilidad de la firma de Java, ya que nos permite "escribir una vez y ejecutar en todas partes", pero es costoso en comparación con ejecutar código nativo.

Afortunadamente, la mayoría de las implementaciones de la JVM utilizan una compilación justo a tiempo para mitigar este costo de interpretación. Para lograr esto, se cuentan las invocaciones de una función. Si se la invoca con la frecuencia suficiente para superar un umbral ( 10,000 de forma predeterminada), se compila en el código nativo durante el tiempo de ejecución a fin de evitar una interpretación más costosa.

La compilación anticipada tiene el enfoque opuesto, que compila todo el código alcanzable en un ejecutable nativo durante la compilación. Esto cambia la portabilidad para la eficiencia de la memoria y otras mejoras de rendimiento en el momento de la ejecución.

Esta es una compensación, y no siempre vale la pena tomarla. Sin embargo, la compilación AOT puede brillar en ciertos casos de uso, como los siguientes:

Aplicaciones de corta duración en las que el tiempo de inicio es importante
Entornos con alta restricción de memoria en los que JIT podría ser demasiado costoso
Como dato curioso, la compilación AOT se introdujo como una función experimental en JDK 9, aunque esta implementación era costosa y nunca se llegó a recuperar, por lo que se quitó de manera discreta en Java 17 a favor de los desarrolladores que solo usan GraalVM.

## GraalVM :
GraalVM es una distribución de JDK de código abierto altamente optimizada que ofrece tiempos de inicio muy rápidos, compilación de imágenes nativas AOT y capacidades políglotas que les permiten a los desarrolladores mezclar varios lenguajes en una sola aplicación.

GraalVM está en desarrollo activo, ganando nuevas capacidades y mejorando las existentes todo el tiempo, por eso alento a los desarrolladores a mantenerse al tanto.

Estos son algunos de los logros más recientes:

Resultado de compilación de una imagen nativa nueva y fácil de usar ( 18/01/2021)
Compatibilidad con Java 17 ( 18/01/2022)
Habilita la compilación de varios niveles de forma predeterminada para mejorar los tiempos de compilación de Polyglot ( 20/04/2021)

##Spring Native: 
En pocas palabras, Spring Native permite usar el compilador de imágenes nativas de GraalVM para convertir aplicaciones de Spring en archivos ejecutables nativos.

Este proceso implica realizar un análisis estático de tu aplicación en el tiempo de compilación para encontrar todos los métodos en tu aplicación a los que se puede acceder desde el punto de entrada.

Básicamente, esto crea una concepción de "mundo cerrado" de tu aplicación, en la que se supone que se conoce todo el código en el tiempo de compilación y no se permite cargar ningún código nuevo durante el tiempo de ejecución.

Es importante tener en cuenta que la generación de imágenes nativas es un proceso que requiere mucha memoria y que lleva más tiempo que la compilación de una aplicación regular e impone [limitaciones](https://www.graalvm.org/22.0/reference-manual/native-image/Limitations/) para ciertos aspectos de Java.

En algunos casos, no se requieren cambios en el código para que una aplicación funcione con Spring Native. Sin embargo, algunas situaciones requieren una configuración nativa específica para funcionar correctamente. En esas situaciones, Spring Native suele proporcionar [sugerencias nativas](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#native-hints) para simplificar este proceso.

Nota: Podemos generar nuestro código inicial de spring desde [spring initializr](https://start.spring.io/)
##Configura nuestros repositorios de Maven: 
Como este proyecto aún se encuentra en la fase experimental, tendremos que configurar nuestra app para encontrar artefactos experimentales, que no están disponibles en el repositorio central de Maven.

Para ello, deberá agregar los siguientes elementos a nuestro archivo pom.xml. Puede hacerlo en el editor que prefiera.

Agregue las siguientes secciones y los complementos de pluginRepositories a nuestro pom:


*<repositories>
    <repository>
        <id>spring-release</id>
        <name>Spring release</name>
        <url>https://repo.spring.io/release</url>
    </repository>
</repositories>*

*<pluginRepositories>
    <pluginRepository>
        <id>spring-release</id>
        <name>Spring release</name>
        <url>https://repo.spring.io/release</url>
    </pluginRepository>
</pluginRepositories>*

##Cómo agregar nuestras dependencias:
Luego, agregue la dependencia nativa de resorte, que es necesaria para ejecutar una aplicación de Spring como imagen nativa. Nota: Este paso no es necesario si usas Gradle.


*<dependencies>
    <!-- ... -->
    <dependency>
        <groupId>org.springframework.experimental</groupId>
        <artifactId>spring-native</artifactId>
        <version>0.11.2</version>
    </dependency>
</dependencies>*

##Cómo agregar y habilitar nuestros complementos:
Ahora, agrega el complemento AOT para mejorar la huella y la compatibilidad de las imágenes nativas ( más información):


*<plugins>
    <!-- ... -->
    <plugin>
        <groupId>org.springframework.experimental</groupId>
        <artifactId>spring-aot-maven-plugin</artifactId>
        <version>0.11.2</version>
        <executions>
            <execution>
                <id>generate</id>
                <goals>
                    <goal>generate</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
</plugins>*
Ahora, actualizaremos el complemento spring-boot-maven para habilitar la compatibilidad con imágenes nativas y usaremos el compilador Paketo para compilar nuestra imagen nativa:


*<plugins>
    <!-- ... -->
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
            <image>
                <builder>paketobuildpacks/builder:tiny</builder>
                <env>
                    <BP_NATIVE_IMAGE>true</BP_NATIVE_IMAGE>
                </env>
            </image>
        </configuration>
    </plugin>
</plugins>*

Ten en cuenta que la imagen [tiny compilador](https://github.com/paketo-buildpacks/tiny-stack-release) es solo una de varias opciones. Es una buena opción para nuestro caso de uso, ya que tiene muy pocas bibliotecas y utilidades adicionales, lo que ayuda a minimizar nuestra superficie de ataque.

Por ejemplo, si compilaste una app que necesitaba acceso a algunas bibliotecas comunes de C o si no estabas seguro de los requisitos de tu app, el [compilador completo](https://github.com/paketo-buildpacks/full-builder) podría ser la mejor opción.

##Cómo compilar y ejecutar una app nativa:
Para compilar la imagen, sigue estos pasos:


mvn spring-boot:build-image
Una vez que esté compilado, ya podemos ver a la aplicación nativa en acción.

Para ejecutar nuestra app:


*docker run --rm -p 8080:8080 demo:0.0.1-SNAPSHOT*
En este punto, nos encontramos en una excelente posición para ver ambos lados de la ecuación de la aplicación nativa.

Dedicamos un poco de tiempo y uso adicional de memoria en el tiempo de compilación, pero a cambio, obtenemos una aplicación que puede iniciarse mucho más rápido y consumir mucha menos memoria (según la carga de trabajo).

También debemos tener en cuenta que, en casos de uso más complejos, se requieren modificaciones adicionales para informar al compilador de AOT lo que hará la app en el tiempo de ejecución. Por este motivo, ciertas cargas de trabajo predecibles (como los trabajos por lotes) pueden ser muy adecuadas para esto, mientras que otras pueden ser más grandes.

