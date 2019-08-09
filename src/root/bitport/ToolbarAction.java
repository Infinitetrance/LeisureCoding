package root.bitport;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ToolbarAction {

    private EventHandler<ActionEvent> onAction;

    private String iconText;
    private Node graphic;

    public ToolbarAction(EventHandler<ActionEvent> onAction, String iconText,Node graphic) {
        this.onAction = onAction;
        this.iconText = iconText;
        this.graphic=graphic;
    }

    public EventHandler<ActionEvent> getOnAction() {
        return onAction;
    }

    public String getIconText() {
        return iconText;
    }
    public Node getGraphic()
    {
    	return graphic;
    }
}
