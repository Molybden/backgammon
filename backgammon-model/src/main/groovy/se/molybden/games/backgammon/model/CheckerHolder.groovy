package se.molybden.games.backgammon.model

import se.molybden.games.backgammon.model.exceptions.NoCheckersException

/**
 * Created by joachim on 2017-02-08.
 */
class CheckerHolder {
	ArrayList<Checker> checkers = new ArrayList<>(5)

	boolean add(Checker checker) {
		if (checkers.empty) {
			checkers.add checker
			return
		}
		checkers.add checker
	}

	boolean isEmpty() { checkers.isEmpty() }

	Color getColor() {
		if (isEmpty()) {
			throw new NoCheckersException()
		}
		checkers.get(0).color
	}

	Checker remove() {
		if (isEmpty()) {
			throw new NoCheckersException()
		}
		checkers.remove(0)
	}
}
