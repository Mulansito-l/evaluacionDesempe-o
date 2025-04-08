import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el nombre del archivo: ");
        String nombreArchivo = scanner.nextLine();
        System.out.println("Ingrese el número de clases del problema: ");
        Integer numeroClases = scanner.nextInt();

        Evaluador evaluador = new Evaluador(nombreArchivo,numeroClases);
        evaluador.evaluarPredicciones();
    }
}