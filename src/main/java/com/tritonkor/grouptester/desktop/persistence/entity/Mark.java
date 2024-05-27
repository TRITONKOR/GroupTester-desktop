package com.tritonkor.grouptester.desktop.persistence.entity;

/**
 * The {@code Mark} class represents a mark with its associated properties.
 */
public class Mark {

    private final int MAX_MARK = 100;

    private int mark;

    /**
     * Constructs a {@code Mark} instance with the specified mark value.
     *
     * @param mark The mark value.
     */
    public Mark(int mark) {
        this.mark = mark;
    }

    public Mark() {}

    /**
     * Gets the mark value.
     *
     * @return The mark value.
     */
    public int getMark() {
        return mark;
    }

    /**
     * Sets the mark value.
     *
     * @param mark The new mark value.
     */
    public void setMark(int mark) {
        this.mark = mark;
    }

    /**
     * Returns a string representation of the mark.
     *
     * @return A string representation of the mark.
     */
    @Override
    public String toString() {
        return  mark + "/" + MAX_MARK;
    }
}
