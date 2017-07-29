/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * 
 * @created Jul 27, 2017
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

import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import org.testng.annotations.Test;

import us.freeandfair.corla.comparisonaudit.AuditRunner.CVRAudit;

/**
 * Tests for the ComparisonAudit class.
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class ComparisonAuditTest {

  /**
   * A minimal test.
   */
  @Test()
  public void testAudit() {
    HashSet<String> audit_set =new HashSet<>();
    audit_set.add("Contest");
    final ComparisonAudit comparison_audit = new ComparisonAudit(100, BigDecimal.valueOf(.05), audit_set);
    comparison_audit.addContest("Contest", 1, new HashMap<>());
    assertTrue(comparison_audit.addCandidateVotes("Contest", "Candidate1", 60));
    assertTrue(comparison_audit.addCandidateVotes("Contest", "Candidate2", 40));
    for(int i=0; i<34; i++) {
      comparison_audit.addAuditedBallot("Contest", CVRAudit.NEUTRAL);
    }
    assertTrue(comparison_audit.auditComplete());
  }
}
