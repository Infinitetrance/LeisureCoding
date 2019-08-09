package root.util.uitoolkit;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * Useful when required to show UI components in terminal based
 * application.<br/>
 * 
 * e.g. we write window/UI less program that does something like periodically
 * ping some server, now when server is not reachable it should show error
 * message. Now for terminal based application to show error means print it on
 * console that means you have to run JAR for this application from terminal to
 * see error message which is not appropriate as just to see some rare case
 * error message we keep a terminal window open. So now we may think of
 * implementing UI based application and this is also somewhat not appropriate
 * as to write all boiler plate code of initializing Stage, Scene etc. just to
 * show some error message which may or may not happen. So in such scenario when
 * we do not need UI but for some rare corner cases where we may need to alert
 * user or ask user input with bare minimum UI components without having any UI
 * window implementation I find this toolkit suitable to use.
 *
 */
public class UIToolkit implements Closeable {

	public static class Monitor {
		public synchronized void doWait() {
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		public synchronized void doNotifyAll() {
			notifyAll();
		}
	}

	private UIToolkit() {
	}

	private static final UIToolkit UIToolkitInstance = new UIToolkit();

	public static UIToolkit getUIToolkit() {
		return UIToolkitInstance;
	}

	/**
	 * Submit a UI render task in queue.
	 * 
	 * @param renderTask UIRender Task e.g. Display some UI component like alert,
	 *                   window.
	 */
	public void submit(Runnable renderTask) {
		UIToolkitBase.submit(renderTask);
	}

	/**
	 * Shows JavaFX Alert window.
	 * 
	 * @param title
	 * @param headerText
	 * @param contentText
	 * @param alertType
	 * @return Monitor if you wish to wait till user cancel or does provide input
	 *         than do {@code showAlert( ... ).doWait()}
	 */
	public Monitor showAlert(String title, String headerText, String contentText, AlertType alertType) {
		Monitor monitor = new Monitor();
		UIToolkitInstance.submit(() ->
		{
			try
			{
				Alert alert = new Alert(alertType);
				alert.setTitle(title);
				alert.setHeaderText(headerText);
				alert.setContentText(contentText);
				alert.showAndWait();
			} finally
			{
				monitor.doNotifyAll();
			}
		});
		return monitor;
	}

	/**
	 * 
	 * @param title
	 * @param headerText
	 * @param contentText
	 * @param onOk
	 * @param onCancel
	 * @return Monitor if you wish to wait till user cancel or does provide input
	 *         than do {@code showConfirmationDialog( ... ).doWait()}
	 */
	public Monitor showConfirmationDialog(String title, String headerText, String contentText, Runnable onOk,
			Runnable onCancel) {
		Monitor monitor = new Monitor();
		UIToolkitInstance.submit(() ->
		{
			try
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle(title);
				alert.setHeaderText(headerText);
				alert.setContentText(contentText);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK)
				{
					if (onOk != null)
						onOk.run();
				} else if (onCancel != null)
					onCancel.run();
			} finally
			{
				monitor.doNotifyAll();
			}
		});
		return monitor;
	}

	public Monitor showInputDialog(String title, String headerText, String contentText, String defaultInputText,
			Consumer<String> inputConsumer) {
		Monitor monitor = new Monitor();
		UIToolkitInstance.submit(() ->
		{
			try
			{
				TextInputDialog dialog = new TextInputDialog(defaultInputText);
				dialog.setTitle(title);
				dialog.setHeaderText(headerText);
				dialog.setContentText(contentText);

				Optional<String> result = dialog.showAndWait();

				result.ifPresent(inputConsumer::accept);
			} finally
			{
				monitor.doNotifyAll();
			}
		});
		return monitor;
	}

	public Monitor showLoginDialog(String title, String headerText, BiConsumer<String, String> credentialConsumer) {
		Monitor monitor = new Monitor();
		UIToolkitInstance.submit(() ->
		{
			try
			{
				Dialog<Pair<String, String>> dialog = new Dialog<>();
				dialog.setTitle(title);
				dialog.setHeaderText(headerText);

				ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

				GridPane grid = new GridPane();
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(20, 150, 10, 10));

				TextField username = new TextField();
				username.setPromptText("Username");
				PasswordField password = new PasswordField();
				password.setPromptText("Password");

				grid.add(new Label("Username:"), 0, 0);
				grid.add(username, 1, 0);
				grid.add(new Label("Password:"), 0, 1);
				grid.add(password, 1, 1);

				// Enable/Disable login button depending on whether a username was entered.
				Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
				loginButton.setDisable(true);

				username.textProperty().addListener((observable, oldValue, newValue) ->
				{
					loginButton.setDisable(newValue.trim().isEmpty());
				});

				dialog.getDialogPane().setContent(grid);

				// Request focus on the username field by default.
				Platform.runLater(() -> username.requestFocus());

				// Convert the result to a username-password-pair when the login button is
				// clicked.
				dialog.setResultConverter(dialogButton ->
				{
					if (dialogButton == loginButtonType)
					{
						return new Pair<>(username.getText(), password.getText());
					}
					return null;
				});

				Optional<Pair<String, String>> result = dialog.showAndWait();
				result.ifPresent(usernamePassword ->
				{
					credentialConsumer.accept(usernamePassword.getKey(), usernamePassword.getValue());
				});
			} finally
			{
				monitor.doNotifyAll();
			}
		});
		return monitor;
	}

	/**
	 * Show asynchronous alert using Windows platform's native {@code msg} command.
	 * Intended for Windows Platform only.
	 * 
	 * @param message Alert message
	 */
	public void showAlertWindowsNative(String message) {
		try
		{
			Runtime.getRuntime().exec("msg " + System.getProperty("user.name") + " " + message);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Shutdown this UIToolkit, terminates internal JavaFX application
	 */
	@Override
	public void close() throws IOException {
		UIToolkitBase.dispose();
	}

}
