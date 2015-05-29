package itc4j;

import java.io.Serializable;

/**
 * LeafID
 *
 * @author Benjamim Sonntag <benjamimsonntag@gmail.com>
 * @version 28/mai/2015
 */
final class LeafID extends ID implements Serializable {

    private static final long serialVersionUID = 870626177742300327L;
    
    private final int value;

    LeafID(int value) {
        this.value = value;
    }

    @Override
    ID getLeft() {
        return null;
    }

    @Override
    ID getRight() {
        return null;
    }

    @Override
    boolean isZero() {
        return value == 0;
    }

    @Override
    boolean isOne() {
        return value == 1;
    }

    @Override
    boolean isLeaf() {
        return true;
    }

    @Override
    ID normalize() {
        return this;
    }

    @Override
    ID[] split() {
        if (isZero()) {
            return new ID[] { IDs.zero(), IDs.zero()};
        }
        else { // if (isOne()) {
            return new ID[] {
                IDs.with(IDs.one(), IDs.zero()),
                IDs.with(IDs.zero(), IDs.one())
            };
        }
    }

    @Override
    ID sum(ID other) {
        assert other != null;
        if (this.isZero()) {
            return other;
        }
        else if (other.isZero()) {
            return this;
        }
        else {
            throw new IllegalArgumentException("Can't sum " + this + " with " + other);
        }
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof LeafID)) {
            return false;
        }
        else {
            LeafID other = (LeafID)object;
            return value == other.value;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.value;
        return hash;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    protected ID clone() {
        return super.clone();
    }
    
}
