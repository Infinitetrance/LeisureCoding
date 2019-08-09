package root.bitport;
import javafx.scene.paint.Paint;

class Panel
{
	private ContentPane contentPane;
	private Paint toolbarColor;
	private String title;
	private IconType icon;
	private ToolbarAction controls[];
	Panel(ContentPane contentPane,Paint toolbarColor,String title,IconType icon,ToolbarAction ...controls)
	{
		this.contentPane=contentPane;
		this.toolbarColor=toolbarColor;
		this.title=title;
		this.icon=icon;
		this.controls=controls;
		//by default all ContentPanes are hidden to manage multiple content panes 
		this.getContentPane().setOpacity(0);
	}
	public ContentPane getContentPane() {
		return contentPane;
	}
	
	public Paint getToolbarColor() {
		return toolbarColor;
	}
	
	public String getTitle() {
		return title;
	}
	
	public IconType getIcon() {
		return icon;
	}
	
	public ToolbarAction[] getControls() {
		return controls;
	}
	
	
}