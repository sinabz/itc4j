package itc4j;

import java.io.Serializable;

/**
 * @author Sina Bagherzadeh
 */
public final class ID implements Serializable {

    public static final ID ID_0 = new ID(0);
    public static final ID ID_1 = new ID(1);

    private ID left;
    private ID right;
    private int value = -1;

    private ID(int value) {
        this.value = value;
    }

    ID(ID left, ID right) {
        this.left = left;
        this.right = right;
    }

    public static ID newID_1() {
        return new ID(1);
    }

    public static ID newID_0() {
        return new ID(0);
    }

    public ID getLeft() {
        return left;
    }

    public ID getRight() {
        return right;
    }

    @SuppressWarnings("ConstantConditions")
    protected static ID norm(ID id) {
        assert id != null;
        if (id.right != null)
            id.right = norm(id.right);
        if (id.left != null)
            id.left = norm(id.left);
        if (id.left == null && id.right == null) {
            if (id.value == 0)
                return newID_0();
            if (id.value == 1)
                return newID_1();
        }
        if (id.left.equals(ID_0) && id.right.equals(ID_0))
            return newID_0();
        if (id.left.equals(ID_1) && id.right.equals(ID_1))
            return newID_1();
        return id;
    }

    protected static ID[] split(ID id) {
        assert id != null;
        if (id.equals(ID_0))
            return new ID[]{newID_0(), newID_0()};
        if (id.equals(ID_1))
            return new ID[]{new ID(newID_1(), newID_0()), new ID(newID_0(), newID_1())};
        if (id.left != null && id.left.equals(ID_0)) {
            ID[] rightSplit = split(id.right);
            return new ID[]{new ID(newID_0(), rightSplit[0]), new ID(newID_0(), rightSplit[1])};
        }
        if (id.right != null && id.right.equals(ID_0)) {
            ID[] leftSplit = split(id.left);
            return new ID[]{new ID(leftSplit[0], newID_0()), new ID(leftSplit[1], newID_0())};
        }
        return new ID[]{new ID(id.left, newID_0()), new ID(newID_0(), id.right)};
    }

    protected static ID sum(ID id1, ID id2) {
        assert id1 != null && id2 != null;
        if (id1.equals(ID_0))
            return id2;
        if (id2.equals(ID_0))
            return id1;
        return norm(new ID(sum(id1.left, id2.left), sum(id1.right, id2.right)));
    }

    @SuppressWarnings({"ConstantConditions", "SimplifiableIfStatement"})
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof ID))
            return false;
        ID id = (ID) o;
        if ((left == null && id.left != null) || (left != null && id.left == null))
            return false;
        if ((right != null && id.right == null) || (right == null && id.right != null))
            return false;
        if (left == null && id.left == null && right == null && id.right == null)
            return value == id.value;
        if (left != null && right != null && id.left != null && id.right != null)
            return left.equals(id.left) && right.equals(id.right);
        return false;
    }

    @SuppressWarnings({"ConstantConditions"})
    public String toString() {
        if (left == null && right == null)
            return String.valueOf(value);
        if (left.equals(ID_0))
            return "(0, " + right + ")";
        if (right.equals(ID_0))
            return "(" + left + ", 0)";
        return "(" + left + ", " + right + ")";
    }

    @SuppressWarnings({"CloneDoesntCallSuperClone"})
    public ID clone() {
        ID clone = new ID(value);
        if (right != null)
            clone.right = right.clone();
        if (left != null)
            clone.left = left.clone();
        return clone;
    }

}
