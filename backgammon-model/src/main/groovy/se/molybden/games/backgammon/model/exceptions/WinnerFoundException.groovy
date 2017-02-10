package se.molybden.games.backgammon.model.exceptions

import se.molybden.games.backgammon.model.Color

/**
 * Created by joachim on 2017-02-10.
 */
class WinnerFoundException extends Exception {
	private Color color

	WinnerFoundException(Color color) {

		this.color = color
	}
}
