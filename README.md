  Sean bienvenidos a mi proyecto ChallengeLiterAlura.
  Mi nombre es Estanislao Torres Campi, soy de Tucumán, Argentina. Soy un apasionado de la programación que esta 
aprendiendo un lenguaje nuevo (para mi) que es JAVA. A continuación, les paso a explicar sobre este proyecto.
  Este es un proyecto que forma parte de la formación de Alura junto con Oracle llamado ONE.
  En este proyecto vamos a utilizar el lenguaje de programación “JAVA” junto con la utilización de lambdas, stream y Spring 
Framework. Usando persistencia de datos y consultas Spring Data JPA. Como base de datos usamos PostgreSQL y como API usamos https://gutendex.com/books/ 
el cual nos brinda un catalogo de mas de 70.000 libros para hacer las consultas que necesitemos sin costo alguno.
  Este proyecto es una aplicación de consola ya que este curso es sobre Back-End. En el mismo nos vamos a encontrar con un menú enumerado 
del 1 al 5 en el cual nos solicita ingresar un numero según la tarea que deseemos realizar.

1 - Buscar libro por nombre
2 - Mostrar autores guardados
3 - Mostrar libros guardados
4 - Buscar autores vivos por año
5 - Mostrar libros por idioma

0 - Salir

  En la opción 1 (uno) podemos buscar un libro en la API colocando el nombre del mismo. 
Esto nos mostrara un mensaje que se encontró el libro y que se guardó con éxito en la base de datos.
  En la opción 2 (dos) nos muestra los datos de los autores que hemos buscado previamente con la 
opción número 1 y que fueron guardados en la base de datos
  En la opción 3 (tres) nos muestra los datos de los libros que hemos buscado previamente con la 
opción número 1 y que fueron guardados en la base de datos
  En la opción 4 (cuatro) nos permite buscar por año en el que el autor estaba vivo, los cuales se 
buscaran en la base de datos que cargamos manualmente con la opción número 1.
  En la opción 5 (cinco) nos permite buscar mediante el idioma que nos muestra a continuación:
        """
        ---------------------------------
        Ingrese el idioma para buscar los libros: 
        
        1 - [es] -> Español
        2 - [en] -> Inglés
        3 - [fr] -> Frances
        4 - [pr] -> Portugués
        ---------------------------------
        """

  En este caso nos mostrara todos los libros que estén cargados en la base de datos con el idioma que seleccionamos.

  Muchas gracias por si atencion.
