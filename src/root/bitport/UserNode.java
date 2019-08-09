package root.bitport;

import java.io.File;
import java.net.InetAddress;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

class UserNode extends HBox {
	// Contains GUI node representation of online users
	private static VBox container;
	// Reference to side content pane in download page to show shared files by this
	// user
	private static ContentPane showSharedFilesPanel;
	// to open and close by sliding side content pane in download page
	private static TranslateTransition openShowSharedFilesPanel, closeShowSharedFilesPanel;
	// intial translateX of side content pane of download page
	private static double closedTransaltX;
	// denotes whether side content pane is opened by user node or not
	public static boolean isShowSharedFilesPanelOpened;
	// user node responsible for opening ShowSharedFilesPanel i.e is side panel
	private static Node openingNode;

	// background colors of this GUI node when unfocused and focused respectively
	private static final String background = "white";
	private static final String focusedBackgound = "lightgray";
	private static final Color sharedFileIconColor = Color.ROYALBLUE;
	private static final Color sharedFileIconColorOnClicked = Color.DEEPPINK;
	private static final Color downloadButtonColor = Color.DEEPPINK;

	private Label name;
	// show files shared by this user button
	private Button showSharedFiles;
	// list of files shared by this user
	private List<File> sharedFileList;
	// ip of this user
	private InetAddress loc;

	static
	{
		UserNode.isShowSharedFilesPanelOpened = false;
	}

	UserNode(InetAddress loc) {
		this.loc = loc;

		this.name = new Label(loc.toString());
		this.name.setFont(new Font(15));
		this.name.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");

		sharedFileList = null;

		Circle showSharedFilesIcon = new Circle(8);
		showSharedFilesIcon.setFill(UserNode.sharedFileIconColor);
		showSharedFiles = new Button();
		showSharedFiles.setGraphic(showSharedFilesIcon);
		showSharedFiles.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");

		HBox nameBox = new HBox(this.name);
		nameBox.setAlignment(Pos.CENTER_LEFT);

		UserNode.setHgrow(nameBox, Priority.ALWAYS);
		this.getChildren().addAll(this.showSharedFiles, nameBox);
		this.setAlignment(Pos.CENTER_LEFT);
		this.setPadding(new Insets(5, 15, 5, 15));

		this.setStyle("-fx-background-color:" + background + "");
		nameBox.setStyle("-fx-background-color:" + background + "");

		this.setFocusTraversable(true);
		this.focusedProperty().addListener(i ->
		{
			if (UserNode.this.isFocused())
			{
				UserNode.this.setStyle("-fx-background-color:" + focusedBackgound + "");
				nameBox.setStyle("-fx-background-color:" + focusedBackgound + "");
			}

			else
			{
				UserNode.this.setStyle("-fx-background-color:" + background + "");
				nameBox.setStyle("-fx-background-color:" + background + "");
			}

		});

		this.showSharedFiles.setFocusTraversable(false);
		this.showSharedFiles.setOnAction(action ->
		{

			// out.println("UserNode showSharedFiles.setOnAction :task ->open shared files
			// view by this user");
			if (!UserNode.isShowSharedFilesPanelOpened && !Downloader.isOpened)// if isShowSharedFilesPanelOpened is not
																				// opened and downloader panel is also
																				// not opened
			{ // than open slider i.e is ShowSharedFilesPanel in this case
				((Circle) UserNode.this.showSharedFiles.getGraphic()).setFill(sharedFileIconColorOnClicked);
				UserNode.openingNode = UserNode.this;
				UserNode.isShowSharedFilesPanelOpened = true;
				UserNode.openShowSharedFilesPanel.play();
				/**
				 * to avoid flickering nodes are added to ShowSharedFilesPanel after completion
				 * of translation
				 */
				UserNode.openShowSharedFilesPanel.setOnFinished(act ->
				{
					for (File f : UserNode.this.getsharedFileList())
					{
						SharedNode node = new SharedNode(f.toString(), null, downloadButtonColor);
						node.setOperationAction(operationAction ->
						{
							// out.println("downloadAction");
							// "UserNode.this" tells which user node created this shared node i.e who shared
							// this file
							UserNode parentof_remoteSharedNode = UserNode.this;
							int index = SharedNode.getRemoteContainer().getChildren().indexOf(node);
							// and now in creator of this remote shared node is a list of shared files by
							// remote user i.e "private List<File> sharedFileList"
							//// file which local user has requested to download
							File locator = parentof_remoteSharedNode.getsharedFileList().get(index);
							// add to downloading list this file
							Downloader.getDownloading()
									.add(new Downloader(locator.toString(), locator, UserNode.this.loc));
						});
						SharedNode.getRemoteContainer().getChildren().add(node);
					}
				});
			} else if (UserNode.isShowSharedFilesPanelOpened && (UserNode.openingNode.equals(UserNode.this)))// whether
																												// opening
																												// node
																												// and
																												// closing
																												// node
																												// are
																												// same
			{
				((Circle) UserNode.this.showSharedFiles.getGraphic()).setFill(UserNode.sharedFileIconColor);

				SharedNode.getRemoteContainer().getChildren().clear();// clear than play to avoid flickering
				// out.println("close show shared file
				// SharedNode.getRemoteContainer().getChildren().size()::"+SharedNode.getRemoteContainer().getChildren().size());
				UserNode.isShowSharedFilesPanelOpened = false;
				UserNode.closeShowSharedFilesPanel.play();
			}
		});
	}

	public List<File> getsharedFileList() {
		return sharedFileList;
	}

	public void setsharedFileList(List<File> sharedFileList) {
		this.sharedFileList = sharedFileList;
	}

	public static VBox getContainer() {
		return container;
	}

	public static void setContainer(VBox container) {
		UserNode.container = container;
	}

	public static ContentPane getShowSharedFilesPanel() {
		return showSharedFilesPanel;
	}

	public static void setShowSharedFilesPanel(ContentPane showSharedFilesPanel) {
		UserNode.showSharedFilesPanel = showSharedFilesPanel;
		UserNode.closedTransaltX = showSharedFilesPanel.getTranslateX();

		openShowSharedFilesPanel = new TranslateTransition();
		openShowSharedFilesPanel.setToX(100);
		openShowSharedFilesPanel.setDuration(new Duration(500));
		openShowSharedFilesPanel.setNode(UserNode.showSharedFilesPanel);

		closeShowSharedFilesPanel = new TranslateTransition();
		closeShowSharedFilesPanel.setToX(UserNode.closedTransaltX);
		closeShowSharedFilesPanel.setDuration(new Duration(500));
		closeShowSharedFilesPanel.setNode(UserNode.showSharedFilesPanel);
	}
}