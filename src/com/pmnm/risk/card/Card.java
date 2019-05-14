package com.pmnm.risk.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.pmnm.risk.map.province.Province;

public class Card implements Serializable {

	private static final long serialVersionUID = -3610958010376376612L;

	public static Map<Province, Card> PROVINCE_CARDS = new HashMap<>();

	public static List<Card> UNDISTRIBUTED_CARDS = new ArrayList<>();

	private Province province;
	private CardType type;

	public Card() {
		UNDISTRIBUTED_CARDS.add(this);
	}

	public Card setType(CardType type) {
		this.type = type;
		return this;
	}

	public Card setProvince(Province province) {
		this.province = province;
		return this;
	}

	public Province getProvince() {
		return province;
	}

	public CardType getType() {
		return type;
	}

	/**
	 * Gets a random, undistributed card.
	 * 
	 * @return a random card
	 */
	public static Card getRandomCard() {
		return UNDISTRIBUTED_CARDS.remove(ThreadLocalRandom.current().nextInt(UNDISTRIBUTED_CARDS.size() - 1));
	}

	public static void printAllCards() {
		PROVINCE_CARDS.entrySet().forEach(entry -> System.out.println(entry.getValue().toString()));
	}

	@Override
	public String toString() {
		return "Card [province=" + province.getName() + ", type=" + type + "]";
	}
}
