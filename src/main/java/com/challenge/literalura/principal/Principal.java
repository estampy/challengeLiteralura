package com.challenge.literalura.principal;

import com.challenge.literalura.model.*;
import com.challenge.literalura.repository.AutorRepository;
import com.challenge.literalura.repository.LibroRepository;
import com.challenge.literalura.service.ConsumoAPI;
import com.challenge.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final ConvierteDatos conversor = new ConvierteDatos();

    private List<DatosLibro> datosLibro;
    private List<Libro> libro;
    private List<Autor> autores;
    private Optional<Libro> libroBuscado;

    private final LibroRepository repositorio;
    private final AutorRepository autorRepository;

    @Autowired
    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repositorio = repository;
        this.autorRepository = autorRepository;
    }

    public void mostrarDatos() {
        System.out.println(consumoAPI.obtenerDatos(URL_BASE));
    }

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por nombre
                    2 - Mostrar autores guardados
                    3 - Mostrar libros guardados
                    4 - Buscar autores vivos por año
                    5 - Mostrar libros por idioma
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarAutoresGuardados();
                    break;
                case 3:
                    mostrarLibrosGuardados();
                    break;
                case 4:
                    mostrarAutoresVivosPorFecha();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    public String mostrarIdiomas() {
        var idioma = "";
        var menu = """
                ---------------------------------
                Ingrese el idioma para buscar los libros: 
                
                1 - [es] -> Español
                2 - [en] -> Inglés
                3 - [fr] -> Frances
                4 - [pr] -> Portugués
                ---------------------------------
                """;
        System.out.println(menu);
        return teclado.nextLine().replace(" ", "").toLowerCase();
    }

    private DatosLibro getDatosLibro() {
        try {
            System.out.println("Escriba el nombre del libro que desea buscar");
            var nombreLibro = teclado.nextLine().trim();

            if (nombreLibro.isEmpty()) {
                System.out.println("Ingrese un Nombre valido.");
                return null;
            }

            var url = URL_BASE + "?search=" + nombreLibro.replace(" ", "+");
            var json = consumoAPI.obtenerDatos(url);

            if (json == null || json.isEmpty()) {
                System.out.println("Ocurrio un ERROR inesperado.");
                return null;
            }

            var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

            if (datosBusqueda == null || datosBusqueda.libro() == null || datosBusqueda.libro().isEmpty()) {
                System.out.println("No se encontro el libro: " + nombreLibro + ".");
                return null;
            }

            Optional<DatosLibro> libroBuscado = datosBusqueda.libro().stream()
                    .filter(l -> l.titulo() != null && l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                    .findFirst();

            return libroBuscado.orElse(null);
        } catch (Exception e) {
            System.out.println("Ocurrió un error al buscar el libro. Por favor, intente nuevamente.");
            return null;
        }
    }

    private void buscarLibro() {
        DatosLibro libros = getDatosLibro();
        if (libros == null) {
            System.out.println("Regresando al menú principal.");
            return;
        } else {
            System.out.println("Libro encontrado: " + libros);
        }

        String idioma = libros.idiomas().isEmpty() ? "Idioma desconocido" : libros.idiomas().get(0);
        Double numeroDeDescargas = libros.numeroDeDescargas();

        for (DatosAutor datosAutor : libros.autores()) {
            Autor autor = autorRepository.findByNombreIgnoreCase(datosAutor.nombre())
                    .orElseGet(() -> {
                        Autor nuevoAutor = new Autor();
                        nuevoAutor.setNombre(datosAutor.nombre());
                        nuevoAutor.setFechaNacimiento(datosAutor.fechaNacimiento());
                        nuevoAutor.setFechaMuerte(datosAutor.fechaMuerte());
                        autorRepository.save(nuevoAutor);
                        return nuevoAutor;
                    });
            Libro libro = new Libro();
            libro.setTitulo(libros.titulo());
            libro.setIdiomas(idioma);
            libro.setNumeroDeDescargas(numeroDeDescargas);
            libro.setAutor(autor);

            repositorio.save(libro);

            System.out.println("Libro guardado: " + libros);
        }

    }

    private void mostrarAutoresGuardados() {
        autores = autorRepository.findAll();

        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void mostrarLibrosGuardados() {
        libro = repositorio.findAll();

        libro.stream()
                .sorted(Comparator.comparing(Libro::getIdiomas))
                .forEach(System.out::println);
    }

    private void mostrarAutoresVivosPorFecha() {
        System.out.println("Ingrese el año para mostrar autores vivos en esa fecha");
        var year = teclado.nextInt();
        teclado.nextLine();
        autores = autorRepository.findAliveInYear(year);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año" + year);
        } else {
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNombre))
                    .forEach(System.out::println);
        }
    }

    private void mostrarLibrosPorIdioma() {
        var idioma = mostrarIdiomas();
        libro = repositorio.findByIdiomasContainingIgnoreCase(idioma);

        if (libro.isEmpty()) {
            System.out.println("No se encontraron libros con el idioma seleccionado en la base de datos");
        } else {
            libro.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(System.out::println);
        }
    }
}
