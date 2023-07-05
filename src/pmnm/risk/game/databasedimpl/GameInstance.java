package pmnm.risk.game.databasedimpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.javatuples.Quintet;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.gameui.RiskGameScreenUI;

import doa.engine.core.DoaWindow;
import doa.engine.scene.DoaScene;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pmnm.risk.game.GameConfig;

public final class GameInstance {

	private static final String SAVE_FILE = "save";
	private static final String METADATA_FILE = "metadata";
	private static final File SAVE_LOC = Path.of(System.getProperty("user.home"), "Documents", "My Games", "Risk Digital Cut", "Saves").toFile();

	public static Quintet<Metadata, Metadata, Metadata, Metadata, Metadata> readMetadataFromDisk() {
		if (!SAVE_LOC.exists()) { return Quintet.with(null, null, null, null, null); }

		final int count = 5;
		Metadata[] metas = new Metadata[count];

		for (int i = 0; i < count; i++) {
			File saveFolder = new File(SAVE_LOC, Integer.toString(i));
			if (!saveFolder.exists()) { continue; }

			File metaFile = new File(saveFolder, METADATA_FILE);
			try (FileInputStream metaFIS = new FileInputStream(metaFile);
				ObjectInputStream metaOIS = new ObjectInputStream(metaFIS);) {
				metas[i] = (Metadata) metaOIS.readObject();
			} catch (ClassCastException | StreamCorruptedException ex) {
				ex.printStackTrace();
				metas[i] = null;
			} catch (IOException | ClassNotFoundException ex) {
				/* swallow */
				ex.printStackTrace();
			}
		}
		return Quintet.fromArray(metas);
	}

	public static void instantiateGameWithUI(GameInstance instance) {
		DoaScene scene = Scenes.getGameScene();
		RiskGameScreenUI.destroyUI(scene);
		RiskGameContext context = instance.context;
		RiskGameScreenUI.initUIFor(context, scene, context.getGameType());
		context.addToScene(scene);
	}

	@Getter private RiskGameContext context;
	@Getter private Metadata metadata;

	public GameInstance(RiskGameContext context) {
		this.context = context;

		BufferedImage snapshot = new BufferedImage(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		DoaWindow.dumpFrameBufferTo(snapshot);
		metadata = new Metadata(
			context.getConfig(),
			context.getMapName(),
			Date.from(Instant.now()),
			Globals.GAME_VERSION,
			snapshot,
			-1
		);
	}

	private GameInstance(ObjectInputStream ctxIn, ObjectInputStream metaIn) throws IOException {
		try {
			context = (RiskGameContext) ctxIn.readObject();
		} catch(ClassCastException | StreamCorruptedException ex) {
			JOptionPane.showConfirmDialog(
				null,
				"CORRUPT SAVE DETECTED! Data is corrupt!",
				"ERROR",
				JOptionPane.OK_OPTION,
				JOptionPane.ERROR_MESSAGE
			);
		} catch(ClassNotFoundException ex) {
			/* swallow */
			ex.printStackTrace();
		}

		try {
			metadata = (Metadata) metaIn.readObject();
		} catch(ClassCastException | StreamCorruptedException ex) {
			JOptionPane.showConfirmDialog(
				null,
				"CORRUPT SAVE DETECTED! Metadata is corrupt!",
				"ERROR",
				JOptionPane.OK_OPTION,
				JOptionPane.ERROR_MESSAGE
			);
		} catch(ClassNotFoundException ex) {
			/* swallow */
			ex.printStackTrace();
		}
	}

	public void saveToDisk(int order) {
		/* STEP 0: Create the save folder if does not exist */
		File saveFolder = new File(SAVE_LOC, Integer.toString(order));
		saveFolder.mkdirs();

		/* STEP 1: Set-up files */
		File ctxFile = new File(saveFolder, SAVE_FILE);
		File metaFile = new File(saveFolder, METADATA_FILE);

		/* STEP 2: Delete old save data */
		ctxFile.delete();
		metaFile.delete();

		/* STEP 3: Set up streams */
		try (
			FileOutputStream ctxFOS = new FileOutputStream(ctxFile);
			ObjectOutputStream ctxOOS = new ObjectOutputStream(ctxFOS);
			FileOutputStream metaFOS = new FileOutputStream(metaFile);
			ObjectOutputStream metaOOS = new ObjectOutputStream(metaFOS);) {
			/* STEP 4: Write to disk */
			ctxOOS.writeObject(context);
			metadata.order = order;
			metaOOS.writeObject(metadata);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static GameInstance loadGame(int order) {
		/* STEP 0: Check if save folder exists */
		File saveFolder = new File(SAVE_LOC, Integer.toString(order));
		if (!saveFolder.exists()) {
			JOptionPane.showConfirmDialog(
				null,
				"CORRUPT SAVE DETECTED! Save folder is absent for order: " + order,
				"ERROR",
				JOptionPane.OK_OPTION,
				JOptionPane.ERROR_MESSAGE
			);
			return null;
		}

		/* STEP 1: Find and verify files */
		File ctxFile = new File(saveFolder, SAVE_FILE);
		File metaFile = new File(saveFolder, METADATA_FILE);
		if (!ctxFile.exists()) {
			JOptionPane.showConfirmDialog(
				null,
				"CORRUPT SAVE DETECTED! Save file is absent order: " + order,
				"ERROR",
				JOptionPane.OK_OPTION,
				JOptionPane.ERROR_MESSAGE
			);
			return null;
		}
		if (!metaFile.exists()) {
			JOptionPane.showConfirmDialog(
				null,
				"CORRUPT SAVE DETECTED! Meta file is absent! Bugs may happen order: " + order,
				"WARNING",
				JOptionPane.OK_OPTION,
				JOptionPane.WARNING_MESSAGE
			);
			return null;
		}

		try (
			FileInputStream ctxFIS = new FileInputStream(ctxFile);
			ObjectInputStream ctxOIS = new ObjectInputStream(ctxFIS);
			FileInputStream metaFIS = new FileInputStream(metaFile);
			ObjectInputStream metaOIS = new ObjectInputStream(metaFIS);) {
			return new GameInstance(ctxOIS, metaOIS);
		} catch (IOException ex) {
			/* swallow */
			ex.printStackTrace();
			return null;
		}
	}

	@Data
	@EqualsAndHashCode
	@AllArgsConstructor
	@ToString(includeFieldNames = true)
	public static final class Metadata implements Serializable {

		private static final long serialVersionUID = 3750370033085075148L;

		@Getter
		@NonNull
		private GameConfig config;

		@Getter
		@NonNull
		private String mapName;

		@Getter
		@NonNull
		private Date timestamp;

		@Getter
		@NonNull
		private String version;

		@Getter
		@NonNull
		private transient BufferedImage snapshotImage;

		@Getter
		private int order;

		private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
			stream.writeObject(config);
			stream.writeObject(mapName);
			stream.writeObject(timestamp);
			stream.writeObject(version);
			stream.writeInt(order);
			ImageIO.write(snapshotImage, "PNG", stream);
		}

		private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
			config = (GameConfig) stream.readObject();
			mapName = (String) stream.readObject();
			timestamp = (Date) stream.readObject();
			version = (String) stream.readObject();
			order = stream.readInt();
			snapshotImage = ImageIO.read(stream);
		}
	}
}
