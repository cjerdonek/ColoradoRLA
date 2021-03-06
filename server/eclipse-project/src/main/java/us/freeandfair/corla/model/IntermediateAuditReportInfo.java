/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * @created Aug 2, 2017
 * @copyright 2017 Free & Fair
 * @license GNU General Public License 3.0
 * @author Daniel M. Zimmerman <dmz@galois.com>
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.model;

import static us.freeandfair.corla.util.EqualsHashcodeHelper.nullableEquals;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.annotations.JsonAdapter;

import us.freeandfair.corla.json.IntermediateAuditReportJsonAdapter;
import us.freeandfair.corla.persistence.AbstractEntity;

/**
 * An audit investigation report.
 * 
 * @author Daniel M. Zimmerman
 * @version 0.0.1
 */
@Entity
@Table(name = "intermediate_audit_report")
//this class has many fields that would normally be declared final, but
//cannot be for compatibility with Hibernate and JPA.
@SuppressWarnings("PMD.ImmutableField")
@JsonAdapter(IntermediateAuditReportJsonAdapter.class)
public class IntermediateAuditReportInfo extends AbstractEntity implements Serializable {
  /**
   * The serialVersionUID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The audit board dashboard to which this report belongs. 
   */
  @ManyToOne(optional = false)
  @JoinColumn
  private CountyDashboard my_dashboard;
  
  /**
   * The timestamp of this report.
   */
  @Column(updatable = false)
  private Instant my_timestamp;
 
  /**
   * The report for this report.
   */
  @Column(updatable = false)
  private String my_report;
  
  /**
   * Constructs an empty AuditInvestigationReport, solely for persistence.
   */
  public IntermediateAuditReportInfo() {
    super();
  }
  
  /**
   * Constructs an audit investigation report with the specified 
   * parameters.
   * 
   * @param the_timestamp The timestamp.
   * @param the_name The name.
   * @param the_report The report.
   */
  public IntermediateAuditReportInfo(final Instant the_timestamp,
                                final String the_report) {
    super();
    my_timestamp = the_timestamp;
    my_report = the_report;
  }
  
  /**
   * Sets the dashboard that owns this record; this should only be called by
   * the AuditBoardDashboard class.
   * 
   * @param the_dashboard The dashboard.
   */
  protected void setDashboard(final CountyDashboard the_dashboard) {
    my_dashboard = the_dashboard;
  }
  
  /**
   * @return the dashboard.
   */
  public CountyDashboard dashboard() {
    return my_dashboard;
  }
  
  /**
   * @return the timestamp.
   */
  public Instant timestamp() {
    return my_timestamp;
  }
  
  /**
   * @return the report.
   */
  public String report() {
    return my_report;
  }
  
  /**
   * @return a String representation of this cast vote record.
   */
  @Override
  public String toString() {
    return "AuditInterimReport [timestamp=" + my_timestamp + 
           ", report=" + my_report + "]";
  }
  
  /**
   * Compare this object with another for equivalence.
   * 
   * @param the_other The other object.
   * @return true if the objects are equivalent, false otherwise.
   */
  @Override
  public boolean equals(final Object the_other) {
    boolean result = true;
    if (the_other instanceof IntermediateAuditReportInfo) {
      final IntermediateAuditReportInfo other_report = 
          (IntermediateAuditReportInfo) the_other;
      result &= nullableEquals(other_report.timestamp(), timestamp());
      result &= nullableEquals(other_report.report(), report());
    } else {
      result = false;
    }
    return result;
  }
  
  /**
   * @return a hash code for this object.
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
