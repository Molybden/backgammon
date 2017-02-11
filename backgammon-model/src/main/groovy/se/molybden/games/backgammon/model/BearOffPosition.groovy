package se.molybden.games.backgammon.model

import se.molybden.games.backgammon.model.exceptions.IllegalMoveException

import static se.molybden.games.backgammon.model.Color.BLACK
import static se.molybden.games.backgammon.model.Color.WHITE

/**
 * Created by joachim on 2017-02-08.
 */
class BearOffPosition implements Position {

	final Color color
	final static WHITE_OUT = new BearOffPosition(WHITE)
	final static BLACK_OUT = new BearOffPosition(BLACK)

	private BearOffPosition(Color color) { this.color = color }

	Color getColor() {
		return color
	}

	void checkDistance(Position to) {
		throw new IllegalMoveException("Not possible to move FROM Bearoff position")
	}

	@Override
	int getRelativePositionCount() {
		return 24
	}

	static BearOffPosition getBearOffPosition(Color color) {
		if (color == BLACK) return BLACK_OUT
		return WHITE_OUT
	}

	@Override
	String toString() {
		return "OFF"
	}
}
