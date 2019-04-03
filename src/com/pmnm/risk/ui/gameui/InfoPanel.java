package com.pmnm.risk.ui.gameui;

import java.awt.Rectangle;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;

public class InfoPanel extends DoaObject {

	private static final long serialVersionUID = -59674351675589726L;

	private static final DoaVectorF GARRISON_HOLDER = new DoaVectorF(23f, 786f);
	private static final DoaVectorF GARRISON_HOLDER_ICON = new DoaVectorF(164f, 794f);

	private static final DoaVectorF OWNER_HOLDER = new DoaVectorF(23f, 828f);
	private static final DoaVectorF OWNER_HOLDER_ICON = new DoaVectorF(200f, 835f);

	private static final DoaVectorF PROVINCE_NAME_HOLDER = new DoaVectorF(23f, 870f);
	private static final DoaVectorF PROVINCE_NAME_HOLDER_ICON = new DoaVectorF(245f, 876f);

	private static final DoaVectorF MINIMAP_HOLDER = new DoaVectorF(23f, 914f);

	private static final DoaSprite TEXTURE = DoaSprites.get("infoPanel");
	private static final DoaSprite GARRISON_HOLDER_TEXTURE = DoaSprites.get("garrisonHolder");
	private static final DoaSprite GARRISON_HOLDER_ICON_TEXTURE = DoaSprites.get("garrisonHolderIcon");
	private static final DoaSprite OWNER_HOLDER_TEXTURE = DoaSprites.get("ownerHolder");
	private static final DoaSprite OWNER_HOLDER_ICON_TEXTURE = DoaSprites.get("ownerHolderIcon");
	private static final DoaSprite PROVINCE_NAME_HOLDER_TEXTURE = DoaSprites.get("provinceNameHolder");
	private static final DoaSprite PROVINCE_NAME_HOLDER_ICON_TEXTURE = DoaSprites.get("provinceNameHolderIcon");
	private static final DoaSprite MINIMAP_HOLDER_TEXTURE = DoaSprites.get("mini");

	public InfoPanel() {
		super(-227f, 732f, TEXTURE.getWidth() - 100, TEXTURE.getHeight() - 100, DoaObject.STATIC_FRONT);
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(TEXTURE, position.x, position.y, width, height);
		g.drawImage(GARRISON_HOLDER_TEXTURE, GARRISON_HOLDER.x, GARRISON_HOLDER.y);
		g.drawImage(GARRISON_HOLDER_ICON_TEXTURE, GARRISON_HOLDER_ICON.x, GARRISON_HOLDER_ICON.y);
		g.drawImage(OWNER_HOLDER_TEXTURE, OWNER_HOLDER.x, OWNER_HOLDER.y);
		g.drawImage(OWNER_HOLDER_ICON_TEXTURE, OWNER_HOLDER_ICON.x, OWNER_HOLDER_ICON.y);
		g.drawImage(PROVINCE_NAME_HOLDER_TEXTURE, PROVINCE_NAME_HOLDER.x, PROVINCE_NAME_HOLDER.y);
		g.drawImage(PROVINCE_NAME_HOLDER_ICON_TEXTURE, PROVINCE_NAME_HOLDER_ICON.x, PROVINCE_NAME_HOLDER_ICON.y);
		g.drawImage(MINIMAP_HOLDER_TEXTURE, MINIMAP_HOLDER.x, MINIMAP_HOLDER.y);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) position.x, (int) position.y, width, height);
	}
}
