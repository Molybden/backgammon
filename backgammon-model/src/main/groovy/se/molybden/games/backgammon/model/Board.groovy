package se.molybden.games.backgammon.model

import se.molybden.games.backgammon.model.exceptions.IllegalMoveException

import static se.molybden.games.backgammon.model.Color.BLACK
import static se.molybden.games.backgammon.model.Color.WHITE

/**
 * Created by joachim on 2017-02-08.
 */
class Board {
	private BoardState confirmedBoardState
	private BoardState currentBoardState = new BoardState()

	Board() {
		currentBoardState.initBoard()
		confirmState()
	}

	List<Position> getAvailablePositions(Color color) {
		currentBoardState.getAvailablePositions(color)
	}

	def resetState() {
		currentBoardState = confirmedBoardState.clone()
	}

	def confirmState() {
		confirmedBoardState = currentBoardState
		currentBoardState = currentBoardState.clone()
	}

	def move(Position from, Position to) {
		try {
		currentBoardState.move(from, to)
		} catch (IllegalMoveException e) {
			resetState()
			throw e
		}
	}

	/**
	 * Used in toString
	 */
	private String s(int index, int row) {
		currentBoardState.getStringRepresentationOf(index, row)
	}

	@Override
	String toString() {
		StringBuilder builder = new StringBuilder(200)
		builder.append "   v  v  v  v  v  v     v  v  v  v  v  v"
		builder.append System.lineSeparator()
		builder.append "*--------------------*--------------------*"
		builder.append System.lineSeparator()
		for (int r = 1; r <= 5; r++) {
			int p = 23
			builder.append "|  ${s(p--, r)}  ${s(p--, r)}  ${s(p--, r)}  ${s(p--, r)}  ${s(p--, r)}  ${s(p--, r)}  |  ${s(p--, r)}  ${s(p--, r)}  ${s(p--, r)}  ${s(p--, r)}  ${s(p--, r)}  ${s(p--, r)}  |"
			builder.append System.lineSeparator()
		}
		builder.append "|                    |                    |  W: "
		builder.append currentBoardState.getBar().get(WHITE).checkers.size()
		builder.append System.lineSeparator()
		builder.append "|                    |                    |  B: "
		builder.append currentBoardState.getBar().get(BLACK).checkers.size()
		builder.append System.lineSeparator()
		for (int r = 5; r >= 1; r--) {
			int p = 0
			builder.append "|  ${s(p++, r)}  ${s(p++, r)}  ${s(p++, r)}  ${s(p++, r)}  ${s(p++, r)}  ${s(p++, r)}  |  ${s(p++, r)}  ${s(p++, r)}  ${s(p++, r)}  ${s(p++, r)}  ${s(p++, r)}  ${s(p++, r)}  |"
			builder.append System.lineSeparator()
		}
		builder.append "*--------------------*--------------------*"
		builder.append System.lineSeparator()
		builder.append "   ^  ^  ^  ^  ^  ^     ^  ^  ^  ^  ^  ^"
		builder.append System.lineSeparator()
		builder.toString()
	}

}
