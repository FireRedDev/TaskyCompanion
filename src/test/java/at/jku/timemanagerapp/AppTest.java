///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package at.jku.timemanagerapp;
//
//
//import at.jku.timemanagerapp.testpackage.model.Activity;
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.control.Label;
//import javafx.scene.control.TableView;
//import org.junit.Test;
//import org.loadui.testfx.GuiTest;
//
//import java.io.IOException;
//
//import static org.junit.Assert.assertTrue;
//import static org.loadui.testfx.Assertions.verifyThat;
//import static org.loadui.testfx.controls.Commons.hasText;
//
//
///**
// *
// * @author chris
// */
//public class AppTest extends GuiTest {
//
//
//
//    @Override
//    protected Parent getRootNode() {
//        Parent parent = null;
//        try {
//            parent = FXMLLoader.load(MainApp.class.getResource("RootLayout" + ".fxml"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return parent;
//    }
//
//
//    @Test
//    public void testLabel() throws Exception {
//        final Label label = find( "#filterLabel" );
//        assertTrue(label.getText().equals("Filter Table:"));
//    }
//}
