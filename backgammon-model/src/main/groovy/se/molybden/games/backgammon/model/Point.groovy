package se.molybden.games.backgammon.model

import se.molybden.games.backgammon.model.exceptions.HitException
import se.molybden.games.backgammon.model.exceptions.OccupiedByOtherColorException

/**
 * Created by joachim on 2017-02-08.
 */
class Point extends CheckerHolder {
	@Override
	boolean add(Checker checker) {
		if (!isEmpty() && getColor() != checker.color) {
			if (checkers.size() == 1) {
				def removed = checkers.remove(0)
				checkers.add checker
				throw new HitException(removed)
			}
			throw new OccupiedByOtherColorException()
		}
		return super.add(checker)
	}
}
