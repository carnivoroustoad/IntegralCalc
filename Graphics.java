import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.control.*;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.chart.*;

/**
 * Runs the graphics and input for the integral function
 *
 * @author Ryan Sundermeyer
 * @version 5/24/2022
 */
public class Graphics extends Application
{
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    //instance variables
    public GridPane pane = new GridPane();
    final TextField function = new TextField();
    final TextField bounds = new TextField();
    final TextField order = new TextField();
    final Slider slider = new Slider();
    Stage stage = new Stage();
    /*
     * @param Stage tStage temporary stage placeholder
     */
    @Override
    public void start(Stage tStage)
    {
        stage = tStage;
        
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setMinSize(500, 500);
        pane.setVgap(10);
        pane.setHgap(10);
        
        //PRE GRAPH OUTPUT
        Label title = new Label("INTEGRAL CALCULATOR");
        pane.add(title, 0, 0);
        Label prompt = new Label("Enter function (m * x1 + b)");
        pane.add(prompt, 0, 1);
        Label boundPrompt = new Label("Enter bounds (start, end)");
        pane.add(boundPrompt, 1, 1);
        Label orderPrompt = new Label("Enter the order");
        pane.add(orderPrompt, 2, 1);
        
        //text boxes
        pane.add(function,0,2);
        pane.add(bounds,1,2);
        pane.add(order, 2, 2);
        
        //slider
        Label sliderLabel = new Label("Accuracy Slider:");
        pane.add(sliderLabel,0,3);
        Label slideComment = new Label("         Makes sure to enter your bounds in the correct order.");
        pane.add(slideComment, 1, 4);
        
        slider.setMin(0);
        slider.setMax(10000);
        slider.setValue(1000);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1000);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(100);
        pane.add(slider, 0, 4);
        
        //button
        Button enter = new Button("Enter");
        pane.add(enter, 3, 2);
        enter.setOnAction(this::clickEnter);
        
        //Input Description Labels
        Label l1 = new Label("To enter your function, place a space between each unit.\nWrite out all operations (not implicit).\nEach subsequent x-value must be written with what\nnumber of x it is starting from 1 (ex: x1)\n\nINPUT EXAMPLE:\n3 * x1 ^ 2 + x2 / 3 + 1");
        Label l2 = new Label("Make sure to enter 2 bounds for every order.\nFor example, if your order is 3 you need\n3 pairs of bounds. You can even enter\nfunctions as bounds.\n\n\n0, 1, 2, 3");
        Label l3 = new Label("Enter a single integer for your order.\nOrder means the number of integrals you are taking.\n\n\n\n\n2\n\n\n");
        pane.add(l1, 0, 5);
        pane.add(l2, 1, 5);
        pane.add(l3, 2, 5);
            
        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(pane, 1000,600);
        stage.setTitle("INPUT PAGE");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();
    }
    
    /*
     * @param ActionEvent event action event used for button click detection
     */
    private void clickEnter(ActionEvent event)
    {
        //save the input
        final String FXN = function.getText();
        final ArrayList<ArrayList<String>> BOUNDS = Eval.getBoundsArray(bounds.getText());
        final int ORDER = Integer.parseInt(order.getText());
        final int SLICES = (int) Math.pow((double)slider.getValue(), 1.0 / ORDER); //makes the tool take less slices for higher order integrals to prevent lag
        ArrayList<Double> emptyArr = new ArrayList<Double>();
        
        //clear the screen
        pane.getChildren().clear();
        stage.setTitle("SOLUTION");
        
        //write error message which gets overwritten if the code works
        Label eval = new Label("Error - invalid input");
        pane.add(eval,0,1);
        
        //call integral method
        double integral = Eval.integral(ORDER, Eval.convertToArrayList(FXN), BOUNDS, SLICES, emptyArr);
        eval.setText("INTEGRAL VALUE:  " + Eval.round(integral, 3)); //sets the error text to the integral if it actually runs
        Label userInput = new Label("Function: " + FXN + "\nBounds: " + bounds.getText() + "\nOrder: " + order.getText() + "\nSlices: " + SLICES);
        pane.add(userInput, 0, 0);
    }
}
