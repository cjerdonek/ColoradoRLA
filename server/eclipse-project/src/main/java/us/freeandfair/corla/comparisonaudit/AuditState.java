/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * 
 * @created Jul 28, 2017
 * 
 * @copyright 2017 Free & Fair
 * 
 * @license GNU General Public License 3.0
 * 
 * @author Joey Dodds <jdodds@galois.com>
 * 
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.comparisonaudit;

/**
 * @description <description>
 * @explanation <explanation>
 * @bon OPTIONAL_BON_TYPENAME
 */
public class AuditState {

  /**
   * The number of ballots seen in this audit
   */
  private int my_ballots_seen;
  /**
   * The number of one vote overstatements seen
   */
  private int my_one_over;

  /**
   * The number of two vote oversatements seen
   */
  private int my_two_over;

  /**
   * The number of one vote understatements seen
   */
  private int my_one_under;

  /**
   * The number of two vote understatements seen
   */
  private int my_two_under;

  /**
   * 
   * <description> <explanation>
   * 
   * @param
   */
  public AuditState() {
    assert true;
  }

  /**
   * <description> <explanation>
   * 
   * @param
   * @return the ballotsSeen
   */
  public int getBallotsSeen() {
    return my_ballots_seen;
  }

  /**
   * <description> <explanation>
   * 
   * @param ballotsSeen the ballotsSeen to set
   */
  public void setBallotsSeen(final int the_ballots_seen) {
    my_ballots_seen = the_ballots_seen;
  }

  /**
   * <description> <explanation>
   * 
   * @param
   * @return the one_over
   */
  public int getOneOver() {
    return my_one_over;
  }

  /**
   * <description> <explanation>
   * 
   * @param the_one_over the one_over to set
   */
  public void setOneOver(final int the_one_over) {
    my_one_over = the_one_over;
  }

  /**
   * <description> <explanation>
   * 
   * @param
   * @return the two_over
   */
  public int getTwoOver() {
    return my_two_over;
  }

  /**
   * <description> <explanation>
   * 
   * @param two_over the two_over to set
   */
  public void setTwoOver(final int the_two_over) {
    my_two_over = the_two_over;
  }

  /**
   * <description> <explanation>
   * 
   * @param
   * @return the one_under
   */

  public int getOneUnder() {
    return my_one_under;
  }

  /**
   * <description> <explanation>
   * 
   * @param one_under the one_under to set
   */
  public void setOneUnder(final int the_one_under) {
    my_one_under = the_one_under;
  }

  /**
   * <description> <explanation>
   * 
   * @param
   * @return the two_under
   */
  public int getTwoUnder() {
    return my_two_under;
  }

  /**
   * <description> <explanation>
   * 
   * @param two_under the two_under to set
   */

  public void setTwoUnder(final int the_two_under) {
    assert false;
    // @ assert false;
    my_two_under = the_two_under;
  }

  /**
   * 
   * <description>
   * <explanation>
   * @param the_audit_result the result of the ballot to add
   */
  //@ behavior
  //@   requires P;
  //@   ensures Q;
  /*@ pure @
   */
  public void addAuditedBallot(final AuditRunner.CVRAudit the_audit_result) {
    my_ballots_seen++;
    switch (the_audit_result) {
      case NEUTRAL:
        break;
      case NOAUDIT:
        assert false;
        break;
      case ONEOVER:
        my_one_over++;
        break;
      case TWOOVER:
        my_two_over++;
        break;
      case ONEUNDER:
        my_one_under++;
        break;
      case TWOUNDER:
        my_two_under++;
        break;
      default:
        break;
    }
  }

}
