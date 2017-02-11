package se.molybden.games.backgammon.model

import se.molybden.games.backgammon.model.exceptions.*

import static se.molybden.games.backgammon.model.BarPosition.getBarPosition
import static se.molybden.games.backgammon.model.BoardPosition.getBoardPositionFromIndex
import static se.molybden.games.backgammon.model.Color.BLACK
import static se.molybden.games.backgammon.model.Color.WHITE

/**
 * Created by joachim on 2017-02-08.
 */
class BoardState implements Cloneable {
	final Point[] points = new Point[24]
	final Map<Color, Bar> bar = new HashMap<>(2)
	private Color winner

	BoardState() {
		bar.put(WHITE, new Bar(WHITE))
		bar.put(BLACK, new Bar(BLACK))
		for (int i = 0; i < 24; i++) {
			points[i] = new Point()
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		def clone = new BoardState()
		for (int i = 0; i < points.length; i++) {
			def checkers = points[i].checkers
			if (!checkers.isEmpty()) {
				def color = checkers.get(0).color
				clone.put(checkers.size(), color, getBoardPositionFromIndex(color, i).myCount)
			}
		}
		for (Color color : Color.values()) {
			for (Checker checker : bar.get(color).checkers) {
				clone.bar.get(color).checkers.add(new Checker(color))
			}
		}
		return clone
	}

	void initBoard() {
		Color.values().each { color ->
			put(2, color, 1)
			put(5, color, 12)
			put(3, color, 17)
			put(5, color, 19)
		}
	}

	private void put(int numberOfCheckers, Color color, int myPosition) {
		def position = new BoardPosition(color, myPosition)
		for (int i = 0; i < numberOfCheckers; i++) {
			put(new Checker(color), position)
		}
	}

	private void put(Checker checker, Position position) {
		if (position instanceof BoardPosition) {
			points[position.arrayIndex].add checker
		} else if (position instanceof BarPosition) {
			bar.get(position.color).add checker
		} else if (position instanceof BearOffPosition) {
			//Just remove it from game
		}
	}

	List<Position> getAvailablePositions(Color color) {
		List<Position> positions = new ArrayList<>()
		if (isOnBar(color)) {
			for (int i = 0; i < bar.get(color).checkers.size(); i++) {
				positions.add(getBarPosition(color))
			}
			return positions
		}
		int index
		for (Point point : points) {
			if (!point.isEmpty()) {
				if (point.color == color) {
					for (int i = 0; i < point.checkers.size(); i++) {
						positions.add(getBoardPositionFromIndex(color, index))
					}
				}
			}
			index++
		}
		positions
	}

	private boolean isOnBar(Color color) {
		!bar.get(color).isEmpty()
	}

	def move(Position from, Position to) {
		Checker checker = getChecker(from)
		try {
			from.checkDistance(to)
			verifyBearOff(from, to)
			put(checker, to)
		} catch (OccupiedByOtherColorException e) {
			put(checker, from)
			throw new IllegalMoveException(e)
		} catch (HitException hit) {
			checker = hit.checker
			put(checker, getBarPosition(checker.color))
		} finally {
			checkWinner(checker.color)
		}
	}

	def checkWinner(Color color) {
		def remaining = points.findAll { !it.isEmpty() && it.color == color }
		if (remaining.isEmpty() && bar.get(color).empty) {
			winner = color
			throw new WinnerFoundException(color)
		}
	}

	def verifyBearOff(Position from, Position to) {
		if (!(to instanceof BearOffPosition)) {
			return
		}
		try {
			for (Position position : getAvailablePositions(from.color)) {
				position.checkDistance(to)
			}
		} catch (IllegalMoveException e) {
			throw new IllegalMoveException("One or more other checkers are not in home board")
		}
	}

	private Checker getChecker(Position from) {
		try {
			def checker
			if (from instanceof BarPosition) {
				checker = bar.get(from.color).remove()
			} else if (from instanceof BoardPosition) {
				if (isOnBar(from.color)) {
					throw new IllegalMoveException("Bar not empty")
				}
				checker = points[from.arrayIndex].remove()
			}
			checker
		} catch (NoCheckersException e) {
			throw new IllegalMoveException(e)
		}
	}

	String getStringRepresentationOf(int index, int row) {
		if (points[index].isEmpty()) return " "
		def type = "W"
		if (points[index].color == BLACK) {
			type = "B"
		}
		if (points[index].checkers.size() >= row) {
			if (row == 5 && points[index].checkers.size() > 5) {
				return "*"
			}
			return type
		}
		return " "
	}

	boolean hasWinner() {
		winner != null
	}
}
