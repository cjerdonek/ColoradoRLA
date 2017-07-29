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

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import us.freeandfair.corla.crypto.PseudoRandomNumberGenerator;
import us.freeandfair.corla.util.Pair;

/**
 * @description <description>
 * @explanation <explanation>
 * @bon OPTIONAL_BON_TYPENAME
 */
public class AuditRunner {

  /**
   * 
   * @description The possible audit outcomes of a contest in a ballot
   * @explanation <explanation>
   * @bon OPTIONAL_BON_TYPENAME
   */
  public enum CVRAudit {
    NOAUDIT, NEUTRAL, ONEOVER, TWOOVER, ONEUNDER, TWOUNDER
  }

  /**
   * The comparison audit
   */
  private final ComparisonAudit my_audit;

  /**
   * The prng
   */
  private final PseudoRandomNumberGenerator my_prng;

  /**
   * The audit progress
   */
  private final LinkedHashMap<Integer, CVRAudit> my_audit_progress;

  /**
   * 
   * <description>
   * <explanation>
   * @param
   */
  public AuditRunner(final int the_total_ballots, final BigDecimal the_risk,
                     final Map<String, Pair<Integer, Map<String, Integer>>> the_contests,
                     final String the_seed, final Set<String> the_audit_contests) {
    my_audit =
        new ComparisonAudit(the_total_ballots, the_risk, the_contests, the_audit_contests);
    my_prng = new PseudoRandomNumberGenerator(the_seed, false, 0, the_total_ballots - 1);

    final int initial_sample_size = my_audit.getMaxAsn();
    final List<Integer> initial_sample = my_prng.getRandomNumbers(0, initial_sample_size - 1);

    my_audit_progress = new LinkedHashMap<>();
    for (final Integer item : initial_sample) {
      addIndexToAudit(item);
    }
  }

  /**
   * TODO: Remove this when class is finished temporary method to make static
   * analysis happy
   * 
   * @return
   */
  public boolean useStuff() {
    return my_audit_progress == null || my_prng == null;
  }

  private void addIndexToAudit(final Integer the_index) {
    my_audit_progress.put(the_index, CVRAudit.NOAUDIT);
  }

  private boolean runAuditSoFar() {

    return false;
    //TODO: STUB
  }

  /**
   * 
   * <description>
   * <explanation>
   * @param
   */
  public boolean addAuditedCVR(final int the_index, final CVRAudit the_result) {
    my_audit_progress.put(the_index, the_result);
    return runAuditSoFar();
  }

}
