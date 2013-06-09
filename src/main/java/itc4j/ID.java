package itc4j;

import java.io.Serializable;

/**
 * @author Sina Bagherzadeh
 */
public final class ID implements Serializable {

    protected ID left;
    protected ID right;

    protected ID() {
    }

    protected ID(ID left, ID right) {
        this.left = left;
        this.right = right;
    }

    protected static ID norm(ID id) {
        if (id.left != null && id.right != null && id.left.left == null && id.left.right == null &&
                id.right.left == null && id.right.right == null)
            return new ID();
        else if (id.left == null && id.right == null)
            return null;
        return id.clone();
    }

    protected static ID[] split(ID id) {
        if (id == null)
            return new ID[]{null, null};
        else {
            if (id.left == null && id.right == null)
                return new ID[]{new ID(new ID(), null), new ID(null, new ID())};
            else if (id.left == null) {
                ID[] ids = split(id.right);
                return new ID[]{new ID(null, ids[0]), new ID(null, ids[1])};
            } else if (id.right == null) {
                ID[] ids = split(id.left);
                return new ID[] {new ID(ids[0], null), new ID(ids[1], null)};
            } else
                return new ID[]{new ID(id.left.clone(), null), new ID(null, id.right.clone())};
        }
    }

    protected static ID sum(ID id1, ID id2) {
        if (id1 == null)
            return id2 != null ? id2.clone() : id2;
        else if (id2 == null)
            return id1.clone();
        else
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
        if ((right != null  && id.right == null) || (right == null  && id.right != null))
            return false;
        if (left == null && id.left == null && right == null && id.right == null)
            return true;
        if (left != null && right != null && id.left != null && id.right != null)
            return left.equals(id.left) && right.equals(id.right);
        return false;
    }

    @SuppressWarnings({"ConstantConditions"})
    public String toString() {
        if (left == null && right == null)
            return "1";
        else if (left == null && right != null)
            return "(0, " + right + ")";
        else if (left != null && right == null)
            return "(" + left + ", 0)";
        else 
            return "(" + left + ", " + right + ")";
    }

    @SuppressWarnings({"CloneDoesntCallSuperClone"})
    public ID clone() {
        ID clone = new ID();
        if (right != null)
            clone.right = right.clone();
        if (left != null)
            clone.left = left.clone();
        return clone;
    }

}
