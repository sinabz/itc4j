package itc4j;

import java.io.Serializable;

/**
 * @author Sina Bagherzadeh
 */
public final class ID implements Serializable {

    public static final ID ID_0 = newID_0();
    public static final ID ID_1 = newID_1();

    public static ID newID_0() {
        return new ID(0);
    }

    public static ID newID_1() {
        return new ID(1);
    }

    private ID left;
    private ID right;
    private final int value;

    private ID(int value) {
        this.value = value;
    }

    ID(ID left, ID right) {
        this.left = left;
        this.right = right;
        this.value = -1;
    }

    private int getValue() {
        return value;
    }

    public ID getLeft() {
        return left;
    }

    public ID getRight() {
        return right;
    }
    
    private boolean hasLeft() {
        return left != null;
    }
    
    private boolean hasRight() {
        return right != null;
    }
    
    boolean isLeaf() {
        return !hasLeft() && !hasRight();
    }
    
    boolean isZero() {
        return isLeaf() && value == 0;
    }
    
    boolean isOne() {
        return isLeaf() && value == 1;
    }

    ID normalize() {
        if (!isLeaf()) {
            normalizeChildren();
            if (left.isZero() && right.isZero()) {
                return newID_0();
            }
            else if (left.isOne() && right.isOne()) {
                return newID_1();
            }
        }
        return this;
    }

    private void normalizeChildren() {
        if (hasRight()) {
            normalizeRight();
        }
        if (hasLeft()) {
            normalizeLeft();
        }
    }

    private void normalizeRight() {
        right = right.normalize();
    }

    private void normalizeLeft() {
        left = left.normalize();
    }

    ID[] split() {
        if (isLeaf()) {
            return splitLeaf();
        }
        else {
            return splitNode();
        }
    }

    private ID[] splitLeaf() {
        if (isZero()) {
            return new ID[] { newID_0(), newID_0() };
        }
        else { // if (isOne()) {
            return new ID[] {
                new ID(newID_1(), newID_0()),
                new ID(newID_0(), newID_1())
            };
        }
    }

    private ID[] splitNode() {
        if (hasLeft() && left.isZero()) {
            return splitWithLeftZero();
        }
        else if (hasRight() && getRight().isZero()) {
            return splitWithRightZero();
        }
        else {
            return new ID[] {
                new ID(left.clone(), newID_0()),
                new ID(newID_0(), right.clone())
            };
        }
    }

    private ID[] splitWithLeftZero() {
        ID[] rightSplit = right.split();
        return new ID[] {
            new ID(newID_0(), rightSplit[0]),
                new ID(newID_0(), rightSplit[1])
        };
    }

    private ID[] splitWithRightZero() {
        ID[] leftSplit = left.split();
        return new ID[] {
            new ID(leftSplit[0], newID_0()),
                new ID(leftSplit[1], newID_0())
        };
    }

    ID sum(ID other) {
        assert other != null;
        if(this.isOne() && other.isOne()) {
            throw new IllegalArgumentException("Can't sum 1 with 1.");
        }
        if (this.isZero()) {
            return other;
        }
        else if (other.isZero()) {
            return this;
        }
        else {
            ID leftSum = left.sum(other.getLeft());
            ID rightSum = right.sum(other.getRight());
            ID sum = new ID(leftSum, rightSum);
            return sum.normalize();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ID)) {
            return false;
        }
        else {
            return equalsID((ID)object);
        }
    }

    private boolean equalsID(ID other) {
        if (this.isLeaf() && other.isLeaf()) {
            return value == other.getValue();
        }
        else if(!this.isLeaf() && !this.isLeaf()) {
            return left.equalsID(other.getLeft()) && right.equalsID(other.getRight());
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (isLeaf()) {
            return String.valueOf(getValue());
        }
        else {
            return "(" + getLeft() + ", " + getRight() + ")";
        }
    }

    @Override
    @SuppressWarnings({"CloneDoesntCallSuperClone"})
    public ID clone() {
        if (isLeaf()) {
            return new ID(getValue());
        }
        else {
            return cloneNode();
        }
    }

    private ID cloneNode() {
        ID leftClone = null;
        ID rightClone = null;
        if (hasLeft()) {
            leftClone = left.clone();
        }
        if (hasRight()) {
            rightClone = right.clone();
        }
        return new ID(leftClone, rightClone);
    }

}
