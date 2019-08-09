package root.bitport;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * front end GUI is handled by this class
 */
class Root {
	// back end networking is handle by this BER
	private BackEndRootx BER;

	private MaterialDesignPane materialPane;
	private StackPane stackPane;

	private FileChooser fileChooser;
	private Stage stage;

	/**
	 * panel is an object containing main controls on top right of a page, that
	 * page's toolbar color ,title ,and the content pane of that page
	 */
	private Panel panels[];
	// side content pane in download page
	private ContentPane sidePanel;

	// vbox which are content node of scroll panel of different panels as named
	public VBox sharePanelRoot, downloadPanelMainRoot, downloadPanelSideRoot, uploadPanelRoot;

	private int currentPanelIndex;
	private final int NO_OF_PANELS = 4;

	Root(Stage stage) {
		this.stage = stage;
		fileChooser = new FileChooser();

		materialPane = new MaterialDesignPane();
		stackPane = new StackPane();

		panels = new Panel[NO_OF_PANELS];
		panels[0] = getMainPanel();
		panels[1] = getSharePanel();
		panels[2] = getDownloadPanel();
		panels[3] = getUploadPanel();

		stackPane.getChildren().add(materialPane);
		for (Panel p : panels)
		{
			stackPane.getChildren().add(p.getContentPane());
			StackPane.setAlignment(p.getContentPane(), Pos.CENTER);
			StackPane.setMargin(p.getContentPane(), new Insets((p.getContentPane().get_height() * 18) / 100, 0, 0, 0));
		}

		// default content panel of 0th panel is visible
		panels[0].getContentPane().setOpacity(1);
		currentPanelIndex = 0;

		materialPane.getToolbar().animateTo(panels[currentPanelIndex].getToolbarColor(),
				panels[currentPanelIndex].getTitle(), panels[currentPanelIndex].getIcon(),
				panels[currentPanelIndex].getControls());

		materialPane.getToolbar().getMainIcon().setOnMouseClicked(clicked ->
		{
			panels[currentPanelIndex].getContentPane().setOpacity(0);

			currentPanelIndex = ((++currentPanelIndex) % NO_OF_PANELS);
			materialPane.getToolbar().animateTo(panels[currentPanelIndex].getToolbarColor(),
					panels[currentPanelIndex].getTitle(), panels[currentPanelIndex].getIcon(),
					panels[currentPanelIndex].getControls());
			panels[currentPanelIndex].getContentPane().setOpacity(1);
			// Because since multiple panels added to stack panel whose content pane is at
			// back will not receive events even if nodes on top of it are transparent
			// even if nodes on top of it are transparent because only front node receive
			// events in stack or back node may if it is not overlapped by another front
			// node
			panels[currentPanelIndex].getContentPane().toFront();
		});
	}

	public void setBER(BackEndRootx backend) {
		this.BER = backend;
	}

	private Panel getSharePanel() {
		final Color shareFilesIconFill = Color.CHOCOLATE;
		final Color loaclSharedNodeFill = Color.DARKSLATEBLUE;
		final Color updateShareIconFill = Color.HONEYDEW;
		final LinearGradient sharePanelFill = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
				new Stop[] { new Stop(0, Color.ORANGE), new Stop(0.3, Color.CHOCOLATE) });

		Circle shareFilesIcon = new Circle(15);
		shareFilesIcon.setFill(shareFilesIconFill);
		ToolbarAction shareFilesAction = new ToolbarAction(action ->
		{
			// out.println("shareFileAction");
			fileChooser.setTitle("Select Files To Share");
			List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

			if (selectedFiles != null)
			{
				/*********************** BACK END ROOT INJECTION ***********************/
				BackEndRootx.RELATION_PACKET.sharedFiles.addAll(selectedFiles);

				for (File f : selectedFiles)
				{
					SharedNode node = new SharedNode(f.toString(), null, loaclSharedNodeFill);
					node.setOperationAction(operationAction ->
					{
						// out.println("SharedNode delete OnAction");
						int index = SharedNode.getNativeContainer().getChildren().indexOf(node);
						SharedNode.getNativeContainer().getChildren().remove(index);
						/*********************** BACK END ROOT INJECTION ***********************/
						BackEndRootx.RELATION_PACKET.sharedFiles.remove(index);
					});
					sharePanelRoot.getChildren().add(node);
				}
			}

		}, "", shareFilesIcon);

