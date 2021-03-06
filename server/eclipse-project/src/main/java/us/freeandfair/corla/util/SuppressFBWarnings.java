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
 * @author Daniel M. Zimmerman <dmz@freeandfair.us>
 * 
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to suppress FindBugs warnings.
 * 
 * @author Daniel M. Zimmerman
 * @version 0.0.1
 */
@Retention(RetentionPolicy.CLASS)
public @interface SuppressFBWarnings {
  /**
   * The set of FindBugs warnings that are to be suppressed in annotated
   * element. The value can be a bug category, kind or pattern.
   */
  String[] value() default {};

  /**
   * Optional documentation of the reason why the warning is suppressed
   */
  String justification() default "";
}
