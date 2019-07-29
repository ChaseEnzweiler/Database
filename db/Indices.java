package db;

import java.util.List;

public class Indices {

    /**
     * This class is used to store the indices of the rows to keep in each(right and left) table when doing
     * a natural inner join.
     */

    private List<Integer> leftIndices;

    private List<Integer> rightIndices;

    Indices(List<Integer> left, List<Integer> right){

        leftIndices = left;

        rightIndices = right;

    }

    /**
     * getter for left indices list
     * @return list of integer indices of rows needed to be kept
     */
    List<Integer> getLeftIndices() {
        return leftIndices;
    }


    /**
     * getter for right indices list
     * @return list of integer indices of rows needed to be kept
     */
    List<Integer> getRightIndices() {
        return rightIndices;
    }



    @Override
    public boolean equals(Object other){

        if(this == other){

            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Indices otherIndex = (Indices) other;

        if(this.leftIndices.equals(((Indices) other).leftIndices) &&
                this.rightIndices.equals(((Indices) other).rightIndices)){

            return true;
        }

        return false;

    }

}
