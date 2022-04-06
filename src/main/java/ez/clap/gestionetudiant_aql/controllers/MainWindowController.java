package ez.clap.gestionetudiant_aql.controllers;

import ez.clap.gestionetudiant_aql.MainWindow;
import ez.clap.gestionetudiant_aql.entities.Course;
import ez.clap.gestionetudiant_aql.entities.Student;
import ez.clap.gestionetudiant_aql.utilities.Data;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowController {

    enum Action{
        CREATE_STUDENT,
        EDIT_STUDENT,
        DELETE_STUDENT,
        SHOW_GRADES,
        CREATE_COURSE,
        EDIT_COURSE,
        DELETE_COURSE
    }

    @FXML
    public Button buttonAddStudent, buttonEditStudent, buttonDeleteStudent, buttonAddCourse, buttonEditCourse, buttonDeleteCourse;
    @FXML
    public TableView<Student> tableViewStudent;
    @FXML
    public TableView<Course> tableViewCourse;
    @FXML
    public TableColumn<Student,String> tableColumnFirstName, tableColumnSecondName, tableColumnNumber;
    @FXML
    public TableColumn<Student, ComboBox<String>> tableColumnCourse;
    @FXML
    public TableColumn<Course,String> tableColumnCourseTitle,tableColumnCourseNumber,tableColumnCourseCode;


    @FXML
    private void onButtonAddStudentClick() throws IOException {
        Stage stage = setupStudentStage(Action.CREATE_STUDENT);
        stage.show();
    }


    @FXML
    private void onButtonEditStudentClick() throws IOException {
        Stage stage = setupStudentStage(Action.EDIT_STUDENT);
        stage.show();
    }

    @FXML
    private void onButtonDeleteStudentClick() throws IOException {
        Stage stage = setupStudentStage(Action.DELETE_STUDENT);
        stage.show();
    }

    @FXML
    private void onButtonAddCourseClick() throws IOException {
        Stage stage = setupCourseStage(Action.CREATE_COURSE, "manage-course-window.fxml");
        stage.show();
    }

    @FXML
    private void onButtonEditCourseClick() throws IOException {
        Stage stage = setupCourseStage(Action.EDIT_COURSE, "manage-course-window.fxml");
        stage.show();
    }

    @FXML
    private void onButtonDeleteCourseClick() throws IOException {
        Stage stage = setupCourseStage(Action.DELETE_COURSE, "delete-warning-window.fxml");
        stage.show();
    }



    private Stage setupStudentStage(Action action) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("manage-student-window.fxml"));
        Parent root; //= loader.load();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        // TODO: Ajouter dans le CourseListView la liste de tous les cours disponible et implementer dans l'objet etudiant seulement eu qui son cocher

        switch(action){
            // switch pour setup la fenetre
            case EDIT_STUDENT:{
                root = loader.load();
                stage.setTitle("Modifier l'étudiant");
                ManageStudentController manageStudentController = loader.getController();
                manageStudentController.loadStudent(tableViewStudent.getSelectionModel().getSelectedItem());
                manageStudentController.buttonConfirm.setText("Modifier");
                manageStudentController.buttonConfirm.setOnAction(event -> {
                    if(isStudentFieldsValid(manageStudentController)){
                        removeSelectedStudent(manageStudentController);
                        addStudentFromManageStudentController(manageStudentController);
                        closeWindow(manageStudentController.buttonConfirm);
                    }else{
                        showWarningPopup("Erreur", "Information manquante", "OK");
                    }
                });
                break;
            }
            case CREATE_STUDENT:{
                stage.setTitle("Creer un étudiant");
                root = loader.load();
                ManageStudentController manageStudentController = loader.getController();

                manageStudentController.buttonConfirm.setOnAction(event -> {
                    if(isStudentFieldsValid(manageStudentController)){
                        addStudentFromManageStudentController(manageStudentController);

                        closeWindow(manageStudentController.buttonConfirm);
                    }else {
                        showWarningPopup("Erreur", "Information manquante!", "OK");
                    }
                });
                break;
            }
            case DELETE_STUDENT:{
                loader = new FXMLLoader(MainWindow.class.getResource("delete-warning-window.fxml"));
                root = loader.load();

                stage.setTitle("Attention!");
                DeleteWarningController deleteWarningController = loader.getController();
                Student selectedStudent = tableViewStudent.getSelectionModel().getSelectedItem();
                deleteWarningController.labelStudentName.setText(selectedStudent.getFirstName() + " " + selectedStudent.getSecondName());

                deleteWarningController.buttonConfirm.setOnAction(event->{
                    Data.getStudentList().remove(selectedStudent);
                    tableViewStudent.getItems().remove(selectedStudent);
                    closeWindow(deleteWarningController.buttonConfirm);
                });
                break;
            }
            default:{
                loader = new FXMLLoader(getClass().getResource("manage-student-window.fxml"));
                root = loader.load();
            }
        }

        stage.setScene(new Scene(root));
        return stage;
    }

    private Stage setupCourseStage (Action action, String resource) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource(resource));
        Parent root = loader.load();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        switch (action){
            case CREATE_COURSE:{
                stage.setTitle("Creer un cours");
                ManageCourseController manageCourseController = loader.getController();

                manageCourseController.buttonConfirm.setOnAction(event ->{
                    if(isCourseFieldsValid(manageCourseController)){
                        addCourseFromManageCourseController(manageCourseController);
                        closeWindow(manageCourseController.buttonConfirm);
                    }
                    else{
                        showWarningPopup("Erreur", "Information manquante","OK");
                    }
                });
                break;
            }
            case EDIT_COURSE:{
                stage.setTitle("Modifier le cours");
                ManageCourseController manageCourseController = loader.getController();
                manageCourseController.loadCourse(tableViewCourse.getSelectionModel().getSelectedItem());
                manageCourseController.buttonConfirm.setText("Modifier");
                manageCourseController.buttonConfirm.setOnAction(event -> {
                    if(isCourseFieldsValid(manageCourseController)){
                        removeSelectedCourse(manageCourseController);
                        addCourseFromManageCourseController(manageCourseController);
                        closeWindow(manageCourseController.buttonConfirm);
                    }
                    else{
                        showWarningPopup("Erreur", "Information manquante", "OK");
                    }
                });
                break;
            }
            // TODO: vérifier le nom du controller et des attributs
            case DELETE_COURSE:{
                stage.setTitle("Attention!");
                DeleteWarningController deleteCourseController = loader.getController();
                Course selectedCourse = tableViewCourse.getSelectionModel().getSelectedItem();
                deleteCourseController.labelStudentName.setText(selectedCourse.getTitle() + " " + selectedCourse.getCourseNumber());

                deleteCourseController.buttonConfirm.setOnAction(event -> {
                    Data.getCourseList().remove(selectedCourse);
                    tableViewCourse.getItems().remove(selectedCourse);
                    closeWindow(deleteCourseController.buttonConfirm);
                });
                break;
            }
        }

        stage.setScene(new Scene(root));
        return stage;
    }



    private boolean isStudentFieldsValid(ManageStudentController manageStudentController){
        return !manageStudentController.textFieldFirstName.getText().isEmpty() &&
                !manageStudentController.textFieldSecondName.getText().isEmpty() &&
                !manageStudentController.textFieldNumber.getText().isEmpty();
    }

    private void showWarningPopup(String title, String warningMessage, String buttonText){
        Dialog dialog = new Dialog();
        dialog.setTitle(title);
        dialog.setContentText(warningMessage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        ButtonType closeButton = new ButtonType(buttonText, ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        dialog.show();
    }

    private void addStudentFromManageStudentController(ManageStudentController manageStudentController){
        Student student = new Student(manageStudentController.textFieldFirstName.getText(),
                manageStudentController.textFieldSecondName.getText(),
                manageStudentController.textFieldNumber.getText());
        Data.getStudentList().add(student);
        tableViewStudent.getItems().setAll(Data.getStudentList());
    }

    private void removeSelectedStudent(ManageStudentController manageStudentController){
        Student selectedStudent = tableViewStudent.getSelectionModel().getSelectedItem();
        Data.getStudentList().remove(selectedStudent);
        tableViewStudent.getItems().remove(selectedStudent);
    }

    // TODO: enlever la répétition
    private  boolean isCourseFieldsValid(ManageCourseController manageCourseController){
        return !manageCourseController.textFieldNumber.getText().isEmpty() &&
                !manageCourseController.textFieldCode.getText().isEmpty() &&
                !manageCourseController.textFieldTitle.getText().isEmpty();
    }

    private  void addCourseFromManageCourseController(ManageCourseController manageCourseController){
        Course course = new Course(manageCourseController.textFieldTitle.getText(),
                manageCourseController.textFieldCode.getText(),
                manageCourseController.textFieldNumber.getText());
        Data.getCourseList().add(course);
        tableViewCourse.getItems().setAll(Data.getCourseList());
    }

    private void removeSelectedCourse(ManageCourseController manageCourseController){
        Course selectedCourse = tableViewCourse.getSelectionModel().getSelectedItem();
        Data.getCourseList().remove(selectedCourse);
        tableViewCourse.getItems().remove(selectedCourse);
    }




    private void closeWindow(Control control){
        ((Stage)control.getScene().getWindow()).close();
    }


}