package se.molybden.games.backgammon.model.exceptions

/**
 * Created by joachim on 2017-02-08.
 */
class IllegalMoveException extends Exception {
	IllegalMoveException(Exception e) {
		super(e)
	}

	IllegalMoveException(String message) {
		super(message)
	}
}
