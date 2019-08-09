package root.bitport;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
/**
 * this class is used as GUI node representation of shared file by local user in shared page 
 * as well as by remote user in download page
 */
class SharedNode extends HBox
{
	//contains GUI node representation of shared files by local user in shared page
	private static VBox nativeContainer;
	//contains GUI node representation of shared files by remote user whose "showSharedFiles" button is clicked in download page  
	private static VBox remoteContainer;
	
	private Label fileName;
	//operation acts as delete button in shared page and download button in download page 
	private Button operation;
	
	private static final String background="white";
	private static final String focusedBackgound="lightgray";
	
	private EventHandler<ActionEvent> operationAction;
	
	SharedNode(String fileName,EventHandler<ActionEvent> operation,Color operationIconColor)
	{
		this.fileName=new Label(fileName);
		this.fileName.setFont(new Font(15));
		this.fileName.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
		
		this.operationAction=operation;
		this.operation=new Button();
		this.operation.setFocusTraversable(false);
		this.operation.setGraphic(getOperationIcon(operationIconColor));
		this.operation.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
		this.operation.setOnAction(this.operationAction);
		HBox operationBox=new HBox(this.operation);
		operationBox.setStyle("-fx-background-color:"+background+"");
		operationBox.setAlignment(Pos.CENTER_RIGHT);
		
		this.setFocusTraversable(true);
		this.focusedProperty().addListener(i->{
			if(SharedNode.this.isFocused())
			{
				SharedNode.this.setStyle("-fx-background-color:"+focusedBackgound+"");
				operationBox.setStyle("-fx-background-color:"+focusedBackgound+"");
			}
			
			else
			{
				SharedNode.this.setStyle("-fx-background-color:"+background+"");
				operationBox.setStyle("-fx-background-color:"+background+"");
			}
			
		});
		
		this.setStyle("-fx-background-color:"+background+"");
		this.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(operationBox, Priority.ALWAYS);
		this.setPadding(new Insets(5,15,5,15));
		this.getChildren().addAll(this.fileName,operationBox);
	}
	private Circle getOperationIcon(Color OperationIconColor)
	{
		Circle delIcon=new Circle(8);
		delIcon.setFill(OperationIconColor);
		return delIcon;
	}
	public static VBox getNativeContainer() {
		return nativeContainer;
	}
	public static void setNativeContainer(VBox container) {
		SharedNode.nativeContainer = container;
	}
	public static VBox getRemoteContainer() {
		return remoteContainer;
	}
	public static void setRemoteContainer(VBox remoteContainer) {
		SharedNode.remoteContainer = remoteContainer;
	}
	public void setOperationAction(EventHandler<ActionEvent> operationAction)
	{
		this.operationAction=operationAction;
		this.operation.setOnAction(this.operationAction);
	}
}