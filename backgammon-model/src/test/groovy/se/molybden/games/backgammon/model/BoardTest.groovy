package se.molybden.games.backgammon.model

import org.junit.Test
import se.molybden.games.backgammon.model.exceptions.IllegalMoveException
import se.molybden.games.backgammon.model.exceptions.WinnerFoundException
import spock.lang.Specification

import static se.molybden.games.backgammon.model.BarPosition.getBarPosition
import static se.molybden.games.backgammon.model.BearOffPosition.getBearOffPosition
import static se.molybden.games.backgammon.model.Color.BLACK
import static se.molybden.games.backgammon.model.Color.WHITE

/**
 * Created by joachim on 2017-02-08.
 */
class BoardTest extends Specification {
	final static String INIT_STATE = """\
	   v  v  v  v  v  v     v  v  v  v  v  v
	*--------------------*--------------------*
	|  W              B  |     B           W  |
	|  W              B  |     B           W  |
	|                 B  |     B           W  |
	|                 B  |                 W  |
	|                 B  |                 W  |
	|                    |                    |  W: 0
	|                    |                    |  B: 0
	|                 W  |                 B  |
	|                 W  |                 B  |
	|                 W  |     W           B  |
	|  B              W  |     W           B  |
	|  B              W  |     W           B  |
	*--------------------*--------------------*
	   ^  ^  ^  ^  ^  ^     ^  ^  ^  ^  ^  ^"""

