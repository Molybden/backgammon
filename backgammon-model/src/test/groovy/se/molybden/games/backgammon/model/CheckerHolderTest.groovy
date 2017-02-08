package se.molybden.games.backgammon.model

import org.junit.Test
import se.molybden.games.backgammon.model.exceptions.NoCheckersException
import spock.lang.Specification

import static se.molybden.games.backgammon.model.Color.WHITE

/**
 * Created by joachim on 2017-02-08.
 */
class CheckerHolderTest extends Specification {
	@Test
	void shouldAddOKToEmptyHolder() throws Exception {
		given:
		def holder = new CheckerHolder()
		when:
		holder.add(new Checker(WHITE))
		holder.add(new Checker(WHITE))
		then:
		!holder.empty
	}

	@Test
	void shouldThrowExceptionWhenGetColorOfEmptyHolder() throws Exception {
		given:
		def holder = new CheckerHolder()
		when:
		holder.getColor()
		then:
		thrown NoCheckersException.class
	}

	@Test
	void shouldThrowExceptionWhenGetRemovingFromEmptyHolder() throws Exception {
		given:
		def holder = new CheckerHolder()
		holder.add(new Checker(WHITE))
		holder.remove()
		when:
		holder.remove()
		then:
		thrown NoCheckersException.class
	}

	@Test
	void shouldRemoveOKFromNonEmptyHolder() throws Exception {
		given:
		def holder = new CheckerHolder()
		holder.add(new Checker(WHITE))
		when:
		holder.remove()
		then:
		holder.empty
	}

	@Test
	void shouldAddAndRemoveSeveralTimes() throws Exception {
		given:
		def holder = new CheckerHolder()
		when:
		holder.add(new Checker(WHITE))
		holder.remove()
		holder.add(new Checker(WHITE))
		holder.add(new Checker(WHITE))
		holder.remove()
		holder.remove()
		then:
		holder.empty
	}


}