package itc4j;

/**
 * Reasons about the order of two events.
 * @author Tiago Cogumbreiro (cogumbreiro@users.sf.net)
 *
 */
public class Causality {
	private static final int lift(Event e) {
		return e.isLeaf() ? 0 : e.getValue();
	}

	private static final Event tryLeft(Event e) {
		return e.isLeaf() ? e: e.getLeft();
	}

	private static final Event tryRight(Event e) {
		return e.isLeaf() ? e : e.getRight();
	}

	/**
	 * Less than-equal operator for causality: either e1 happens before e2 or e1
	 * equals e2.
	 * 
	 * @param offset1
	 *            The accumulated lifted value for event e1.
	 * @param e1
	 *            The first event being compared.
	 * @param offset2
	 *            The accumulated lifted value for event e2
	 * @param e2
	 *            The second event being compared.
	 * @return Returns if e1 is precedes or equals e2.
	 */
	private static final boolean lessThanEquals(final int offset1,
			final Event e1, final int offset2, Event e2) {
		final int new_a = offset1 + e1.getValue();
		if (e1.isLeaf()) {
			return new_a <= offset2 + e2.getValue();
		}
		final int new_b = lift(e2) + offset2;
		if (!lessThanEquals(new_a, e1.getLeft(), new_b, tryLeft(e2))) {
			return false;
		}
		return lessThanEquals(new_a, e1.getRight(), new_b, tryRight(e2));
	}

	/**
	 * The causality between two events.
	 *
	 */
	private static enum Order {
		HAPPENS_BEFORE, HAPPENS_AFTER, EQUALS, UNCOMPARABLE;
		public boolean isUnordered() {
			return this == EQUALS || this == UNCOMPARABLE;
		}

		/**
		 * Compose two causality events.
		 * 
		 * @param c1
		 * @param c2
		 * @return
		 */
		public static Order compose(Order c1, Order c2) {
			switch (c1) {
			case EQUALS:
				return c2;
			case UNCOMPARABLE:
				return Order.UNCOMPARABLE;
			case HAPPENS_BEFORE: {
				switch (c2) {
				case HAPPENS_BEFORE:
				case EQUALS:
					return Order.HAPPENS_BEFORE;
				default:
					return Order.UNCOMPARABLE;
				}
			}
			case HAPPENS_AFTER: {
				switch (c2) {
				case HAPPENS_AFTER:
				case EQUALS:
					return Order.HAPPENS_AFTER;
				default:
					return Order.UNCOMPARABLE;
				}
			}
			}
			throw new IllegalStateException();
		}
	}

	/**
	 * Base case of comparison.
	 * 
	 * @param offset
	 * @param e1
	 * @param e2
	 * @return
	 */
	private static Order compare0(int offset, Event e1, Event e2) {
		if (e1.getValue() < e2.getValue()) {
			return lessThanEquals(offset, e1, offset, e2) ? Order.HAPPENS_BEFORE
					: Order.UNCOMPARABLE;
		} else if (e1.getValue() > e2.getValue()) {
			return lessThanEquals(offset, e2, offset, e1) ? Order.HAPPENS_AFTER
					: Order.UNCOMPARABLE;
		}
		// Since one of the events is a leaf event, then only one leq is called.
		if (lessThanEquals(offset, e1, offset, e2)) {
			if (lessThanEquals(offset, e2, offset, e1)) {
				return Order.EQUALS;
			}
			return Order.HAPPENS_BEFORE;
		}
		if (lessThanEquals(offset, e2, offset, e1)) {
			return Order.HAPPENS_AFTER;
		}
		return Order.UNCOMPARABLE;
	}

	/**
	 * Checks if a given event happens-before (LT), happens-after (GT), equals,
	 * or is undefined
	 * 
	 * @param offset
	 * @param e1
	 * @param e2
	 * @return
	 */
	private static Order compare(int offset, Event e1, Event e2) {
		if (e1.getValue() != e2.getValue() || e1.isLeaf() || e2.isLeaf()) {
			return compare0(offset, e1, e2);
		}
		int newOffset = offset + e1.getValue();
		return Order.compose(compare(newOffset, e1.getLeft(), e2.getLeft()),
				compare(newOffset, e1.getRight(), e2.getRight()));
	}

	/**
	 * Check if timestamp <code>s1</code> happens before or equals to timestamp <code>s2</code>
	 * 
	 * @param e2
	 * @return
	 */
	public static boolean lessThanEquals(Stamp s1, Stamp s2) {
		return lessThanEquals(s1.getEvent(), s2.getEvent());
	}

	/**
	 * Check if event <code>e1</code> precedes or equals event <code>e2</code>
	 * 
	 * @param e2
	 * @return
	 */
	public static boolean lessThanEquals(Event e1, Event e2) {
		return lessThanEquals(0, e1, 0, e2);
	}

	/**
	 * Checks if this event is concurrent with <code>e</code>. If
	 * <code>e1.isConcurrent(e2)</code>, then <code>e2.isConcurrent(e1)</code>.
	 * 
	 * @param other
	 *            The event this object is being compared against.
	 * @return
	 */
	public static boolean isConcurrent(Event e1, Event e2) {
		return compare(0, e1, e2).isUnordered();
	}

	/**
	 * Checks if this event happened before the other. If neither event happened
	 * before the other, we say that they are concurrent.
	 * 
	 * @param e
	 * @return
	 */
	public static boolean happensBefore(Stamp s1, Stamp s2) {
		return happensBefore(s1.getEvent(), s2.getEvent());
	}
	/**
	 * Checks if this event happened before the other. If neither event happened
	 * before the other, we say that they are concurrent.
	 * 
	 * @param e
	 * @return
	 */
	public static boolean happensBefore(Event e1, Event e2) {
		return compare(0, e1, e2) == Order.HAPPENS_BEFORE;
	}
}