	@Test
	void "Should be able to make some moves"() {
		given:
		when:
		def board = new Board()
		verifyState(INIT_STATE, board)

		String afterFirstMove = """\
		   v  v  v  v  v  v     v  v  v  v  v  v
		*--------------------*--------------------*
		|  W              B  |     B           W  |
		|  W              B  |     B           W  |
		|                 B  |     B           W  |
		|                 B  |                 W  |
		|                 B  |                 W  |
		|                    |                    |  W: 0
		|                    |                    |  B: 0
		|                 W  |                 B  |
		|                 W  |                 B  |
		|                 W  |     W           B  |
		|                 W  |     W           B  |
		|  B     B        W  |     W           B  |
		*--------------------*--------------------*
		   ^  ^  ^  ^  ^  ^     ^  ^  ^  ^  ^  ^"""
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 3))
		verifyState(afterFirstMove, board)

		String afterHittingBlack = """\
		   v  v  v  v  v  v     v  v  v  v  v  v
		*--------------------*--------------------*
		|  W              B  |     B           W  |
		|  W              B  |     B           W  |
		|                 B  |     B           W  |
		|                 B  |                 W  |
		|                 B  |                 W  |
		|                    |                    |  W: 0
		|                    |                    |  B: 1
		|                    |                 B  |
		|                 W  |                 B  |
		|                 W  |     W           B  |
		|                 W  |     W           B  |
		|  B     W        W  |     W           B  |
		*--------------------*--------------------*
		   ^  ^  ^  ^  ^  ^     ^  ^  ^  ^  ^  ^"""
		board.move(new BoardPosition(WHITE, 19), new BoardPosition(WHITE, 22))
		verifyState(afterHittingBlack, board)

		String afterBlackReturningHit = """\
		   v  v  v  v  v  v     v  v  v  v  v  v
		*--------------------*--------------------*
		|  W              B  |     B           W  |
		|  W              B  |     B           W  |
		|                 B  |     B           W  |
		|                 B  |                 W  |
		|                 B  |                 W  |
		|                    |                    |  W: 1
		|                    |                    |  B: 0
		|                    |                 B  |
		|                 W  |                 B  |
		|                 W  |     W           B  |
		|                 W  |     W           B  |
		|  B     B        W  |     W           B  |
		*--------------------*--------------------*
		   ^  ^  ^  ^  ^  ^     ^  ^  ^  ^  ^  ^"""
		board.move(getBarPosition(BLACK), new BoardPosition(BLACK, 3))
		verifyState(afterBlackReturningHit, board)

		String whiteGetsOutAgain = """\
		   v  v  v  v  v  v     v  v  v  v  v  v
		*--------------------*--------------------*
		|  W  W           B  |     B           W  |
		|  W              B  |     B           W  |
		|                 B  |     B           W  |
		|                 B  |                 W  |
		|                 B  |                 W  |
		|                    |                    |  W: 0
		|                    |                    |  B: 0
		|                    |                 B  |
		|                 W  |                 B  |
		|                 W  |     W           B  |
		|                 W  |     W           B  |
		|  B     B        W  |     W           B  |
		*--------------------*--------------------*
		   ^  ^  ^  ^  ^  ^     ^  ^  ^  ^  ^  ^"""
		board.move(getBarPosition(WHITE), new BoardPosition(WHITE, 2))
		verifyState(whiteGetsOutAgain, board)
		then:
		noExceptionThrown()
	}

	@Test
	void "Invalid move to occupied point"() {
		given:
		def board = new Board()
		when:
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 6))
		then:
		thrown IllegalMoveException
		verifyState(INIT_STATE, board)
	}

	@Test
	void "Invalid move too far"() {
		given:
		def board = new Board()
		when:
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 11))
		then:
		thrown IllegalMoveException
		verifyState(INIT_STATE, board)
	}

	@Test
	void "Invalid bear off position"() {
		given:
		def board = new Board()
		when:
		board.move(new BoardPosition(BLACK, 1), getBearOffPosition(BLACK))
		then:
		IllegalMoveException e = thrown()
		e.message.contains("Cannot bear off from")
		verifyState(INIT_STATE, board)
	}

	@Test
	void "Invalid bear off state"() {
		given:
		def board = new Board()
		when:
		board.move(new BoardPosition(BLACK, 19), getBearOffPosition(BLACK))
		then:
		IllegalMoveException e = thrown()
		e.message.contains("home board")
		verifyState(INIT_STATE, board)
	}

	@Test
	void "Invalid move on board if checker on bar"() {
		given:
		def board = new Board()
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 3))
		board.move(new BoardPosition(WHITE, 19), new BoardPosition(WHITE, 22))
		board.confirmState()
		when:
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 3))
		then:
		thrown IllegalMoveException
		String expectedState = """\
		   v  v  v  v  v  v     v  v  v  v  v  v
		*--------------------*--------------------*
		|  W              B  |     B           W  |
		|  W              B  |     B           W  |
		|                 B  |     B           W  |
		|                 B  |                 W  |
		|                 B  |                 W  |
		|                    |                    |  W: 0
		|                    |                    |  B: 1
		|                    |                 B  |
		|                 W  |                 B  |
		|                 W  |     W           B  |
		|                 W  |     W           B  |
		|  B     W        W  |     W           B  |
		*--------------------*--------------------*
		   ^  ^  ^  ^  ^  ^     ^  ^  ^  ^  ^  ^"""
		verifyState(expectedState, board)
	}

	@Test
	void "Get list of available moves from start"() {
		given:
		def board = new Board()
		when:
		List<BoardPosition> availablePositions = board.getAvailablePositions(BLACK)
		then:
		availablePositions.size() == 15
		availablePositions.findAll { it.myCount == 1 }.size() == 2
		availablePositions.findAll { it.myCount == 12 }.size() == 5
		availablePositions.findAll { it.myCount == 17 }.size() == 3
		availablePositions.findAll { it.myCount == 19 }.size() == 5
	}

	@Test
	void "Get list of available moves on bar"() {
		given:
		def board = new Board()
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 3))
		board.move(new BoardPosition(WHITE, 19), new BoardPosition(WHITE, 22))
		when:
		List<Position> availablePositions = board.getAvailablePositions(BLACK)
		then:
		availablePositions.size() == 1
		availablePositions.get(0) instanceof BarPosition
	}

	@Test
	void "Get list of available moves when two on bar"() {
		given:
		def board = new Board()
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 3))
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 4))
		board.move(new BoardPosition(WHITE, 19), new BoardPosition(WHITE, 21))
		board.move(new BoardPosition(WHITE, 19), new BoardPosition(WHITE, 22))
		when:
		List<Position> availablePositions = board.getAvailablePositions(BLACK)
		then:
		availablePositions.size() == 2
		availablePositions.get(0) instanceof BarPosition
		availablePositions.get(1) instanceof BarPosition
	}

	@Test
	void "Some moves, confirm, some more moves, reset then verify previous confirmed state"() {
		given:
		def board = new Board()
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 3))
		board.move(new BoardPosition(WHITE, 19), new BoardPosition(WHITE, 22))
		board.confirmState()
		String expectedState = """\
		   v  v  v  v  v  v     v  v  v  v  v  v
		*--------------------*--------------------*
		|  W              B  |     B           W  |
		|  W              B  |     B           W  |
		|                 B  |     B           W  |
		|                 B  |                 W  |
		|                 B  |                 W  |
		|                    |                    |  W: 0
		|                    |                    |  B: 1
		|                    |                 B  |
		|                 W  |                 B  |
		|                 W  |     W           B  |
		|                 W  |     W           B  |
		|  B     W        W  |     W           B  |
		*--------------------*--------------------*
		   ^  ^  ^  ^  ^  ^     ^  ^  ^  ^  ^  ^"""
		verifyState(expectedState, board)
		when:
		board.move(new BoardPosition(WHITE, 19), new BoardPosition(WHITE, 22))
		board.resetState()
		then:
		verifyState(expectedState, board)
	}

	@Test
	void "Move all checkers off board, declare winner"() {
		given:
		def board = new Board()
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 7))
		board.move(new BoardPosition(BLACK, 1), new BoardPosition(BLACK, 7))
		board.move(new BoardPosition(BLACK, 7), new BoardPosition(BLACK, 12))
		board.move(new BoardPosition(BLACK, 7), new BoardPosition(BLACK, 12))
		board.move(new BoardPosition(BLACK, 12), new BoardPosition(BLACK, 18))
		board.move(new BoardPosition(BLACK, 12), new BoardPosition(BLACK, 18))
		board.move(new BoardPosition(BLACK, 12), new BoardPosition(BLACK, 18))
		board.move(new BoardPosition(BLACK, 12), new BoardPosition(BLACK, 18))
		board.move(new BoardPosition(BLACK, 12), new BoardPosition(BLACK, 18))
		board.move(new BoardPosition(BLACK, 12), new BoardPosition(BLACK, 18))
		board.move(new BoardPosition(BLACK, 12), new BoardPosition(BLACK, 18))
		board.move(new BoardPosition(BLACK, 17), new BoardPosition(BLACK, 19))
		board.move(new BoardPosition(BLACK, 17), new BoardPosition(BLACK, 19))
		board.move(new BoardPosition(BLACK, 17), new BoardPosition(BLACK, 19))
		board.move(new BoardPosition(BLACK, 18), new BoardPosition(BLACK, 19))
		board.move(new BoardPosition(BLACK, 18), new BoardPosition(BLACK, 19))
		board.move(new BoardPosition(BLACK, 18), new BoardPosition(BLACK, 19))
		board.move(new BoardPosition(BLACK, 18), new BoardPosition(BLACK, 19))
		board.move(new BoardPosition(BLACK, 18), new BoardPosition(BLACK, 19))
		board.move(new BoardPosition(BLACK, 18), new BoardPosition(BLACK, 19))
		board.move(new BoardPosition(BLACK, 18), new BoardPosition(BLACK, 19))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		String expectedState = """\
		   v  v  v  v  v  v     v  v  v  v  v  v
		*--------------------*--------------------*
		|  W              B  |                 W  |
		|  W                 |                 W  |
		|                    |                 W  |
		|                    |                 W  |
		|                    |                 W  |
		|                    |                    |  W: 0
		|                    |                    |  B: 0
		|                 W  |                    |
		|                 W  |                    |
		|                 W  |     W              |
		|                 W  |     W              |
		|                 W  |     W              |
		*--------------------*--------------------*
		   ^  ^  ^  ^  ^  ^     ^  ^  ^  ^  ^  ^"""
		when:
		verifyState(expectedState, board)
		board.move(new BoardPosition(BLACK, 19), new BearOffPosition(BLACK))
		then:
		thrown WinnerFoundException
	}

	private void verifyState(String state, Board board) {
		def boardArray = board.toString().split("\n")
		def stateArray = state.stripIndent().split("\n")
		for (int i = 0; i < stateArray.length; i++) {
			if (!boardArray[i].equals(stateArray[i])) {
				throw new AssertionError("Failed to find line: ${stateArray[i]}, was: ${boardArray[i]}")
			}
		}
	}

}