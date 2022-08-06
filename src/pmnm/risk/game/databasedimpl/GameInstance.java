package pmnm.risk.game.databasedimpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;

import javax.imageio.ImageIO;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.gameui.RiskGameScreenUI;

import doa.engine.core.DoaWindow;
import doa.engine.scene.DoaScene;
import lombok.Getter;
import pmnm.risk.game.IRiskGameContext;
import pmnm.risk.game.IRiskGameContext.GameType;

public final class GameInstance {

	public static void instantiateGameFromWithUI(GameInstance instance) {
		RiskGameScreenUI.destroyUI();
		RiskGameContext context = (RiskGameContext)instance.context;
		DoaScene scene = Scenes.getGameScene();
		RiskGameScreenUI.initUIFor(context, scene, GameType.SINGLE_PLAYER);
		context.addToScene(scene);
	}
	
	public static GameInstance from(IRiskGameContext context, int order) {
		GameInstance instance = new GameInstance(context, order);
		return instance;
	}

	@Getter private IRiskGameContext context;
	@Getter private Date timestamp;
	@Getter private String version;
	@Getter private BufferedImage snapshotImage;
	@Getter private int order;
	
	private GameInstance(IRiskGameContext context, int order) {
		this.context = context;
		timestamp = Date.from(Instant.now());
		version = Globals.GAME_VERSION;
		snapshotImage = new BufferedImage(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.order = order;
		DoaWindow.dumpFrameBufferTo(snapshotImage); 
	}
	
	private GameInstance(ObjectInputStream ctxIn, ObjectInputStream savIn) throws ClassNotFoundException, IOException {
		context = (IRiskGameContext) ctxIn.readObject();
		timestamp = (Date) savIn.readObject();
		version = (String) savIn.readObject();
		order = savIn.readInt();
		snapshotImage = ImageIO.read(savIn);
	}
	
	public void saveToDisk() throws IOException {
		String mapName = context.getMapName();
		File dir = Path.of(System.getProperty("user.home"), "Documents", "My Games", "Risk Digital Cut", "Saves").toFile();
		dir.mkdirs();
		File ctxFile = new File(dir, sanitizeFileName("save_" + timestamp + "_" + version + "_" + mapName + ".ctx"));
		File savFile = new File(dir, sanitizeFileName("save_" + timestamp + "_" + version + "_" + mapName + ".sav"));
		try (
			FileOutputStream ctxFOS = new FileOutputStream(ctxFile); 
			ObjectOutputStream ctxOOS = new ObjectOutputStream(ctxFOS);
			FileOutputStream savFOS = new FileOutputStream(savFile);
			ObjectOutputStream savOOS = new ObjectOutputStream(savFOS);) {
			ctxOOS.writeObject(context);
			savOOS.writeObject(timestamp);
			savOOS.writeObject(version);
			savOOS.writeInt(order);
			ImageIO.write(snapshotImage, "PNG", savOOS);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static GameInstance loadGame(File savFile) throws IOException, ClassNotFoundException {
		String ctxPath = savFile.getPath();
		ctxPath = ctxPath.substring(0, ctxPath.lastIndexOf("."));
		ctxPath += ".ctx";

		try (
			FileInputStream ctxFIS = new FileInputStream(ctxPath);
			ObjectInputStream ctxOIS = new ObjectInputStream(ctxFIS);
			FileInputStream savFIS = new FileInputStream(savFile);
			ObjectInputStream savOIS = new ObjectInputStream(savFIS);) {
			return new GameInstance(ctxOIS, savOIS);
		}
	}

	private static String sanitizeFileName(String input) {
		return input.replaceAll("[:\\\\/*\"?|<>']", "_");
	}
}
