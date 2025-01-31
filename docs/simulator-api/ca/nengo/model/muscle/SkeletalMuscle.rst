.. java:import:: ca.nengo.model Node

.. java:import:: ca.nengo.model Probeable

SkeletalMuscle
==============

.. java:package:: ca.nengo.model.muscle
   :noindex:

.. java:type:: public interface SkeletalMuscle extends Node, Probeable

   A model of a skeletal muscle or muscle group.

   :author: Bryan Tripp

Fields
------
ACTIVATION
^^^^^^^^^^

.. java:field:: public static final String ACTIVATION
   :outertype: SkeletalMuscle

   activation

DYNAMIC_SPINDLE_ORIGIN
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: public static final String DYNAMIC_SPINDLE_ORIGIN
   :outertype: SkeletalMuscle

   MuscleSpindle.DYNAMIC_ORIGIN_NAME

EXCITATION_TERMINATION
^^^^^^^^^^^^^^^^^^^^^^

.. java:field:: public static final String EXCITATION_TERMINATION
   :outertype: SkeletalMuscle

   excitation

FORCE
^^^^^

.. java:field:: public static final String FORCE
   :outertype: SkeletalMuscle

   force

GTO_ORIGIN
^^^^^^^^^^

.. java:field:: public static final String GTO_ORIGIN
   :outertype: SkeletalMuscle

   GolgiTendonOrgan.ORIGIN_NAME

LENGTH
^^^^^^

.. java:field:: public static final String LENGTH
   :outertype: SkeletalMuscle

   length

STATIC_SPINDLE_ORIGIN
^^^^^^^^^^^^^^^^^^^^^

.. java:field:: public static final String STATIC_SPINDLE_ORIGIN
   :outertype: SkeletalMuscle

   MuscleSpindle.STATIC_ORIGIN_NAME

Methods
-------
getForce
^^^^^^^^

.. java:method:: public float getForce()
   :outertype: SkeletalMuscle

   :return: force generated by the muscle

setLength
^^^^^^^^^

.. java:method:: public void setLength(float length)
   :outertype: SkeletalMuscle

   :param length: length of the muscle

