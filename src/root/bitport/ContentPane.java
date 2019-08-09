package root.bitport;

import java.awt.Dimension;
import javafx.scene.paint.Paint;
import java.awt.Toolkit;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ContentPane extends Pane 
{
	private double width,height;
	private Paint background;
	ContentPane()
	{
		Dimension size=Toolkit.getDefaultToolkit().getScreenSize();
		width=size.getWidth() - ((size.getWidth()*10)/100);
		height=size.getHeight() -((size.getHeight()*34)/100);
		
		this.background=Color.WHITE;
		this.setBackground(new Background(new BackgroundFill(this.background,null,null)));
		//by default pane do not clip its children so children may layout outside bounds of pane 
		Rectangle clip=new Rectangle(width,height);
		clip.setArcHeight(30);
		clip.setArcWidth(30);
		this.setClip(clip);
	}
	 @Override
	    protected double computeMaxWidth(double height) {
	        return width;
	    }

	    @Override
	    protected double computeMaxHeight(double width) {
	        return height;
	    }

	    @Override
	    protected double computeMinWidth(double height) {
	        return 0;
	    }

	    @Override
	    protected double computeMinHeight(double width) {
	        return 0;
	    }
	    public void setFill(Paint color){
	    	this.background=color;
	    	this.setBackground(new Background(new BackgroundFill(this.background,null,null)));
	    }
	    public double get_height(){
	    	return height;
	    }
	    public double get_width(){
	    	return width;
	    }
	    public void set_height(double height){
	    	this.height=height;
	    }
	    public void set_width(double width){
	    	this.width=width;
	    }
}