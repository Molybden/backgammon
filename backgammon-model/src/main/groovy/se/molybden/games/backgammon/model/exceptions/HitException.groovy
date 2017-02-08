package se.molybden.games.backgammon.model.exceptions

import se.molybden.games.backgammon.model.Checker

/**
 * Created by joachim on 2017-02-08.
 */
class HitException extends Exception {
	private Checker checker

	HitException(Checker checker) {
		this.checker = checker
	}
}
