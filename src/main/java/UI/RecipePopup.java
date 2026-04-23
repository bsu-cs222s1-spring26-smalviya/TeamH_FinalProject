package UI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RecipePopup {

    public static void display(String title, String details) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        TextFlow flow = buildStyledText(details);

        ScrollPane scroll = new ScrollPane(flow);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(500);

        Button close = new Button("Close");
        close.setOnAction(e -> window.close());

        VBox layout = new VBox(10, scroll, close);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 500, 600);
        window.setScene(scene);
        window.showAndWait();
    }

    private static TextFlow buildStyledText(String details) {
        TextFlow flow = new TextFlow();
        flow.setLineSpacing(4);

        while (details.contains("[RED]")) {
            int start = details.indexOf("[RED]");
            int end = details.indexOf("[/RED]");

            if (end == -1) break;

            String before = details.substring(0, start);
            String redText = details.substring(start + 5, end);
            String after = details.substring(end + 6);

            if (!before.isEmpty()) {
                flow.getChildren().add(new Text(before));
            }

            Text red = new Text(redText);
            red.setFill(Color.RED);
            red.setFont(Font.font("System", FontWeight.BOLD, 14));
            flow.getChildren().add(red);

            details = after;
        }

        if (!details.isEmpty()) {
            flow.getChildren().add(new Text(details));
        }

        return flow;
    }
}
