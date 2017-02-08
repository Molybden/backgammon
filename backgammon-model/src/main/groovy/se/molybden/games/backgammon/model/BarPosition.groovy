package se.molybden.games.backgammon.model

import se.molybden.games.backgammon.model.exceptions.IllegalMoveException

import static se.molybden.games.backgammon.model.Color.BLACK
import static se.molybden.games.backgammon.model.Color.WHITE

/**
 * Created by joachim on 2017-02-08.
 */
class BarPosition implements Position {
	final Color color
	final static WHITE_BAR = new BarPosition(WHITE)
	final static BLACK_BAR = new BarPosition(BLACK)

	private BarPosition(Color color) { this.color = color }

	@Override
	Color getColor() {
		return color
	}

	void checkDistance(Position to) {
		if (to instanceof BarPosition) {
			throw new IllegalArgumentException("Cannot move from bar to bar. Except on St Paddy's day.")
		}
		if (to instanceof BoardPosition) {
			if (to.myCount > 6) {
				throw new IllegalMoveException()
			}
		} else {
			throw new IllegalArgumentException("Way to far!")
		}
	}

	static BarPosition getBarPosition(Color color) {
		if (color == BLACK) return BLACK_BAR
		return WHITE_BAR
	}
}
