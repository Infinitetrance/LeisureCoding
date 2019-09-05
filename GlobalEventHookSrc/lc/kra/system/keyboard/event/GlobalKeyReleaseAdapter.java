package lc.kra.system.keyboard.event;

@FunctionalInterface
public interface GlobalKeyReleaseAdapter extends GlobalKeyListener
{
	@Override
	default public void keyPressed(GlobalKeyEvent event)
	{
	}
}
