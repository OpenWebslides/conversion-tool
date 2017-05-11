/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructures;

/**
 * A generic implementation of a Pair datastructure
 * @author Laurens
 */
public class Pair<L,R> {

  private final L left;
  private final R right;

  /**
   * Creates a new Pair with any datatype as left or right part
   * Also note that the left and right part don't have to be the same datatype
   * @param left Any type to be used as left part of the pair
   * @param right Any type to be used as right part of the pair
   */
  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public L getLeft() { return left; }
  public R getRight() { return right; }

  @Override
  public int hashCode() { return left.hashCode() ^ right.hashCode(); }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pair)) return false;
    Pair pairo = (Pair) o;
    return this.left.equals(pairo.getLeft()) &&
           this.right.equals(pairo.getRight());
  }

}