import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Evaluador {
    ArrayList<ArrayList<String>> datos;
    ArrayList<String> clases;

    Float precisionModelo = 0.0f;
    Float recallModelo = 0.0f;
    Float accuracyModelo = 0.0f;
    Float fscoreModelo = 0.0f;

    Evaluador(String archivo, Integer numeroClases){
        datos = new ArrayList<ArrayList<String>>();
        clases = new ArrayList<String>();
        BufferedReader bufferedReader;
        String line;
        
        try {
            bufferedReader = new BufferedReader(new FileReader(archivo));
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return;
        }

        do {
            try {
                line = bufferedReader.readLine();
                if (line != null) {
                    datos.add(new ArrayList<String>(Arrays.asList(line.split(","))));
                }
            } catch (IOException e) {
                System.out.println(e);
                return;
            }
        } while (line != null);

        try {
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        obtenerClases(numeroClases);
        System.out.println(clases);
    }

    void obtenerClases(Integer numeroClases){
        for (int i = 1; i < datos.size(); i++) {
            if(!clases.contains(datos.get(i).get(1)))
                clases.add(datos.get(i).get(1));

            if(clases.size() >= numeroClases)
                return;
        }
    }

    void evaluarPredicciones(){

        FileWriter writer;
        FileWriter writerDesempeno;
        File evaluacionesCSV = new File("evaluaciones.csv");
        File desempenoCSV = new File("desempeno.csv");

        if(evaluacionesCSV.exists()){
            evaluacionesCSV.delete();
        }

        if(desempenoCSV.exists()){
            desempenoCSV.delete();
        }

        try {
            writer = new FileWriter(evaluacionesCSV);
        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        try {
            writerDesempeno = new FileWriter(desempenoCSV);
        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        for (int i = 0; i < clases.size(); i++) {
            float sumTP = 0;
            float sumTN = 0;
            float sumFP = 0;
            float sumFN = 0;
            try {
                writer.write("Evaluación para la clase: " + clases.get(i) + "\n");
                writer.write("ID,Evaluación\n");
            } catch (IOException e) {
                System.out.println(e);
            }
            
            for (int j = 1; j < datos.size(); j++) {
                int id = Integer.parseInt(datos.get(j).get(0));
                String real = datos.get(j).get(1);
                String predecida = datos.get(j).get(2);

                if(predecida.equals(clases.get(i)) && predecida.equals(real)){
                    sumTP++;
                    try {
                        writer.write(id + ",TP\n");
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }else if(predecida.equals(clases.get(i)) && !predecida.equals(real)){
                    sumFP++;
                    try {
                        writer.write(id + ",FP\n");
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }else if(!predecida.equals(clases.get(i)) && !real.equals(clases.get(i))){
                    sumTN++;
                    try {
                        writer.write(id + ",TN\n");
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }else if(!predecida.equals(clases.get(i)) && real.equals(clases.get(i))){
                    sumFN++;
                    try {
                        writer.write(id + ",FN\n");
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }

            Float precision = 0.0f;
            Float recall = 0.0f;
            Float accuracy = 0.0f;
            Float fscore = 0.0f;

            try {
                precision = ((sumTP)/(sumTP+sumFP));
                recall = ((sumTP)/(sumTP + sumFN));
                accuracy = ((sumTP + sumTN)/(sumTP + sumTN + sumFP + sumFN));
                fscore = 2 * ((precision * recall)/(precision + recall));

                precisionModelo += precision;
                recallModelo += recall;
                accuracyModelo += accuracy;
                fscoreModelo += fscore;
            } catch (ArithmeticException e) {
                
            }

            try {
                writerDesempeno.write("Desempeño clase " + clases.get(i) + "\n");
                writerDesempeno.write("Sum TP:," + sumTP + ",Sum TN:," + sumTN + ",Sum FP:," + sumFP + ",Sum FN:," + sumFN + "\n");
                writerDesempeno.write("Precision:," + precision + ",Recall:," + recall + ",Accuracy:," + accuracy + ",F-Score:," + fscore + "\n");
            } catch (IOException e) {
                System.out.println(e);
            }
        }


        precisionModelo /= clases.size();
        recallModelo /= clases.size();
        accuracyModelo /= clases.size();
        fscoreModelo /= clases.size();

        try {
            writerDesempeno.write("Desempeño general del modelo\n");
            writerDesempeno.write("Precision:," + precisionModelo + ",Recall:," + recallModelo + ",Accuracy:," + accuracyModelo + ",F-Score:," + fscoreModelo + "\n");
        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            writerDesempeno.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
