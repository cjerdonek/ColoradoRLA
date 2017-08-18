/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * @created Aug 17, 2017
 * @copyright 2017 Free & Fair
 * @license GNU General Public License 3.0
 * @author Daniel M. Zimmerman <dmz@galois.com>
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.PersistenceException;

import us.freeandfair.corla.Main;
import us.freeandfair.corla.persistence.Persistence;
import us.freeandfair.corla.query.PersistentASMStateQueries;

/**
 * Utility classes that are generally useful for working with ASMs.
 * 
 * @author Daniel M. Zimmerman
 * @version 0.0.1
 */
public final class ASMUtilities {
  /**
   * Private constructor to prevent instantiation.
   */
  private ASMUtilities() {
    // do nothing
  }
  
  /**
   * Gets the ASM for the specified ASM class and identity, initialized to its
   * state on the database.
   * 
   * @param the_class The class.
   * @param the_identity The identity.
   * @return the ASM, or null if the ASM cannot be instantiated.
   */
  public static <T extends AbstractStateMachine> T asmFor(final Class<T> the_class, 
                                                          final String the_identity) {
    T result = null;
    
    try {
      if (the_identity == null) {
        // this is a singleton ASM, so it has a no-argument constructor
        result = the_class.getConstructor().newInstance();
      } else {
        // this ASM has an identity, so we need a 1-argument constructor 
        // that takes a String
        final Constructor<T> constructor = the_class.getConstructor(String.class);
        result = constructor.newInstance(the_identity);
      }
    } catch (final IllegalAccessException | InstantiationException | 
                   InvocationTargetException | NoSuchMethodException e) {
      Main.LOGGER.error("Unable to construct ASM of class " + the_class +
                        " with identity " + the_identity);
    }
    
    final PersistentASMState asm_state = 
        PersistentASMStateQueries.get(the_class, the_identity);
    
    if (asm_state == null) {
      Main.LOGGER.error("Unable to retrieve ASM state for class " + the_class + 
                        " with identity " + the_identity);
    } else if (result != null) {
      asm_state.applyTo(result);
    } 
    
    return result;
  }
  
  /**
   * Saves the state of the specified ASM to the database.
   * 
   * @param the_asm The ASM.
   * @return true if the save was successful, false otherwise
   */
  public static boolean save(final AbstractStateMachine the_asm) {
    boolean result = false;
    
    final PersistentASMState asm_state = 
        PersistentASMStateQueries.get(the_asm.getClass(), the_asm.identity());
    
    if (asm_state == null) {
      Main.LOGGER.error("Unable to retrieve ASM state for " + the_asm);
    } else {
      asm_state.updateFrom(the_asm);
      try {
        Persistence.saveOrUpdate(asm_state);
        result = true;
      } catch (final PersistenceException e) {
        Main.LOGGER.error("Could not save state for ASM " + the_asm);
      }
    }
    
    return result;
  }
  
  /**
   * Attempts to step with the specified event on the ASM of the specified
   * class and identity, and persist the resulting state.
   * 
   * @param the_event The event.
   * @param the_asm_class The class.
   * @param the_asm_identity The identity.
   * @return true if the state transition succeeds, false if the state machine
   * could not be loaded or the resulting state could not be persisted.
   * @exception IllegalStateException if the state transition is illegal.
   */
  public static boolean step(final ASMEvent the_event, 
                             final Class<? extends AbstractStateMachine> the_asm_class, 
                             final String the_asm_identity) {
    boolean result = false;
    final AbstractStateMachine asm = asmFor(the_asm_class, the_asm_identity);
    
    if (asm != null) {
      asm.stepEvent(the_event);
      result = save(asm);
    }

    return result;
  }
}
