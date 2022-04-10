package ez.clap.gestionetudiant_aql.utilities;

import ez.clap.gestionetudiant_aql.entities.Course;
import ez.clap.gestionetudiant_aql.entities.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {
    private static ArrayList<Student> studentList = new ArrayList<>();

    private static ArrayList<Course> courseList = new ArrayList<>();

    private static File mainFile = validateFolder("./Data");

    private static File studentFile = new File(mainFile.getPath() + "/student.txt");

    private static File courseFile = new File(mainFile.getPath() + "/course.txt");



    public static ArrayList<Student> getStudentList(){
        return Data.studentList;
    }

    public static ArrayList<Course> getCourseList(){
        return Data.courseList;
    }

    public static void setStudentList(ArrayList<Student> studentList){
        Data.studentList = studentList;
    }

    public static void setCourseList(ArrayList<Course> courseList){
        Data.courseList = courseList;
    }


    public static void loadDataFromFiles() {
        // Faire une fonction pour ne pas dupliquer le code.
        //Student
        ArrayList<Object> studentObjectList = readObjectListFromFile(studentFile);
        Data.studentList.clear();
        for (Object o : studentObjectList) {
            Data.studentList.add((Student)o);
        }

        // Course
        ArrayList<Object> courseObjectList = readObjectListFromFile(courseFile);
        Data.courseList.clear();
        for(Object o : courseObjectList){
            Data.courseList.add((Course)o);
        }

    }

    public static void saveDataToFiles(){
        writeCourseToFile();
        writeStudentToFile();
    }

    private static File validateFolder(String folderPath) {
        File fileToValidate = new File(folderPath);
        fileToValidate.mkdir();
        return fileToValidate;
    }

    private static void writeStudentToFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Data.studentFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            for(Student student : Data.studentList) {
                objectOutputStream.writeObject(student);
            }

            objectOutputStream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeCourseToFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Data.courseFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            for(Course course : Data.courseList) {
                objectOutputStream.writeObject(course);
            }

            objectOutputStream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Object> readObjectListFromFile(File file){
        ArrayList<Object> objectList = new ArrayList<>();
        if(file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                while (fileInputStream.available() > 0) {
                    objectList.add(objectInputStream.readObject());
                }

                return objectList;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return objectList;
    }


}
