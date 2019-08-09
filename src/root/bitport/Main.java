package root.bitport;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try
		{
			Root FER = new Root(stage);
			BackEndRootx BER = new BackEndRootx(null);
			BER.setFER(FER);
			FER.setBER(BER);

			Scene scene = new Scene(FER.getRoot(), 650, 400);
			stage.setScene(scene);
			stage.setOnCloseRequest(onClose ->
			{
				Downloader.getExecutor().shutdownNow();
				Uploader.getExecutor().shutdownNow();
				BER.stop();
			});
			stage.show();
			BER.play();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
