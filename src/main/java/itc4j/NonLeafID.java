package itc4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * NonLeafID
 *
 * @author Sina Bagherzadeh
 * @author Benjamim Sonntag <benjamimsonntag@gmail.com>
 * @version 28/mai/2015
 */
final class NonLeafID extends ID implements Serializable {

    private static final long serialVersionUID = -5030081211956985797L;

    private final ID left;
    private final ID right;

    NonLeafID(ID left, ID right) {
        this.left = left;
        this.right = right;
    }

    ID getLeft() {
        return left;
    }

    ID getRight() {
        return right;
    }

    @Override
    boolean isOne() {
        return false;
    }

    @Override
    boolean isZero() {
        return false;
    }

    @Override
    boolean isLeaf() {
        return false;
    }

    @Override
    ID normalize() {
        return normalize(left.normalize(), right.normalize());
    }

    private static ID normalize(ID left, ID right) {
        if (left.isZero() && right.isZero()) {
            return IDs.zero();
        }
        else if (left.isOne() && right.isOne()) {
            return IDs.one();
        }
        else {
            return IDs.with(left, right);
        }
    }

    @Override
    ID[] split() {
        if (left.isZero()) {
            return splitWithLeftZero();
        }
        else if (right.isZero()) {
            return splitWithRightZero();
        }
        else {
            return new ID[] {
                IDs.with(left, IDs.zero()),
                IDs.with(IDs.zero(), right)
            };
        }
    }

    private ID[] splitWithLeftZero() {
        ID[] rightSplit = right.split();
        return new ID[] {
            IDs.with(IDs.zero(), rightSplit[0]),
            IDs.with(IDs.zero(), rightSplit[1])
        };
    }

    private ID[] splitWithRightZero() {
        ID[] leftSplit = left.split();
        return new ID[] {
            IDs.with(leftSplit[0], IDs.zero()),
            IDs.with(leftSplit[1], IDs.zero())
        };
    }

    @Override
    ID sum(ID other) {
        assert other != null;
        if (other.isZero()) {
            return this;
        }
        else if (!other.isLeaf()) {
            return sumNonLeaf((NonLeafID)other);
        }
        else {
            throw new IllegalArgumentException("Can't sum " + this + " with 1.");
        }
    }

    private ID sumNonLeaf(ID other) {
        ID leftSum = left.sum(other.getLeft());
        ID rightSum = right.sum(other.getRight());
        ID sum = IDs.with(leftSum, rightSum);
        return sum.normalize();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NonLeafID)) {
            return false;
        }
        else {
            NonLeafID other = (NonLeafID)object;
            return left.equals(other.getLeft()) &&
                   right.equals(other.getRight());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return "(" + left + ", " + right + ")";
    }

}