		Circle updateShareIcon = new Circle(15);
		updateShareIcon.setFill(updateShareIconFill);
		ToolbarAction updateShareAction = new ToolbarAction(action ->
		{
			/*********************** BACK END ROOT INJECTION ***********************/
			/**
			 * module testing remove later during deployment checking whether relation
			 * packet shared list is updated with
			 * SharedNode.getNativeConatainer.getChildren() list
			 */
			/*
			 * for(File f:BER.RELATION_PACKET.sharedFiles) out.println(f.toString());
			 */
			/*********************** BACK END ROOT INJECTION ***********************/
			BER.heart.stop();
			BER.heartAttack.play();
			// err.println("♥♥♥♥♥♥heartAttack "+System.currentTimeMillis());
			// out.println("updateShareAction");
		}, "", updateShareIcon);

		Panel share = new Panel(new ContentPane(), sharePanelFill, "Share", IconType.BACK, shareFilesAction,
				updateShareAction);

		sharePanelRoot = new VBox();
		sharePanelRoot.setFillWidth(true);
		sharePanelRoot.setSpacing(3);

		SharedNode.setNativeContainer(sharePanelRoot);

		ScrollPane scroll = new ScrollPane(sharePanelRoot);
		scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scroll.setFitToWidth(true);
		scroll.setPrefViewportWidth(share.getContentPane().get_width() - 15.3);
		scroll.setPrefViewportHeight(share.getContentPane().get_height() - 15.5);

		share.getContentPane().getChildren().add(scroll);

