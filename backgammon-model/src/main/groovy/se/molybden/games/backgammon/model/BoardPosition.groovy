package se.molybden.games.backgammon.model

import se.molybden.games.backgammon.model.exceptions.IllegalMoveException
import sun.reflect.generics.reflectiveObjects.NotImplementedException

import static se.molybden.games.backgammon.model.Color.WHITE

/**
 * Black position is direct translation plus one. White is reversed.
 * Created by joachim on 2017-02-08.
 */
class BoardPosition implements Position {
	private Color color
	private int myCount

	BoardPosition(Color color, int myCount) {
		this.myCount = myCount
		this.color = color
	}

	static BoardPosition getBoardPositionFromIndex(Color color, int index) {
		int myCount = index + 1
		if (color == WHITE) {
			myCount = 24 - index
		}
		new BoardPosition(color, myCount)
	}

	int getArrayIndex() {
		if (color == WHITE) {
			return 24 - myCount
		}
		myCount - 1
	}

	Color getColor() {
		return color
	}

	void checkDistance(Position to) {
		if (to instanceof BarPosition) {
			return
		}
		if (to.color != color) {
			throw new IllegalMoveException("From color ${color} different from to color ${to.color}")
		}
		if (to instanceof BoardPosition) {
			def distance = to.myCount - myCount
			if (distance > 6) {
				throw new IllegalMoveException("Distance too far: ${distance}")
			}
			return
		}
		if (to instanceof BearOffPosition) {
			if (myCount < 19) {
				throw new IllegalMoveException("Cannot bear off from: ${myCount}")
			}
			return
		}
		throw new NotImplementedException("Missing something here")
	}
}
