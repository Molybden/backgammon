package se.molybden.games.backgammon.model

import se.molybden.games.backgammon.model.dice.DieRoll
import se.molybden.games.backgammon.model.exceptions.IllegalMoveException

/**
 * Created by joachim on 2017-02-11.
 */
class Player {
	//TODO: invalid location for this logic, move to other class
	private Color color

	Player(Color color) {
		this.color = color
	}

	def makeMoves(Board board, Tuple roll) {
		//TODO: check possibility to move from available positions
		println "Roll: ${roll.get(0).value} ${roll.get(1).value}"
		List<DieRoll> remainingRolls = getRollList(roll)
		while (remainingRolls.size() > 0) {
			try {
				List<Position> positions = getAndPrintAvailablePositions(board)
				Position position = getPosition(positions)
				DieRoll selectedRoll = getRoll(remainingRolls)
				Position target
				def targetPositionCount = position.getRelativePositionCount() + selectedRoll.value
				if (targetPositionCount > 23) {
					target = new BearOffPosition(color)
				} else {
					target = new BoardPosition(color, targetPositionCount)
				}
				board.move(position, target)
				remainingRolls.remove(selectedRoll)
			} catch (IllegalMoveException e) {
				println e.getMessage() + "  " + e.getCause().getMessage()
				println "Try again"

			}
		}
	}

	private Position getPosition(List<Position> positions) {
		if (positions.size() == 1) {
			return positions.get(0)
		}
		Position selectedPosition
		while (!selectedPosition) {
			println 'Enter the position to move from:'
			String input = new Scanner(System.in).nextLine()
			selectedPosition = positions.find { it.toString().equals(input) }
		}
		selectedPosition
	}

	private DieRoll getRoll(List<DieRoll> rolls) {
		if (obviousSelection(rolls)) {
			return rolls.get(0)
		}
		DieRoll selectedRoll
		while (!selectedRoll) {
			println "Which die roll?"
			String input = new Scanner(System.in).nextLine()
			selectedRoll = rolls.find { input.equals(String.valueOf(it.value)) }
		}
		selectedRoll
	}

	private boolean obviousSelection(List<?> list) {
		if (list.size() == 1) {
			return true
		}
		list.countBy { it.value }.size() == 1
	}

	private List<Position> getAndPrintAvailablePositions(Board board) {
		List<Position> positions = board.getAvailablePositions(color)
		def builder = new StringBuilder()
		builder.append "Available checker positions: "
		for (Position position : positions) {
			builder.append ", "
			builder.append position
		}
		println builder.toString().trim().replaceFirst(", ", "")
		positions
	}

	List<DieRoll> getRollList(Tuple tuple) {
		List<DieRoll> remainingRolls = new ArrayList<>(4)
		DieRoll first = tuple.get(0)
		DieRoll second = tuple.get(1)
		remainingRolls.add(first)
		remainingRolls.add(second)
		if (first.value == second.value) {
			remainingRolls.add(first)
			remainingRolls.add(second)
		}
		remainingRolls
	}
}