		return share;
	}

	private Panel getDownloadPanel() {
		final Color downloadPanelFill = Color.DEEPPINK;
		final Color openContaningDirIconFill = Color.HOTPINK;
		final Color downloadLogIconFill = Color.PINK;

		Circle openContaningDirIcon = new Circle(15);
		openContaningDirIcon.setFill(openContaningDirIconFill);
		ToolbarAction openContaningDirAction = new ToolbarAction(action ->
		{
			// out.println("openContaningDirAction");
			/**
			 * adding sample data for GUI test adding online users
			 */
			/*
			 * for(int i=0;i<30;i++) { List<File> l=new ArrayList<File>(); UserNode node=new
			 * UserNode("Sample User ID::"+i+""); for(int k=0;k<30;k++) l.add(new
			 * File("Sample file data ,file ID::"+k+"   user ID::"+i+""));
			 * node.setsharedFileList(l); downloadPanelMainRoot.getChildren().add(node); }
			 */
		}, "", openContaningDirIcon);

		Circle downloadLogIcon = new Circle(15);
		downloadLogIcon.setFill(downloadLogIconFill);
		ToolbarAction downloadlogAction = new ToolbarAction(action ->
		{
			/**
			 * downloadlogAction code here
			 */
			// if downloader panel is not opened and ShowSharedFilesPanel is also not opened
			// only then open side panel i.e download panel for this case
			if (!Downloader.isOpened && !UserNode.isShowSharedFilesPanelOpened)
			{
				// indicates that downloader panel is opened
				Downloader.isOpened = true;
				Downloader.openDownloadingPanel.play();
				// add download log nodes to side panel's VBox or downloadPanelSideRoot
				Downloader.getContainer().getChildren().addAll(Downloader.getDownloading());
			} else
			// if downloader panel is opened than only close it
			if (Downloader.isOpened)
			{
				// first clear than close to avoid flickering
				Downloader.getContainer().getChildren().clear();
				// indicate that downloader panel is closed
				Downloader.isOpened = false;
				// than close downloader panel
				Downloader.closeDownloadingPanel.play();
			}
			// out.println("downloadlogAction");
		}, "", downloadLogIcon);

		Panel download = new Panel(new ContentPane(), downloadPanelFill, "Download", IconType.PLAY,
				openContaningDirAction, downloadlogAction);

		// translateX offset actual translateX is
		// download.getContentPane().get_width()-showSharedFilesPanel_translateX
		final int showSharedFilesPanel_translateX = 100;
		sidePanel = new ContentPane();
		sidePanel.set_width(sidePanel.get_width() - 100);
		sidePanel.setTranslateX(download.getContentPane().get_width() - showSharedFilesPanel_translateX);

		downloadPanelSideRoot = new VBox();
		downloadPanelSideRoot.setFillWidth(true);
		downloadPanelSideRoot.setSpacing(3);

		ScrollPane scroll0 = new ScrollPane(downloadPanelSideRoot);
		scroll0.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll0.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scroll0.setFitToWidth(true);
		scroll0.setPrefViewportWidth(sidePanel.get_width() - 15.3);
		scroll0.setPrefViewportHeight(sidePanel.get_height() - 15);

		sidePanel.getChildren().add(scroll0);

		downloadPanelMainRoot = new VBox();
		downloadPanelMainRoot.setFillWidth(true);
		downloadPanelMainRoot.setSpacing(3);

		SharedNode.setRemoteContainer(downloadPanelSideRoot);

		UserNode.setContainer(downloadPanelMainRoot);
		UserNode.setShowSharedFilesPanel(sidePanel);

		Downloader.setContainer(downloadPanelSideRoot);
		Downloader.setDownloadingPanel(sidePanel);

		ScrollPane scroll = new ScrollPane(downloadPanelMainRoot);
		scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scroll.setFitToWidth(true);
		scroll.setPrefViewportWidth(download.getContentPane().get_width() - 15.3 - showSharedFilesPanel_translateX);
		scroll.setPrefViewportHeight(download.getContentPane().get_height() - 15);

		StackPane stack = new StackPane(scroll, sidePanel);
		stack.setAlignment(Pos.CENTER);

		download.getContentPane().getChildren().add(stack);

		return download;
	}

	private Panel getMainPanel() {
		final Color mainPanelFill = Color.DARKSLATEBLUE;
		Panel main = new Panel(new ContentPane(), mainPanelFill, "BitPort", IconType.MENU, new ToolbarAction[] {});

		Path p;
		try
		{
			p = Paths.get(Root.class.getResource("UsageManual.png").toURI()).toRealPath();
			ImageView iv = new ImageView(p.toUri().toString());
			iv.setSmooth(true);
			iv.setPreserveRatio(true);
			main.getContentPane().getChildren().add(iv);
		} catch (IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}

		return main;
	}

	private Panel getUploadPanel() {
		final Color UPLOAD_BAR = Color.ROYALBLUE;

		Panel upload = new Panel(new ContentPane(), UPLOAD_BAR, "Uploads", IconType.ARROW, new ToolbarAction[] {});

		uploadPanelRoot = new VBox();
		uploadPanelRoot.setFillWidth(true);
		uploadPanelRoot.setSpacing(3);

		ScrollPane scroll = new ScrollPane(uploadPanelRoot);
		scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scroll.setFitToWidth(true);
		scroll.setPrefViewportWidth(upload.getContentPane().get_width() - 15.3);
		scroll.setPrefViewportHeight(upload.getContentPane().get_height() - 15.5);

		upload.getContentPane().getChildren().add(scroll);

		Uploader.setContainer(uploadPanelRoot);

		return upload;
	}

	public StackPane getRoot() {
		return this.stackPane;
	}

}